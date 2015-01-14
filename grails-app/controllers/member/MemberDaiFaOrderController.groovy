package member

import ship.DaiFaOrder
import ship.DaiFaGoods
import ship.GoodsLog
import ship.ShipSN
import ship.TranLog
import ship.User
import grails.converters.JSON
import com.yiyu.push.BaiduPush
import ship.PushPOJO
import ship.PushMsg
import ship.Market
import sysinit.SysInitParams
import util.Push
import util.StringUtil
import admin.BaseController
import ship.AreaShip
import ship.Express
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import ship.ReturnOrder
import ship.ReturnGoods
import exception.MessageException
import common.CommonParams
import util.RemoteFileUtil

import java.text.SimpleDateFormat
import java.util.regex.Pattern
import java.util.regex.Pattern
import java.util.regex.Matcher

class MemberDaiFaOrderController extends BaseController {

    def memberXmlGoodsService;
    def randomService;
    def memberDaiFaService
    def mailService;
    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    def isPro = properties.getProperty("isPro");
    def testMail() {
        Thread.start {
            mailService.sendMail {
                async true
                from "service@findyi.com"
                to "wufei1310@126.com"
                subject "金士代发财务报表"
                // body new File("src/groovy/report.html").getText()
                html g.render(template: "myMailTemplate")
            }
        }


        render "333";
    }

    def index() {

        render(view: "/member/daiFaOrder/list")
    }

    def delete() {
        def order = DaiFaOrder.findByIdAndStatusAndAdd_user(params.id, "waitpay", session.loginPOJO.user.id)
        order.status = "delete"
        flash.message = "删除成功";

        render(view: this.commonSuccess)
    }

    def closeGoods() {
        try {
            memberDaiFaService.doCloseGoods(params, User.get(session.loginPOJO.user.id))
            flash.message = "商品取消成功，货款已退到余额账户";
            flash.messageClass = "success"
        } catch (Exception e) {
            flash.message = e.getMessage();
            flash.messageClass = "error"
        }
        render(view: this.commonSuccess)
    }

