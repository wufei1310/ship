package admin

import common.CommonParams
import grails.converters.JSON
import ship.DaiFaGoods
import ship.DaiFaOrder
import ship.GoodsLog
import ship.Market
import ship.MobileMessage
import ship.Pager
import ship.ReturnGoods
import ship.ReturnGoodsPOJO
import ship.ReturnOrder
import ship.ShipSN
import ship.ShipSNGoods
import ship.User

import java.text.SimpleDateFormat

class AdminMobileController {

    def adminAreaService;
    def adminUserService;

    def index() {}

    //分拣
    def fenJian() {


        def returnGoods = ReturnGoods.get(params.returnGoodsId as Long)
        returnGoods.status = "2"
        returnGoods.fenjianTime = new Date();
        returnGoods.fenjianUser = session.loginPOJO.user.email;
        returnGoods.save();

        def mm = new MobileMessage()
        mm.message = "退货商品分拣完毕，等待代发人员领货退"
        mm.result = "success"
        render mm as JSON
    }

    //获取退货市场统计
    def shipForMarket() {

        def returnGoodsList = ReturnGoods.findAllByStatusInList(["1", "2"])


        def searchMarket = {
        }
        def marketList = Market.createCriteria().list(searchMarket)

        def resultList = []

        marketList.each { market ->
            Long return_num = 0
            def m_map = [:]
            returnGoodsList.each { returnGoods ->

                if (returnGoods.status == params.returnGoodsStatus && returnGoods.orderfrom == "kings") {
                    //1:新入库退货商品统计 2:分拣完毕退货商品统计

                    if (market.market_name == returnGoods.market) {
                        return_num++
//                        return_num = return_num + returnGoods.return_num
                    }
                }


            }
            m_map.market = market.market_name
            m_map.num = return_num
            resultList.add(m_map)

        }


        def mm = new MobileMessage()
        mm.message = "获取市场退货商品数量成功"
        mm.result = "success"
        mm.model = [list: resultList]
        render mm as JSON

    }

    //会员提交退货请求处理
    def returnToStall() {
        def returnGoods = ReturnGoods.get(params.returnGoodsId as Long)

        def mm = new MobileMessage()

        if (params.status == "4") {
            returnGoods.status = params.status
            returnGoods.actual_returnTime = new Date()
            returnGoods.actual_return_fee = new BigDecimal(params.actual_return_fee)

            mm.message = "退货成功"


        }


        if (params.status == "7") {
            returnGoods.status = params.status
            returnGoods.actual_returnTime = new Date()

            mm.message = "换货成功"


        }

        if (params.status == "6") {
            returnGoods.status = params.status
            returnGoods.reason = params.reason

            returnGoods.actual_returnTime = new Date()
            returnGoods.actual_return_fee = 0
            returnGoods.reason = params.reason

            mm.message = params.reason + "，退货不成功"
        }

        if (params.status == "8") {
            returnGoods.status = params.status
            returnGoods.actual_returnTime = new Date()
            returnGoods.reason = params.reason
            mm.message = params.reason + "，换货不成功"


        }

        //更改退货申请的状态
        def returnOrder = returnGoods.returnOrder;
        def isTuiOver = "1"  //退货申请下的退货商品是否都处理了？
        //退货

        int returnGoodsCount = 0;
        int tuiGoodsCount = 0;
        int huanGoodsCount = 0;

        int tuiSucc = 0;//退货成功商品
        int tuiFail = 0;//退货失败商品

        int huanSucc = 0;//换货成功商品
        int huanFail = 0;//换货失败商品

        returnOrder.returnGoods.each {
            returnGoodsCount++
            if (it.type == "0") {
                tuiGoodsCount++;
                if (it.status == "4") {
                    tuiSucc++;
                } else if (it.status == "6") {
                    tuiFail++;
                }
            }

            if (it.type == "1") {
                huanGoodsCount++;
                if (it.status == "7") {
                    huanSucc++;
                } else if (it.status == "8") {
                    huanFail++;
                }
            }

        }





        if ((huanSucc != 0) && huanGoodsCount == (huanSucc + huanFail)) {
            returnOrder.needShip = "1"

        } else if (huanGoodsCount == huanFail) {

            returnOrder.needShip = "3"

            returnOrder.status = "2"  //处理结束

        } else {
            returnOrder.needShip = "0"
        }



        if ((tuiSucc != 0) && tuiGoodsCount == (tuiSucc + tuiFail)) {

            if (huanGoodsCount == (huanSucc + huanFail)) {  //订单下商品都处理完才可以退款
                returnOrder.needTui = "1"
                returnOrder.tui_time = new Date()
            }


        } else if (tuiGoodsCount == tuiFail) {
            returnOrder.needTui = "3"
            returnOrder.status = "2"  //处理结束
            println "sn:"+returnOrder.orderSN
            //会员自己民下的单的状态改变
            def memberReturnOrder = ReturnOrder.findByOrderSN(returnOrder.orderSN.replace("K", "M"))
            memberReturnOrder?.status = "2"
            memberReturnOrder?.needTui = "3" //表示已经退货退款到会员账户

            memberReturnOrder?.save()


        } else {
            returnOrder.needTui = "0"
        }



        mm.result = "success"
        render mm as JSON


    }

