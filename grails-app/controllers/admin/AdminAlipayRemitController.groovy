/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//test git
package admin
import ship.AlipayRemit
/**
 *
 * @author DELL
 */
class AdminAlipayRemitController {
	def list() { 
        if (!params.max) params.max = 10  
        if (!params.offset) params.offset = 0  
        if (!params.sort) params.sort = "lastUpdated"  
        if (!params.order) params.order = "desc" 
       
       def searchClosure =  {
            if(params.remitSN){
                eq("remitSN",params.remitSN)
            }
            
            if(params.start_date){
                 ge('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 00:00:00"))
             }
             if(params.end_date){
                 le('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.end_date+" 23:59:59"))
             }
             
            user{
                if(params.email){
                    eq("email",params.email)
                }  
            }
               
            
           
        }
        
        def o = AlipayRemit.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
        render(view: "/admin/alipayRemit/list",model:map)
    }
}

