package admin

import ship.LoginPOJO
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
class AdminEmailController extends BaseController{
    
    def adminEmailService
    def adminFinaceReportService
    def sendEmail(){
        if(session.loginPOJO.user.email == 'superquan'){
//                       adminEmailService.sendEmail(params)
           adminFinaceReportService.sendEmail(params)
            render "邮件发送成功"
            return
        }else{
            render "没有权限"
        }
             
         
    }
     
    
    def outWater(){
      
        //        def c = DaiFaGoods.createCriteria()
        //        def nums = c.get {
        //            projections {
        //                sum "num"
        //            }
        //        }
        //        render nums
        def daiFaOrder = DaiFaOrder.get(2)
        def sql = "select sum(d.num) from DaiFaGoods d where daiFaOrder = :daiFaOrder";
        render DaiFaGoods.executeQuery(sql,[daiFaOrder:daiFaOrder])[0];
        
    }
}
