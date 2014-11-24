package member

import ship.DaiFaOrder
import ship.DaiFaGoods
import ship.DiffGoods
import ship.GoodsLog
import ship.User
import ship.Account
import util.Push
import ship.PushPOJO
import ship.PushMsg
import util.RemoteFileUtil
import util.StringUtil
import ship.TranLog
import ship.ReturnOrder
import ship.ReturnGoods
import common.CommonParams
import grails.converters.JSON
import exception.MessageException
import java.util.regex.Pattern
import java.util.regex.Pattern
import java.util.regex.Matcher

class MemberDaiFaService {

    def payDaiFa(order_id) {
        def daiFaOrder = DaiFaOrder.lock(order_id)
        def totalFee = daiFaOrder.totalFee
        def user = User.get(daiFaOrder.add_user)
        def account = user.account
        account.lock()
        if (account.amount < totalFee) {
            return "余额不足，请先充值！"
        } else if (daiFaOrder.status != 'waitpay') {
            return "页面过期，请刷新！"
        } else {

            BigDecimal memberamount = account.amount;


            daiFaOrder.status = 'waitaccept'
            daiFaOrder.payTime = new Date()
            account.amount = account.amount - totalFee

            //推送
            def shichang = ""
            def goodsNum = 0;
            //商品
            def goodsList = daiFaOrder.daiFaGoods
            goodsList.eachWithIndex { it, i ->
                if (shichang.indexOf(it.market) < 0)
                    shichang = shichang + it.market + ","
                goodsNum = goodsNum + it.num
            }
//            def searchClosure = {
//                eq("user_type", "admin")
//            }

//            def o = PushMsg.createCriteria();
//            def results = o.list(searchClosure)

//            PushPOJO pushPOJO = new PushPOJO();
//            pushPOJO.title = shichang+"有人下单啦，快去代发挣钱呀";
//            pushPOJO.content = "订单号："+daiFaOrder.orderSN+"#拿货数量："+goodsNum+
//                                        "#拿货市场："+shichang
//
//            pushPOJO.pushMsgList = results
//            new Push().pushByStore(pushPOJO)



            //插入资金流水表（商品）
            def tranLog = new TranLog();
            tranLog.shouru_type = "0"
            tranLog.amount = daiFaOrder.goodsFee
            memberamount = memberamount - daiFaOrder.goodsFee
            tranLog.memberamount = memberamount
            tranLog.direction = "0"
            tranLog.type = "7"
            tranLog.orderSN = daiFaOrder.orderSN
            tranLog.order_user = daiFaOrder.add_user
            tranLog.num = goodsNum
            tranLog.save()

            //插入资金流水表（运费）
            def tranLogShip = new TranLog();
            tranLogShip.shouru_type = "0"
            tranLogShip.amount = daiFaOrder.shipFee
            memberamount = memberamount - daiFaOrder.shipFee
            tranLogShip.memberamount = memberamount

            tranLogShip.direction = "0"
            tranLogShip.type = "8"
            tranLogShip.orderSN = daiFaOrder.orderSN
            tranLogShip.order_user = daiFaOrder.add_user
            tranLogShip.num = goodsNum
            tranLogShip.save()

            //插入资金流水表（代发费）
            def tranLogDaiFa = new TranLog();
            tranLogDaiFa.shouru_type = "0"
            tranLogDaiFa.amount = daiFaOrder.serviceFee

            memberamount = memberamount - daiFaOrder.serviceFee
            tranLogDaiFa.memberamount = memberamount

            tranLogDaiFa.direction = "0"
            tranLogDaiFa.type = "9"
            tranLogDaiFa.orderSN = daiFaOrder.orderSN
            tranLogDaiFa.order_user = daiFaOrder.add_user
            tranLogDaiFa.num = goodsNum
            tranLogDaiFa.save()

            if (daiFaOrder.regardsFee > 0) {
                //插入资金流水表（礼品费）
                def tranLogRegards = new TranLog();
                tranLogRegards.shouru_type = "0"
                tranLogRegards.amount = daiFaOrder.regardsFee

                memberamount = memberamount - daiFaOrder.regardsFee
                tranLogShip.memberamount = memberamount

                tranLogRegards.direction = "0"
                tranLogRegards.type = "20"
                tranLogRegards.orderSN = daiFaOrder.orderSN
                tranLogRegards.order_user = daiFaOrder.add_user
                tranLogRegards.num = goodsNum
                tranLogRegards.save()
            }

            return null;
        }

    }