    def chaShipPay() {
        // def daiFaOrder = DaiFaOrder.findByIdAndAdd_user(params.id,session.loginPOJO.user.id)
        //if(params.pay_type == '0'){
        if (!session.loginPOJO.user.safepass) {
            flash.message = "请先设置支付密码"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }
        if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
            flash.message = "支付密码不正确"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }
        try {
            memberDaiFaService.doChaShipPay(params, User.get(session.loginPOJO.user.id))
            flash.message = "补款成功！";
            flash.messageClass = "success"
        } catch (Exception e) {
            flash.message = e.getMessage();
            flash.messageClass = "error"
        }
        render(view: this.commonSuccess)
    }

    def chaPay() {
        // def daiFaOrder = DaiFaOrder.findByIdAndAdd_user(params.id,session.loginPOJO.user.id)
        //if(params.pay_type == '0'){
        if (!session.loginPOJO.user.safepass) {
            flash.message = "请先设置支付密码"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }
        if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
            flash.message = "支付密码不正确"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }
        try {
            memberDaiFaService.doChaPay(params, User.get(session.loginPOJO.user.id))
            flash.message = "补款成功！";
            flash.messageClass = "success"
        } catch (Exception e) {
            flash.message = e.getMessage();
            flash.messageClass = "error"
        }
        render(view: this.commonSuccess)
    }

    def list() {
        if (!params.max) params.max = 10
        if (!params.offset) params.offset = 0
        if (!params.sort) params.sort = "dateCreated"
        if (!params.order) params.order = "desc"

        def searchClosure = {

            if (params.reperson) {
                like('reperson', '%' + params.reperson + '%')
            }


            if (params.orderSN) {
                like('orderSN', '%' + params.orderSN + '%')
            }

            if (params.contphone) {
                like('contphone', '%' + params.contphone + '%')
            }
            if (params.status) {
                if (params.status == "haspay") {
                    or {
                        eq('status', "waitaccept")
                        eq('status', "partaccept")
                        eq('status', "allaccept")
                        eq('status', "problem")
                    }


                } else {
                    eq('status', params.status)
                }

            }
            if (params.isCanExport) {
                eq('isCanExport', '3')
            }
            ne("status", "delete")
            eq('add_user', session.loginPOJO.user.id)
        }

        def o = DaiFaOrder.createCriteria();
        def results = o.list(params, searchClosure)

        def waitPayList = DaiFaOrder.findAllByStatusAndAdd_user("waitpay", session.loginPOJO.user.id)



        def waitpay = DaiFaOrder.countByStatusAndAdd_user("waitpay",session.loginPOJO.user.id)
        def error = DaiFaOrder.countByStatusAndAdd_user("error",session.loginPOJO.user.id)
        def diffship = DaiFaOrder.countByStatusAndAdd_user("diffship",session.loginPOJO.user.id)


        def map = [diffship:diffship,error:error,waitpay:waitpay,list: results, total: results.totalCount, waitPayList: waitPayList]

        render(view: "/member/daiFaOrder/list", model: map)
    }

    def add() {


        def daiFaOrder = DaiFaOrder.findByAdd_user(session.loginPOJO.user.id, [max: 1, sort: "dateCreated", order: "desc", offset: 0])
        if (!daiFaOrder) {
            daiFaOrder = new DaiFaOrder()
        }
        def goodsList = []
        if(params.isCopy=="last"){
            goodsList=daiFaOrder.daiFaGoods
        }else if(params.hotId){
            Map hotMap = session.hotMap;
            if(!hotMap){
                hotMap = [:]
                def skuMap = [:]
                skuMap.put("size",params.size)
                skuMap.put("color",params.color)
                hotMap.put(params.hotId,skuMap)
                session.hotMap = hotMap;
            }else {
                hotMap = session.hotMap
                def skuMap = [:]
                skuMap.put("size",params.size)
                skuMap.put("color",params.color)
                hotMap.put(params.hotId,skuMap)
            }




            hotMap.each{k,v->

                def sizes = [];
                def colors = [];
                if(v.size instanceof String){
                    sizes.add(v.size)
                }else{
                    sizes =  v.size
                }
                if(v.color instanceof String){
                    colors.add(v.color)
                }  else{
                    colors =  v.color
                }


                if(sizes==null){
                    sizes=["均码"]
                }
                if(colors==null){
                    colors=["图片色"]
                }

                sizes.each{size->
                    colors.each {color->

                        def xmlGoods = memberXmlGoodsService.getXmlGoods(k);
                        def daiFaGoods = new DaiFaGoods();
                        daiFaGoods.market = xmlGoods.market;
                        daiFaGoods.floor = xmlGoods.floor;
                        daiFaGoods.stalls = xmlGoods.stall;
                        daiFaGoods.spec = color+"/"+size;
                        daiFaGoods.attach_id = xmlGoods.pic
                        daiFaGoods.goods_sn = xmlGoods.goods_sn
                        daiFaGoods.price = new BigDecimal(xmlGoods.price)
                        daiFaGoods.num = 1
                        goodsList.add(daiFaGoods)
                    }

                }

            }










        }else{
            goodsList = null;
        }


        def market = Market.list().market_name
        def areaChild = SysInitParams.getAreaChildMap().get("0100");


        def map = [daiFaOrder: daiFaOrder, market: market,areaChild:areaChild,goodsList:goodsList]
        render(view: "/member/daiFaOrder/add", model: map)
    }


    def giveup(){
        def kingsReturnOrder = ReturnOrder.get(params.id)
        kingsReturnOrder.returnGoods.each{
            if(it.status=="6"){
                it.status="10"
                it.save();
            }
        }
        redirect(controller: "memberDaiFaOrder",action: "saleReturnShow",id:params.mid  )
    }

    //退货不成的商品继续退货
    def returnGoOn(){


        if (!session.loginPOJO.user.safepass) {
            flash.message = "请先设置支付密码"
            flash.messageClass = "error"
            redirect(controller: "memberDaiFaOrder",action: "saleReturnShow",id:params.mid  )
            return false
        }
        if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
            flash.message = "支付密码不正确"
            flash.messageClass = "error"
            redirect(controller: "memberDaiFaOrder",action: "saleReturnShow",id:params.mid  )
            return false
        }


        def kingsReturnOrder = ReturnOrder.get(params.id)
        kingsReturnOrder.returnGoods.each{
            if(it.status=="6"){
                it.status="1"
            }
            it.save()
        }


        kingsReturnOrder.status = "1"
        kingsReturnOrder.save()

        def memberReturnOrder = ReturnOrder.findByOrderSN(kingsReturnOrder.orderSN.replace("K","M"))



        memberReturnOrder.returnGoods.each{
            if(it.return_fee-5>0){
                it.return_fee = it.return_fee -5
            }else{
                it.return_fee = 1
            }
            it.save()
        }
        memberReturnOrder.status = "1"
        memberReturnOrder.save();


        redirect(controller: "memberDaiFaOrder",action: "saleReturnShow",id:params.mid  )
    }




    //提交为我寄回的商品新订单，该订单提交就是等待发货的状态
    def addBack() {

        def goodsList = []
        def kingsReturnOrder = ReturnOrder.get(params.id)
        kingsReturnOrder.returnGoods.each{
            if(it.status=="6"){
                it.daiFaGoods.num = it.return_num;
                goodsList.add(it.daiFaGoods)
            }
        }



        def market = Market.list().market_name
        def areaChild = SysInitParams.getAreaChildMap().get("0100");


        def map = [kingsReturnOrder:kingsReturnOrder,daiFaOrder: kingsReturnOrder.daiFaOrder, market: market,areaChild:areaChild,goodsList:goodsList]
        render(view: "/member/daiFaOrder/addBack", model: map)
    }



    def payDaiFaAllOrder() {


        if (!session.loginPOJO.user.safepass) {
            flash.message = "请先设置支付密码"
            redirect(action: "list", params: [status: 'waitpay'])
            return;
        }
        if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
            flash.message = "支付密码不正确"
            redirect(action: "list", params: [status: 'waitpay'])
            return;
        }