    //代发人员受理退货
    def daifaShouLiReturn() {
        def returnGoods = ReturnGoods.get(params.returnGoodsId as Long)


        returnGoods.status = "5"//已受理
        returnGoods.shouliUser = session.loginPOJO.user.email
        returnGoods.shouliUserId = session.loginPOJO.user.id
        returnGoods.shouliTime = new Date();

        def mm = new MobileMessage()
        mm.message = "退货商品已领出"
        mm.result = "success"

        render mm as JSON

    }

    //代发人员已经处理的退货，成功或者不成功的,管理员查看
    def hasDoneReturnOfWuliu() {


        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0


        if (!params.sort) params.sort = "actual_returnTime"
        if (!params.order) params.order = "desc"


        def searchClosure = {

            if (params.returnFail == "1") {
                inList("status", ['6', '8'])
            } else {
                eq("status", params.status)
            }


        }

        def o = ReturnGoods.createCriteria();
        def results = o.list(params, searchClosure)
        def m_list = []
        results.each { it ->

            ReturnGoodsPOJO returnGoodsPOJO = new ReturnGoodsPOJO();
            returnGoodsPOJO.returnOrderId = it.returnOrder.id
            returnGoodsPOJO.returnGoodsId = it.id
            returnGoodsPOJO.goodsId = it.daiFaGoods?.id
            returnGoodsPOJO.market = it.daiFaGoods != null ? it.daiFaGoods.market : it.market
            returnGoodsPOJO.floor = it.daiFaGoods != null ? it.daiFaGoods.floor : it.floor
            returnGoodsPOJO.stalls = it.daiFaGoods != null ? it.daiFaGoods.stalls : it.stalls
            returnGoodsPOJO.goods_sn = it.daiFaGoods != null ? it.daiFaGoods.goods_sn : it.goods_sn
            returnGoodsPOJO.spec = it.daiFaGoods?.spec
            returnGoodsPOJO.wuliu = it.returnOrder.wuliu
            returnGoodsPOJO.wuliu_sn = it.returnOrder.wuliu_sn
            returnGoodsPOJO.orderSN = it.returnOrder.orderSN

            returnGoodsPOJO.actual_price = it.actual_price
            returnGoodsPOJO.return_fee = it.return_fee
            returnGoodsPOJO.num = it.return_num   //退回的商品数量

            returnGoodsPOJO.actual_return_fee = it.actual_return_fee
            returnGoodsPOJO.actual_returnTime = it.actual_returnTime

            returnGoodsPOJO.type = it.type;
            returnGoodsPOJO.status = it.status;

            returnGoodsPOJO.shouliUser = it.shouliUser
            returnGoodsPOJO.shouliTime = it.shouliTime

            //记录给换货的商品信息
            returnGoodsPOJO.changeMarket = it.market
            returnGoodsPOJO.changeFloor = it.floor
            returnGoodsPOJO.changeStalls = it.stalls
            returnGoodsPOJO.changeGoods_sn = it.goods_sn
            returnGoodsPOJO.changeSpec = it.spec

            returnGoodsPOJO.returnReason = it.returnReason
            returnGoodsPOJO.reason = it.reason;


            m_list.add(returnGoodsPOJO)

        }





        def map = [list: m_list]


        if (params.mobile == "mobile") {
            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()

            if (params.status == "4") {
                mm.message = "代发人员已成功的退货商品列表"
            }
            if (params.status == "6") {
                mm.message = "代发人员不成功的退货商品列表"
            }


            mm.result = "success"
            mm.model = map
            render mm as JSON
        }
    }

    //提供管理员根据代发员和时间查询他的退货数据
    def hasDoneReturn() {
        def daifa_user_id = params.daifa_user_id
        def actual_returnTime = params.actual_returnTime

        forward(action: 'hasDoneReturnOfDaiFa', params: [daifa_user_id: daifa_user_id, actual_returnTime: actual_returnTime])

    }

    //代发人员已经处理的退货，成功或者不成功的
    def hasDoneReturnOfDaiFa() {
        if (!params.sort) params.sort = "actual_returnTime"
        if (!params.order) params.order = "desc"


        def searchClosure = {


            between('actual_returnTime', Date.parse("yyyy-MM-dd HH:mm:ss", params.actual_returnTime + ' 00:00:00'), Date.parse("yyyy-MM-dd HH:mm:ss", params.actual_returnTime + ' 23:59:59'))

            if (params.daifa_user_id) {
                eq("shouliUserId", params.daifa_user_id as Long)
            } else {
                eq("shouliUserId", session.loginPOJO.user.id)
            }

            eq("status", params.status)

        }

        def o = ReturnGoods.createCriteria();
        def results = o.list(params, searchClosure)
        def m_list = []
        results.each { it ->

            ReturnGoodsPOJO returnGoodsPOJO = new ReturnGoodsPOJO();
            returnGoodsPOJO.returnOrderId = it.returnOrder.id
            returnGoodsPOJO.returnGoodsId = it.id


            returnGoodsPOJO.goodsId = it.daiFaGoods?.id
            returnGoodsPOJO.market = it.daiFaGoods != null ? it.daiFaGoods.market : it.market
            returnGoodsPOJO.floor = it.daiFaGoods != null ? it.daiFaGoods.floor : it.floor
            returnGoodsPOJO.stalls = it.daiFaGoods != null ? it.daiFaGoods.stalls : it.stalls
            returnGoodsPOJO.goods_sn = it.daiFaGoods != null ? it.daiFaGoods.goods_sn : it.goods_sn



            returnGoodsPOJO.spec = it.daiFaGoods?.spec
            returnGoodsPOJO.wuliu = it.returnOrder.wuliu
            returnGoodsPOJO.wuliu_sn = it.returnOrder.wuliu_sn
            returnGoodsPOJO.orderSN = it.returnOrder.orderSN

            returnGoodsPOJO.actual_price = it.actual_price
            returnGoodsPOJO.return_fee = it.return_fee
            returnGoodsPOJO.num = it.return_num   //退回的商品数量

            returnGoodsPOJO.actual_return_fee = it.actual_return_fee
            returnGoodsPOJO.actual_returnTime = it.actual_returnTime

            returnGoodsPOJO.reason = it.reason;



            m_list.add(returnGoodsPOJO)

        }





        def map = [list: m_list]


        if (params.mobile == "mobile") {


            def mm = new MobileMessage()

            if (params.status == "4") {
                mm.message = "代发人员已成功的退货商品列表"
            }
            if (params.status == "6") {
                mm.message = "代发人员不成功的退货商品列表"
            }


            mm.result = "success"
            mm.model = map

            render mm as JSON
        }
    }