    /**
     * 更换档口，新价格低，则退还换钱到会员账户中
     * @param params
     * @param addUser
     */
    def doChangeStallLowPrice(daiFaGoods, addUser) {
        //退还金额
        println "daiFaGoods.actual_price:" + daiFaGoods.actual_price
        def fee = (daiFaGoods.price - daiFaGoods.actual_price) * daiFaGoods.num
        def account = addUser.account
        account.lock()
        BigDecimal memberamount = account.amount;
        account.amount = fee + account.amount

        //插入资金流水表(商品)
        def tranLog = new TranLog();
        tranLog.amount = fee
        memberamount = memberamount + fee
        tranLog.memberamount = memberamount
        tranLog.direction = "1"
        tranLog.type = "22"
        tranLog.orderSN = daiFaGoods.daiFaOrder.orderSN
        tranLog.goods_id = daiFaGoods.id
        tranLog.order_user = addUser.id
        tranLog.num = daiFaGoods.num
        tranLog.save()

        //修改订单总金额　
        daiFaGoods.daiFaOrder.totalFee = daiFaGoods.daiFaOrder.totalFee - fee



        def goodsLog = new GoodsLog();
        goodsLog.daiFaGoods = daiFaGoods
        goodsLog.addUser = addUser;
        goodsLog.logdesc = "更换档口，新档口拿货价低，退款：" + fee
        goodsLog.save()
    }

    def doCloseGoods(params, addUser) {


        def o = DaiFaGoods.createCriteria();
        def goods = o.get {
            or {
                eq('status', '4')
                eq('status', '5')
            }
            eq('id', new Long(params.id))
            daiFaOrder {
                eq('add_user', addUser.id)
            }
            lock: true
        }

        if(!goods){
            throw new RuntimeException("取消操作不合法");
        }


        //修改状态为取消
        goods.status = '6'



        def goodsLog = new GoodsLog();
        goodsLog.daiFaGoods = goods
        goodsLog.addUser = addUser;
        goodsLog.logdesc = "取消成功,退回账户金额：" + goods.price * goods.num
        goodsLog.save();

        //补款的状态也改为取消
        def diffGoods = DiffGoods.findByDaiFaGoodsAndStatus(goods, '0')
        if (diffGoods)
            diffGoods.status = "2"
        //订单状态重置
        def daiFaOrder = goods.daiFaOrder
        def order_status = StringUtil.getOrderStatusByGoods(daiFaOrder.daiFaGoods.status);
        if (!order_status)
            throw new RuntimeException("取消出错");
        daiFaOrder.status = order_status

        //退还金额
        def fee = goods.price * goods.num
        def account = addUser.account
        account.lock()
        BigDecimal memberamount = account.amount;


        //插入资金流水表(商品)
        def tranLog = new TranLog();
        tranLog.amount = fee
        memberamount = memberamount + fee
        tranLog.memberamount = memberamount
        tranLog.direction = "1"
        tranLog.type = "5"
        tranLog.orderSN = daiFaOrder.orderSN
        tranLog.goods_id = goods.id
        tranLog.order_user = daiFaOrder.add_user
        tranLog.num = goods.num
        tranLog.save()

        //修改订单商品价格 和总价格
        daiFaOrder.goodsFee = daiFaOrder.goodsFee - fee
        daiFaOrder.totalFee = daiFaOrder.totalFee - fee

        //如果订单为取消，则退还运费和礼品费
        if (daiFaOrder.status == 'close') {

            def goodsNum = 0;
            //商品
            def goodsList = daiFaOrder.daiFaGoods
            goodsList.eachWithIndex { it, i ->
                goodsNum = goodsNum + it.num
            }



            fee = fee + daiFaOrder.shipFee + daiFaOrder.regardsFee

            //插入资金流水表(商品)
            def tranLogShip = new TranLog();
            tranLogShip.amount = daiFaOrder.shipFee
            memberamount = memberamount + daiFaOrder.shipFee
            tranLogShip.memberamount = memberamount
            tranLogShip.direction = "1"
            tranLogShip.type = "6"
            tranLogShip.orderSN = daiFaOrder.orderSN
            tranLogShip.order_user = daiFaOrder.add_user
            tranLogShip.num = goodsNum
            tranLogShip.save()

            if (daiFaOrder.regardsFee > 0) {
                //插入资金流水表（会员取消商品退礼品费）
                def tranLogRegards = new TranLog();
                tranLogRegards.amount = daiFaOrder.regardsFee

                memberamount = memberamount + daiFaOrder.regardsFee
                tranLogRegards.memberamount = memberamount

                tranLogRegards.direction = "1"
                tranLogRegards.type = "21"
                tranLogRegards.orderSN = daiFaOrder.orderSN
                tranLogRegards.order_user = daiFaOrder.add_user
                tranLogRegards.num = goodsNum
                tranLogRegards.save()
            }

            //修改订单运费 和总价格

            daiFaOrder.totalFee = daiFaOrder.totalFee - daiFaOrder.shipFee - daiFaOrder.regardsFee
            daiFaOrder.shipFee = 0
            daiFaOrder.regardsFee = 0
        }


        account.amount = fee + account.amount


    }

