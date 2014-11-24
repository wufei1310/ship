package admin
import ship.DaiFaOrder
import grails.converters.JSON
import ship.MobileMessage
import ship.Pager
import ship.User
import ship.DiffOrder
import ship.DaiFaGoods
import ship.GoodsLog
import util.StringUtil
import ship.TranLog

class AdminTranLogController extends BaseController{
        
    
    
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

            if(params.userid){
                eq('order_user',params.userid as Long)
            }
           
            if(params.orderSN){
                like('orderSN',"%"+params.orderSN+"%")
            }
            if(params.start_date){
                ge('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 00:00:00"))
            }
            if(params.end_date){
                le('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.end_date+" 23:59:59"))
            }
            if(params.direction){
                eq("direction",params.direction)
            }
            if(params.shouru_type){
                eq("shouru_type",params.shouru_type)
            }
            if(params.type){
                eq("type",params.type)
            }
        }
        
        def o = TranLog.createCriteria();
        def results = o.list(params,searchClosure)
        
        //收入
        searchClosure =  {

            projections{
              sum("amount")
            }
            if(params.userid){
                eq('order_user',params.userid as Long)
            }
            if(params.orderSN){
                like('orderSN',"%"+params.orderSN+"%")
            }
            if(params.start_date){
                ge('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 00:00:00"))
            }
            if(params.end_date){
                le('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.end_date+" 23:59:59"))
            }
            if(params.type){
                eq("type",params.type)
            }
            if(params.shouru_type){
                eq("shouru_type",params.shouru_type)
            }
            eq("direction","0")
        }
        def shouru = TranLog.createCriteria().list(searchClosure)[0]
        
        searchClosure =  {
            projections{
              sum("amount")
            }
            if(params.userid){
                eq('order_user',params.userid as Long)
            }
            if(params.orderSN){
                like('orderSN',"%"+params.orderSN+"%")
            }
            if(params.start_date){
                ge('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 00:00:00"))
            }
            if(params.end_date){
                le('dateCreated',Date.parse("yyyy-MM-dd HH:mm:ss",params.end_date+" 23:59:59"))
            }
            if(params.type){
                eq("type",params.type)
            }
            eq("direction","1")
        }
        def zhichu = TranLog.createCriteria().list(searchClosure)[0]
        
        def map = [list: results, total: results.totalCount,shouru:shouru,zhichu:zhichu]
    
        
         render(view: "/admin/tranLog/list",model:map)
       
        
    }
    
    
}
