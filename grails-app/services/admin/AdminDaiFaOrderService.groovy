package admin

import util.StringUtil
import ship.TranLog
import ship.User
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import ship.ReturnOrder
import ship.ReturnGoods
import exception.MessageException

class AdminDaiFaOrderService {
    def mailService

    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))

    //当拿货后，判断订单下的所有商品是否都 拿货，从而决定订单是否已经可以导出物流单了
    def updateForNaHuo( order) {
        def isCanExport = "1"
        def goodsList = order.daiFaGoods;
        goodsList.each {
            if (it.status == "0"
                ||it.status == "1"
                    || it.status == "3"
                    || it.status == "4"
                    || it.status == "5"
                    || it.status == "8") {
                isCanExport = "0"
            }
        }
        order.isCanExport = isCanExport;
        if(isCanExport == '1'){
            order.canExport_date = new Date()
        }
    }

    def updateForShipped(params, order) {

        def user = User.get(order.add_user)

        order.status = "shipped"
        order.isCanExport = "2"  //订单已经发出
        order.ship_time = new Date();
        order.wuliu_no = params.wuliu_no
        order.actual_shipfee = new BigDecimal(params.actual_shipfee)
        order.save(flush: true)

        //修改商品状态

        int kb_count = 0
        int ship_count = 0 // 发货商品数量
        int quxiao_count = 0 //取消商品数量

        def goodsList = order.daiFaGoods
        goodsList.each {
            if (it.status == '7') {
                it.status = '9'
                ship_count = ship_count + it.num
            }
            if (it.status == '6') {
                quxiao_count = quxiao_count + it.num
            }

            //如果是空包
            if (it.price == 0.01) {
                kb_count = kb_count + it.num
            }

        }

        //如果有取消的商品，那么重新计算运费
        if (quxiao_count > 0) {
            BigDecimal shipAmount = StringUtil.checkShip(order.wuliu, order.area_id, ship_count)
            if (shipAmount < order.shipFee) { //如果运费要少，则退还
                def ship_back_fee = order.shipFee - shipAmount
                order.shipFee = shipAmount
                order.totalFee = order.totalFee - ship_back_fee

                def account = user.account
                account.lock()
                BigDecimal memberamount = account.amount;
                account.amount = ship_back_fee + account.amount
                account.save()

                //退回运费 资金流水表
                def tranLog1 = new TranLog();
                tranLog1.amount = ship_back_fee
                memberamount = memberamount +  ship_back_fee
                tranLog1.memberamount = memberamount
                tranLog1.direction = "1"
                tranLog1.type = "6"
                tranLog1.orderSN = order.orderSN
                tranLog1.order_user = order.add_user
                tranLog1.num = quxiao_count
                tranLog1.save()
            }
        }

        //如果有空包则退还代发费用
        if (kb_count > 0) {


            BigDecimal daifa_free = new BigDecimal(properties.getProperty("daifa.daifa_free"))
            daifa_free = daifa_free * kb_count

            //减去服务费
            order.serviceFee = order.serviceFee - daifa_free
            order.totalFee = order.totalFee - daifa_free

            def account = user.account
            account.lock()
            BigDecimal memberamount = account.amount;
            account.amount = daifa_free + account.amount
            account.save()

            def tranLog1 = new TranLog();
            tranLog1.amount = daifa_free
            memberamount = memberamount + daifa_free
            tranLog1.memberamount =  memberamount
            tranLog1.direction = "1"
            tranLog1.type = "11"
            tranLog1.orderSN = order.orderSN
            tranLog1.order_user = order.add_user
            tranLog1.num = kb_count
            tranLog1.save()
        }

        //插入资金流水表
        def tranLog = new TranLog();
        tranLog.amount = order.actual_shipfee
        tranLog.direction = "1"
        tranLog.type = "2"
        tranLog.orderSN = order.orderSN
        tranLog.order_user = order.add_user
        tranLog.num = ship_count //实际发货商品数量
        tranLog.save()

        //发货成功发邮件

        Thread.start {
            sendMail {
                async true
                from "service@findyi.com"
                to user.email
                subject "您的代发订单（" + order.orderSN + "）已经成功发货"
                body "你在<a href='http://kingsdf.cn' target='_blank'>金士代发</a>上的代发订单（" + order.orderSN + "）已经成功发货，物流单号为 " + params.wuliu_no
            }
        }

    }

    //
    def checkSaleReturn(params, checkUser) {
        def returnOrder = ReturnOrder.get(params.id)
        if (!returnOrder) {
            throw new RuntimeException("退换货不存在")
        }

        if(returnOrder.needTui!="1"&&returnOrder.needShip!="1"){
            throw new RuntimeException("退换货不是处理完成的状态，当前returnOrder.needTui状态："+returnOrder.needTui+",当前returnOrder.needShip状态:"+returnOrder.needShip)
        }



        returnOrder.check_user = checkUser
        returnOrder.check_time = new Date()


        //如果是退货
        if (params.returnOrderOperType == 'tui') {
            def goodsFee = new BigDecimal(0)
            def goodsNum = 0;//退换货商品数量
            returnOrder.returnGoods.each{
                if(it.type=="0" && it.status=="4"){  // 4：已退货，档口退款
                    goodsFee = goodsFee + it.actual_return_fee * it.return_num
                    goodsNum = goodsNum + it.return_num
                }
            }


            returnOrder.status = "2"               //2 退货完成,已退款会员账户

            returnOrder.needTui = "2" //表示已经退货退款到会员账户

            returnOrder.goodsFee = goodsFee
            returnOrder.save()

            //会员自己民下的单的状态改变
            def memberReturnOrder = ReturnOrder.findByOrderSN(returnOrder.orderSN.replace("K","M"))
            memberReturnOrder.status = "2"
            memberReturnOrder.needTui = "2" //表示已经退货退款到会员账户

            memberReturnOrder.goodsFee = goodsFee
            memberReturnOrder.save()

            //插入资金流水表（档口退货回款）
            def tranLog = new TranLog();
            tranLog.amount = goodsFee
            tranLog.direction = "0"
            tranLog.type = "16"
            tranLog.orderSN = returnOrder.orderSN
            tranLog.order_user = returnOrder.add_user
            tranLog.num = goodsNum
            if (returnOrder.flat == '1') { //如果是非平台订单则记录一下
                tranLog.flat = '1'
            }
            tranLog.save();


            //退钱  到会员
            def  addUser = User.get(returnOrder.add_user)
            def account = addUser.account
            account.amount = goodsFee +account.amount

            //插入资金流水表（商品）
            tranLog = new TranLog();
            tranLog.amount = goodsFee
            tranLog.direction = "1"
            tranLog.type = "10"
            tranLog.orderSN = returnOrder.orderSN
            tranLog.order_user = returnOrder.add_user
            if(returnOrder.flat == '1'){ //如果是非平台订单则记录一下
                tranLog.flat = '1'
            }
            tranLog.num = goodsNum
            tranLog.save()






        }



        //如果是换货
        if (params.returnOrderOperType == 'ship') {



            def goodsFee = new BigDecimal(0)
            def goodsNum = 0;//退换货商品数量
            returnOrder.returnGoods.each{
                if(it.type=="1" &&  it.status=="7"){  //
                    goodsFee = goodsFee + it.return_fee * it.return_num
                    goodsNum = goodsNum + it.return_num
                }
            }

            returnOrder.status = "2" //退换货申请处理结束
            returnOrder.needShip = "2" //表示已经发货
            returnOrder.goodsFee = goodsFee
            returnOrder.h_wuliu_sn = params.h_wuliu_sn
            returnOrder.actual_shipFee = new BigDecimal(params.actual_shipFee)
            returnOrder.shipTime = new Date()


            //插入资金流水表（运费）
            def tranLog = new TranLog();
            tranLog.amount = returnOrder.actual_shipFee
            tranLog.direction = "1"
            tranLog.type = "15"
            tranLog.orderSN = returnOrder.orderSN
            tranLog.order_user = returnOrder.add_user
            tranLog.num = goodsNum
            tranLog.save()

            if (returnOrder.chajia) {
                //17换货补差价
                def tranLog1 = new TranLog();
                tranLog1.amount = returnOrder.chajia
                tranLog1.direction = "1"
                tranLog1.type = "17"
                tranLog1.orderSN = returnOrder.orderSN
                tranLog1.order_user = returnOrder.add_user
                tranLog1.num = goodsNum
                tranLog1.save()
            }

        }

    }


    //管理员审核，退款到会员账户