    def doChaPay(params, addUser) {

        def daiFaOrder = DaiFaOrder.findByIdAndAdd_user(new Long(params.id), addUser.id)


        def o = DiffGoods.createCriteria();
        def diffGoods = o.list {
            eq("orderSN", daiFaOrder.orderSN)
            eq("status", "0")
            lock: true
        }

        //   def diffGoods = DiffGoods.findAllByOrderSNAndStatus(daiFaOrder.orderSN, '0',lock:true)
        if (!diffGoods) {
            throw new RuntimeException("支付出错！")
        } else {
            BigDecimal decimal = new BigDecimal(0);
            def goodsNum = 0;//商品数量


            diffGoods.each {
                decimal = decimal + it.daiFaGoods.diffFee * it.daiFaGoods.num
            }
            def account = addUser.account
            account.lock()
            if (account.amount < decimal) {
                throw new RuntimeException("余额不足，请先充值")
            }





            diffGoods.each {
                it.status = '1'
                it.payTime = new Date()
                it.daiFaGoods.status = '1' //已受理

                if (it.reason == "changeStall") {
                    it.daiFaGoods.status = '0' //未受理；如果是自己更换档口自己补款的刚支付完成后重新受理
                }


                it.daiFaGoods.price = it.daiFaGoods.price + it.daiFaGoods.diffFee;

//                decimal = decimal + it.daiFaGoods.diffFee * it.daiFaGoods.num

                goodsNum = goodsNum + it.daiFaGoods.num


                def goodsLog = new GoodsLog();
                goodsLog.daiFaGoods = it.daiFaGoods
                goodsLog.addUser = addUser;
                goodsLog.logdesc = "补款成功,金额：" + it.daiFaGoods.diffFee * it.daiFaGoods.num
                goodsLog.save()

            }

            def order_status = StringUtil.getOrderStatusByGoods(daiFaOrder.daiFaGoods.status);
            daiFaOrder.status = order_status
            daiFaOrder.totalFee = daiFaOrder.totalFee + decimal;
            daiFaOrder.goodsFee = daiFaOrder.goodsFee + decimal;
            //资金划拨


            BigDecimal memberamount = account.amount;
            account.amount = account.amount - decimal


            def poSearchClosure = {
                eq("user_type", "admin")
            }
//            def po = PushMsg.createCriteria();
//            def results = po.list(poSearchClosure)

//            def shichang = ""
//            //商品
//            def goodsList = daiFaOrder.daiFaGoods
//            goodsList.eachWithIndex{it, i ->
//                if(shichang.indexOf(it.market)<0)
//                shichang = shichang + it.market+","
//            }

//            PushPOJO pushPOJO = new PushPOJO();
//            pushPOJO.title = shichang+"有订单补齐差额，快去代发挣钱呀";
//            pushPOJO.content = "订单号："+daiFaOrder.orderSN+"差额补款已付款,快去拿货吧";
//            println "=================="
//            println pushPOJO.content
//            pushPOJO.pushMsgList = results
//            new Push().pushByStore(pushPOJO)

            //插入资金流水表
            def tranLog = new TranLog();
            tranLog.shouru_type = "0"
            tranLog.amount = decimal
            memberamount = memberamount - decimal
            tranLog.memberamount = memberamount
            tranLog.direction = "0"
            tranLog.type = "3"
            tranLog.orderSN = daiFaOrder.orderSN
            tranLog.order_user = daiFaOrder.add_user
            tranLog.num = goodsNum
            tranLog.save()


        }

    }

