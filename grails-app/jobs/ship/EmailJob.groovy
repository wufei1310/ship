/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ship

import grails.converters.JSON

import java.text.SimpleDateFormat
import ship.TranLog
import ship.DaiFaOrder
import ship.User
import util.StringUtil
/**

 *
 * @author DELL
 */
class EmailJob {
    def adminFinaceReportService
    static triggers = {
        cron name: 'myTrigger', cronExpression: "1 0 0 * * ?"
    }
    def execute() {

        adminFinaceReportService.sendEmail(null)


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




        def returnOrderList = ReturnOrder.findAllByOrderfromAndStatusAndNeedTui("kings","2","1")
        // render    returnOrderList as JSON

        returnOrderList.each{
          //  render it.orderSN.substring(1)
            def memberReturnOrder = ReturnOrder.findByOrderSNAndOrderfrom("M"+it.orderSN.substring(1),"member")
           // render memberReturnOrder as JSON
            if(memberReturnOrder){
                it.ishuiyuanxiadan="1"
                it.save();
            }
        }

        //无退款订单状态更新
        def noReturnOrderList = ReturnOrder.findAllByOrderfromAndStatusAndNeedTui("kings","2","3")
        returnOrderList.each{
            //  render it.orderSN.substring(1)
            def memberReturnOrder = ReturnOrder.findByOrderSNAndOrderfrom("M"+it.orderSN.substring(1),"member")
            // render memberReturnOrder as JSON
            if(memberReturnOrder){
                it.ishuiyuanxiadan="1"
                it.status = "2"
                it.save();
            }
        }
    }
}

