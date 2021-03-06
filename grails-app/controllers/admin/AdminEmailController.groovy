package admin

import ship.LoginPOJO
import ship.ReturnGoods
import ship.ReturnOrder
import ship.TranLog
import ship.User
import ship.Menu
import ship.Role
import ship.MobileMessage
import grails.converters.JSON
import ship.Pager
import ship.Account
import ship.Market
import ship.DaiFaGoods
import ship.DaiFaOrder

import java.text.SimpleDateFormat

class AdminEmailController extends BaseController {

    def adminEmailService
    def adminFinaceReportService

    def sendEmail() {
        if (session.loginPOJO.user.email == 'superquan') {
//                       adminEmailService.sendEmail(params)
            adminFinaceReportService.sendEmail(params)
            render "邮件发送成功"
            return
        } else {
            render "没有权限"
        }


    }


    def outWater() {

        //        def c = DaiFaGoods.createCriteria()
        //        def nums = c.get {
        //            projections {
        //                sum "num"
        //            }
        //        }
        //        render nums
        def daiFaOrder = DaiFaOrder.get(2)
        def sql = "select sum(d.num) from DaiFaGoods d where daiFaOrder = :daiFaOrder";
        render DaiFaGoods.executeQuery(sql, [daiFaOrder: daiFaOrder])[0];

    }


    def rebuildReturnData() {
        def sn = []
        sn.add("141114030704-1")
        sn.add("141114066246-2")
        sn.add("141115029727-1")
        sn.add("141112206938-1")
        sn.add("141114171228+5")
        sn.add("141116030246-1")
        sn.add("141116075690-1")
        sn.add("1411170213082-1")

        sn.each {
            println it
            def returnOrder = ReturnOrder.findByOrderSN(it)

            returnOrder.orderSN = "K" + returnOrder.orderSN
            returnOrder.save();

            ReturnOrder returnOrderClone = returnOrder.clone();
            returnOrderClone.id = null;
            returnOrderClone.orderfrom = "member"
            returnOrderClone.orderSN = returnOrder.orderSN.replace("K", "M")

            returnOrderClone.returnGoods = null;
            returnOrder.returnGoods.each() {
                ReturnGoods returnGoodsClone = it.clone();
                returnGoodsClone.id = null;
                returnOrderClone.addToReturnGoods(returnGoodsClone);
            }

            render returnOrderClone.save();
        }


    }


    def updateReturnOrderKM() {

        //本已经退货完成的商品，由于退回价格高于会员期往价
        //在会员降低期望价后，重新提交退货申请，此时k表退货商品已经是退货完成状态，但k表状态变为1了
        //该查询集合用于变更k表申请状态为2
        def autoReturnOrderList = ReturnOrder.findAllByOrderfromAndStatusAndNeedTui("kings", "1", "1")
        autoReturnOrderList.each{autoReturnOrder->
            autoReturnOrder.status = "2" ;//如果这里把状态变为2会员后台将会再次看见收到包裹 情况
            autoReturnOrder.returnGoods.each{autoReturnOrderGoods->
                if(autoReturnOrderGoods.status == "1"){
                    autoReturnOrder.status = "1"//当有商品还是等待分拣状态时，该退货申请还是待处理状态
                }
            }
        }


        def returnOrderList = ReturnOrder.findAllByOrderfromAndStatusAndNeedTui("kings", "2", "1")
        // render    returnOrderList as JSON
        int i = 0;
        returnOrderList.each {
            //  render it.orderSN.substring(1)
            def memberReturnOrder = ReturnOrder.findByOrderSNAndOrderfrom("M" + it.orderSN.substring(1), "member")
            // render memberReturnOrder as JSON

            if (memberReturnOrder) {
                it.ishuiyuanxiadan = "1"
                it.save();
                i++;
                render it.orderSN + "<br/>"
            }
        }
        render "同步"+i+"条退货完成数据，会员已经下单，可审核"
    }

    def updateReturnOrderKM1115() {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        def startDate = Date.parse("yyyy-MM-dd HH:mm:ss", "2014-11-1 00:00:00")
        def endDate = Date.parse("yyyy-MM-dd HH:mm:ss", "2014-11-15 23:59:59")

        def returnOrderList = ReturnOrder.findAllByDateCreatedBetween(startDate, endDate);
        returnOrderList.each {

            if (it.orderSN.startsWith("K")) {

            } else {
                if (!it.orderSN.startsWith("M")) {
                    it.orderSN = "M" + it.orderSN
                }
            }

            println it.orderSN
        }
    }

    def searchNoPayOrderList() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        def startDate = Date.parse("yyyy-MM-dd HH:mm:ss", "2014-10-15 00:00:00")
        def endDate = Date.parse("yyyy-MM-dd HH:mm:ss", "2014-11-27 23:59:59")

        def returnOrderList = ReturnOrder.findAllByDateCreatedBetweenAndOrderfrom(startDate, endDate, "member");
//        render returnOrderList.orderSN
        def nokorder = []
        render '<table border="1"><tr><td>物流单</td><td>M单号</td><td>K单号</td><td>退款金额</td><td>退款时间</td><td>代发结账</td></tr>'
        returnOrderList.each {
            if(it.orderSN.startsWith("M")){
                def sn = it.orderSN.substring(1)
                def korder = ReturnOrder.findByOrderSN("K"+sn);
                if(!korder){

                    def tranLog = TranLog.findByTypeAndOrderSNLike("10","%"+sn)


                    render '<tr><td>'+it.wuliu_sn+'</td><td>'+it.orderSN+'</td><td>'+ korder?.orderSN+'</td><td>'+ tranLog?.amount+'</td><td>'+tranLog?.dateCreated +'</td><td>'+ it.returnGoods.is_qianshou+ '</td></tr>'

                }

            }

        }
        render "</table>"
    }
}