//        def waitpayorderid = [];
        def waitpayorderid = params.waitpayorderidstr.split(",")
        def waitPayAmount = 0.00;




        waitpayorderid.each { it ->
            if (it != "") {
                def daiFaOrder = DaiFaOrder.findByIdAndAdd_user(it as Long, session.loginPOJO.user.id)
                waitPayAmount = waitPayAmount + daiFaOrder.totalFee;
            }

        }

        def user = User.get(session.loginPOJO.user.id)
        def account = user.account
        account.lock()
        if (account.amount < waitPayAmount) {
            flash.message = "余额不足，请先充值"
            redirect(action: "list", params: [status: 'waitpay'])
            return;
        }
        waitpayorderid.each { it ->
            if (it != "") {
                memberDaiFaService.payDaiFa(it as Long);
            }

        }


        flash.message = "合并支付成功！我们尽快按您的要求取货，请您每隔一段时间登陆您的账号查看取货状态!"
        redirect(action: "list")
    }


    def payDaiFaOrder() {
        def daiFaOrder = DaiFaOrder.findByIdAndAdd_user(params.id, session.loginPOJO.user.id)
        //if(params.pay_type == '0'){
        if (!session.loginPOJO.user.safepass) {
            flash.message = "请先设置支付密码"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }
        if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
            flash.message = "支付密码不正确"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }
        def msg = memberDaiFaService.payDaiFa(daiFaOrder.id)
        if (msg) {
            flash.message = msg
        } else {
            flash.message = "支付成功！我们尽快按您的要求取货，请您每隔一段时间登陆您的账号查看取货状态"
        }
        render(view: this.commonSuccess)
        return false
        // }
    }



    def proOrderSN(){
        java.text.DateFormat format2 = new java.text.SimpleDateFormat("yyMMdd");


        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = df.format(new Date())
        def startDate = Date.parse("yyyy-MM-dd HH:mm:ss", nowDate + " 00:00:00")
        def endDate = Date.parse("yyyy-MM-dd HH:mm:ss", nowDate + " 23:59:59")

        def todayOrder = DaiFaOrder.executeQuery("select count(a.id) from DaiFaOrder a " +
                "where a.dateCreated >= ? and a.dateCreated <= ?",
                [startDate, endDate])[0]



        String nowOrder = String.valueOf(todayOrder + 1)
        int less0 = 4 - nowOrder.length()
        for (int i = 0; i < less0; i++) {
            nowOrder = "0" + nowOrder;
        }

        def randoNum = randomService?.nextInteger(100, 999) as String; //加入订单号

        def newNo = nowOrder[0]+randoNum[0]+nowOrder[1]+randoNum[1]+nowOrder[2]+randoNum[2]+nowOrder[3];


        return format2.format(new Date()).toString() + newNo;
    }


    def doAddBack(){

        if (params.pay_type == '0') {
            if (!session.loginPOJO.user.safepass) {
                flash.message = "请先设置支付密码"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }
            if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
                flash.message = "支付密码不正确"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }

        }
        def kingsReturnOrder = ReturnOrder.get(params.kid as Long)

        def TReturnOrder = DaiFaOrder.findByOrderSN("T"+kingsReturnOrder.daiFaOrder.orderSN)
        if(TReturnOrder){
            flash.message = "您已经向该退货申请提交寄回商品了，无需重新提交！"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }


        def daiFaOrder = new DaiFaOrder(params)
        daiFaOrder.senddesc = "帮我寄走退货不成功商品"
        daiFaOrder.orderSN = "T"+kingsReturnOrder.daiFaOrder.orderSN;
        daiFaOrder.add_user = session.loginPOJO.user.id
        daiFaOrder.status = "allaccept"
        daiFaOrder.diffFee = 0
        def num = 0;
        kingsReturnOrder.returnGoods.each{
            if(it.status=="6"){
                it.status = "9"
                num = (it.num as Long) + num
                def daiFaGoods = it.daiFaGoods;

                Calendar cal = Calendar.getInstance();
                cal.setTime(it.actual_returnTime);
                int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

                daiFaGoods.stalls = day + "货架";
                daiFaGoods.num = it.return_num;
                daiFaGoods.status = "7"
                daiFaOrder.addToDaiFaGoods(daiFaGoods)

            }
        }

        daiFaOrder.serviceFee = 0;
        daiFaOrder.goodsFee = 0
        daiFaOrder.shipFee = StringUtil.checkShip(daiFaOrder.wuliu, daiFaOrder.area_id, num)
        daiFaOrder.regardsFee = 0
        daiFaOrder.totalFee = daiFaOrder.shipFee
        daiFaOrder.save(flush: true);

        daiFaOrder.payTime = new Date();


        def msg = memberDaiFaService.payDaiFa(daiFaOrder.id)
        if (msg) {
            flash.message = msg
        } else {
            flash.message = "支付成功！我们尽快按您的要求,将货物寄回！"
        }
        flash.messageClass = "success"
        render(view: this.commonSuccess)
        return false

    }

    def doAdd() {
        if(session.hotMap) {
            session.hotMap = null
        }

        if (params.pay_type == '0') {
            if (!session.loginPOJO.user.safepass) {
                flash.message = "请先设置支付密码"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }
            if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
                flash.message = "支付密码不正确"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }

        }
        def daiFaOrder = new DaiFaOrder(params)

        if (!daiFaOrder.senddesc) {
            daiFaOrder.senddesc = ""
        }
        Pattern pattern = Pattern.compile("\\d{5,}");
        Matcher matcher = pattern.matcher(daiFaOrder.senddesc);
        daiFaOrder.senddesc = matcher.replaceAll("");








        daiFaOrder.orderSN = proOrderSN();



        daiFaOrder.add_user = session.loginPOJO.user.id
        daiFaOrder.status = "waitpay"
        daiFaOrder.diffFee = 0

        def shichang = ""
        def goodsNum = 0;

        if (params.market instanceof String) {//判断提交一个商品还是多个商品
            def daiFaGoods = new DaiFaGoods(num: params.num, price: params.price);
            daiFaGoods.market = params.market
            daiFaGoods.floor = params.floor
            daiFaGoods.stalls = params.stalls.replaceAll(params.market,"").replaceAll(params.floor,"").replaceAll("F","").replaceAll("L","")
            daiFaGoods.goods_sn = params.goods_sn.replaceAll(" ","").replaceAll("＃","#").replaceAll("　","");
            daiFaGoods.spec = params.spec
            daiFaGoods.status = '0'
            daiFaGoods.is_qianshou = '0'
            daiFaGoods.add_user = session.loginPOJO.user.id

            if(params.attach_id.contains("http:")){
                daiFaGoods.attach_id = download(params.attach_id)
            }else{
                daiFaGoods.attach_id = RemoteFileUtil.remoteFileCopy(request, params.attach_id)
            }




            daiFaOrder.addToDaiFaGoods(daiFaGoods)

            shichang = params.market
        } else {
            params.market.eachWithIndex { it, i ->
                def daiFaGoods = new DaiFaGoods(num: params.num[i], price: params.price[i]);
                daiFaGoods.market = params.market[i]
                daiFaGoods.floor = params.floor[i]
                daiFaGoods.stalls = params.stalls[i].replaceAll(params.market[i],"").replaceAll(params.floor[i],"").replaceAll("F","").replaceAll("L","")
                daiFaGoods.goods_sn = params.goods_sn[i].replaceAll(" ","").replaceAll("＃","#").replaceAll("　","");
                daiFaGoods.spec = params.spec[i]
                daiFaGoods.status = '0'
                daiFaGoods.is_qianshou = '0'
                daiFaGoods.add_user = session.loginPOJO.user.id

                //  daiFaGoods.attach_id = params.attach_id[i]


                if(params.attach_id[i].contains("http:")){
                    daiFaGoods.attach_id = download(params.attach_id[i])
                }else{
                    daiFaGoods.attach_id = RemoteFileUtil.remoteFileCopy(request, params.attach_id[i])
                }



                daiFaOrder.addToDaiFaGoods(daiFaGoods)

                if (shichang.indexOf(params.market[i]) < 0) {
                    shichang = shichang + params.market[i] + ","

                }


            }
        }




        checkOrderFee(daiFaOrder)



        daiFaOrder.daiFaGoods.each { it ->
            goodsNum = goodsNum + it.num
        }









        def line = "-"
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = df.format(new Date())
        def time16 = Date.parse("yyyy-MM-dd HH:mm:ss", nowDate + " 16:00:00")
        def nowTime = new Date();
        if (nowTime > time16) {
            line = "+"
        }

        def hasorder = DaiFaOrder.findByAdd_userAndStatus(session.loginPOJO.user.id, "shipped")
        if (!hasorder) {
            line = "="
        }
        if(!line){
            line = "-"
        }
        daiFaOrder.orderSN = daiFaOrder.orderSN + line + goodsNum


        if(daiFaOrder.goodsFee==0.01){
            daiFaOrder.status = "allaccept"
        }


        daiFaOrder.save(flush: true);


        def searchClosure = {
            eq("user_type", "admin")
        }

        def o = PushMsg.createCriteria();
        def results = o.list(searchClosure)



        if (params.pay_type == '0') {
            def msg = memberDaiFaService.payDaiFa(daiFaOrder.id)
            if (msg) {
                flash.message = msg
            } else {
                flash.message = "支付成功！我们尽快按您的要求取货。您可将ＱＱ绑定微信账号，您将容易收到取货状况信息！"
            }
            flash.messageClass = "success"
            render(view: this.commonSuccess)
            return false
        } else if (params.pay_type == '2') {
            flash.message = "保存订单成功!在未付款订单中，您可以勾选需要的订单合并支付！"
            redirect(controller: "memberDaiFaOrder", action: "list", params: [status: "waitpay"])
            return false
        } else {
            redirect(controller: "memberAlipay", action: "reqPay", params: [payType: "0", orderSN: daiFaOrder.orderSN])
        }

    }

    def show() {
        def market = Market.list().market_name
        def order = DaiFaOrder.findByIdAndAdd_user(params.id, session.loginPOJO.user.id)
        def map = [order: order, market: market]
        //print order.diffOrder
        render(view: "/member/daiFaOrder/show", model: map)
    }


    def doChangeStall() {

        def daiFaGoods = DaiFaGoods.get(params.goodsId)


        if(daiFaGoods.status!="5"){
            flash.message = "你的提交不合法!"
            redirect(action: "show", params: [id: params.orderId])
            return
        }





        if (daiFaGoods.changeStallsNum >= 2) {
            if (!session.loginPOJO.user.safepass) {
                flash.message = "请先设置支付密码"
                flash.messageClass = "error"
                redirect(action: "show", params: [id: params.orderId])
                return;
            }
            if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
                flash.message = "支付密码不正确"
                flash.messageClass = "error"
                redirect(action: "show", params: [id: params.orderId])
                return;
            }

            BigDecimal decimal = new BigDecimal(daiFaGoods.num * 1);
            def user = User.get(session.loginPOJO.user.id)
            def account = user.account
            account.lock()
            if (account.amount < decimal) {
                flash.message = "余额不足，请先充值"
                flash.messageClass = "error"
                redirect(action: "show", params: [id: params.orderId])
                return;
            } else {
                BigDecimal memberamount = account.amount;
                account.amount = account.amount - decimal
                //插入资金流水表
                def tranLog = new TranLog();
                tranLog.shouru_type = "0"
                tranLog.amount = decimal
                memberamount = memberamount - decimal
                tranLog.memberamount = memberamount
                tranLog.direction = "0"
                tranLog.type = "23"
                tranLog.orderSN = daiFaGoods.daiFaOrder.orderSN
                tranLog.order_user = daiFaGoods.add_user
                tranLog.num = daiFaGoods.num
                tranLog.save()


            }


        }




        daiFaGoods.changeStallsNum = (daiFaGoods.changeStallsNum != null ? daiFaGoods.changeStallsNum : 0) + 1;


        daiFaGoods.market = params.market;
        daiFaGoods.floor = params.floor;
        daiFaGoods.stalls = params.stalls;
        daiFaGoods.goods_sn = params.goods_sn
        daiFaGoods.actual_price = new BigDecimal(params.actual_price)
        daiFaGoods.spec = params.spec
        daiFaGoods.status = "0"

        if (daiFaGoods.daiFaOrder.senddesc != params.beizhu) {


            if (daiFaGoods.daiFaOrder.h_senddesc) {
                daiFaGoods.daiFaOrder.h_senddesc = daiFaGoods.daiFaOrder.h_senddesc + "|" + daiFaGoods.daiFaOrder.senddesc
            } else {
                daiFaGoods.daiFaOrder.h_senddesc = daiFaGoods.daiFaOrder.senddesc
            }
        }

        daiFaGoods.daiFaOrder.senddesc = params.beizhu


        daiFaGoods.save();

        if (daiFaGoods.actual_price > daiFaGoods.price) {
            memberDaiFaService.doBukuan(params, User.get(session.loginPOJO.user.id));
        } else if (daiFaGoods.actual_price < daiFaGoods.price) {
            memberDaiFaService.doChangeStallLowPrice(daiFaGoods, User.get(session.loginPOJO.user.id))
            daiFaGoods.price = daiFaGoods.actual_price
        } else {
            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = daiFaGoods
            goodsLog.addUser = session.loginPOJO.user;
            goodsLog.logdesc = "更换档口"
            goodsLog.save()
        }

        //更换档口重新维护订单状态　
        def order = daiFaGoods.daiFaOrder
        def goodsStatusList = order.daiFaGoods.status
        def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