    //管理员查询退货中的商品
    def returnGoodsListOfAdmin() {


        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0


        if (!params.sort) params.sort = "shouliTime"
        if (!params.order) params.order = "desc"





        def searchClosure = {


            if (params.goods_sn) {
                def daiFaGoods = DaiFaGoods.findAllByGoods_snLike("%" + params.goods_sn + "%")
                inList("daiFaGoods", daiFaGoods)
            }

            if (params.thIng == "1") {  //退货处理中
                eq("status", '5')
            }
            if (params.thFail == "1") { //退不成功
                inList("status", ['6', '8'])
            }


        }

        def o = ReturnGoods.createCriteria();
        def results = o.list(params, searchClosure)
        def m_list = []
        results.each { it ->

            ReturnGoodsPOJO returnGoodsPOJO = new ReturnGoodsPOJO();
            returnGoodsPOJO.returnOrderId = it.returnOrder.id
            returnGoodsPOJO.returnGoodsId = it.id
            returnGoodsPOJO.goodsId = it.daiFaGoods?.id
            returnGoodsPOJO.market = it.daiFaGoods != null ? it.daiFaGoods.market : it.market
            returnGoodsPOJO.floor = it.daiFaGoods != null ? it.daiFaGoods.floor : it.floor
            returnGoodsPOJO.stalls = it.daiFaGoods != null ? it.daiFaGoods.stalls : it.stalls
            returnGoodsPOJO.goods_sn = it.daiFaGoods != null ? it.daiFaGoods.goods_sn : it.goods_sn
            returnGoodsPOJO.spec = it.daiFaGoods != null ? it.daiFaGoods.spec : it.spec
            returnGoodsPOJO.wuliu = it.returnOrder.wuliu
            returnGoodsPOJO.wuliu_sn = it.returnOrder.wuliu_sn
            returnGoodsPOJO.orderSN = it.returnOrder.orderSN

            returnGoodsPOJO.actual_price = it.actual_price
            returnGoodsPOJO.return_fee = it.return_fee
            returnGoodsPOJO.num = it.return_num   //退回的商品数量

            returnGoodsPOJO.actual_return_fee = it.actual_return_fee
            returnGoodsPOJO.actual_returnTime = it.actual_returnTime

            returnGoodsPOJO.type = it.type;
            returnGoodsPOJO.status = it.status;

            returnGoodsPOJO.shouliUser = it.shouliUser
            returnGoodsPOJO.shouliTime = it.shouliTime

            //记录给换货的商品信息
            returnGoodsPOJO.changeMarket = it.market
            returnGoodsPOJO.changeFloor = it.floor
            returnGoodsPOJO.changeStalls = it.stalls
            returnGoodsPOJO.changeGoods_sn = it.goods_sn
            returnGoodsPOJO.changeSpec = it.spec

            returnGoodsPOJO.returnReason = it.returnReason
            returnGoodsPOJO.reason = it.reason;



            m_list.add(returnGoodsPOJO)

        }





        def map = [list: m_list]


        if (params.mobile == "mobile") {
            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()

            mm.message = "物流获取退货商品列表成功"


            mm.result = "success"
            mm.model = map
            render mm as JSON
        }


    }