    def doChaShipPay(params, addUser) {
        def daiFaOrder = DaiFaOrder.findByIdAndAdd_userAndStatus(new Long(params.id), addUser.id, "diffship", [lock: true])
        if (!daiFaOrder) {
            throw new RuntimeException("支付出错！")
        }

        //资金划拨
        def account = addUser.account
        account.lock()
        if (account.amount < daiFaOrder.diffShip) {
            throw new RuntimeException("余额不足，请先充值")
        }


        daiFaOrder.status = "allaccept"
        daiFaOrder.totalFee = daiFaOrder.totalFee + daiFaOrder.diffShip
        daiFaOrder.shipFee = daiFaOrder.shipFee + daiFaOrder.diffShip

        def goodsNum = 0
        //商品
        def goodsList = daiFaOrder.daiFaGoods
        goodsList.eachWithIndex { it, i ->
            goodsNum = goodsNum + it.num
        }


        BigDecimal memberamount = account.amount;
        account.amount = account.amount - daiFaOrder.diffShip

        def poSearchClosure = {
            eq("user_type", "admin")
            eq("role", "admin")
        }
//        def po = PushMsg.createCriteria();
//        def results = po.list(poSearchClosure)

//        PushPOJO pushPOJO = new PushPOJO();
//        pushPOJO.title = "订单号："+daiFaOrder.orderSN+"运费差额已付款";
//        pushPOJO.content = "订单号："+daiFaOrder.orderSN+"运费差额补款已付款,快去发货吧";
//        pushPOJO.pushMsgList = results
//        new Push().pushByStore(pushPOJO)

        //插入资金流水表
        def tranLog = new TranLog();
        tranLog.shouru_type = "0"
        tranLog.amount = daiFaOrder.diffShip
        memberamount = memberamount - daiFaOrder.diffShip
        tranLog.memberamount = memberamount
        tranLog.direction = "0"
        tranLog.type = "4"
        tranLog.orderSN = daiFaOrder.orderSN
        tranLog.order_user = daiFaOrder.add_user
        tranLog.num = goodsNum
        tranLog.save()
    }

