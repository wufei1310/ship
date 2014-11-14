package admin
import ship.Menu

class AdminMenuController extends BaseController{
     def index() { 
       
    }
    
     def list() { 
       
        
        if (!params.max) {
            params.max = 10  
        }else{
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0  
        if(!params.pId){
           params.pId = "0"; 
        }
        def searchClosure =  {
            
           
            if(params.menuName){
                like('menuName',"%"+params.menuName+"%")
            }
            
               eq('pId',new Long(params.pId))
           
        } 
        
        def o = Menu.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
        
        render(view: "/admin/menu/list",model:map)
        
        
    }
    
    def toAdd(){
        
        def p_name;
        if(params.pId=='0'){
            p_name = "一级菜单"
        }else{
            p_name = Menu.get(params.pId).menuName;
        }

        def map = [p_name:p_name]
        render(view: "/admin/menu/toAdd",model:map)
    }
    
    def doAdd(){
        print params
        def Menu = new Menu(params);
        Menu.save();
        flash.message="添加菜单成功";
        render(view:this.commonSuccess)
    }
    def toUpdate(){
        def menu = Menu.get(params.id);
        def p_name;
        if(menu.pId==0){
            p_name = "一级菜单"
        }else{
            p_name = Menu.get(menu.pId).menuName;
        }
        def map = [menu:menu,p_name:p_name]
        render(view: "/admin/menu/toUpdate",model:map)
    }
    def doUpdate(){
        def menu = Menu.get(params.id);
        menu.properties = params
        flash.message="修改菜单成功";
        render(view:this.commonSuccess)
    }
    def doDelete(){
        def menu = Menu.get(params.id);
        Menu.executeUpdate("delete Menu m where m.pId = :pId", [pId:menu.id])
        menu.delete();
        flash.message="删除菜单成功";
        render(view:this.commonSuccess)
    }
}