//        if(!o_status)
//        throw new RuntimeException("补款出错") ;
        order.status = o_status





        flash.message = "更换档口成功，金士代发将为您重新拿货！温馨提示：您已更换档口${daiFaGoods.changeStallsNum}次，超过两2次将按每1元/件次收费."
        redirect(action: "show", params: [id: params.orderId])


    }

    def update() {

        def order = DaiFaOrder.findByIdAndAdd_userAndStatus(params.id, session.loginPOJO.user.id, "waitpay")
        def market = Market.list().market_name
        def map = [order: order, market: market]
        render(view: "/member/daiFaOrder/update", model: map)
    }

    def doUpdate() {
        def daiFaOrder = DaiFaOrder.findByIdAndAdd_userAndStatus(params.id, session.loginPOJO.user.id, "waitpay")

        if (daiFaOrder.senddesc != params.senddesc) {

            Pattern pattern = Pattern.compile("\\d{5,}");
            Matcher matcher = pattern.matcher(daiFaOrder.senddesc);
            daiFaOrder.senddesc = matcher.replaceAll("");

            if (daiFaOrder.h_senddesc) {
                daiFaOrder.h_senddesc = daiFaOrder.h_senddesc + "|" + daiFaOrder.senddesc
            } else {
                daiFaOrder.h_senddesc = daiFaOrder.senddesc
            }
        }

        daiFaOrder.properties = params
        daiFaOrder.add_user = session.loginPOJO.user.id
        daiFaOrder.diffFee = 0

        DaiFaGoods.executeUpdate("delete DaiFaGoods d where d.daiFaOrder = :daiFaOrder", [daiFaOrder: daiFaOrder])



        if (params.market instanceof String) {//判断提交一个商品还是多个商品
            def daiFaGoods = new DaiFaGoods(num: params.num, price: params.price);
            daiFaGoods.market = params.market
            daiFaGoods.floor = params.floor
            daiFaGoods.stalls = params.stalls
            daiFaGoods.goods_sn = params.goods_sn
            daiFaGoods.spec = params.spec
            daiFaGoods.status = '0'
            daiFaGoods.is_qianshou = '0'
            daiFaGoods.add_user = session.loginPOJO.user.id
            daiFaGoods.attach_id = RemoteFileUtil.remoteFileCopy(request, params.attach_id)


            daiFaOrder.addToDaiFaGoods(daiFaGoods)


        } else {
            params.market.eachWithIndex { it, i ->
                def daiFaGoods = new DaiFaGoods(num: params.num[i], price: params.price[i]);
                daiFaGoods.market = params.market[i]
                daiFaGoods.floor = params.floor[i]
                daiFaGoods.stalls = params.stalls[i]
                daiFaGoods.goods_sn = params.goods_sn[i]
                daiFaGoods.spec = params.spec[i]
                daiFaGoods.status = '0'
                daiFaGoods.is_qianshou = '0'
                daiFaGoods.add_user = session.loginPOJO.user.id


                daiFaGoods.attach_id = RemoteFileUtil.remoteFileCopy(request, params.attach_id[i])

                daiFaOrder.addToDaiFaGoods(daiFaGoods)


            }
        }

//        aOrder/listMobile - parameters:
//        status: allaccept
//        mobile: mobile
//        String index out of range: 10. Stacktrace follows:
//                java.lang.StringIndexOutOfBoundsException: String index out of range: 10
//        at java.lang.String.substring(String.java:1934)
//        at admin.AdminDaiFaOrderController$_listMobile_closure6.doCall(AdminDaiFaOrderController.groovy:261)
//        at admin.AdminDaiFaOrderController.listMobile(AdminDaiFaOrderController.groovy:252)
//        at grails.plugin.cache.web.filter.PageFragmentCachingFilter.doFilter(PageFragmentCachingFilter.java:195)
//        at grails.plugin.cache.web.filter.AbstractFilter.doFilter(AbstractFilter.java:63)
//        at java.lang.Thread.run(Thread.java:662)


        checkOrderFee(daiFaOrder)

        def goodsNum = 0;
        daiFaOrder.daiFaGoods.each { it ->
            goodsNum = goodsNum + it.num
        }


        def orderSN = daiFaOrder.orderSN;

        orderSN = orderSN.substring(0, 14) + goodsNum

        daiFaOrder.orderSN = orderSN;
        daiFaOrder.save();




        flash.message = "修改代发订单成功！"
        render(view: this.commonSuccess)
    }

    def updateBeiZhu() {
        def daiFaOrder = DaiFaOrder.findByIdAndAdd_user(params.id, session.loginPOJO.user.id)


        if (daiFaOrder.senddesc != params.beizhu) {


            if (daiFaOrder.h_senddesc) {
                daiFaOrder.h_senddesc = daiFaOrder.h_senddesc + "|" + daiFaOrder.senddesc
            } else {
                daiFaOrder.h_senddesc = daiFaOrder.senddesc
            }
        }

        daiFaOrder.senddesc = params.beizhu



        Pattern pattern = Pattern.compile("\\d{5,}");
        Matcher matcher = pattern.matcher(daiFaOrder.senddesc);
        daiFaOrder.senddesc = matcher.replaceAll("");


        flash.message = "修改订单备注成功！"
        render(view: this.commonSuccess)
    }

    def acceptAjax() {
        print "=========================1============================="
        render "1"
    }

    def checkShipAjax() {
        render StringUtil.checkShip(params.wuliu, params.area_id, Integer.valueOf(params.num))

    }

    //    def checkShip(wuliu,area_id,num){
    //        if(area_id.length()>7)
    //        area_id = area_id.substring(0,7)
    //        def express = Express.findByName(wuliu)
    //        def areaShip = AreaShip.findByExpressAndArea_id(express,area_id)
    //         print areaShip as JSON
    //        if(!areaShip){
    //            areaShip = AreaShip.findByExpressAndArea_id(express,"0100")
    //        }
    //       // print areaShip as JSON
    //        if(num<=5){
    //           return areaShip.f_price
    //        }else{
    //            int f_num = (num-5-1)/5+1
    //            return areaShip.f_price + areaShip.x_price*f_num
    //        }
    //    }

    public void checkOrderFee(daiFaOrder) {
        BigDecimal daifa_free = new BigDecimal(properties.getProperty("daifa.daifa_free"))
        def goods = daiFaOrder.daiFaGoods
        def num = 0
        def ship = 0
        def goodsFee = 0
        def serviceFee = 0
        def regardsFee = 0
        def totalFee = 0
        goods.each {
            num = it.num + num
            goodsFee = goodsFee + it.num * it.price
        }
        def regardsMap = CommonParams.regardsMap
        if (regardsMap.containsKey(daiFaOrder.regards)) {
            regardsFee = regardsMap.get(daiFaOrder.regards)
        }


        if (daiFaOrder.wuliu == "大包") {
            ship = 0;
            daiFaOrder.serviceFee = 2 * num
        } else {
            ship = StringUtil.checkShip(daiFaOrder.wuliu, daiFaOrder.area_id, num)
            daiFaOrder.serviceFee = daifa_free * num
        }



        daiFaOrder.goodsFee = goodsFee

        daiFaOrder.shipFee = ship
        daiFaOrder.regardsFee = regardsFee
        daiFaOrder.totalFee = daiFaOrder.goodsFee + daiFaOrder.serviceFee + daiFaOrder.shipFee + daiFaOrder.regardsFee
    }

    def toSaleReturn() {
        def daiFaOrder = DaiFaOrder.findByIdAndAdd_userAndStatus(params.id, session.loginPOJO.user.id, "shipped")
        def map = [order: daiFaOrder]
        //print order.diffOrder
        render(view: "/member/saleReturn/saleReturn", model: map)
    }

    def toSaleExchange() {//换货
        def daiFaOrder = DaiFaOrder.findByIdAndAdd_userAndStatus(params.id, session.loginPOJO.user.id, "shipped")


//        if(daiFaOrder.type=="1"){
//            flash.message = "订单已经提交过一次退货申请，不支持重复提交"
//            flash.messageClass = "error"
//            redirect(action: "list")
//        }

        def market = Market.list().market_name
        def map = [order: daiFaOrder, market: market]
        //print order.diffOrder


        if(params.wuliu_sn){
            def returnOrder = ReturnOrder.findByDaiFaOrderAndWuliu_sn(daiFaOrder,params.wuliu_sn)
            map.returnOrder = returnOrder;
        }


        render(view: "/member/saleReturn/saleExchange", model: map)
    }

    //判断下单时期有无超过１５天，不许再下单
    def checkReturnAllow() {
        def daiFaOrder = DaiFaOrder.findByIdAndAdd_userAndStatus(params.id, session.loginPOJO.user.id, "shipped")
        if (new Date() - daiFaOrder.ship_time > 15) {
            flash.message = "该商品已经发货超过１５天，档口不再支持退货服务"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }

        return true;
    }


    def doSaleExchange() {


        if (params.pay_type == '0') {
            if (!session.loginPOJO.user.safepass) {
                flash.message = "请先设置支付密码"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }
            if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
                flash.message = "支付密码不正确"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }

        }

        def daiFaOrder = DaiFaOrder.findByIdAndAdd_userAndStatus(params.id, session.loginPOJO.user.id, "shipped")



        def returnOrder = ReturnOrder.findByOrderSNAndAdd_user("M"+daiFaOrder.orderSN,session.loginPOJO.user.id)
        if(returnOrder){
            flash.message = "您已经提交过该订单号的退货申请了"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }



        if(isPro=="true"&&!checkReturnAllow()){
            return false;
        }


        def addUser = User.get(session.loginPOJO.user.id)

        returnOrder = memberDaiFaService.doSaleExchange(params, addUser)//添加申请
        returnOrder.isScan = "0"//默认退货申请是未扫描的,当找到了与他对应的无主包裹时，我们认为这个退货申请扫描过了的

        def kreturnOrder = ReturnOrder.findByOrderSN(returnOrder.orderSN.replace("M", "K"))

        if(kreturnOrder){
            kreturnOrder.ishuiyuanxiadan = "1"
            returnOrder.isScan = "1" //如果有k表记录，表明会员的这个退货已经在处理了

        }

        def shipSN = ShipSN.findByWuliu_sn(params.wuliu_sn)

        if (shipSN) { //在提交新的退货申请时，判断是否已经有了物流单号。



            if (shipSN.status == "noowner" || shipSN.status == "new" ) { //如果有物流单号在无主包裹中，我们认为这相退货申请等同于扫描入库了，从无主包裹中消失
                shipSN.status = "new"
                shipSN.needTui = "1"
                if(!shipSN.orderSN?.contains(daiFaOrder.orderSN)){
                    shipSN.orderSN = shipSN.orderSN + "|" + daiFaOrder.orderSN
                }



                returnOrder.isScan = "1"
                returnOrder.returnGoods.each { it ->
                    it.rukuUser = session.loginPOJO.user.email
                    it.rukuTime = new Date();
                    it.status = "1" //记录该退货商品办事处入库
                }

            }



        }


        if(isPro=="true"){  //生产环境
            returnOrder.wuliupic = RemoteFileUtil.remoteFileCopy(request,params.wuliupic)     //退回快递单　图片凭证
        }


        if (params.pay_type == '0') {
            def msg = memberDaiFaService.paySaleReturn(returnOrder.id)
            if (msg) {
                flash.message = msg
            } else {
                flash.message = "支付成功！我们会尽快为您处理"
            }
            flash.messageClass = "success"
            render(view: this.commonSuccess)
            return false
        } else {
            redirect(controller: "memberAlipay", action: "reqPay", params: [payType: "3", orderSN: returnOrder.orderSN, body: returnOrder.id])
        }

    }


    def doSaleReturn() {
        if (params.pay_type == '0') {
            if (!session.loginPOJO.user.safepass) {
                flash.message = "请先设置支付密码"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }
            if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
                flash.message = "支付密码不正确"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }

        }

        def addUser = User.get(session.loginPOJO.user.id)
        def returnOrder
        try {
            returnOrder = memberDaiFaService.doSaleReturn(params, addUser)//添加申请
            returnOrder.wuliupic = RemoteFileUtil.remoteFileCopy(request,params.wuliupic)     //退回快递单　图片凭证
        } catch (MessageException e) {
            flash.message = e.getMessage()
            flash.messageClass = "error"
            if (params.pay_type == '0') {
                render(view: this.commonSuccess)
                return false
            } else {
                render flash.message
                return false
            }
        }


        if (params.pay_type == '0') {
            def msg = memberDaiFaService.paySaleReturn(returnOrder.id)
            if (msg) {
                flash.message = msg
            } else {
                flash.message = "支付成功！我们会尽快为您处理"
            }
            flash.messageClass = "success"
            render(view: this.commonSuccess)
            return false
        } else {
            redirect(controller: "memberAlipay", action: "reqPay", params: [payType: "3", orderSN: returnOrder.orderSN, body: returnOrder.id])
        }

    }

    def toSaleReturnAdd() {
        def market = Market.list().market_name

        def returnOrder = ReturnOrder.findByAdd_user(session.loginPOJO.user.id, [max: 1, sort: "dateCreated", order: "desc", offset: 0])
        if (!returnOrder) {
            returnOrder = new ReturnOrder()
        }

        def map = [market: market, returnOrder: returnOrder]
        render(view: "/member/saleReturn/add", model: map)
    }

    def doSaleReturnAdd() {
        if (params.pay_type == '0') {
            if (!session.loginPOJO.user.safepass) {
                flash.message = "请先设置支付密码"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }
            if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
                flash.message = "支付密码不正确"
                flash.messageClass = "error"
                render(view: this.commonSuccess)
                return false
            }

        }

        def addUser = User.get(session.loginPOJO.user.id)

        def returnOrder = memberDaiFaService.doSaleReturnAdd(params, addUser)//添加申请


        if (params.pay_type == '0') {
            def msg = memberDaiFaService.paySaleReturn(returnOrder.id)
            if (msg) {
                flash.message = msg
            } else {
                flash.message = "支付成功！我们会尽快为您处理"
            }
            flash.messageClass = "success"
            render(view: this.commonSuccess)
            return false
        } else {
            redirect(controller: "memberAlipay", action: "reqPay", params: [payType: "3", orderSN: returnOrder.orderSN, body: returnOrder.id])
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

            eq("orderfrom","member")

            if (params.orderSN) {
                like('orderSN', "%" + params.orderSN + "%")
            }
            if (params.status) {
                eq('status', params.status)

            }
            eq("add_user", session.loginPOJO.user.id)
        }

        def o = ReturnOrder.createCriteria();
        def results = o.list(params, searchClosure)

        def map = [list: results, total: results.totalCount]



        render(view: "/member/saleReturn/list", model: map)

    }

    def saleReturnShow() {
        def returnOrder = ReturnOrder.findByIdAndAdd_user(params.id, session.loginPOJO.user.id)


        def kingsReturnOrder  = ReturnOrder.findByOrderSNAndOrderfrom("K"+returnOrder.orderSN.substring(1),"kings")


        def map = [returnOrder: returnOrder,kingsReturnOrder:kingsReturnOrder]
//        if (returnOrder.type == '0') {
//            render(view: "/member/saleReturn/show", model: map)
//        } else {
        render(view: "/member/saleReturn/saleExchangeShow", model: map)
//        }

    }

    def paySaleReturn() {
        def returnOrder = ReturnOrder.findByIdAndAdd_user(params.id, session.loginPOJO.user.id)
        //if(params.pay_type == '0'){
        if (!session.loginPOJO.user.safepass) {
            flash.message = "请先设置支付密码"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }
        if (!params.safepass || params.safepass.encodeAsPassword() != session.loginPOJO.user.safepass) {
            flash.message = "支付密码不正确"
            flash.messageClass = "error"
            render(view: this.commonSuccess)
            return false
        }
        def msg = memberDaiFaService.paySaleReturn(returnOrder.id)
        if (msg) {
            flash.message = msg
        } else {
            flash.message = "支付成功！我们会尽快为您处理!"
        }
        render(view: this.commonSuccess)
    }

    def goToReturn() {
        def map = [:]
        render(view: "/member/saleReturn/returnDesc", model: map)
    }

    def goToReturn2(){
        if (!params.sort) params.sort = "dateCreated"
        if (!params.order) params.order = "desc"


        def retrunOrder = ReturnOrder.findAllByWuliu_sn(params.wuliu_sn)

        def searchClosure = {

            ne("status", "delete")
            eq('add_user', session.loginPOJO.user.id)
            inList('orderSN',retrunOrder.orderSN)
        }

        def o = DaiFaOrder.createCriteria();
        def results = o.list(params, searchClosure)



        def map = [list: results]

        render(view: "/member/saleReturn/returnDesc2", model: map)
    }


//会员强制取消订单   地
    def killOrder(){


        memberDaiFaService.killOrderIng(params)

//
//
        render "1"
    }



    public download(String strUrl) {

        String img_url=util.SysPropertUtil.getString("img_url");

        String fileName = strUrl.substring(strUrl.lastIndexOf("/") + 1);
        fileName = fileName.replace("_.webp","")
        String path = properties.getProperty("taobaocache")
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdir();
        path += fileName
        if(new File(path).exists()){
            path = path.replace("/home/picserver/p/p","")
            return img_url+path;
        }
        URL url = null;
        try {
            url = new URL(strUrl.replace("_.webp",""));
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            return;
        }

        InputStream is = null;
        try {
            is = url.openStream();
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = is.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            path = path.replace("/home/picserver/p/p","")
            return img_url+path;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

}
