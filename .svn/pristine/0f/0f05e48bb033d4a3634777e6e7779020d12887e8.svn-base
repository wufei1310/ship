package admin

import common.CommonParams
import grails.converters.JSON
import ship.DaiFaOrder
import ship.MobileMessage
import ship.Pager
import ship.ReturnGoods
import ship.ReturnGoodsPOJO
import ship.ReturnOrder
import ship.ShipSN
import ship.ShipSNGoods
import ship.TranLog
import ship.User

class AdminShipSNController extends BaseController{
    def randomService

    def index() {}

    def list(){
        if (!params.max) {
            params.max = 10
        }else{
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "dateCreated"
        if (!params.order) params.order = "desc"

        def searchClosure =  {


            if(params.orderSN){
                like('orderSN',"%"+params.orderSN+"%")
            }
            if(params.status){
                eq('status',params.status)
            }
            if(params.needTui){
                eq('needTui',params.needTui)
            }

            if(params.add_user){
                eq('add_user',params.add_user as Long)
            }
            if(params.date){
                ge('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.date+" 00:00:00"))
            }
            if(params.date){
                le('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.date+" 23:59:59"))
            }


            if(params.actual_returnTime){
                ge('actual_returnTime',Date.parse("yyyy-MM-dd HH:mm:ss",params.actual_returnTime+" 00:00:00"))
            }
            if(params.actual_returnTime){
                le('actual_returnTime',Date.parse("yyyy-MM-dd HH:mm:ss",params.actual_returnTime+" 23:59:59"))
            }

            if(params.reqUser=="daifa"){
                eq("shouliUserId",session.loginPOJO.user.id)
            }

            if(params.reqUser=="admin"){
                eq("shouliUserId",params.shouliUserId as Long)
            }


            sort:"dateCreated"
            order:"asc"

        }

        def o = ShipSN.createCriteria();
        def results = o.list(params,searchClosure)

        println results as JSON


        def map = [list: results, total: results.totalCount]


        if(params.mobile=="mobile"){

            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()
            mm.message = "获取包裹数据成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        }else{
            render(view: "/admin/shipSN/list",model:map)
        }



    }

    //通过包裹查看可能对应的退换货申请数据。若包裹中的物流单号和退换货申请对应，
    // 或者物流单号已经与退换货申请关联，则直接显示这个退换货申请

    def saleReturnList(){


        ReturnOrder returnOrder = ReturnOrder.findByWuliu_sn(params.wuliu_sn)
        println  returnOrder
        if(returnOrder){
            def list = []
            list.add(returnOrder)
            def map = [list: list]

            render(view: "/admin/shipSN/returnList",model:map)
            return
        }

        if(params.orderSN){
            returnOrder = ReturnOrder.findByOrderSN(params.orderSN)
            if(returnOrder){
                def list = []
                list.add(returnOrder)
                def map = [list: list]

                render(view: "/admin/shipSN/returnList",model:map)
                return
            }
        }



        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)
        def daiFaOrders = DaiFaOrder.findAllByArea_idLike(shipSN.area_id+"%");
        def searchClosure =  {

            if(daiFaOrders){
                inList("daiFaOrder", daiFaOrders )
            }


                eq("isScan","0")


        }

        def o = ReturnOrder.createCriteria();
        def results = o.list(params,searchClosure)

        def map = [list: results]

        render(view: "/admin/shipSN/returnList",model:map)
    }


    //将无主包裹与退换货申请关联
    def checkReturn(){
        ReturnOrder returnOrder = ReturnOrder.get(params.returnOrderId)


        returnOrder.isScan = "1"
        returnOrder.returnGoods.each { it ->

            it.rukuUser = session.loginPOJO.user.email
            it.rukuTime = new Date();
            it.status = "1" //记录该退换货商品办事处入库
        }

        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)

        shipSN.orderSN = returnOrder.orderSN;
        shipSN.status = "3" //入库
        shipSN.save()

        render(view:this.commonSuccess)
        return
    }

    //为包裹核实到一个退换货申请,则该申请等同与扫描入库
    def returnShow(){
        def returnOrder = ReturnOrder.get(params.id)

        def wuliu_list = []
        def list = []
        returnOrder.returnGoods.each { it ->




            ReturnGoodsPOJO returnGoodsPOJO = new ReturnGoodsPOJO();
            returnGoodsPOJO.returnOrderId = it.returnOrder.id
            returnGoodsPOJO.shipFee = it.returnOrder.shipFee
            returnGoodsPOJO.returnGoodsId = it.id

            returnGoodsPOJO.status = it.status

            returnGoodsPOJO.reason = it.reason;
            returnGoodsPOJO.returnReason  = it.returnReason;

            returnGoodsPOJO.shouliUser = it.shouliUser
            returnGoodsPOJO.shouliTime = it.shouliTime

            println "==========="
            println it
            println  it.daiFaGoods

            returnGoodsPOJO.goodsId = it.daiFaGoods.id
            returnGoodsPOJO.market = it.daiFaGoods.market
            returnGoodsPOJO.floor = it.daiFaGoods.floor
            returnGoodsPOJO.stalls = it.daiFaGoods.stalls
            returnGoodsPOJO.goods_sn = it.daiFaGoods.goods_sn
            returnGoodsPOJO.spec = it.daiFaGoods.spec


            //记录给换货的商品信息
            returnGoodsPOJO.changeMarket = it.market
            returnGoodsPOJO.changeFloor = it.floor
            returnGoodsPOJO.changeStalls = it.stalls
            returnGoodsPOJO.changeGoods_sn = it.goods_sn
            returnGoodsPOJO.changeSpec = it.spec



            returnGoodsPOJO.wuliu = it.returnOrder.wuliu
            returnGoodsPOJO.wuliu_sn = it.returnOrder.wuliu_sn
            returnGoodsPOJO.orderSN = it.returnOrder.orderSN

            returnGoodsPOJO.actual_price = it.actual_price
            returnGoodsPOJO.return_fee = it.return_fee
            returnGoodsPOJO.num = it.return_num   //退回的商品数量
            returnGoodsPOJO.type = it.type   //退

            list.add(returnGoodsPOJO)

            if(params.fromReq=="wuliu"&&it.type=="1"){
                wuliu_list.add(returnGoodsPOJO)
            }


        }

        if(params.fromReq=="wuliu"){
            list = wuliu_list;
        }



        def map = [list:list,area_name:g.areaName("area_id":returnOrder.daiFaOrder.area_id),daiFaOrder:returnOrder.daiFaOrder,returnOrder: returnOrder]



        render(view: "/admin/shipSN/returnShow",model:map)
    }

    //为无主包裹元配代发人员
    def noOwnerShipSNFenPei(){
        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)
        shipSN.shouliUserId = params.daifaId as Long;
        shipSN.shouliUser = params.daifaName

        shipSN.shouliTime = new Date();

        shipSN.status = "4"
        shipSN.save();

        redirect(controller: "adminMobile",action: "shipList",params: [status:"noowner",message:"分配无主包裹成功！"])
    }