    //添加退货申请
    def doSaleExchange(params, addUser) {

        //params.returnReason[i]




        def daiFaOrder = DaiFaOrder.findByIdAndAdd_userAndStatus(params.id, addUser.id, "shipped")




//        def returnOrder = ReturnOrder.findByDaiFaOrderAndWuliu_sn(daiFaOrder, params.wuliu_sn)

//        if (returnOrder) {  //根据物流单号和订单查到系统已经由包裹先到已经生成的退货申请
//            returnOrder.wuliu = params.wuliu //物流公司
//            returnOrder.wuliu_sn = params.wuliu_sn
//            returnOrder.add_user = addUser.id
//
//
//            if (params.goods_id instanceof String) {//判断提交一个商品还是多个商品
//                returnOrder.returnGoods.each {
//                    it.add_user = addUser.id
//                    it.returnReason = params.returnReason
//                    def goodsLog = new GoodsLog();
//                    goodsLog.daiFaGoods = it.daiFaGoods
//                    goodsLog.addUser = addUser
//                    goodsLog.logdesc = "会员自己提交退货申请，更新由于包裹先到产生的退货商品数据记录"
//                    goodsLog.save()
//
//
//                }
//            } else {
//                params.goods_id.eachWithIndex { it, i ->
//                    returnOrder.returnGoods.each { returnGoods ->
//                        if ((params.goods_id[i] as Long) == returnGoods.daiFaGoods.id) {
//                            returnGoods.add_user = addUser.id
//                            returnGoods.returnReason = params.returnReason[i]
//                        }
//
//                        def goodsLog = new GoodsLog();
//                        goodsLog.daiFaGoods = returnGoods.daiFaGoods
//                        goodsLog.addUser = addUser
//                        goodsLog.logdesc = "会员自己提交退货申请，更新由于包裹先到产生的退货商品数据记录"
//                        goodsLog.save()
//                    }
//
//                }
//            }
//
//
//
//
//
//            return returnOrder
//        }



        def returnOrder = new ReturnOrder()
        returnOrder.daiFaOrder = daiFaOrder
        returnOrder.orderSN = "M"+daiFaOrder.orderSN
        returnOrder.status = '0'
        returnOrder.add_user = addUser.id
        returnOrder.wuliu = params.wuliu //物流公司
        returnOrder.wuliu_sn = params.wuliu_sn.trim()
        returnOrder.orderfrom = "member"

        Pattern pattern = Pattern.compile("\\d{5,}");
        Matcher matcher = pattern.matcher(returnOrder.wuliu);
        returnOrder.wuliu = matcher.replaceAll("");


        returnOrder.sendperson = daiFaOrder.sendperson
        returnOrder.sendaddress = daiFaOrder.sendaddress
        returnOrder.sendcontphone = daiFaOrder.sendcontphone



        long return_num_all = 0
        long huan_num_all = 0 //换货的商品总数
        BigDecimal goodsFee = new BigDecimal(0)


        if (params.goods_id instanceof String) {//判断提交一个商品还是多个商品
            def returnGoods = new ReturnGoods()
            def goods = DaiFaGoods.get(params.goods_id)



            returnGoods.market = params.market
            returnGoods.floor = params.floor
            returnGoods.stalls = params.stalls

            returnGoods.num = goods.num
            returnGoods.price = goods.price
            returnGoods.actual_price = goods.actual_price
            returnGoods.returnReason = params.returnReason


            returnGoods.daiFaGoods = goods
            returnGoods.orderfrom = "member"



            if (params.returnGoodsType != "0" && params.returnGoodsType != "1") {
                params.returnGoodsType = "0";
            }


            returnGoods.type = params.returnGoodsType
            returnOrder.type = params.returnGoodsType //退货申请状态
            returnGoods.status = "0"
            returnGoods.add_user = addUser.id


            returnGoods.goods_sn = params.goods_sn
            returnGoods.spec = params.spec
            returnGoods.return_num = params.return_num as Long
            returnGoods.return_fee = new BigDecimal(params.price)



            returnOrder.addToReturnGoods(returnGoods)

            return_num_all = return_num_all + returnGoods.return_num

            if (params.returnGoodsType == "1") {
                returnGoods.returnReason = ""
                huan_num_all = huan_num_all + returnGoods.return_num //用来计算运费
            }

            goodsFee = goodsFee + returnGoods.return_fee * returnGoods.return_num


        } else {
            params.goods_id.eachWithIndex { it, i ->
                def returnGoods = new ReturnGoods()
                def goods = DaiFaGoods.get(params.goods_id[i])

                returnGoods.market = params.market[i]
                returnGoods.floor = params.floor[i]
                returnGoods.stalls = params.stalls[i]
                returnGoods.num = goods.num
                returnGoods.price = goods.price
                returnGoods.actual_price = goods.actual_price



                returnGoods.daiFaGoods = goods
                //如果传到后台是其它值
                if (params.returnGoodsType[i] != "0" && params.returnGoodsType[i] != "1") {
                    params.returnGoodsType[i] = "0";
                }

                returnGoods.type = params.returnGoodsType[i]

                if (!returnOrder.type) {
                    returnOrder.type = params.returnGoodsType[i]
                } else if (returnOrder.type != params.returnGoodsType[i]) {   //说明退货申请中同时有退两人种类型的商品
                    returnOrder.type = "2"
                }

                if (params.returnGoodsType[i] == "1") {
                    returnGoods.returnReason = ""
                } else {
                    returnGoods.returnReason = params.returnReason[i]
                }



                returnGoods.status = "0"
                returnGoods.add_user = addUser.id



                returnGoods.goods_sn = params.goods_sn[i]
                returnGoods.spec = params.spec[i]
                returnGoods.return_num = params.return_num[i] as Long




                if (params.returnGoodsType[i] == "0") {

                    returnGoods.return_fee = new BigDecimal(params.price[i])
                } else {
                    returnGoods.return_fee = goods.price
                }





                returnOrder.addToReturnGoods(returnGoods)

                return_num_all = return_num_all + returnGoods.return_num

                if (params.returnGoodsType[i] == "1") {
                    huan_num_all = huan_num_all + returnGoods.return_num
                }

                goodsFee = goodsFee + returnGoods.return_fee * returnGoods.return_num
            }
        }

        returnOrder.goodsFee = goodsFee
        returnOrder.serviceFee = new BigDecimal(CommonParams.return_free) * return_num_all
        if (params.chajia) { //差价
            returnOrder.chajia = new BigDecimal(params.chajia)
        } else {
            returnOrder.chajia = new BigDecimal(0)
        }
        returnOrder.shipFee = StringUtil.checkShip(daiFaOrder.wuliu, daiFaOrder.area_id, huan_num_all)//根据需要换货的数量计算运费

        returnOrder.totalFee = returnOrder.serviceFee + returnOrder.chajia + returnOrder.shipFee
        returnOrder.flat = '0' //平台订单
        returnOrder.save(flush: true)
        returnOrder.daiFaOrder.type = '1'
        return returnOrder
    }

