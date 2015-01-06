/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package member

import admin.BaseController
import ship.AlipayRemit
/**
 *
 * @author DELL
 */
class MemberAlipayRemitController extends BaseController {
	
    
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
       def remitSN = "C"+(new Date().getTime()).toString();
       def total_fee = new BigDecimal(params.amount)
       if(total_fee>0){
           redirect(controller:"memberAlipay",action: "reqPay", params: [total_fee:params.amount,payType:"4",orderSN:remitSN,body:session.loginPOJO.user.id])
       }else{
           render "充值出错"
       }
       
    }

}