    //物流查询退货中的商品
    def returnGoodsListOfWuliu() {


        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0


        if (!params.sort) params.sort = "shouliTime"
        if (!params.order) params.order = "asc"





        def searchClosure = {


            if (params.goods_sn) {
                def daiFaGoods = DaiFaGoods.findAllByGoods_snLike("%" + params.goods_sn + "%")
                inList("daiFaGoods", daiFaGoods)
            }


            eq("status", '5')


        }

        def o = ReturnGoods.createCriteria();
        def results = o.list(params, searchClosure)
        def m_list = []
        results.each { it ->

            ReturnGoodsPOJO returnGoodsPOJO = new ReturnGoodsPOJO();
            returnGoodsPOJO.returnOrderId = it.returnOrder.id
            returnGoodsPOJO.returnGoodsId = it.id
            returnGoodsPOJO.goodsId = it.daiFaGoods?.id
            returnGoodsPOJO.market = it.daiFaGoods != null ? it.daiFaGoods.market : it.market
            returnGoodsPOJO.floor = it.daiFaGoods != null ? it.daiFaGoods.floor : it.floor
            returnGoodsPOJO.stalls = it.daiFaGoods != null ? it.daiFaGoods.stalls : it.stalls
            returnGoodsPOJO.goods_sn = it.daiFaGoods != null ? it.daiFaGoods.goods_sn : it.goods_sn
            returnGoodsPOJO.spec = it.daiFaGoods != null ? it.daiFaGoods.spec : it.spec
            returnGoodsPOJO.wuliu = it.returnOrder.wuliu
            returnGoodsPOJO.wuliu_sn = it.returnOrder.wuliu_sn
            returnGoodsPOJO.orderSN = it.returnOrder.orderSN

            returnGoodsPOJO.actual_price = it.actual_price
            returnGoodsPOJO.return_fee = it.return_fee
            returnGoodsPOJO.num = it.return_num   //退回的商品数量

            returnGoodsPOJO.actual_return_fee = it.actual_return_fee
            returnGoodsPOJO.actual_returnTime = it.actual_returnTime

            returnGoodsPOJO.type = it.type;
            returnGoodsPOJO.status = it.status;

            returnGoodsPOJO.shouliUser = it.shouliUser
            returnGoodsPOJO.shouliTime = it.shouliTime

            //记录给换货的商品信息
            returnGoodsPOJO.changeMarket = it.market
            returnGoodsPOJO.changeFloor = it.floor
            returnGoodsPOJO.changeStalls = it.stalls
            returnGoodsPOJO.changeGoods_sn = it.goods_sn
            returnGoodsPOJO.changeSpec = it.spec

            returnGoodsPOJO.returnReason = it.returnReason
            returnGoodsPOJO.reason = it.reason;



            m_list.add(returnGoodsPOJO)

        }





        def map = [list: m_list]


        if (params.mobile == "mobile") {
            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()

            mm.message = "物流获取退货商品列表成功"


            mm.result = "success"
            mm.model = map
            render mm as JSON
        }


    }

    //已被代发人员领出办理处，需要退处理的
    def returnGoodsListOfDaiFa() {
        def returnGoods = ReturnGoods.findAllByShouliUserIdAndStatus(session.loginPOJO.user.id, "5")
        def m_list = []
        returnGoods.each { it ->

            ReturnGoodsPOJO returnGoodsPOJO = new ReturnGoodsPOJO();
            returnGoodsPOJO.returnOrderId = it.returnOrder.id
            returnGoodsPOJO.returnGoodsId = it.id

            returnGoodsPOJO.status = it.status

            returnGoodsPOJO.goodsId = it.daiFaGoods?.id
            returnGoodsPOJO.market = it.market
            returnGoodsPOJO.floor = it.floor
            returnGoodsPOJO.stalls = it.stalls
            returnGoodsPOJO.goods_sn = it.goods_sn
            returnGoodsPOJO.spec = it.spec

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
            returnGoodsPOJO.type = it.type   //退回的商品数量

            returnGoodsPOJO.returnReason = it.returnReason
            returnGoodsPOJO.reason = it.reason;


            println it.returnReason

            m_list.add(returnGoodsPOJO)

        }

        def mm = new MobileMessage()
        mm.message = "获取我的退货商品列表成功"
        mm.result = "success"
        mm.model = [list: m_list]

        render mm as JSON
    }

    //代发人员的退货商品列表接口
    def returnGoodsListForDaiFa() {

        def returnGoods = ReturnGoods.findAllByStatusAndOrderfromAndMarket("2", "kings", params.market)


        def m_list = []
        returnGoods.each { it ->

            ReturnGoodsPOJO returnGoodsPOJO = new ReturnGoodsPOJO();
            returnGoodsPOJO.returnOrderId = it.returnOrder.id
            returnGoodsPOJO.returnGoodsId = it.id
            returnGoodsPOJO.goodsId = it.daiFaGoods?.id
            returnGoodsPOJO.market = it.market
            returnGoodsPOJO.floor = it.floor
            returnGoodsPOJO.stalls = it.stalls
            returnGoodsPOJO.goods_sn = it.goods_sn
            returnGoodsPOJO.spec = it.spec

            returnGoodsPOJO.num = it.num as Long
            returnGoodsPOJO.wuliu = it.returnOrder.wuliu
            returnGoodsPOJO.wuliu_sn = it.returnOrder.wuliu_sn
            returnGoodsPOJO.orderSN = it.returnOrder.orderSN


            returnGoodsPOJO.actual_price = it.actual_price
            returnGoodsPOJO.return_fee = it.return_fee
            returnGoodsPOJO.num = it.return_num
            returnGoodsPOJO.type = it.type   //退回的商品数量
            returnGoodsPOJO.status = it.status

            m_list.add(returnGoodsPOJO)

        }

        def mm = new MobileMessage()
        mm.message = "获取" + params.market + "退货商品列表成功,等待代发人员受理"
        mm.result = "success"
        mm.model = [list: m_list]

        render mm as JSON
    }