    def paySaleReturn(returnOrder_id) {
        def returnOrder = ReturnOrder.lock(returnOrder_id)
        def totalFee = returnOrder.totalFee
        def user = User.get(returnOrder.add_user)
        def account = user.account
        account.lock()
        if (account.amount < totalFee) {
            return "余额不足，请先充值！"
        }
//        else if (returnOrder.status != '0') {
//            return "页面过期，请刷新！"
//        }
        else {

            returnOrder.status = '1'
            returnOrder.payTime = new Date()
            // returnOrder.daiFaOrder.type = '1'
            BigDecimal memberamount = account.amount;
            account.amount = account.amount - totalFee


            def goodsNum = 0;//退货商品数量
            //商品
            def goodsList = returnOrder.returnGoods
            goodsList.eachWithIndex { it, i ->
                goodsNum = goodsNum + it.return_num
            }

            //插入资金流水表（退货服务费）
            def tranLog = new TranLog();
            tranLog.shouru_type = "0"
            tranLog.amount = returnOrder.serviceFee
            memberamount = memberamount - returnOrder.serviceFee
            tranLog.memberamount =  memberamount
            tranLog.direction = "0"
            tranLog.type = "12"
            tranLog.orderSN = returnOrder.orderSN
            tranLog.order_user = returnOrder.add_user
            tranLog.num = goodsNum
            if (returnOrder.flat == '1') { //如果是非平台订单则记录一下
                tranLog.flat = '1'
            }

            tranLog.save()

            //如果是换货 记录运费和差价
            if (returnOrder.type == '1') {
                def tranLog1 = new TranLog();
                tranLog1.shouru_type = "0"
                tranLog1.amount = returnOrder.shipFee
                memberamount = memberamount - returnOrder.shipFee
                tranLog1.memberamount =  memberamount
                tranLog1.direction = "0"
                tranLog1.type = "14"
                tranLog1.orderSN = returnOrder.orderSN
                tranLog1.order_user = returnOrder.add_user
                tranLog1.num = goodsNum
                tranLog1.save()

                if (returnOrder.chajia) {
                    def tranLog2 = new TranLog();
                    tranLog2.shouru_type = "0"
                    tranLog2.amount = returnOrder.chajia
                    memberamount = memberamount - returnOrder.chajia
                    tranLog2.memberamount =  memberamount
                    tranLog2.direction = "0"
                    tranLog2.type = "13"
                    tranLog2.orderSN = returnOrder.orderSN
                    tranLog2.order_user = returnOrder.add_user
                    tranLog2.num = goodsNum
                    tranLog2.save()
                }
            }

            return null

        }

    }

    def doSaleReturnAdd(params, addUser) {
        def returnOrder = new ReturnOrder()
        returnOrder.orderSN = "T" + (new Date().getTime()).toString()
        returnOrder.status = '0'
        returnOrder.type = '0'
        returnOrder.add_user = addUser.id
        returnOrder.wuliu = params.wuliu //物流公司
        returnOrder.wuliu_sn = params.wuliu_sn

        returnOrder.sendperson = params.sendperson
        returnOrder.sendaddress = params.sendaddress
        returnOrder.sendcontphone = params.sendcontphone

        long return_num_all = 0
        BigDecimal goodsFee = new BigDecimal(0)

        if (params.market instanceof String) {//判断提交一个商品还是多个商品
            def returnGoods = new ReturnGoods()
            long return_num = Long.valueOf(params.return_num)
            if (return_num > 0) {

                returnGoods.return_num = return_num


                returnGoods.market = params.market
                returnGoods.floor = params.floor
                returnGoods.stalls = params.stalls
                returnGoods.spec = params.spec



                returnGoods.goods_sn = params.goods_sn
                returnGoods.type = "0"
                returnGoods.add_user = addUser.id
                returnGoods.return_fee = new BigDecimal(params.return_fee)


                returnGoods.price = returnGoods.return_fee
                returnGoods.actual_price = returnGoods.return_fee

                returnOrder.addToReturnGoods(returnGoods)

                return_num_all = return_num_all + returnGoods.return_num
                goodsFee = goodsFee + returnGoods.return_fee * returnGoods.return_num
            }
        } else {
            params.market.eachWithIndex { it, i ->
                def returnGoods = new ReturnGoods()
                long return_num = Long.valueOf(params.return_num[i])
                if (return_num > 0) {

                    returnGoods.return_num = return_num


                    returnGoods.market = params.market[i]
                    returnGoods.floor = params.floor[i]
                    returnGoods.stalls = params.stalls[i]
                    returnGoods.spec = params.spec[i]


                    returnGoods.goods_sn = params.goods_sn[i]
                    returnGoods.type = "0"
                    returnGoods.add_user = addUser.id
                    returnGoods.return_fee = new BigDecimal(params.return_fee[i])

                    returnGoods.price = returnGoods.return_fee
                    returnGoods.actual_price = returnGoods.return_fee

                    returnOrder.addToReturnGoods(returnGoods)

                    return_num_all = return_num_all + returnGoods.return_num
                    goodsFee = goodsFee + returnGoods.return_fee * returnGoods.return_num
                }
            }
        }

        returnOrder.goodsFee = goodsFee
        returnOrder.serviceFee = new BigDecimal(CommonParams.return_free) * return_num_all
        returnOrder.totalFee = returnOrder.serviceFee
        returnOrder.flat = '1' // 非平台订单
        returnOrder.save(flush: true)
        return returnOrder
    }

