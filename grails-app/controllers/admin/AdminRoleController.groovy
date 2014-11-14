/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin

import ship.Role
import ship.Menu
/**
 *
 * @author DELL
 */
class AdminRoleController extends BaseController{
    def index() { 
       
    }
    
    def list() { 
         
        
        
        
         
        if (!params.max) 
        params.max = 10
        if (!params.offset) params.offset = 0  

        
        def searchClosure =  {
            
           
            if(params.roleName){
                like('roleName',"%"+params.roleName+"%")
            }
           
        }
        
        def o = Role.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
       
        render(view: "/admin/role/list",model:map)
        
       
    }
    
    def toAdd(){
        render(view: "/admin/role/toAdd")
    }
    
    def doAdd(){
        def Role = new Role(params);
        Role.save();
        flash.message="添加角色成功";
        render(view:this.commonSuccess)
    }
    def toUpdate(){
        def role = Role.get(params.id);
        def map = [role:role]
        render(view: "/admin/role/toUpdate",model:map)
    }
    def doUpdate(){
        def role = Role.get(params.id);
        role.properties = params
        flash.message="修改角色成功";
        render(view:this.commonSuccess)
    }
    def toAuth(){
        def role = Role.get(params.id);
        def menu = Menu.findAllByPIdAndStatus(0,"1")
        menu.each { it ->
            def childrenMenu = Menu.findAllByPIdAndStatus(it.id,"1")
            it.childrenMenu = childrenMenu
        }
        def map = [menuList:menu,role:role,checkedMenu:role.menu.id]
        
        render(view: "/admin/role/toAuth",model:map)
    }
    def doAuth(){
        def role = Role.get(params.id);
        role.menu=null;
        if(params.menu_id instanceof String){
            role.addToMenu(Menu.get(params.menu_id))
        } else{
            params.menu_id.eachWithIndex{it, i ->
                role.addToMenu(Menu.get(params.menu_id[i]))
            }
        }

        flash.message="分配菜单权限成功";
        render(view:this.commonSuccess)
    }
}