//    def saleReturnAmount(params, checkUser) {
//        def returnOrder = ReturnOrder.findByIdAndStatusAndType(params.id, "4", "0", [lock: true])
//        if (!returnOrder || checkUser.email != 'superquan') {
//            throw new RuntimeException("操作出错")
//        }
//        returnOrder.status = "2"
//        returnOrder.check_user = checkUser
//        returnOrder.check_time = new Date()
//        //退钱
//        def addUser = User.get(returnOrder.add_user)
//        def account = addUser.account
//        account.lock()
//        account.amount = returnOrder.goodsFee + account.amount
//
//        //插入资金流水表（商品）
//        def tranLog = new TranLog();
//        tranLog.amount = returnOrder.goodsFee
//        tranLog.direction = "1"
//        tranLog.type = "10"
//        tranLog.orderSN = returnOrder.orderSN
//        tranLog.order_user = returnOrder.add_user
//        if (returnOrder.flat == '1') { //如果是非平台订单则记录一下
//            tranLog.flat = '1'
//        }
//
//
//        def goodsNum = 0;//退换货商品数量
//        //商品
//        def goodsList = returnOrder.returnGoods
//        goodsList.eachWithIndex { it, i ->
//            goodsNum = goodsNum + it.return_num
//        }
//        tranLog.num = goodsNum
//
//        tranLog.save()
//    }
}