    //因为换档口价格变高，需要补款
    def doBukuan(params, addUser) {
        def o = DaiFaGoods.createCriteria();
        def goods = o.get { eq("id", new Long(params.goodsId)) lock true }
        if (!goods || addUser.role != 'member') {
            throw new RuntimeException("补款出错");
        } else {
            goods.status = "4" //补款
            //goods.diffFee = new BigDecimal(params.diffFee) //补款数据

            goods.diffFee = goods.actual_price - goods.price //补差单价

            def order = goods.daiFaOrder
            def goodsStatusList = order.daiFaGoods.status
            def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
            if (!o_status)
                throw new RuntimeException("补款出错");
            order.status = o_status
            //order.totalFee = order.totalFee + goods.diffFee
            //添加补款数据

            def diffGoods = new DiffGoods();
            diffGoods.diffFee = goods.diffFee
            diffGoods.status = '0'
            diffGoods.addUser = addUser
            diffGoods.addTime = new Date()
            diffGoods.orderSN = order.orderSN
            diffGoods.reason = "changeStall"
            goods.addToDiffGoods(diffGoods)

            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = addUser
            goodsLog.logdesc = "更换档口，价格变高，需要补款，单件差额" + goods.diffFee
            goodsLog.save()
        }
        return goods
    }

    //会员提交中止发货请求
    def killOrderIng(params) {

        def daifaOrder = DaiFaOrder.get(params.orderId as Long)




        daifaOrder.status = "kill"


        def add_user = User.get(daifaOrder.add_user)
        daifaOrder.daiFaGoods.each {

            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = it
            goodsLog.addUser = add_user
            if (it.status == "0") {
                it.status = "killing"

                goodsLog.logdesc = "会员强制停止订单,商品未受理，中止发货处理中"
                goodsLog.save()
            } else if (it.status == "1") {
                it.status = "kill_wait"
                goodsLog.logdesc = "会员强制停止订单,商品已受理，等待代发员判断是否已经拿货"
                goodsLog.save()
            } else if (it.status == "2") {
                it.status = "kill_wait"
                goodsLog.logdesc = "会员强制停止订单,商品已拿货，由代发人员自行退货"
                goodsLog.save()
            } else if (it.status == "3") {
                it.status = "killing"
                goodsLog.logdesc = "会员强制停止订单,商品价格过高，暂不拿货，中止发货处理中"
                goodsLog.save()
            } else if (it.status == "4") {
                it.status = "killing"
                goodsLog.logdesc = "会员强制停止订单,商品等待补款，中止发货处理中"
                goodsLog.save()
            } else if (it.status == "5") {
                it.status = "killing"
                goodsLog.logdesc = "会员强制停止订单,商品缺货，中止发货处理中"
                goodsLog.save()
            } else if (it.status == "7") {
                it.status = "kill_return"
                goodsLog.logdesc = "会员强制停止订单,商品已验收，等待进入退货流程"
                goodsLog.save()
            } else if (it.status == "8") {
                it.status = "killing"
                goodsLog.logdesc = "会员强制停止订单,商品暂时缺货，中止发货处理中"
                goodsLog.save()
            }

            it.save();

        }

        daifaOrder.save();
        killOrderService(daifaOrder)

    }


