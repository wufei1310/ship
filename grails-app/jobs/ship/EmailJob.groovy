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

