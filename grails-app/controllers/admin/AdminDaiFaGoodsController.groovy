package admin

import ship.DaiFaOrder
import grails.converters.JSON
import ship.MobileMessage
import ship.Pager
import ship.ReturnGoods
import ship.ReturnOrder
import ship.ShipSN
import ship.User
import ship.DiffOrder
import ship.DiffGoods
import ship.DaiFaGoods
import ship.GoodsLog
import ship.Market
import sysinit.SysInitParams
import util.PrintDic
import util.StringUtil
import ship.PushPOJO
import util.Push
import ship.PushMsg
import view.Qianshouview

import java.text.SimpleDateFormat

class AdminDaiFaGoodsController extends BaseController {

    def adminDaiFaGoodsService
    def adminDaiFaOrderService

    def memberDaiFaService






    def toOrderGoods() {
        def g = DaiFaGoods.get(params.id)
        if(params.status){
            if(g.status!=params.status){
                def mm = new MobileMessage()
                if(params.status=="2"){
                    mm.message = "该商品还不是等待验收状态"
                }

                mm.result = "fail"
                render mm as JSON
                return;
            }
        }






        def diffGoods = DiffGoods.findAllByDaiFaGoods(g, [sort: "id", order: "desc"])

        def goodsLog = GoodsLog.findAllByDaiFaGoods(g, [sort: "id", order: "desc"])



        if (params.mobile == "mobile") {
            def paramsMap = new HashMap()
            g.properties.each { k, v ->
                if (v) {

                    if(k=="market"){
                        paramsMap.put("market_en", PrintDic.market2en(v))
                    }
                    if(k=="floor"){
                        paramsMap.put("floor_en", PrintDic.floor2en(v))
                    }

                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }
            }

            def area_name  = SysInitParams.areaMap.get(g.daiFaOrder.area_id).name
            paramsMap.put('area_name', area_name)
            paramsMap.put('payTime', g.daiFaOrder.payTime)
            paramsMap.put("orderSN", g.daiFaOrder.orderSN)

            def sn = g.daiFaOrder.orderSN
            def order_sn_print =  sn.substring(6)
            paramsMap.put("order_sn_print", order_sn_print)

            paramsMap.put("order_status", g.daiFaOrder.status)
            paramsMap.put("senddesc", g.daiFaOrder.senddesc)
            paramsMap.put("h_senddesc", g.daiFaOrder.h_senddesc)
            paramsMap.put("id", g.id)



            paramsMap.put("zzfw", g.daiFaOrder.regards)


            goodsLog.each {
                println it as JSON
                if(it.addUser.user_type=="member"){
                    it.operuser = "会员";
                }else{
                    it.operuser = it.addUser.email;
                }

            }





            if (g.daifa_user) {
                paramsMap.put("df_user_id", g.daifa_user.id)
                paramsMap.put('daifa_user_name', g.daifa_user.email)
            }

            if(g.check_user){
                paramsMap.put('check_user_name', g.check_user.email)
            }

            def mm = new MobileMessage()
            mm.message = "获得代发商品成功"
            mm.result = "success"
            mm.model.put("ordergoods", paramsMap)
            mm.model.put("goodsLogList",goodsLog)
            render mm as JSON
            return;
        } else {
            def map = [daiFaGoods: g, diffGoodsList: diffGoods, goodsLogList: goodsLog]
            render(view: "/admin/daiFaGoods/show", model: map)
        }


    }

    def showMy() {
        def g = DaiFaGoods.get(params.id)
        def diffGoods = g.diffGoods
        def goodsLog = GoodsLog.findAllByDaiFaGoods(g)
        def map = [daiFaGoods: g, diffGoodsList: diffGoods, goodsLogList: goodsLog]
        render(view: "/admin/daiFaGoods/showMy", model: map)
    }


    def orderGoodsDiff() {
        def g = DaiFaGoods.get(params.id)
        def diffGoods = g.diffGoods
        if (params.mobile == "mobile") {


            def paramsMap = new HashMap()
            g.properties.each { k, v ->
                if (v) {
                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }
            }
            paramsMap.put('id', g.id)
            paramsMap.put('orderSN', g.daiFaOrder.orderSN)
            if (g.daifa_user) {
                paramsMap.put('daifa_user_name', g.daifa_user.email)
            } else {
                paramsMap.put('daifa_user_name', null)
            }
            if (g.check_user) {
                paramsMap.put('check_user_name', g.check_user.email)
            } else {
                paramsMap.put('check_user_name', null)
            }


            def mm = new MobileMessage()
            mm.message = "获得代发商品补差价列表成功"
            mm.result = "success"
            mm.model.put("daifaGoods", paramsMap)
            mm.model.put("diffGoods", diffGoods)
            render mm as JSON
            return;
        }

    }


    def index() {}


    def waitquhuomarket(){
        def searchMarket = {}
        def marketList = Market.createCriteria().list(searchMarket)

        def resultList = []
        marketList.each { it ->
            def m_map = [:]


            m_map.market = it.market_name
            m_map.num = DaiFaGoods.executeQuery("select count(a.id) from DaiFaGoods a join a.daiFaOrder d where a.market = ? and a.status='0' and d.status<>'delete' and  d.status<>'waitpay'", [it.market_name])[0]

            resultList.add(m_map)
        }


        def mm = new MobileMessage()
        mm.message = "获取市场等待取货商品数量成功"
        mm.result = "success"
        mm.model = [list: resultList]
        render mm as JSON

    }


