package admin

import grails.converters.JSON
import ship.DaiFaGoods
import ship.DaiFaOrder
import ship.MobileMessage
import ship.TranLog
import ship.User

class TuiController {

    def index() {}

    def tuiYunFei() {


        def exitTranLog =  TranLog.findByOrderSNAndType(params.orderSN,"26")
        if(exitTranLog){
            def mm = new MobileMessage()
            mm.message = "该订单已经退过运费了"
            mm.result = "fail"
            render mm as JSON
            return
        }


        def daifaOrder = DaiFaOrder.findByOrderSN(params.orderSN)
        def add_user = User.get(daifaOrder.add_user)

        //退钱  到会员
        def account = add_user.account
        account.amount = new BigDecimal(params.return_fee) + account.amount
        //26[支出]管理员直接对订单退运费
        def tranLog = new TranLog();
        tranLog.amount = new BigDecimal(params.return_fee)
        tranLog.direction = "1"
        tranLog.type = "26"
        tranLog.orderSN = daifaOrder.orderSN
        tranLog.order_user = daifaOrder.add_user
        tranLog.add_user = session.loginPOJO.user.id
        tranLog.save()


        def mm = new MobileMessage()
        mm.message = "退运费成功"
        mm.result = "success"
        render mm as JSON
        return

    }

    def tuiGoodsFee() {
        def exitTranLog =  TranLog.findByGoods_idAndType(params.goodsId,"27")
        if(exitTranLog){
            def mm = new MobileMessage()
            mm.message = "该商品已经退过货款了"
            mm.result = "fail"
            render mm as JSON
            return
        }



        def goods = DaiFaGoods.get(params.goodsId as Long)
        def add_user = User.get(goods.add_user)


        //退钱  到会员
        def account = add_user.account
        account.amount = new BigDecimal(params.return_fee) + account.amount
        //26[支出]管理员直接对商品退货款
        def tranLog = new TranLog();
        tranLog.amount = new BigDecimal(params.return_fee)
        tranLog.direction = "1"
        tranLog.type = "27"
        tranLog.goods_id = goods.id
        tranLog.orderSN = goods.daiFaOrder.orderSN
        tranLog.order_user = goods.add_user
        tranLog.add_user = session.loginPOJO.user.id
        tranLog.save()


        def mm = new MobileMessage()
        mm.message = "退货款成功"
        mm.result = "success"
        render mm as JSON
        return
    }


}