    //根据市场查询下面的退货商品
    def returnGoodsList() {

//        def shipSN = ShipSN.findAllByStatus("1")
//
//        def searchClosure = {
//            or{
//                and{
//                    eq("status","1")
//                    or{
//                        eq("type", "3")//查询中止订单自动生成的退货
//                        eq("type", "4")  //查询由于包裹先到生成的退货申请
//                        eq("type", "5")  //查询由于管理员为包裹录入商品数据生成的退货申请
//                    }
//                }
//
//
//                inList('wuliu_sn',shipSN.wuliu_sn)
//            }
//
//
//        }
//
//        def o = ReturnOrder.createCriteria();
//        def returnOrder = o.list(searchClosure)
//

//        def returnOrder = ReturnOrder.findAllByTypeOrWuliu_snInList ("3",shipSN.wuliu_sn)


        def returnGoodsList = ReturnGoods.findAllByStatusInListAndOrderfrom(["1", "2"], 'kings')
        def m_list = []
        returnGoodsList.each { returnGoods ->
            if (returnGoods.status == params.returnGoodsStatus) {  //1:新入库2:分拣完毕
                if (params.market == returnGoods.market) {

                    ReturnGoodsPOJO returnGoodsPOJO = new ReturnGoodsPOJO();
                    returnGoodsPOJO.returnOrderId = returnGoods.returnOrder.id
                    returnGoodsPOJO.returnGoodsId = returnGoods.id



                    returnGoodsPOJO.goodsId = returnGoods.daiFaGoods?.id
                    returnGoodsPOJO.market = returnGoods.market
                    returnGoodsPOJO.floor = returnGoods.floor
                    returnGoodsPOJO.stalls = returnGoods.stalls
                    returnGoodsPOJO.goods_sn = returnGoods.goods_sn
                    returnGoodsPOJO.spec = returnGoods.spec
                    returnGoodsPOJO.wuliu = returnGoods.returnOrder.wuliu
                    returnGoodsPOJO.wuliu_sn = returnGoods.returnOrder.wuliu_sn
                    returnGoodsPOJO.orderSN = returnGoods.returnOrder.orderSN

                    returnGoodsPOJO.actual_price = returnGoods.actual_price
                    returnGoodsPOJO.return_fee = returnGoods.return_fee
                    returnGoodsPOJO.num = returnGoods.return_num
                    returnGoodsPOJO.type = returnGoods.type   //退回的商品数量
                    returnGoodsPOJO.status = returnGoods.status

                    m_list.add(returnGoodsPOJO)
                }
            }

        }

        def mm = new MobileMessage()
        mm.message = "获取" + params.market + "退货商品列表成功"
        mm.result = "success"
        mm.model = [list: m_list]

        render mm as JSON


    }

    //物流单号列表查询
    def shipList() {


        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "orderSN"
        if (!params.order) params.order = "desc"


        def searchClosure = {


            if (params.status == "wait") {
                eq("status", "1")
            }


            if (params.status == "noowner") {

                eq("status", "1")
            }

            if (params.status == "noownerandhasreturn") {
                eq("needTui", "2")
            }
            if (params.wuliu_sn) {
                like("wuliu_sn", "%" + params.wuliu_sn + "%")
            }

        }

        def o = ShipSN.createCriteria();
        def results = o.list(params, searchClosure)
        def daifaList = adminUserService.selUser("daifa")
        def map = [list: results, total: results.totalCount, daifaList: daifaList]


        if (params.mobile == "mobile") {

            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()

            if (params.message) {
                mm.message = params.message
            } else {
                mm.message = "获取物流登记统计列表成功"
            }


            mm.result = "success"
            mm.model = map

            render mm as JSON
        }


    }

    def regShipSNNum() {
        //查询入库商品数量
        def shipSN = ShipSN.findAllByStatus("1")
        def confirmOrder = ReturnOrder.findAllByWuliu_snInList(shipSN.wuliu_sn)


        Long new_num = 0;//新入库商品数量
        Long wait_num = 0;//分拣完毕商品数量
        confirmOrder.returnGoods.flatten().each { returnGoods ->
            if (returnGoods.status == "1") {
                new_num = new_num + returnGoods.return_num
            }
            if (returnGoods.status == "2") {
                wait_num = wait_num + returnGoods.return_num
            }

        }


        def noowner = ShipSN.countByStatusInList(["0", "2"])

        def map = [new_num: new_num, wait: wait_num, noowner: noowner]
        def mm = new MobileMessage()
        mm.message = "获取物流登记统计数据成功"
        mm.result = "success"
        mm.model = map

        render mm as JSON
    }


    def checkNoOwnerPackgeWuliu_SN() {


        def returnOrderList = ReturnOrder.findAllByWuliu_sn(params.wuliu_sn)
        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)
        returnOrderList.each {
            it.isScan = "1";
            shipSN.orderSN = shipSN.orderSN + "|" + it.orderSN
        }
        shipSN.save();


        def mm = new MobileMessage()
        mm.result = "success"
        mm.message = "获取商品成功"
        if (returnOrderList.size() == 0) {
            mm.result = "fail"
            mm.message = "未查询到商品"
        }