    def list() {

        def searchMarket = {

            user {
                eq("id", session.loginPOJO.user.id)
            }
        }
        def marketList = []
        if (session.loginPOJO.user.user_type == "admin") {
            if(params.market){
                marketList.add(params.market)
            }else{
                marketList = Market.findAll().market_name;
            }

        }

        if (session.loginPOJO.user.user_type == "daifa") {
            marketList = Market.createCriteria().list(searchMarket).market_name
        }


        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        params.sort = "dateCreated"
        params.order = "desc"

        def results = []
        def map
        if (marketList) {
            def searchClosure = {


                if (params.goods_sn) {
                    like('goods_sn', "%" + params.goods_sn + "%")
                }
                if (params.market) {
                    like('market', "%" + params.market + "%")
                    params.sort = "floor"
                    params.order = "desc"
                }
                if (params.status) {
                    eq("status", params.status)
                    if (params.status == "2") {
                        params.sort = "daiFaOrder.payTime"
                        params.order = "asc"
                    }
                }
                daiFaOrder {
                    ne("status", "delete")
                    ne("status", "waitpay")
                    if (params.orderSN) {
                        like('orderSN', "%" + params.orderSN + "%")
                    }
                }
                if (params.email) {
                    daifa_user {
                        like('email', "%" + params.email + "%")
                    }
                }


                'in'("market", marketList)


            }

            def o = DaiFaGoods.createCriteria();
            results = o.list(params, searchClosure)





            map = [list: results, total: results.totalCount]
        } else {
            map = [list: results, total: 0]
        }

        map.marketList = marketList



        if (params.mobile == "mobile") {
            List m_result = new ArrayList()

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String nowDate = df.format(new Date())
            def startDate = Date.parse("yyyy-MM-dd HH:mm:ss",nowDate+" 00:00:00")




            results.each { it ->
                def paramsMap = new HashMap()
                it.properties.each { k, v ->
                    if (v) {
                        if(k=="market"){
                            paramsMap.put("market_en", PrintDic.market2en(v))
                        }
                        if(k=="floor"){
                            paramsMap.put("floor_en", PrintDic.floor2en(v))
                        }
                        paramsMap.put(k, v.toString())
                    } else {
                        paramsMap.put(k, null)
                    }
                }

                paramsMap.put("tip","")
                if(it.daiFaOrder.payTime>startDate){//判断是否当天下的单　
                    if(it.tip){
                        paramsMap.put("tip",it.tip)
                    }
                }

                paramsMap.put('payTime', it.daiFaOrder.payTime)
                paramsMap.put('id', it.id)
                paramsMap.put('orderSN', it.daiFaOrder.orderSN)
                paramsMap.put('senddesc',it.daiFaOrder.senddesc)
                paramsMap.put('zzfw',it.daiFaOrder.regards)
                def sn = it.daiFaOrder.orderSN
//                def order_sn_print =  sn[0..6]+ " "+sn[6..8] + " " +sn.substring(9)
                def order_sn_print =  sn.substring(6)
                paramsMap.put("order_sn_print", order_sn_print)
                if (it.daifa_user) {
                    paramsMap.put('daifa_user_name', it.daifa_user.email)
                } else {
                    paramsMap.put('daifa_user_name', null)
                }
                if (it.check_user) {
                    paramsMap.put('check_user_name', it.check_user.email)
                } else {
                    paramsMap.put('check_user_name', null)
                }
                m_result.add(paramsMap)
            }
            map.list = m_result
            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((map.total - 1) / params.max + 1) as int, total: map.total)
            map.put("pager", pager)

            def mm = new MobileMessage()
            mm.message = "获取商品列表成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        } else {
            render(view: "/admin/daiFaGoods/list", model: map)
        }

    }


    def myList() {



        def searchMarket = {

            user {
                eq("id", session.loginPOJO.user.id)
            }
        }
        def marketList = Market.createCriteria().list(searchMarket).market_name

        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }


        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "dateCreated"
        if (!params.order) params.order = "desc"

        def searchClosure = {


            if (params.goods_sn) {
                like('goods_sn', "%" + params.goods_sn + "%")
            }
            if (params.market) {
                like('market', "%" + params.market + "%")
            }
            if (params.orderSN) {
                daiFaOrder {

                    like('orderSN', "%" + params.orderSN + "%")
                }
            }
            if (params.status) {
                if (params.mobile == "mobile" && params.status == '100') {
                    or {
                        eq("status", "2")
                        eq("status", "3")
                        eq("status", "8")
                    }
                } else {
                    eq("status", params.status)
                }

            }
            daifa_user {
                eq("id", session.loginPOJO.user.id)
            }
        }

        def o = DaiFaGoods.createCriteria();
        def results = o.list(params, searchClosure)

        def map = [list: results, total: results.totalCount, marketList: marketList]


        if (params.mobile == "mobile") {

            //如果是已受理商品，传送价格总数
            if (params.status == '1') {
                def shouli_amount = DaiFaGoods.executeQuery("select sum(d.price*d.num) as amount from DaiFaGoods d " +
                        "where d.status = '1' and d.daifa_user = ?", [User.get(session.loginPOJO.user.id)])[0]
                map.shouli_amount = shouli_amount
            }

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
                paramsMap.put('payTime', it.daiFaOrder.payTime)
                paramsMap.put('id', it.id)
                paramsMap.put('orderSN', it.daiFaOrder.orderSN)
                if (it.daifa_user) {
                    paramsMap.put('daifa_user_name', it.daifa_user.email)
                } else {
                    paramsMap.put('daifa_user_name', null)
                }
                if (it.check_user) {
                    paramsMap.put('check_user_name', it.check_user.email)
                } else {
                    paramsMap.put('check_user_name', null)
                }
                m_result.add(paramsMap)
            }
            map.list = m_result

            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((results.totalCount - 1) / params.max + 1) as int, total: results.totalCount)
            map.put("pager", pager)

            def mm = new MobileMessage()
            mm.message = "获取商品列表成功"
            mm.result = "success"
            mm.model = map

            render mm as JSON
        } else {
            render(view: "/admin/daiFaGoods/mylist", model: map)
        }
    }

    //代发人员受理商品
    def process() {
        def o = DaiFaGoods.createCriteria();
        def goods = o.get {
            eq("status", "0")
            eq("id", new Long(params.id))
            lock: true
        }


        if (!goods) {
            flash.message = "商品已受理";
            flash.messageClass = this.error
        } else {
            goods.status = "1"
            goods.processtime = new Date()
            goods.daifa_user = User.get(session.loginPOJO.user.id)
            flash.message = "商品受理成功";
            flash.messageClass = this.success
            goods.save();

            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = goods.daifa_user
            goodsLog.logdesc = "已受理"
            goodsLog.save()
        }
        if (params.mobile == "mobile") {

            def mm = new MobileMessage()
            mm.message = flash.message
            if (flash.messageClass == this.error) {
                mm.result = "fail"
            } else {
                mm.result = "success"



                def paramsMap = new HashMap()
                goods.properties.each { k, v ->
                    if (v) {
                        paramsMap.put(k, v.toString())
                    } else {
                        paramsMap.put(k, null)
                    }
                }
                paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
                paramsMap.put("id", goods.id)



                mm.model.put("ordergoods", paramsMap)
            }
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }


    def killIsNaHuo(){
        def o = DaiFaGoods.createCriteria();
        def goods = o.get { eq("status", "kill_wait") eq("id", new Long(params.id)) }

        if (!goods || goods.daifa_user.id != session.loginPOJO.user.id) {
            flash.message = "这不是你的商品";
            flash.messageClass = this.error
        } else{
            if(params.status=="hasNaHuo"){
                goods.status = "2"
                goods.nahuotime = new Date()
                goods.save();
            }
            if(params.status=="noNaHuo"){
                goods.status = "0"
                goods.save();
            }
            flash.message = "提交成功"
        }

        params.orderId =  goods.daiFaOrder.id;
        memberDaiFaService.killOrderIng(params);


        def mm = new MobileMessage()
        mm.message = flash.message
        mm.result = "success"
        render mm as JSON
    }




    def nahuo() {
        def o = DaiFaGoods.createCriteria();
        def goods = o.get { eq("status", "1") eq("id", new Long(params.id)) }


        if (!goods || goods.daifa_user.id != session.loginPOJO.user.id) {
            flash.message = "拿货出错";
            flash.messageClass = this.error
        } else {
            goods.status = "2"
            goods.actual_price = new BigDecimal(params.actual_price)
            goods.nahuotime = new Date()
            flash.message = "商品拿货成功";
            flash.messageClass = this.success
            goods.save();

            adminDaiFaOrderService.updateForNaHuo(goods.daiFaOrder)

            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = goods.daifa_user
            goodsLog.logdesc = "已拿货,实际单价" + goods.actual_price
            goodsLog.save()
        }
        if (params.mobile == "mobile") {

            def paramsMap = new HashMap()
            goods.properties.each { k, v ->
                if (v) {
                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }
            }





            def shouli_amount = DaiFaGoods.executeQuery("select sum(d.price*d.num) as amount from DaiFaGoods d " +
                    "where d.status = '1' and d.daifa_user = ?", [User.get(session.loginPOJO.user.id)])[0]


            if(!shouli_amount){
                shouli_amount = "0"
            }

            paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
            paramsMap.put("id", goods.id)


            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "success"
            mm.model.put("ordergoods", paramsMap)
            mm.model.put("nahuook", "1")
            mm.model.put("shouli_amount", shouli_amount)
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def noNahuo() {
        def o = DaiFaGoods.createCriteria();
        def goods = o.get { eq("status", "1") eq("id", new Long(params.id)) }


        if (!goods || goods.daifa_user.id != session.loginPOJO.user.id) {
            flash.message = "操作出错";
            flash.messageClass = this.error
        } else {
            goods.status = "3"
            goods.actual_price = new BigDecimal(params.actual_price)
            //goods.nahuotime = new Date()
            flash.message = "操作成功";
            flash.messageClass = this.success
            goods.save();


            params.id = goods.id
//            goods = adminDaiFaGoodsService.doBukuan(params, User.get(session.loginPOJO.user.id))
//            flash.message = "提交补款成功"
//            flash.messageClass = this.success

            //暂时价格过高修改状态,进入手机的问题订单，提醒管理员操作
            def order = goods.daiFaOrder
            def goodsStatusList = order.daiFaGoods.status
            def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
            order.status = o_status

            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = goods.daifa_user
            goodsLog.logdesc = "商品价格过高，暂不拿货，实际单价" + goods.actual_price
            goodsLog.save()

            //价格过高推送
//            def poSearchClosure = {
//                eq("user_type", "admin")
//                eq("role", "admin")
//            }
//            def po = PushMsg.createCriteria();
//            def results = po.list(poSearchClosure)
//
//
//            PushPOJO pushPOJO = new PushPOJO();
//            pushPOJO.title = "订单号：" + order.orderSN + "出现问题";
//            pushPOJO.content = "订单号：" + order.orderSN + "#商品货号：" + goods.goods_sn + "#价格过高，暂不拿货，请尽快处理";
//            pushPOJO.pushMsgList = results
//            new Push().pushByStore(pushPOJO)

        }
        if (params.mobile == "mobile") {

            def paramsMap = new HashMap()
            goods.properties.each { k, v ->
                if (v) {
                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }
            }
            paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
            paramsMap.put("id", goods.id)
            def shouli_amount = DaiFaGoods.executeQuery("select sum(d.price*d.num) as amount from DaiFaGoods d " +
                    "where d.status = '1' and d.daifa_user = ?", [User.get(session.loginPOJO.user.id)])[0]

            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "success"
            mm.model.put("ordergoods", paramsMap)
            mm.model.put("shouli_amount", shouli_amount)
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def bukuan() {

        def goods
        try {
            goods = adminDaiFaGoodsService.doBukuan(params, User.get(session.loginPOJO.user.id))
            flash.message = "提交补款成功"
            flash.messageClass = this.success

        } catch (Exception e) {
            flash.message = e.getMessage()
            flash.messageClass = this.error
        }
        if (params.mobile == "mobile") {
            if (flash.messageClass == this.success) {
                def paramsMap = new HashMap()
                goods.properties.each { k, v ->
                    if (v) {
                        paramsMap.put(k, v.toString())
                    } else {
                        paramsMap.put(k, null)
                    }
                }
                paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
                paramsMap.put("id", goods.id)


                def mm = new MobileMessage()
                mm.message = flash.message
                mm.result = "success"
                mm.model.put("ordergoods", paramsMap)
                render mm as JSON
            } else {
                def mm = new MobileMessage()
                mm.message = flash.message
                mm.result = "fail"
                mm.model.put("ordergoods", goods)
                render mm as JSON
            }


        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def zsquehuo() {
        def o = DaiFaGoods.createCriteria();
        def goods = o.get { eq("status", "1") eq("id", new Long(params.id)) }


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = df.format(new Date())
        def time4 = Date.parse("yyyy-MM-dd HH:mm:ss",nowDate+" 16:00:00")




        if (!goods || goods.daifa_user.id != session.loginPOJO.user.id) {
            flash.message = "操作出错";
            flash.messageClass = this.error
        }
//        else if(goods.daiFaOrder.payTime>time4) {
//            goods.tip = nowDate+"暂缺货，明天再去档口拿货"
//
//            goods.status = "0"
//            flash.message = "操作成功";
//            flash.messageClass = this.success
//        }
        else {
            goods.status = "8"
            goods.shuoming = params.shuoming
            //goods.actual_price = new BigDecimal(params.actual_price)
            //goods.nahuotime = new Date()
            flash.message = "操作成功";
            flash.messageClass = this.success
            goods.save();
            params.id = goods.id
            //adminDaiFaGoodsService.doQuehuo(params, User.get(session.loginPOJO.user.id))

            //暂时缺货修改状态,进入手机的问题订单，提醒管理员操作
            def order = goods.daiFaOrder
            def goodsStatusList = order.daiFaGoods.status
            def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
            order.status = o_status

            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = goods.daifa_user
            goodsLog.logdesc = "缺货，暂不拿货"
            goodsLog.save()

            //价格过高推送
//            def poSearchClosure = {
//                eq("user_type", "admin")
//                eq("role", "admin")
//            }
//            def po = PushMsg.createCriteria();
//            def results = po.list(poSearchClosure)
//
//
//            PushPOJO pushPOJO = new PushPOJO();
//            pushPOJO.title = "订单号：" + order.orderSN + "出现问题";
//            pushPOJO.content = "订单号：" + order.orderSN + "#商品货号：" + goods.goods_sn + "#缺货，暂不拿货，请尽快处理";
//            pushPOJO.pushMsgList = results
//            new Push().pushByStore(pushPOJO)

        }
        if (params.mobile == "mobile") {

            def paramsMap = new HashMap()
            goods.properties.each { k, v ->
                if (v) {
                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }
            }
            paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
            paramsMap.put("id", goods.id)
            def shouli_amount = DaiFaGoods.executeQuery("select sum(d.price*d.num) as amount from DaiFaGoods d " +
                    "where d.status = '1' and d.daifa_user = ?", [User.get(session.loginPOJO.user.id)])[0]
            paramsMap.put("shouli_amount", shouli_amount)

            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "success"
            mm.model.put("ordergoods", paramsMap)
            mm.model.put("shouli_amount", shouli_amount)
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }


    def buhege() {
        def goods

        try {
            goods = adminDaiFaGoodsService.buhege(params, User.get(session.loginPOJO.user.id))
            flash.message = "提交信息成功"
            flash.messageClass = this.success

        } catch (Exception e) {
            flash.message = e.getMessage()
            flash.messageClass = this.error
        }


        if (params.mobile == "mobile") {


            def mm = new MobileMessage()
            mm.message = flash.message
            if (flash.messageClass == this.success) {
                def paramsMap = new HashMap()
                goods.properties.each { k, v ->
                    if (v) {
                        paramsMap.put(k, v.toString())
                    } else {
                        paramsMap.put(k, null)
                    }
                }
                paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
                paramsMap.put("id", goods.id)
                mm.result = "success"
                mm.model.put("ordergoods", paramsMap)
            } else {
                mm.result = "fail"
                mm.model.put("ordergoods", null)
            }
            render mm as JSON

        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }


    }

    def yanshou() {
        def goods
        try {
            goods = adminDaiFaGoodsService.doYanshou(params, User.get(session.loginPOJO.user.id))
            flash.message = "验收成功"
            flash.messageClass = this.success

        } catch (Exception e) {
            flash.message = e.getMessage()
            flash.messageClass = this.error
        }
        if (params.mobile == "mobile") {


            def mm = new MobileMessage()
            mm.message = flash.message
            if (flash.messageClass == this.success) {
                def paramsMap = new HashMap()
                goods.properties.each { k, v ->
                    if (v) {
                        paramsMap.put(k, v.toString())
                    } else {
                        paramsMap.put(k, null)
                    }
                }
                paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
                paramsMap.put("orderId", goods.daiFaOrder.id)
                paramsMap.put("orderStatus", goods.daiFaOrder.status)
                paramsMap.put("id", goods.id)
                mm.result = "success"
                mm.model.put("ordergoods", paramsMap)
            } else {
                mm.result = "fail"
                mm.model.put("ordergoods", null)
            }
            render mm as JSON

        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def quehuo() {
        def goods
        try {
            goods = adminDaiFaGoodsService.doQuehuo(params, User.get(session.loginPOJO.user.id))
            flash.message = "确认缺货成功"
            flash.messageClass = this.success

        } catch (Exception e) {
            flash.message = e.getMessage()
            flash.messageClass = this.error
        }
        if (params.mobile == "mobile") {


            def mm = new MobileMessage()
            mm.message = flash.message
            if (flash.messageClass == this.success) {
                def paramsMap = new HashMap()
                goods.properties.each { k, v ->
                    if (v) {
                        paramsMap.put(k, v.toString())
                    } else {
                        paramsMap.put(k, null)
                    }
                }
                paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
                paramsMap.put("id", goods.id)
                mm.result = "success"
                mm.model.put("ordergoods", paramsMap)
            } else {
                mm.result = "fail"
                mm.model.put("ordergoods", null)
            }
            render mm as JSON

        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def noQuehuo() {
        def o = DaiFaGoods.createCriteria();
        def goods = o.get { eq("status", "8") eq("id", new Long(params.id)) }
        println "============"
        println goods
         println  session.loginPOJO.user.role
        if (!goods ) {
            println "============2"
            flash.message = "操作出错";
            flash.messageClass = this.error
        } else {
            println "============3"
            goods.status = "0"
            goods.daifa_user = null
            goods.processtime = null
            goods.actual_price = null
            goods.shuoming = params.shuoming

            // goods.actual_price = new BigDecimal(params.actual_price)
            //goods.nahuotime = new Date()
            flash.message = "取消缺货操作成功";
            flash.messageClass = this.success
            goods.save();

            def order = goods.daiFaOrder
            def goodsStatusList = order.daiFaGoods.status
            def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
            order.status = o_status

            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = User.get(session.loginPOJO.user.id)
            goodsLog.logdesc = "取消缺货，商品等待重新受理"
            goodsLog.save()
        }
        if (params.mobile == "mobile") {

            def paramsMap = new HashMap()
            goods.properties.each { k, v ->
                if (v) {
                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }
            }
            paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
            paramsMap.put("id", goods.id)


            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "success"
            mm.model.put("ordergoods", paramsMap)
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def noBukuan() {
        def o = DaiFaGoods.createCriteria();
        def goods = o.get { eq("status", "3") eq("id", new Long(params.id)) }


        if (!goods || session.loginPOJO.user.role != 'admin') {
            flash.message = "操作出错";
            flash.messageClass = this.error
        } else {
            goods.status = "0"
            goods.daifa_user = null
            goods.processtime = null
            goods.actual_price = null
            // goods.actual_price = new BigDecimal(params.actual_price)
            //goods.nahuotime = new Date()
            flash.message = "取消补款操作成功";
            flash.messageClass = this.success
            goods.save();

            def order = goods.daiFaOrder
            def goodsStatusList = order.daiFaGoods.status
            def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
            order.status = o_status

            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = User.get(session.loginPOJO.user.id)
            goodsLog.logdesc = "取消补款，商品等待重新受理"
            goodsLog.save()
        }
        if (params.mobile == "mobile") {

            def paramsMap = new HashMap()
            goods.properties.each { k, v ->
                if (v) {
                    paramsMap.put(k, v.toString())
                } else {
                    paramsMap.put(k, null)
                }
            }
            paramsMap.put("orderSN", goods.daiFaOrder.orderSN)
            paramsMap.put("id", goods.id)


            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "success"
            mm.model.put("ordergoods", paramsMap)
            render mm as JSON
        } else {
            render(view: this.commonSuccess)
            // redirect(action: "list", params: params)
            return
        }
    }

    def daifaMarketList() {

        //查询退货信息
        def shipSN = ShipSN.findAllByStatus("1")
//        def returnOrdersearchClosure = {
//
//            eq("orderfrom","kings")
//
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
//        def returnOrderSearch = ReturnOrder.createCriteria();
//        def returnOrder = returnOrderSearch.list(returnOrdersearchClosure)



        def searchMarket = {

            user {
                eq("id", session.loginPOJO.user.id)
            }
        }
        def marketList = Market.createCriteria().list(searchMarket)
        List m_result = new ArrayList()
        def map = [:]

        marketList.each {

            Long return_num = 0



            def param = [:]
            param.name = it.market_name
            param.count = DaiFaGoods.executeQuery("select count(a.id) from DaiFaGoods a join a.daiFaOrder d where a.market = ? and a.status='0' and d.status<>'delete' and  d.status<>'waitpay'", [it.market_name])[0]



            param.thh = ReturnGoods.executeQuery("select count(g.id) from ReturnGoods g where g.market = ? and g.status = '2' and g.orderfrom = 'kings'",[it.market_name])[0]
             println   param as JSON
//            returnOrder.returnGoods.flatten().each { returnGoods ->
//                if (returnGoods.status == "2"&&returnGoods.orderfrom=="kings") {  //1:新入库退货商品统计2:分拣完毕退货商品统计
//                    if (it.market_name == returnGoods.market) {
//                        return_num = return_num + returnGoods.return_num
//                    }
//                }
//            }
//            param.thh = return_num

            m_result.add(param)
        }
        map.marketList = m_result

        def searchClosure = {
            rowCount()
            or {
                eq("status", "2")
                eq("status", "3")
                eq("status", "8")
            }

            daifa_user {
                eq('id', session.loginPOJO.user.id)
            }
        }
        def o = DaiFaGoods.createCriteria();
        def results = o.list(params, searchClosure)
        map.shouli = results[0]


        def shouliresults = DaiFaGoods.findAllByDaifa_userAndStatus(session.loginPOJO.user,"2")

        BigDecimal nahuoamount = 0;
        shouliresults.each{
                nahuoamount = nahuoamount + it.actual_price * it.num
        }

        map.nahuoamount = nahuoamount


        searchClosure = {
            rowCount()
            isNotNull('checktime')//已验收的商品
            daifa_user {
                eq('id', session.loginPOJO.user.id)
            }
        }
        o = DaiFaGoods.createCriteria();
        results = o.list(params, searchClosure)
        map.yanshou = results[0]


        searchClosure = {
            rowCount()
            eq('status', '1')
            daifa_user {
                eq('id', session.loginPOJO.user.id)
            }
        }
        o = DaiFaGoods.createCriteria();
        results = o.list(params, searchClosure)
        map.owngoods = results[0]




        searchClosure = {
            rowCount()
            daifa_user {
                eq('id', session.loginPOJO.user.id)
            }
            eq("status", "kill_wait")
        }
        o = DaiFaGoods.createCriteria();
        results = o.list(params, searchClosure)
        map.kill_wait = results[0]

        def myReturn = ReturnGoods.countByShouliUserIdAndStatus(session.loginPOJO.user.id, "5")
        map.myReturn = myReturn

        def myNoOwnerShipSN = ShipSN.countByShouliUserIdAndStatus(session.loginPOJO.user.id, "4")
        map.myNoOwnerShipSN = myNoOwnerShipSN

        def mm = new MobileMessage()
        mm.message = "获得市场列表成功"
        mm.result = "success"
        mm.model = map
        render mm as JSON
    }


    def qianshouList() {


        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        params.sort = "dateCreated"
        params.order = "desc"



        def searchClosure = {

            daiFaOrder {

                if (params.orderSN) {
                    like('orderSN', "%" + params.orderSN + "%")
                }

            }

            daifa_user {
                if (params.email) {
                    like('email', "%" + params.email + "%")
                }
            }
            if (params.start_date) {
                ge('checktime', Date.parse("yyyy-MM-dd HH:mm:ss", params.start_date + " 00:00:00"))
            }
            if (params.end_date) {
                le('checktime', Date.parse("yyyy-MM-dd HH:mm:ss", params.end_date + " 23:59:59"))
            }
            if (params.is_qianshou) {
                eq("is_qianshou", params.is_qianshou)
            }
            or {
                eq("status", "9")
                eq("status", "10")
            }
            //eq("is_qianshou","0")
        }

        def o = DaiFaGoods.createCriteria();
        def results = o.list(params, searchClosure)
        def map = [list: results, total: results.totalCount]


        render(view: "/admin/daiFaGoods/qianshouList", model: map)


    }

    def explorQianshou() {
        def searchClosure = {
            or {
                eq("status", "9")
                eq("status", "10")
            }
            daiFaOrder {

            }
            daifa_user {

            }
            eq("is_qianshou", "0")
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = df.format(new Date())
        def o = DaiFaGoods.createCriteria();
        def goods = o.list(searchClosure)
        Map daifaMap = new HashMap()
        Map xiaojiMap = new HashMap()
        goods.each {
            if (daifaMap.containsKey(it.daifa_user.email)) {
                def goodsList = daifaMap.get(it.daifa_user.email)
                goodsList.add(it)

                def xiaoji = xiaojiMap.get(it.daifa_user.email)
                xiaoji = xiaoji + it.num * it.actual_price
                xiaojiMap.put(it.daifa_user.email, xiaoji)
            } else {
                def goodsList = new ArrayList()
                goodsList.add(it)
                daifaMap.put(it.daifa_user.email, goodsList)

                def xiaoji = new BigDecimal(0)
                xiaoji = xiaoji + it.num * it.actual_price
                xiaojiMap.put(it.daifa_user.email, xiaoji)
            }
        }
        def map = [daifaMap: daifaMap, xiaojiMap: xiaojiMap, nowDate: nowDate]
        render(view: "/admin/daiFaGoods/explorQianshou", model: map)
    }

    //代发人员等待结账列表
    def yanshouList() {
        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "checktime"
        if (!params.order) params.order = "desc"
        def map = [:]
        if (params.checktime) {

            def searchClosure = {
                daifa_user {
                    eq("id", session.loginPOJO.user.id)
                }


                between('checktime', Date.parse("yyyy-MM-dd HH:mm:ss", params.checktime + ' 00:00:00'), Date.parse("yyyy-MM-dd HH:mm:ss", params.checktime + ' 23:59:59'))

            }



            def o = DaiFaGoods.createCriteria();
            def results = o.list(params, searchClosure)
            map = [list: results, total: results.totalCount]

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
                paramsMap.put('payTime', it.daiFaOrder.payTime)
                paramsMap.put('id', it.id)
                paramsMap.put('orderSN', it.daiFaOrder.orderSN)

                m_result.add(paramsMap)
            }
            map.list = m_result


        } else {
            def results = new ArrayList();


            def qianshouview = Qianshouview.executeQuery("select sum(d.num) as num,sum(d.amount) as amount,sum(d.tuiamount) as tuiamount,sum(d.tuinum) as tuinum,d.checktime,d.is_qianshou from Qianshouview d " +
                    "where  d.daifa_user =?  group by substring(d.checktime,1,10) order by d.checktime desc", [session.loginPOJO.user.id],
                    [max: params.max, offset: params.offset])

            def total = Qianshouview.executeQuery("select d.daifa_user from Qianshouview d " +
                    "where d.daifa_user =?  group by substring(d.checktime,1,10)", [session.loginPOJO.user.id]).size();

            qianshouview.each { it ->
                Map paramMap = new HashMap()
                paramMap.num = it[0]
                paramMap.amount = it[1]
                paramMap.actual_return_fee = it[2]
                paramMap.return_num = it[3]
                paramMap.checktime = it[4].toString().substring(0, 10)
                paramMap.is_qianshou = it[5]!=null?it[5]:"0"
                paramMap.status = "" //
                results.add(paramMap)
            }


            map = [list: results, total: total]
        }
        //封装分页传给手机端
        def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((map.total - 1) / params.max + 1) as int, total: map.total)
        map.put("pager", pager)

        def mm = new MobileMessage()
        mm.message = "获取验收列表成功"
        mm.result = "success"
        mm.model = map
        render mm as JSON

    }

    //
    def qianshouMobile() {
        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "checktime"
        if (!params.order) params.order = "desc"
        def map = [:]





        if (params.checktime) {

            def results = new ArrayList();
            def start_time = params.checktime + " 00:00:00"
            def end_time = params.checktime + " 23:59:59"
            def qianshouview = Qianshouview.executeQuery("select sum(d.num) as num,sum(d.amount) as amount,sum(d.tuiamount) as tuiamount,sum(d.tuinum) as tuinum,d.checktime,d.is_qianshou,d.daifa_user,d.qianshou_user,d.qianshou_user_id,d.qianshoutime from Qianshouview d " +
                    " where  d.checktime between '" + start_time + "' and '" + end_time + "'" +
                    " group by daifa_user ")

            qianshouview.each { it ->
                Map paramMap = new HashMap()
                paramMap.num = it[0]
                paramMap.amount = it[1]
                paramMap.actual_return_fee = it[2]
                paramMap.return_num = it[3]
                paramMap.checktime = it[4].toString().substring(0, 10)
                paramMap.is_qianshou = it[5]!=null?it[5]:"0"
                paramMap.daifa_user_name = User.get(it[6] as Long).email
                paramMap.daifa_user_id = it[6]



                if(it[7]!=null){
                    paramMap.qianshou_user_name = it[7]
                }
                if(it[8]!=null){
                    def user = User.get(it[8] as Long);
                    if(user){
                        paramMap.qianshou_user_name = user.email
                    }

                }
                paramMap.qianshoutime =  it[9]


                results.add(paramMap)
            }




            map.list = results

        } else {



            def results = new ArrayList();


            def qianshouview = Qianshouview.executeQuery("select sum(d.num) as num,sum(d.amount) as amount,sum(d.tuiamount) as tuiamount,sum(d.tuinum) as tuinum,d.checktime,d.is_qianshou from Qianshouview d " +
                    " group by substring(d.checktime,1,10) order by d.checktime desc",
                    [max: params.max, offset: params.offset])

            def total = Qianshouview.executeQuery("select d.daifa_user from Qianshouview d " +
                    " group by substring(d.checktime,1,10)").size();

            qianshouview.each { it ->
                Map paramMap = new HashMap()
                paramMap.num = it[0]
                paramMap.amount = it[1]
                paramMap.actual_return_fee = it[2]
                paramMap.return_num = it[3]
                paramMap.checktime = it[4].toString().substring(0, 10)
                paramMap.is_qianshou = it[5]!=null?it[5]:"0"
                paramMap.status = "" //
                results.add(paramMap)
            }





            map = [list: results, total: total]
            //封装分页传给手机端
            def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((map.total - 1) / params.max + 1) as int, total: map.total)
            map.put("pager", pager)
        }


        def mm = new MobileMessage()
        mm.message = "获取签收列表成功"
        mm.result = "success"
        mm.model = map


        render mm as JSON
    }

    def daifaYanshouList() { //代发人员验收列表
        if (session.loginPOJO.user.role != 'admin') {
            def mm = new MobileMessage()
            mm.message = "系统出错"
            mm.result = "fail"
            render mm as JSON
            return
        }
        if (!params.max) {
            params.max = 10
        } else {
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "checktime"
        if (!params.order) params.order = "desc"
        def map = [:]

        def searchClosure = {
            daifa_user {
                eq("id", new Long(params.daifa_user_id))
            }


            between('checktime', Date.parse("yyyy-MM-dd HH:mm:ss", params.checktime + ' 00:00:00'), Date.parse("yyyy-MM-dd HH:mm:ss", params.checktime + ' 23:59:59'))

        }



        def o = DaiFaGoods.createCriteria();
        def results = o.list(params, searchClosure)
        map = [list: results, total: results.totalCount]

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
            paramsMap.put('payTime', it.daiFaOrder.payTime)
            paramsMap.put('id', it.id)
            paramsMap.put('orderSN', it.daiFaOrder.orderSN)

            m_result.add(paramsMap)
        }
        map.list = m_result

        //封装分页传给手机端
        def pager = new Pager(max: params.max, "offset": params.offset, "totalPage": ((map.total - 1) / params.max + 1) as int, total: map.total)
        map.put("pager", pager)

        def mm = new MobileMessage()
        mm.message = "获取验收列表成功"
        mm.result = "success"
        mm.model = map
        render mm as JSON
    }

    //代发人员申请提现
    def applyQianshouMobile() {
        def start_time = params.checktime + " 00:00:00"
        def end_time = params.checktime + " 23:59:59"
        DaiFaGoods.executeUpdate("update DaiFaGoods set is_qianshou = '1' where daifa_user =? and checktime between '" + start_time + "' and '" + end_time + "'", [User.get(session.loginPOJO.user.id)])

        ReturnGoods.executeUpdate("update ReturnGoods set is_qianshou = '1' where shouliUserId =? and actual_returnTime between '" + start_time + "' and '" + end_time + "'", [session.loginPOJO.user.id])



        def mm = new MobileMessage()
        mm.message = "提交提现申请成功"
        mm.result = "success"
        render mm as JSON
    }

    //管理员确认支付代发员提现 ,区分拿货商品和退货成功商品
    def checkQianshouMobile() {
        if (session.loginPOJO.user.role == 'admin') {

            println "params.checktime:"+ params.checktime

            def start_time = params.checktime + " 00:00:00"
            def end_time = params.checktime + " 23:59:59"
            User daifa_user = User.get(params.daifa_user_id)
            User qianshou_user = User.get(session.loginPOJO.user.id)
            DaiFaGoods.executeUpdate("update DaiFaGoods set is_qianshou = '2',qianshou_user=?,qianshoutime = now()  where daifa_user =? and " +
                    "checktime between '" + start_time + "' and '" + end_time + "' and is_qianshou = '1'", [qianshou_user, daifa_user])

            ReturnGoods.executeUpdate("update ReturnGoods set is_qianshou = '2',qianshou_user=?,qianshoutime = now()  where shouliUserId =? and actual_returnTime between '" + start_time + "' and '" + end_time + "' and is_qianshou = '1' ", [qianshou_user.email,daifa_user.id])


        }

        def mm = new MobileMessage()
        mm.message = "确认提现成功"
        mm.result = "success"
        render mm as JSON
    }

}
