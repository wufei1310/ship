/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package member

import admin.BaseController
import ship.AlipayRemit
import util.DecimalUtil
import java.text.DecimalFormat

/**
 *
 * @author DELL
 */
class MemberAlipayRemitController extends BaseController {
	def memberAlipayService;
    
    def list() { 
        if (!params.max) params.max = 10  
        if (!params.offset) params.offset = 0  
        if (!params.sort) params.sort = "lastUpdated"  
        if (!params.order) params.order = "desc" 
       
       def searchClosure =  {
            
           
            user{
                 eq('id',session.loginPOJO.user.id)
            }
               
            
           
        }
        
        def o = AlipayRemit.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
        render(view: "/member/alipayRemit/list",model:map)
    }
    
    def toAdd(){
        render(view: "/member/alipayRemit/add")
    }
    
    def doAdd(){
        def shouxufeepoint = memberAlipayService.shouxufee();
       def remitSN = "C"+(new Date().getTime()).toString();
       def total_fee = new Double(params.amount)
       def shouxu_fee = DecimalUtil.mul(total_fee,new Double(shouxufeepoint))

        if(shouxu_fee<0.5){
            shouxu_fee = 0.5
        }

       def payamount = new BigDecimal(total_fee) + new BigDecimal(shouxu_fee)
        DecimalFormat df = new DecimalFormat("#.00");
        println df.format(payamount)
       if(payamount>0){
           redirect(controller:"memberAlipay",action: "reqPay", params: [shouxu_fee:shouxu_fee, total_fee:df.format(payamount),payType:"4",orderSN:remitSN,body:session.loginPOJO.user.id])
       }else{
           render "充值出错"
       }
       
    }

    def test(){
        def shouxufee = memberAlipayService.shouxufee();

        render shouxufee
        render "<br/>"
        def total_fee = new Double("101.2")
        def ji = DecimalUtil.mul(total_fee,new Double(shouxufee))
        render DecimalUtil.round(ji,1)
    }




}