        render mm as JSON


    }


    def proNoOwnerPackge() {
        ShipSN shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)
        if (!shipSN) {
            shipSN = new ShipSN();
            shipSN.scanTime = new Date();
        }

        shipSN.wuliu_sn = params.wuliu_sn
        shipSN.status = "1"
        shipSN.orderSN = ""
        shipSN.needTui = "0" //无主包裹没有关联退货申请
        shipSN.addUser = session.loginPOJO.user.id
        shipSN.addEmail = session.loginPOJO.user.email


        def imgStr = '';
        params.imgStr.split("\\|").each {
            if (it != "") {
                //def pic = util.RemoteFileUtil.remoteFileCopy(request, it)
               // imgStr = imgStr + "|" + pic
            }
        }

        shipSN.imgStr = imgStr;

        def retunOrderList = ReturnOrder.findAllByWuliu_sn(params.wuliu_sn)
        retunOrderList.each {
            shipSN.orderSN = shipSN.orderSN + "|" + it.orderSN
        }
        shipSN.save();

        def mm = new MobileMessage()
        mm.result = "success"
        mm.message = "根据手机号查寻不到商品,录入无主包裹"
        mm.model = [wuliu_sn: params.wuliu_sn, num: params.num]
        render mm as JSON
        return;
    }

    //扫描退回包裹的物流单号
    def checkReturnOrderSN() {
        def mm = new MobileMessage()
        mm.result = "success"
        ShipSN shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)




        if (!shipSN) {
            shipSN = new ShipSN();
        }
        shipSN.addUser = session.loginPOJO.user.id
        shipSN.addEmail = session.loginPOJO.user.email
        //shipSN.num = params.num as int
        shipSN.wuliu_sn = params.wuliu_sn
        shipSN.status = "1"
        shipSN.needTui = "1" //无主包裹关联退货申请
        shipSN.scanTime = new Date();


        def retunOrderList = ReturnOrder.findAllByWuliu_sn(params.wuliu_sn)
        retunOrderList.each {
            shipSN.orderSN = shipSN.orderSN + "|" + it.orderSN

            it.isScan = '1'
        }
        shipSN.save();



        mm.message = "物流单号扫描成功，录入包裹入库"

        //查询入库商品数量
