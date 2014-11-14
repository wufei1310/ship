/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package member

import admin.BaseController
import ship.TranLog
/**
 *
 * @author DELL
 */
class MemberTranLogController extends BaseController{
	
        def list() { 

        
        
        if (!params.max) {
            params.max = 10  
        }else{
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0  
        params.sort = "dateCreated"  
        params.order = "desc" 
        
        def searchClosure =  {
            
           
            if(params.orderSN){
                like('orderSN',"%"+params.orderSN+"%")
            }
            if(params.start_date){
                ge('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 00:00:00"))
            }
            if(params.end_date){
                le('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.end_date+" 23:59:59"))
            }
            ne("type","1")
            ne("type","2")
            ne("type","15")
            ne("type","16")
            ne("type","17")
            ne("type","18")
            eq("order_user",session.loginPOJO.user.id)
        }
         def o = TranLog.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
        
         render(view: "/member/tranLog/list",model:map)
       
        
    }
}