    //分配给代发人员的无主包裹
    def  noOwnerShipSNOfDaiFa(){

        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "dateCreated"
        if (!params.order) params.order = "desc"


        def searchClosure = {
            eq("shouliUserId",session.loginPOJO.user.id)
            eq("status", "4")

        }

        def o = ShipSN.createCriteria();
        def results = o.list(params, searchClosure)
        def map = [list: results, total: results.totalCount]



        def mm = new MobileMessage()

        def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
        map.put("pager", pager)
        mm.message = "代发人员获取分配给自己的无主包裹成功"
        mm.result = "success"
        mm.model = map

        render mm as JSON
    }


    //代发人员退货完成提交退货金额
    def returnNoOwnerShipSNToStall() {

        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)

        shipSN.actual_return_fee = new BigDecimal(params.actual_return_fee)
        shipSN.actual_returnTime = new Date();
        shipSN.status = "5"

        if(shipSN.needTui=="1"){ //表示该无主包裹已经与一个退换货申请关联了的 ，那么退货完成时，将等待管理员审核退款给会员　
            shipSN.needTui="3"
        }else if(shipSN.needTui=="0"){
            shipSN.needTui="2"
        }

        shipSN.save();


        def mm = new MobileMessage()

        mm.message = "代发人员退货无主包裹成功"
        mm.result = "success"