//        shipSN = ShipSN.findAllByStatus("1")
//        def confirmOrder = ReturnOrder.findAllByWuliu_snInList(shipSN.wuliu_sn)
//
//
//        Long new_num = 0;//新入库商品数量
//        Long wait_num = 0;//分拣完毕商品数量
//        confirmOrder.returnGoods.flatten().each { returnGoods ->
//            if (returnGoods.status == "1") {
//                new_num = new_num + returnGoods.return_num
//            }
//            if (returnGoods.status == "2") {
//                wait_num = wait_num + returnGoods.return_num
//            }
//
//        }


        def noowner = ShipSN.countByStatusInList(["0", "2"])


        def map = [num: params.num, noowner: noowner, wuliu_sn: params.wuliu_sn]

        mm.model = map

        render mm as JSON
        return;
    }

    //收到无主包裹，录入订单号和货号，自动维护退货申请。
    def proReturn(daiFaOrder, goods_sn_map, wuliu_sn) {

        def returnOrder = new ReturnOrder()
        returnOrder.daiFaOrder = daiFaOrder
        returnOrder.orderSN = "K" + daiFaOrder.orderSN



        returnOrder.status = '1'
        returnOrder.add_user = session.loginPOJO.user.id
        returnOrder.wuliu = "包裹到货登记"
        returnOrder.wuliu_sn = wuliu_sn
        returnOrder.sendperson = daiFaOrder.sendperson
        returnOrder.sendaddress = daiFaOrder.sendaddress
        returnOrder.sendcontphone = daiFaOrder.sendcontphone
        returnOrder.type = "4"  //4 扫描无主包裹自动生成退货申请
        long return_num_all = 0
        BigDecimal goodsFee = new BigDecimal(0)//计算退货总计

//        println   goods_sn_map
        goods_sn_map.each { k, v ->
//            println "k:" + k
//            println "V:" + v
            def goods = DaiFaGoods.get(k as Long)
            if (!goods) {
                return
            }

            def returnGoods = new ReturnGoods()
            returnGoods.market = goods.market
            returnGoods.floor = goods.floor
            returnGoods.stalls = goods.stalls



            returnGoods.num = v
            returnGoods.return_num = v as Long

            returnGoods.price = goods.price
            returnGoods.actual_price = goods.price
            returnGoods.returnReason = "无主包裹"


            returnGoods.daiFaGoods = goods
            returnGoods.type = "0"
            returnGoods.status = "1"
            returnGoods.add_user = session.loginPOJO.user.id


            returnGoods.goods_sn = goods.goods_sn
            returnGoods.spec = goods.spec

            returnGoods.return_fee = goods.price

            returnOrder.addToReturnGoods(returnGoods)



            println returnGoods as JSON
            goodsFee = goodsFee + returnGoods.return_fee * returnGoods.return_num
            return_num_all = return_num_all + returnGoods.return_num


            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods

            goodsLog.addUser = session.loginPOJO.user
            goodsLog.logdesc = "退回包裹登记成功，进入退货流程"
            goodsLog.save()


        }



        returnOrder.serviceFee = new BigDecimal(CommonParams.return_free) * return_num_all
        returnOrder.goodsFee = goodsFee

        returnOrder.shipFee = 0//根据需要换货的数量计算运费
        returnOrder.totalFee = returnOrder.serviceFee //会员中止订单退货申请需支付金额只为服务费

        returnOrder.flat = '0' //平台订单
        returnOrder.save(flush: true)


        def mreturnOrder = ReturnOrder.findByOrderSN(returnOrder.orderSN.replace("K", "M"))
        if(mreturnOrder){
            returnOrder.ishuiyuanxiadan = "1"
        }

    }


    //物流app选择商品生成退货申请　
    def wuliuselectgoodsproReturn() {

        println params.ids_nums
        def returnOrderMap = [:]

        def ids_nums_arr = []
        if (params.ids_nums instanceof String) {
            ids_nums_arr.add(params.ids_nums)
        } else {
            ids_nums_arr = params.ids_nums
        }

        ids_nums_arr.each {
            def goodsstr = it.split("#");
            def daifaGoods = DaiFaGoods.get(goodsstr[0] as Long)

            if (returnOrderMap.containsKey(daifaGoods.daiFaOrder)) {
                def goods_sn_map = returnOrderMap.get(daifaGoods.daiFaOrder)
                goods_sn_map.put(daifaGoods.id, goodsstr[1])
            } else {
                def goods_sn_map = [:]
                goods_sn_map.put(daifaGoods.id, goodsstr[1])
                returnOrderMap.put(daifaGoods.daiFaOrder, goods_sn_map)
            }


        }
//        println returnOrderMap
//        println params.wuliu_sn

        def mm = new MobileMessage()
        mm.message = "生成退货申请成功"
        mm.result = "success"

        returnOrderMap.each { k, v ->
            //为无主包裹自动生成退货申请

            if (ReturnOrder.findByOrderSN("K" + k.orderSN)) {
                mm.message = "生成退货申请失败，该退回包裹已经入库登记，已生成退货"
                mm.result = "fail"
                render mm as JSON
                return;
            }

            proReturn(k, v, params.wuliu_sn)
        }

        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)
        shipSN.status = '7'
        shipSN.save();

        render mm as JSON

    }


    def adminCount() {
        def waitpay = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='waitpay'")[0]
        def searchClosure = {
            projections {
                rowCount()
            }
            daiFaOrder {
                ne("status", "delete")
                ne("status", "waitpay")
            }
            eq("status", "0")
        }

        def o = DaiFaGoods.createCriteria();
        def weishouli = o.list(searchClosure)[0]
        searchClosure = {

            projections {
                rowCount()
            }


            daiFaOrder {
                ne("status", "delete")
                ne("status", "waitpay")
            }
            eq("status", "1")
        }

        o = DaiFaGoods.createCriteria();
        def shouli = o.list(searchClosure)[0]
        def error = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='error'")[0]
        def diffship = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='diffship'")[0]
        def partaccept = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='partaccept'")[0]
        def allaccept = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='allaccept'")[0]

        def shipped = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='shipped'")[0]
        def problem = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='problem'")[0]
        searchClosure = {
            projections {
                rowCount()
            }
            daiFaOrder {
                ne("status", "delete")
                ne("status", "waitpay")
            }
            eq("status", "2")
        }

        o = DaiFaGoods.createCriteria();
        def yanshou = o.list(searchClosure)[0]
        def goods_shipped = DaiFaGoods.executeQuery("select count(a.id) from DaiFaGoods a  where  a.status='9'")[0]
        def tuihuancount = ReturnOrder.executeQuery("select count(a.id) from ReturnOrder a  where a.orderfrom='kings' ")[0]
        //统计新提交未扫描的退货申请数据
        def canExport = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.isCanExport in ('1','3')")[0]
        def noowner = ShipSN.countByStatus("1")

        def noownerandhasreturn = ShipSN.countByNeedTui("2")

        def ddthhcount = ReturnGoods.executeQuery("select count(a.id) from ReturnGoods a  where a.orderfrom='kings' and  a.status='2'")[0]
        //等待退货商品数量
        def thIng = ReturnGoods.executeQuery("select count(a.id) from ReturnGoods a  where a.orderfrom='kings' and  a.status='5'")[0]
        //退货处理中,代发已领货
        def thFail = ReturnGoods.countByStatusInListAndOrderfrom(['6', '8'], 'kings')
        //等待发货的退货申请
        def needShip = ReturnOrder.executeQuery("select count(a.id) from ReturnOrder a  where a.orderfrom='kings' and  a.needShip='1'")[0]

        def xrkcount = ReturnGoods.executeQuery("select count(a.id) from ReturnGoods a  where a.orderfrom='kings' and a.status='1'")[0]

        def map = [xrkcount     : xrkcount, needShip: needShip, noownerandhasreturn: noownerandhasreturn, waitpay: waitpay, weishouli: weishouli, shouli: shouli, error: error, diffship: diffship,
                   partaccept   : partaccept, allaccept: allaccept, shipped: shipped, problem: problem, yanshou: yanshou,
                   goods_shipped: goods_shipped, tuihuancount: tuihuancount, canExport: canExport, noowner: noowner,
                   ddthhcount   : ddthhcount, thIng: thIng, thFail: thFail]
        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            mm.message = "获取订单总数成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        } else {
            render map
        }
    }

    def wuliuCount() {
        def allaccept = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='allaccept'")[0]
        def searchClosure = {
            projections {
                rowCount()
            }
            daiFaOrder {
                ne("status", "delete")
                ne("status", "waitpay")
            }
            eq("status", "2")
        }

        def o = DaiFaGoods.createCriteria();
        def yanshou = o.list(searchClosure)[0]
        def canExport = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.isCanExport in ('1','3')")[0]
        searchClosure = {
            projections {
                rowCount()
            }
            daiFaOrder {
                ne("status", "delete")
                ne("status", "waitpay")
            }
            eq("status", "0")
        }

        o = DaiFaGoods.createCriteria();
        def weishouli = o.list(searchClosure)[0]
        searchClosure = {

            projections {
                rowCount()
            }


            daiFaOrder {
                ne("status", "delete")
                ne("status", "waitpay")
            }
            eq("status", "1")
        }

        o = DaiFaGoods.createCriteria();
        def shouli = o.list(searchClosure)[0]


        def shipped = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='shipped'")[0]
        def goods_shipped = DaiFaGoods.executeQuery("select count(a.id) from DaiFaGoods a  where  a.status='9'")[0]


        def partaccept = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a  where  a.status='partaccept'")[0]
        def ddthhcount = ReturnGoods.executeQuery("select count(a.id) from ReturnGoods a  where a.orderfrom='kings' and a.status='2'")[0]
        //等待退货商品数量
        def thIng = ReturnGoods.executeQuery("select count(a.id) from ReturnGoods a  where a.orderfrom='kings' and a.status='5'")[0]
        //退货处理中,代发已领货
        def thFail = ReturnGoods.countByStatusInListAndOrderfrom(['6', '8'], 'kings')
        def noowner = ShipSN.countByStatus("1")
        def xrkcount = ReturnGoods.executeQuery("select count(a.id) from ReturnGoods a  where a.orderfrom='kings' and a.status='1'")[0]
        //新入库商品数量
        def needShip = ReturnOrder.executeQuery("select count(a.id) from ReturnOrder a  where a.orderfrom='kings' and a.needShip='1'")[0]
        //等待发货的退货申请

        def map = [goods_shipped: goods_shipped, shipped: shipped, allaccept: allaccept, yanshou: yanshou, canExport: canExport, weishouli: weishouli, shouli: shouli,
                   partaccept   : partaccept, ddthhcount: ddthhcount, thIng: thIng, thFail: thFail, noowner: noowner, xrkcount: xrkcount, needShip: needShip]

        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            mm.message = "获取订单总数成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        } else {
            render map
        }
    }


    public scanGoods() {
        def goods = DaiFaGoods.get(params.goodsID as Long);
        render goods.goods_sn
    }


    public static final String full2HalfChange(String QJstr)
            throws UnsupportedEncodingException {

        StringBuffer outStrBuf = new StringBuffer("");

        String Tstr = "";

        byte[] b = null;

        for (int i = 0; i < QJstr.length(); i++) {

            Tstr = QJstr.substring(i, i + 1);

            // 全角空格转换成半角空格

            if (Tstr.equals("　")) {

                outStrBuf.append(" ");

                continue;

            }

            b = Tstr.getBytes("unicode");

            // 得到 unicode 字节数据

            if (b[2] == -1) {

                // 表示全角？

                b[3] = (byte) (b[3] + 32);

                b[2] = 0;

                outStrBuf.append(new String(b, "unicode"));

            } else {

                outStrBuf.append(Tstr);

            }

        } // end for.

        return outStrBuf.toString();

    }


    def checkPhone() {

        def mm = new MobileMessage()

        def daifaOrder = DaiFaOrder.findAllByContphoneAndStatus(params.contphone, "shipped")


        if (daifaOrder && daifaOrder.size() > 0) {
            def map = [daifaOrder: daifaOrder[0]]
            mm.result = "success"
            mm.message = "获取订单收货人信息成功"
            mm.model = map
        } else {
            mm.result = "fail"
            mm.message = "手机号：" + params.contphone + "在系统中未查询到订单信息。您可以更换手机号查询，或者将该包裹记入无主包裹"
        }



        render mm as JSON


    }


    def findGoodsFromPhone() {


        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        params.sort = "dateCreated"
        params.order = "desc"

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String findDate = df.format(new Date() - 90)


        def startDate = Date.parse("yyyy-MM-dd HH:mm:ss", findDate + " 00:00:00")

        def searchClosure;
        if (params.orderSN) {
            searchClosure = {

                daiFaOrder {

                    eq('orderSN', params.orderSN)

                }

            }
        } else if (params.contphone) {
            searchClosure = {

                daiFaOrder {

                    if (params.contphone) {
                        eq('contphone', params.contphone)
                    }
                    if (params.reperson) {
                        eq('reperson', params.reperson)
                    }


                    eq('status', 'shipped')

                }
                gt("dateCreated", startDate)

            }
        } else {
            def shipsn = ShipSN.findByWuliu_sn(params.wuliu_sn);
            def ordersn = shipsn.orderSN?.split("\\|")


            searchClosure = {

                daiFaOrder {

                    or {
                        ordersn.each {
                            if (it) {
                                eq("orderSN", it.replace("M", ""))
                            }
                        }
                    }

                }

            }
        }



        def o = DaiFaGoods.createCriteria();
        def results = o.list(params, searchClosure)
        def map = [list: results, total: results.totalCount]

        //封装分页传给手机端
        def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
        map.put("pager", pager)

        def mm = new MobileMessage()
        mm.result = "success"
        mm.message = "获取商品成功"
        if (results.size() == 0) {
            mm.result = "fail"
            mm.message = "未查询到商品"
        }

        mm.model = map

        render mm as JSON
    }
}