    def killOrderService(daifaOrder) {
        def add_user = User.get(daifaOrder.add_user)

        def isKillingAndKill_return = [];//判断订单下的商品是不是都是中止退货，和中止可直接退款的，
        // i当是这种情况，需要生成退货单，并退款

        def killing = []   // 需退款的商品集合
        def killreturn = []  // 需退货流程的商品集合

        //=======================遍历订单下商品，查看
        daifaOrder.daiFaGoods.each {

            println "it.status:" + it.status

            if (it.status == "killing" || it.status == "kill_return" || it.status == "6") {
                isKillingAndKill_return.add(true)

                if (it.status == "killing") {
                    killing.add(it)
                }
                if (it.status == "kill_return") {
                    killreturn.add(it)
                }

            } else {
                isKillingAndKill_return.add(false);
            }
        }

        println "isKillingAndKill_return:" + isKillingAndKill_return
        println "killreturn:" + killreturn


        if (isKillingAndKill_return.count(false) == 0) {    //当　false数量为０时，说明  订单下的商品都已经是中止退货，和中止可直接退款的

            BigDecimal return_fee = 0.0
            println "killingkillingkilling"+killing
            killing.each { goodskilling ->
                return_fee = return_fee + (goodskilling.price * goodskilling.num)     //计算需要直接退款的金额
                goodskilling.status = "killed"


                def goodsLog = new GoodsLog();
                goodsLog.daiFaGoods = goodskilling
                goodsLog.addUser = add_user
                goodsLog.logdesc = "会员强制停止订单,对商品进行退款，此次退款将在退款总数上扣除3.5元服务费"
                goodsLog.save()


            }
            //====================进入退款＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
            if (return_fee > 0) {
                //退钱  到会员
                def account = add_user.account
                BigDecimal memberamount = account.amount;
                account.amount = (return_fee + daifaOrder.shipFee - 3.5) + account.amount
                //插入资金流水表（商品）
                def tranLog = new TranLog();
                tranLog.amount = return_fee
                memberamount = memberamount + return_fee
                tranLog.memberamount = memberamount
                tranLog.direction = "1"
                tranLog.type = "25"
                tranLog.orderSN = daifaOrder.orderSN
                tranLog.order_user = daifaOrder.add_user
                tranLog.save()

                //插入资金流水表（商品）
                tranLog = new TranLog();
                tranLog.amount = daifaOrder.shipFee
                memberamount = memberamount + daifaOrder.shipFee
                tranLog.memberamount = memberamount
                tranLog.direction = "1"
                tranLog.type = "28"
                tranLog.orderSN = daifaOrder.orderSN
                tranLog.order_user = daifaOrder.add_user
                tranLog.save()

                //会员强制停止订单收取３.5元费用
                tranLog = new TranLog();
                tranLog.amount = 3.5
                memberamount = memberamount -3.5
                tranLog.memberamount = memberamount
                tranLog.direction = "0"
                tranLog.type = "24"
                tranLog.orderSN = daifaOrder.orderSN
                tranLog.order_user = daifaOrder.add_user
                tranLog.save();
            }

            //=====================退款结束＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

            //====================进入生成退货申请程序＝＝＝＝＝＝＝＝＝＝＝＝
            if (killreturn.size() > 0) {
                println "====================进入生成退货申请程序＝＝＝＝＝＝＝＝"
                killReturn(daifaOrder, killreturn,"member") ;
                killReturn(daifaOrder, killreturn,"kings");
            }

        }

    }

    //中止订单开始生成退货申请单
    def killReturn(daiFaOrder, killreturn,orderfrom) {

        def returnOrder = new ReturnOrder()
        returnOrder.daiFaOrder = daiFaOrder
        if(orderfrom=="member"){
            returnOrder.status = '1'
            returnOrder.orderSN = "M"+daiFaOrder.orderSN
        }
        if(orderfrom=="kings"){
            returnOrder.ishuiyuanxiadan = "1"
            returnOrder.orderSN = "K"+daiFaOrder.orderSN
            returnOrder.status = '2'
        }

        returnOrder.add_user = daiFaOrder.add_user
        returnOrder.wuliu = "会员中止订单发货"
        returnOrder.wuliu_sn = "会员中止订单发货"
        returnOrder.sendperson = daiFaOrder.sendperson
        returnOrder.sendaddress = daiFaOrder.sendaddress
        returnOrder.sendcontphone = daiFaOrder.sendcontphone
        returnOrder.type = "3"
        returnOrder.orderfrom = orderfrom
        long return_num_all = 0
        BigDecimal goodsFee = new BigDecimal(0)//计算退货总计
        killreturn.each {

            it.status = "killed"

            def returnGoods = new ReturnGoods()
            returnGoods.orderfrom = orderfrom
            returnGoods.market = it.market
            returnGoods.floor = it.floor
            returnGoods.stalls = it.stalls

            returnGoods.num = it.num
            returnGoods.price = it.price
            returnGoods.actual_price = it.price
            returnGoods.returnReason = "会员中止订单发货"


            returnGoods.daiFaGoods = it
            returnGoods.type = "0"
            returnGoods.status = "1"
            returnGoods.add_user = daiFaOrder.add_user


            returnGoods.goods_sn = it.goods_sn
            returnGoods.spec = it.spec
            returnGoods.return_num = it.num
            returnGoods.return_fee = it.price

            returnOrder.addToReturnGoods(returnGoods)


            goodsFee = goodsFee + returnGoods.return_fee * returnGoods.return_num
            return_num_all = return_num_all + returnGoods.return_num


            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = it
            goodsLog.addUser = User.get(daiFaOrder.add_user)
            goodsLog.logdesc = "会员强制停止订单,商品已经拿货，现对商品生成退货申请。此次退货申请，商品总费用中将扣除退货服务费"
            goodsLog.save()


        }



        returnOrder.serviceFee = new BigDecimal(CommonParams.return_free) * return_num_all
        def fee =  goodsFee - returnOrder.serviceFee
        returnOrder.goodsFee = fee<0?0:fee //这里的退回商品金额应减去服务费

        returnOrder.shipFee = 0//根据需要换货的数量计算运费
        returnOrder.totalFee = returnOrder.serviceFee //会员中止订单退货申请需支付金额只为服务费

        returnOrder.flat = '0' //平台订单


        println     returnOrder as JSON
        returnOrder.save(flush: true)



    }


}
