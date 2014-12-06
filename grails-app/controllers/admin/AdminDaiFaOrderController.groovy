package admin

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import ship.DaiFaOrder
import grails.converters.JSON
import ship.MobileMessage
import ship.Pager
import ship.ReturnGoodsPOJO
import ship.ShipSN
import ship.User
import ship.DiffOrder
import ship.DaiFaGoods
import ship.TranLog
import ship.ReturnOrder
import ship.ReturnGoods
import exception.MessageException
import util.StringUtil

class AdminDaiFaOrderController extends BaseController {
    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    def isPro = properties.getProperty("isPro");
    def mailService
    def adminDaiFaOrderService

    def index() {}

    def delete() {
        def order = DaiFaOrder.findByIdAndStatus(params.id, "waitpay")
        if (!order || session.loginPOJO.user.email != 'superquan') {
            flash.message = "删除失败";
            flash.messageClass = this.success
        } else {
            order.status = "delete"
            flash.message = "删除成功";
            flash.messageClass = this.success
        }

        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "success"
            def map = [:]
            map.order = order
            mm.model = map
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            //  redirect(action: "list", params: params)
            return
        }
    }


    def reqOrderCount() {

        def waitpay = DaiFaOrder.countByStatus("waitpay")
        def waitaccept = DaiFaOrder.countByStatus("waitaccept")
        def partaccept = DaiFaOrder.countByStatus("partaccept")
        def allaccept = DaiFaOrder.countByStatus("allaccept")
        def shipped = DaiFaOrder.countByStatus("shipped")
        def error = DaiFaOrder.countByStatus("error")
        def close = DaiFaOrder.countByStatus("close")
        def delete = DaiFaOrder.countByStatus("delete")
        def problem = DaiFaOrder.countByStatus("problem")
        def diffship = DaiFaOrder.countByStatus("diffship")

        def canExport = DaiFaOrder.countByIsCanExport("1")

        //def goods_shipped = DaiFaGoods.executeQuery("select count(a.id) from DaiFaGoods a  where  a.status='9'")[0]
        def goods_shipped = DaiFaGoods.countByStatus('9')


        def tuihuancount = ReturnOrder.countByIsScan("0")//统计新提交未扫描的退货申请数据

        def searchClosure = {

            projections {
                rowCount()
            }


            daiFaOrder {
                ne("status", "delete")
                ne("status", "waitpay")
            }
            eq("status", "1")
        }

        def o = DaiFaGoods.createCriteria();
        def shouli = o.list(searchClosure)

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
        def yanshou = o.list(searchClosure)


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
        def weishouli = o.list(searchClosure)

        def qianshou = DaiFaGoods.countByChecktimeIsNotNull()

        def noowner = ShipSN.countByStatusInList(["0", "2"])
        def waitReturn = ShipSN.countByStatus("1")


        def returnFail = ReturnGoods.countByStatusInList(['6', '8'])
        def ddthhcount = ReturnGoods.countByStatus('2') //等待退货商品数量
        def xrkcount = ReturnGoods.countByStatus('1') //新入库商品数量
        def thIng = ReturnGoods.countByStatus('5') //退货处理中,代发已领货
        def thFail = returnFail //退不成功
        def needShip = ReturnOrder.countByNeedShip("1");//等待发货的退货申请


        def map = [needShip     : needShip, xrkcount: xrkcount, thFail: thFail, thIng: thIng, ddthhcount: ddthhcount, canExport: canExport, tuihuancount: tuihuancount, waitpay: waitpay, waitaccept: waitaccept,
                   partaccept   : partaccept, allaccept: allaccept,
                   shipped      : shipped, error: error, close: close,
                   delete       : delete, problem: problem, shouli: shouli[0],
                   yanshou      : yanshou[0], weishouli: weishouli[0], diffship: diffship,
                   goods_shipped: goods_shipped, qianshou: qianshou, noowner: noowner,
                   waitReturn   : waitReturn, returnFail: returnFail]


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

    def listMobile() {
        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0

        if (!params.order) params.order = "desc"
        def map = [:]
        if (params.dateCreated) {

            def searchClosure = {

                if (params.status) {
                    eq("status", params.status)
                }
                if (params.orderSN) {
                    like('orderSN', "%" + params.orderSN + "%")
                }
                ne("status", "delete")
                if (params.status == "shipped") { //如果是已发货的订单，按照发货时间查询
                    between('ship_time', Date.parse("yyyy-MM-dd HH:mm:ss", params.dateCreated + ' 00:00:00'), Date.parse("yyyy-MM-dd HH:mm:ss", params.dateCreated + ' 23:59:59'))
                    if (!params.sort) params.sort = "ship_time"
                } else if (params.status == "waitpay") {
                    between('dateCreated', Date.parse("yyyy-MM-dd HH:mm:ss", params.dateCreated + ' 00:00:00'), Date.parse("yyyy-MM-dd HH:mm:ss", params.dateCreated + ' 23:59:59'))
                    if (!params.sort) params.sort = "dateCreated"
                } else {
                    between('payTime', Date.parse("yyyy-MM-dd HH:mm:ss", params.dateCreated + ' 00:00:00'), Date.parse("yyyy-MM-dd HH:mm:ss", params.dateCreated + ' 23:59:59'))
                    if (!params.sort) params.sort = "payTime"
                }

            }



            def o = DaiFaOrder.createCriteria();
            def results = o.list(params, searchClosure)
            map = [list: results, total: results.totalCount]

            if (params.status == 'allaccept') {
                List m_result = new ArrayList()

                results.each { it ->
                    def paramsMap = new HashMap()
                    it.properties.each { k, v ->
                        if (v) {
                            paramsMap.put(k, v.toString())
                        } else {
                            paramsMap.put(k, null)
                        }
                    }
                    paramsMap.put("id", it.id)

                    int yanshou_count = 0
                    it.daiFaGoods.each { goods ->
                        if (goods.status == "7") {
                            yanshou_count = yanshou_count + goods.num;
                        }

                    }

                    paramsMap.put("yanshou_count", yanshou_count)
                    m_result.add(paramsMap)


                    def trandLog = TranLog.findAllByOrderSNAndType(it.orderSN,"4")

                    if(trandLog.size()>0){
                        paramsMap.put("buyunfei", "1")
                    }

                }
                map.list = m_result
            }

        } else if (params.status) {

            def date_time = "d.payTime"
            if (params.status == "shipped") {
                date_time = "d.ship_time"
            }

            if (params.status == "waitpay") {
                date_time = "d.dateCreated"
            }


            def results = new ArrayList();
            def daiFaOrder = DaiFaOrder.executeQuery("select count(d.id) as num," + date_time + " from DaiFaOrder d " +
                    "where d.status = ?  group by substring(" + date_time + ",1,10) order by " + date_time + " desc", [params.status],
                    [max: params.max, offset: params.offset])
            def total = DaiFaOrder.executeQuery("select d.id from DaiFaOrder d " +
                    "where d.status = ?  group by substring(" + date_time + ",1,10)", [params.status]).size()
            println daiFaOrder
            daiFaOrder.each { it ->



//


                Map paramMap = new HashMap()
                paramMap.num = it[0]
                paramMap.dateCreated = it[1].toString().substring(0, 10)
                paramMap.status = params.status

                //如果是发货订单，查询验收商品数量，和实际发货金额
                if (params.status == "shipped") {
                    def daifa_sn = DaiFaOrder.findAllByStatusAndShip_timeBetween("shipped", Date.parse("yyyy-MM-dd HH:mm:ss", paramMap.dateCreated + ' 00:00:00'), Date.parse("yyyy-MM-dd HH:mm:ss", paramMap.dateCreated + ' 23:59:59')).orderSN

                    def searchClosure = {

                        projections {
                            sum("amount")
                        }
                        eq("type", "1")
                        'in'("orderSN", daifa_sn)

                    }

                    paramMap.yanshou_amount = TranLog.createCriteria().list(searchClosure)[0]

                    searchClosure = {

                        projections {
                            sum("amount")
                        }
                        eq("type", "2")
                        'in'("orderSN", daifa_sn)

                    }
                    paramMap.ship_amount = TranLog.createCriteria().list(searchClosure)[0]
//                    paramMap.yanshou_amount = TranLog.executeQuery("select sum(amount) from TranLog where type = '1' and orderSN =?",[daifa_sn])[0]
//                    paramMap.ship_amount = TranLog.executeQuery("select sum(amount) from TranLog where type = '2' and orderSN =?",[daifa_sn])[0]
                }

                results.add(paramMap)
            }
            map = [list: results, total: total]
        }
        //封装分页传给手机端
        def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((map.total - 1) / params.max + 1) as int, total: map.total)
        map.put("pager", pager)

        def mm = new MobileMessage()
        mm.message = "获取订单列表成功"
        mm.result = "success"
        mm.model = map
        render mm as JSON

        //        if(params.mobile=="mobile"){
        //            //封装分页传给手机端
        //            def pager = new Pager(max:params.max,"offset":params.offset,"totalPage":(results.totalCount/params.max + 1) as int ,total:results.totalCount)
        //            map.put("pager",pager)
        //            
        //            def mm = new MobileMessage()
        //            mm.message = "获取订单列表成功"
        //            mm.result = "success"
        //            mm.model = map
        //            
        //            render mm as JSON
        //        }else{
        //            render(view: "/admin/daiFaOrder/list",model:map)
        //        }
    }

    def list() {

        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "dateCreated"
        if (!params.order) params.order = "desc"

        def searchClosure = {

            if (params.wuliu_no) {
                like('wuliu_no', "%" + params.wuliu_no + "%")
            }
            if (params.orderSN) {
                like('orderSN', "%" + params.orderSN + "%")
            }
            if (params.status) {
                eq('status', params.status)
            }
            if (params.add_user) {
                eq('add_user', params.add_user as Long)
            }
            if (params.start_date) {
                ge('ship_time', Date.parse("yyyy-MM-dd HH:mm:ss", params.start_date + " 00:00:00"))
            }
            if (params.end_date) {
                le('ship_time', Date.parse("yyyy-MM-dd HH:mm:ss", params.end_date + " 23:59:59"))
            }
            sort: "payTime"
            order: "asc"

            ne("status", "delete")
        }

        def o = DaiFaOrder.createCriteria();
        def results = o.list(params, searchClosure)


        List m_result = new ArrayList()

        results.each { it ->
            def paramsMap = new HashMap()
            it.properties.each { k, v ->
                if (v) {
                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }
            }
            paramsMap.put("id", it.id)

            int yanshou_count = 0
            it.daiFaGoods.each { goods ->
                if (goods.status == "7") {
                    yanshou_count = yanshou_count + goods.num;
                }

            }

            paramsMap.put("yanshou_count", yanshou_count)
            m_result.add(paramsMap)


            def trandLog = TranLog.findAllByOrderSNAndType(it.orderSN,"4")

            if(trandLog.size()>0){
                paramsMap.put("buyunfei", "1")
            }

        }



        def map = [list: m_result, total: results.totalCount]


        if (params.mobile == "mobile") {

            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()
            mm.message = "获取订单列表成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        } else {
            render(view: "/admin/daiFaOrder/list", model: map)
        }

    }

    //取得已经导出物流单的订单号，就是已经都给货的
    def printWuliuOrderList() {
        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "canExport_date"
        if (!params.order) params.order = "asc"

        def searchClosure = {


            if (params.orderSN) {
                like('orderSN', "%" + params.orderSN + "%")
            }
            if (params.wuliu) {
                eq('wuliu', params.wuliu)
            }

            if (params.add_user) {
                eq('add_user', params.add_user as Long)
            }
            if (params.start_date) {
                ge('ship_time', Date.parse("yyyy-MM-dd HH:mm:ss", params.start_date + " 00:00:00"))
            }
            if (params.end_date) {
                le('ship_time', Date.parse("yyyy-MM-dd HH:mm:ss", params.end_date + " 23:59:59"))
            }
            inList("isCanExport", ['1'])
            ne("status", "delete")
            ne("status", "kill")
        }

        def o = DaiFaOrder.createCriteria();
        def results = o.list(params, searchClosure)

        def map = [list: results, total: results.totalCount]


        if (params.mobile == "mobile") {

            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()
            mm.message = "获取订单列表成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        } else {
            render(view: "/admin/daiFaOrder/list", model: map)
        }
    }

    //将关联订单物流单号
    def printWuliuToOrder() {
        def o = DaiFaOrder.createCriteria();
        def order = o.get { eq("id", new Long(params.id)) }
        def map = [:]
        if (!order) {
            flash.message = "订单出错";
            flash.messageClass = this.error
        } else {
            order.wuliu_no = params.wuliu_no
            order.isCanExport = '3'
            flash.message = "修改物流单号成功";
            flash.messageClass = this.success




            Thread.start {
                mailService.sendMail {
                    from "service@findyi.com"
                    if (isPro == "true") {
                        to order.email
                    } else {
                        to "wufei1310@126.com"
                    }

                    subject "金士代发订单(" + order.orderSN + ")货已备齐"
                    html "您在金士代发订单(订单号：" + order.orderSN + ")货已备齐，正在移交物流公司并做最后一次质量检查。物流单号：" + params.wuliu_no + "!<a href='http://kingsdf.cn' target='_blank' >去金士代发</a>"

                }
            }


        }
        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            map.order = order
            mm.message = flash.message
            mm.result = "success"
            mm.model = map
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }


    def myList() {
        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "dateCreated"
        if (!params.order) params.order = "desc"

        def searchClosure = {


            if (params.orderSN) {
                like('orderSN', "%" + params.orderSN + "%")
            }
            if (params.status) {
                eq('status', params.status)
            }
            eq("processing_user_id", session.loginPOJO.user.id)
            ne("status", "delete")
        }

        def o = DaiFaOrder.createCriteria();
        def results = o.list(params, searchClosure)

        def map = [list: results, total: results.totalCount]


        if (params.mobile == "mobile") {
            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()
            mm.message = "获取订单列表成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        } else {
            render(view: "/admin/daiFaOrder/list", model: map)
        }
    }

    def show() {

        def order = DaiFaOrder.get(params.id)




        if (!order && params.orderSN) {
            order = DaiFaOrder.findByOrderSN(params.orderSN)

            if (!order) {
                def mm = new MobileMessage()
                mm.message = "订单号错误"
                mm.result = "fail"

                render mm as JSON
                return
            }

        }

        if (!order && params.wuliu_no) {
            order = DaiFaOrder.findByWuliu_no(params.wuliu_no)

            if (!order) {
                def mm = new MobileMessage()
                mm.message = "该物流单号还未绑定订单"
                mm.result = "fail"

                render mm as JSON
                return
            }

        }




        def map = [diffOrderList: order?.diffOrder, order: order, goods: order.daiFaGoods]

        def diffWaitpayOrder = DiffOrder.findByOrderSNAndStatus(order.orderSN, 'waitpay')
        map.diffWaitpayOrder = diffWaitpayOrder
        map.email = User.get(order.add_user).email

        if (params.mobile == "mobile") {

            //map.order.address = g.areaName("area_id":map.order.area_id) + map.order.address
            def paramsMap = new HashMap()
            order.properties.each { k, v ->
                if (v) {
                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }

            }

            Long num = 0
            order.daiFaGoods.each {
                if (it.status == "7") {
                    num = num + it.num
                }
            }

            paramsMap.put("wuliu_no", order.wuliu_no)
            paramsMap.put("wuliu", order.wuliu)
            paramsMap.put("id", order.id)
            paramsMap.put("num", num)

            def address = g.areaName("area_id": paramsMap.area_id) + paramsMap.address
            paramsMap.address = address
            map.order = paramsMap

            def mm = new MobileMessage()
            mm.message = "获取订单详细页成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON


        } else {
            render(view: "/admin/daiFaOrder/show", model: map)
        }

    }

    def updateForProcessing() {

        def o = DaiFaOrder.createCriteria();
        def order = o.get { eq("status", "waitaccept") eq("id", new Long(params.id)) }
        def map = [:]
        if (!order) {
            flash.message = "受理失败";
            flash.messageClass = this.error
            map = [status: 'waitaccept']
        } else {
            //order.lock() 
            order.status = "processing"
            order.processing_user_id = session.loginPOJO.user.id
            order.processing_user_name = session.loginPOJO.user.email
            order.processing_time = new Date();
            order.save()
            flash.message = "受理成功";
            flash.messageClass = this.success
            map = [status: 'processing']

            //受理成功发邮件
            def user = User.get(order.add_user)
            Thread.start {
                sendMail {
                    async true
                    from "service@findyi.com"
                    to user.email
                    subject "您的代发订单（" + order.orderSN + "）已经被成功受理"
                    body "您的代发订单（" + order.orderSN + "）已经被成功受理，请耐心等待发货通知"
                }
                print "邮件地址" + user.email + " success"
            }
        }
        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            map.order = order

            mm.message = flash.message
            mm.result = "success"
            mm.model = map
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            //  redirect(action: "list", params: params)
            return
        }

    }

    def updateForShippedAgain() {
        def o = DaiFaOrder.createCriteria();
        def order = o.get { eq("status", "shipped") eq("id", new Long(params.id)) }
        def map = [:]
        if (!order) {
            flash.message = "订单出错";
            flash.messageClass = this.error
        } else {
            order.wuliu_no = params.wuliu_no
            flash.message = "修改物流单号成功";
            flash.messageClass = this.success

        }
        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            map.order = order
            mm.message = flash.message
            mm.result = "success"
            mm.model = map
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }


    def updateForShipped() {
        def mm = new MobileMessage()
        def o = DaiFaOrder.createCriteria();
        def order = o.get { eq("status", "allaccept") eq("id", new Long(params.id)) }
        def map = [:]


        if (!order) {
            flash.message = "发货失败,订单不存在";
            flash.messageClass = this.error
            map = [status: 'allaccept']
        } else {
            // order.lock()

             order.weight = params.weight

            Float weight = new Float(params.weight)

            BigDecimal actual_shipfee = StringUtil.checkShipByWeight(order.wuliu,order.area_id,weight)

            if(actual_shipfee>order.shipFee){
                flash.message = "实际运费"+actual_shipfee+"高于会员支付运费";
                order.actual_shipfee = actual_shipfee
                mm.result = "fail"
                map = [order: order]
            }else{
                params.actual_shipfee =   actual_shipfee
                adminDaiFaOrderService.updateForShipped(params, order)
                flash.message = "发货成功";
                flash.messageClass = this.success
                mm.result = "success"
                map = [status: 'shipped']
            }






        }
        if (params.mobile == "mobile") {

            map.order = order
            mm.message = flash.message

            mm.model = map
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def shipDiff() {
        def o = DaiFaOrder.createCriteria();
        def order = o.get { eq("status", "allaccept") eq("id", new Long(params.id)) }
        def actual_shipfee = new BigDecimal(params.actual_shipfee)
        def map = [:]
        if (!order) {
            flash.message = "提交运费补款失败,订单出错";
            flash.messageClass = this.error
        } else if (actual_shipfee <= order.shipFee) {
            flash.message = "提交运费补款失败,订单出错";
            flash.messageClass = this.error
        } else {
            order.status = "diffship"
            order.diffShip = actual_shipfee - order.shipFee
            order.actual_shipfee = actual_shipfee

            def orderUser = User.get(order.add_user)
            Thread.start {
                mailService.sendMail {
                    from "service@findyi.com"
                    if (isPro == "true") {
                        to orderUser.email
                    } else {
                        to "wufei1310@126.com"
                    }

                    subject "金士代发补运费通知"
                    html "您在金士代发订单(订单号：" + order.orderSN + ")已经拿货完成。由于实物超重，需要补运费，为不影响您的发货，请尽快补齐运费!<a href='http://kingsdf.cn' target='_blank' >去金士代发</a>"

                }
            }

            flash.message = "提交运费补款成功";
            flash.messageClass = this.success
        }
        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            map.order = order
            mm.message = flash.message
            mm.result = "success"
            mm.model = map
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def updateForDiff() {
        def o = DaiFaOrder.createCriteria();
        def order = o.get { eq("status", "processing") eq("id", new Long(params.id)) }
        def map = [:]


        if (!order) {
            flash.message = "补款失败,订单出错";
            flash.messageClass = this.error
            map = [status: 'processing']
        } else if (!order.processing_user_id.equals(session.loginPOJO.user.id)) {
            flash.message = "补款失败，不可以补款别人受理的订单";
            flash.messageClass = this.error
            map = [status: 'processing']
        } else {
            //  order.lock() 
            BigDecimal diffFee = new BigDecimal(params.diffFee)
            order.status = "waitpaydiff"
            order.totalFee = order.totalFee + diffFee
            order.diffFee = order.diffFee + diffFee

            def diffOrder = new DiffOrder();

            diffOrder.diffFee = diffFee
            diffOrder.reason = params.reason
            diffOrder.status = "waitpay"
            diffOrder.orderSN = order.orderSN
            diffOrder.add_user = session.loginPOJO.user.id
            order.addToDiffOrder(diffOrder)

            order.save()
            flash.message = "补款成功";
            flash.messageClass = this.success
            map = [status: 'waitpaydiff']

            //补款成功发邮件
            def user = User.get(order.add_user)
            Thread.start {
                sendMail {
                    async true
                    from "service@findyi.com"
                    to user.email
                    subject "您的代发订单（" + order.orderSN + "）需要补款差额"
                    body "您的代发订单（" + order.orderSN + "）需要补款差额，请尽快登录金士代发平台支付差额 "
                }
            }
        }
        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            map.order = order
            map.diffOrderList = order.diffOrder
            mm.message = flash.message
            mm.result = "success"
            mm.model = map
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }

    }

    def updateForDiffAgain() {
        def o = DaiFaOrder.createCriteria();
        def order = o.get {
            eq("status", "waitpaydiff") eq("id", new Long(params.id)) eq("processing_user_id", session.loginPOJO.user.id)
        }
        def map = [:]


        if (!order) {
            flash.message = "修改补款失败,订单出错";
            flash.messageClass = this.error
            map = [status: 'waitpaydiff']
        } else {
            //order.lock() 

            def diffWaitpayOrder = DiffOrder.findByOrderSNAndStatus(order.orderSN, 'waitpay')

            BigDecimal diffFee = new BigDecimal(params.diffFee)

            order.totalFee = order.totalFee + diffFee - diffWaitpayOrder.diffFee
            order.diffFee = order.diffFee + diffFee - diffWaitpayOrder.diffFee

            order.save()



            diffWaitpayOrder.diffFee = diffFee
            diffWaitpayOrder.reason = params.reason
            diffWaitpayOrder.save()





            flash.message = "修改补款";
            flash.messageClass = this.success
            map = [status: 'waitpaydiff']


        }
        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            map.order = order
            map.diffOrderList = order.diffOrder
            mm.message = flash.message
            mm.result = "success"
            mm.model = map
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }

    }


    def saleReturnList() {
        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        params.sort = "dateCreated"
        params.order = "desc"

        def searchClosure = {

            if (params.orderSN) {
                like('orderSN', "%" + params.orderSN + "%")
            }

            if (params.wuliu_sn) {
                like('wuliu_sn', "%" + params.wuliu_sn + "%")
            }

            if (params.status) {
                eq('status', params.status)
            }
            if (params.needTui) {
                eq('needTui', params.needTui)
            }
            if (params.type) {
                eq('type', params.type)
            }
            if (params.needShip) {
                eq('needShip', params.needShip)
            }
//            if (params.isScan) {
//                eq('isScan', params.isScan)
//            }


            if (params.mobile == "mobile"){
                params.orderfrom="kings"
            }
            println params.orderfrom
            if (params.orderfrom) {
                eq('orderfrom', params.orderfrom)
            }

            if(params.ishuiyuanxiadan){
                eq('ishuiyuanxiadan', params.ishuiyuanxiadan)

            }


        }

        def o = ReturnOrder.createCriteria();
        def results = o.list(params, searchClosure)

        def map = [list: results, total: results.totalCount]

        if (params.mobile == "mobile") {

            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)


            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "success"
            mm.model = map
            render mm as JSON
        } else {
            render(view: "/admin/saleReturn/list", model: map)
        }
    }

    def saleReturnShow() {
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
            returnGoodsPOJO.returnReason = it.returnReason;

            returnGoodsPOJO.shouliUser = it.shouliUser
            returnGoodsPOJO.shouliTime = it.shouliTime


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
            returnGoodsPOJO.type = it.type   //退

            list.add(returnGoodsPOJO)

            if (params.fromReq == "wuliu" && it.type == "1") {
                wuliu_list.add(returnGoodsPOJO)
            }


        }

        if (params.fromReq == "wuliu") {
            list = wuliu_list;
        }



        def map = [list: list, area_name: g.areaName("area_id": returnOrder.daiFaOrder?.area_id), daiFaOrder: returnOrder.daiFaOrder, returnOrder: returnOrder]



        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            if ((params.fromReq == "wuliu") && map.returnOrder.needShip == '2') {
                mm.result = "error"
            } else {
                mm.message = flash.message
                mm.result = "success"
                mm.model = map
            }
            render mm as JSON
        } else {
//            if(returnOrder.type=='0'){
//                render(view: "/admin/saleReturn/show",model:map)
//            }else{
            render(view: "/admin/saleReturn/saleExchangeShow", model: map)
//            }
        }


    }

    def checkSaleReturn() {
        try {
            adminDaiFaOrderService.checkSaleReturn(params, User.get(session.loginPOJO.user.id))
            flash.message = "操作成功"
            flash.messageClass = this.success
        } catch (MessageException e) {
            flash.message = e.getMessage()
            flash.messageClass = "error"

        }



        if (params.mobile == "mobile") {
            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "success"
            if (flash.messageClass == "error") {
                mm.result = "fail"
            }
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            return
        }

    }

    def saleReturnAmountList() {
        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        params.sort = "dateCreated"
        params.order = "desc"

        def searchClosure = {

            if (params.orderSN) {
                like('orderSN', "%" + params.orderSN + "%")
            }
            if (params.needTui) {
                eq('needTui', params.needTui)
            } else {
                inList('needTui', ['1', '2'])
            }

            if (params.isling) {
                def userlist = User.findAllByUser_type(params.isling)
                println userlist.id
                inList('add_user', userlist.id)
            }


        }

        def o = ReturnOrder.createCriteria();
        def results = o.list(params, searchClosure)

        def map = [list: results, total: results.totalCount]



        render(view: "/admin/saleReturn/amountList", model: map)

    }

    def saleReturnAmountShow() {
        def returnOrder = ReturnOrder.get(params.id)

        def returnFee = 0

        returnOrder.returnGoods.each {
            if (it.type == "0" && it.status == "4") {  // 4：已退货，档口退款
                returnFee = returnFee + it.actual_return_fee * it.return_num
            }
        }

        def memberReturnOrder
        if(returnOrder.ishuiyuanxiadan=="1"){
            memberReturnOrder  = ReturnOrder.findByOrderSNAndOrderfrom("M"+returnOrder.orderSN.substring(1),"member")
        }


        def map = [memberReturnOrder:memberReturnOrder,returnOrder: returnOrder, returnFee: returnFee]
        render(view: "/admin/saleReturn/amountShow", model: map)

    }

    def test(){
        def returnOrderList = ReturnOrder.findAllByOrderfromAndStatusAndNeedTui("kings","2","1")
       // render    returnOrderList as JSON

        returnOrderList.each{
            render it.orderSN.substring(1)
            def memberReturnOrder = ReturnOrder.findByOrderSNAndOrderfrom("M"+it.orderSN.substring(1),"member")
            render memberReturnOrder as JSON
            if(memberReturnOrder){
              it.ishuiyuanxiadan="1"
              it.save();
            }
        }


    }


    def saleReturnAmount() {
        adminDaiFaOrderService.checkSaleReturn(params, User.get(session.loginPOJO.user.id))
        flash.message = "退款成功"
        flash.messageClass = this.success

        render(view: this.commonSuccess)

        return
    }

}


//
//reperson: 李佳宁
//contphone: 18714512495
//wuliu_sn: 560163826414
//mobile: mobile
//Cannot invoke method split() on null object. Stacktrace follows:
//java.lang.NullPointerException: Cannot invoke method split() on null object
//at admin.AdminMobileController.findGoodsFromPhone(AdminMobileController.groovy:1539)
//at grails.plugin.cache.web.filter.PageFragmentCachingFilter.doFilter(PageFragmentCachingFilter.java:195)
//at grails.plugin.cache.web.filter.AbstractFilter.doFilter(AbstractFilter.java:63)
//at java.lang.Thread.run(Thread.java:662)
//Hibernate: select count(returngood0_.id) as col_0_0_ from return_goods returngood0_ where returngood0_.market=? and returngood0_.status='2' and returngood0_.orderfrom='kings'
//        {"name":"西街","count":2,"thh":0}
//Hibernate: select count(daifagoods0_.id) as col_0_0_ from dai_fa_goods daifagoods0_ inner join dai_fa_order daifaorder1_ on daifagoods0_.dai_fa_order_id=daifaorder1_.id where daifagoods0_.market=? and daifagoods0_.status