        render mm as JSON

    }

    //管理员对包裹审核，确认退款给会员
    def tuikuan(){
        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)
        if (!shipSN) {

            flash.message = "包裹不存在"
        }

        if(shipSN.needTui!="3"){
            flash.message = "包裹不是等待审核退款状态，提交无效"
        }

        shipSN.tui_user = session.loginPOJO.user
        shipSN.tui_time = new Date();

        shipSN.needTui = "4"


        def goodsNum = 0;//退换货商品数量
        def returnOrder = ReturnOrder.findByOrderSN(shipSN.orderSN)
        returnOrder.returnGoods.each{
            if(it.type=="0" && it.status=="4"){  // 4：已退货，档口退款
                goodsNum = goodsNum + it.return_num
            }
        }


        returnOrder.status = "2"               //2 退货完成,已退款会员账户

        returnOrder.needTui = "2" //表示已经退货退款到会员账户

        returnOrder.save()


        //插入资金流水表（档口退货回款）
        def tranLog = new TranLog();
        tranLog.amount = shipSN.actual_return_fee
        tranLog.direction = "0"
        tranLog.type = "16"
        tranLog.orderSN = shipSN.orderSN
        tranLog.order_user = session.loginPOJO.user.id

        tranLog.num = shipSN.num
        tranLog.save();


        //退钱  到会员

        def  addUser = User.get(returnOrder.add_user)
        def account = addUser.account
        account.amount = shipSN.actual_return_fee +account.amount

        //插入资金流水表（商品）
        tranLog = new TranLog();
        tranLog.amount = shipSN.actual_return_fee
        tranLog.direction = "1"
        tranLog.type = "10"
        tranLog.orderSN = returnOrder.orderSN
        tranLog.order_user = session.loginPOJO.user.id
        tranLog.num = shipSN.num
        tranLog.save()


        flash.message = "退款成功"
        redirect(action: "list")
    }



    //管理员对无主包裹添加商品信息
    def addShipSNGoods(){

        ShipSNGoods shipSNGoods = new ShipSNGoods();
        shipSNGoods.wuliu_sn = params.wuliu_sn;
        shipSNGoods.goods_sn = params.goods_sn
        shipSNGoods.market = params.market
        shipSNGoods.floor = params.floor
        shipSNGoods.stalls = params.stalls
        shipSNGoods.num = params.num as int
        shipSNGoods.addUser = session.loginPOJO.user.id
        shipSNGoods.save();

        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)
        if(shipSN.goods_sn){
            shipSN.goods_sn = shipSN.goods_sn + "," + params.goods_sn
        }else{
            shipSN.goods_sn = params.goods_sn
        }

        shipSN.lurunum = shipSN.lurunum + (params.num as int)
        shipSN.save();
        def mm = new MobileMessage()
        mm.message = "为无主包裹添加商品信息成功"


        mm.result= "success"
        mm.model = [shipSN:shipSN]
        render mm as JSON
    }


    def tuiNoOwnerShipSN(){


        java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyMMdd");
        def randoNum = randomService?.nextInteger(10000, 99999); //加入订单号
        def orderSN = "noowner"+format2.format(new Date()).toString()  + randoNum;

        def returnOrder = new ReturnOrder()
        returnOrder.status = '1'
        returnOrder.orderSN = orderSN
        returnOrder.add_user = session.loginPOJO.user.id
        returnOrder.wuliu =  "无主包裹生成退货订单"
        returnOrder.wuliu_sn = params.wuliu_sn
        returnOrder.sendperson = "管理员退无主包裹"
        returnOrder.sendaddress = "管理员退无主包裹"
        returnOrder.sendcontphone = "管理员退无主包裹"
        returnOrder.type = "5"  //管理员对无主包裹录入商品后生成退货
        long return_num_all = 0
        BigDecimal goodsFee = new BigDecimal(0)//计算退货总计

        def shipSNGoods = ShipSNGoods.findAllByWuliu_sn(params.wuliu_sn)
        shipSNGoods.each{
            def returnGoods = new ReturnGoods()
            returnGoods.market = it.market
            returnGoods.floor = it.floor
            returnGoods.stalls = it.stalls

            returnGoods.num = it.num
            returnGoods.returnReason = "管理员退无主包裹"


            returnGoods.type =  "0"
            returnGoods.status = "1"
            returnGoods.add_user = session.loginPOJO.user.id
            returnGoods.return_fee = 0
            returnGoods.spec = ""

            returnGoods.goods_sn = it.goods_sn
            returnGoods.return_num = it.num

            returnOrder.addToReturnGoods(returnGoods)
        }
        returnOrder.flat = "0"
        returnOrder.serviceFee =0
        returnOrder.goodsFee = 0

        returnOrder.shipFee = 0//
        returnOrder.totalFee = 0
        returnOrder.save(flush: true)

        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)
        shipSN.status = "8"
        shipSN.save();

        def mm = new MobileMessage()
        mm.message = "无主包裹提交退货成功"

        mm.model = [shipSN:shipSN]
        mm.result= "success"
        render mm as JSON
    }
}
