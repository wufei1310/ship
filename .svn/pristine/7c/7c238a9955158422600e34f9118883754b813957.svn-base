package admin
import ship.Remit
import ship.User

class AdminRemitController extends BaseController{
    def adminRemitService;
    
    def index() { }
    
    def doCheck(){
        def status = params.status
        def id = params.id
        def reason = params.reason
        def msg = adminRemitService.doCheck(id,status,reason,session.loginPOJO.user)
        if(msg){
            flash.message=msg;
        }else{
             flash.message="审核成功";
        }
       
        render(view:this.commonSuccess)
    }
    
    def list() { 
        
        if (!params.max) {
            params.max = 10  
        }else{
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0  
        if (!params.sort) params.sort = "lastUpdated"  
        if (!params.order) params.order = "desc" 
        
        def searchClosure =  {
            
           
            if(params.status){
                eq('status',params.status)
            }
           
        }
        
        def o = Remit.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
        render(view: "/admin/remit/list",model:map)
        
    }
    
   
    
}
