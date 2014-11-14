/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin

import grails.converters.JSON
import ship.Area
import ship.MobileMessage
import sysinit.SysInitParams
/**
 *
 * @author DELL
 */
class AdminAreaController extends BaseController{

    def adminAreaService


    def mobileList(){
        def results = adminAreaService.mobileList(params)

        def map = [list: results]

        def mm = new MobileMessage()
        mm.result = "success"
        mm.message = "获取地区成功"
        mm.model = map
        render mm as JSON
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
        if(!params.level){
           params.level = "0"; 
        }
        def searchClosure =  {
            
           
            if(params.name){
                like('name',"%"+params.name+"%")
            }
            
               eq('pId',params.pId)
           
        } 
        
        def o = Area.createCriteria();
        def results = o.list(params,searchClosure)
        
        List topList = new ArrayList();
        String top_pId = params.pId
        def areaMap = SysInitParams.getAreaMap()
        while(top_pId!='0'){
           def area = areaMap.get(top_pId)
           top_pId = area.pId
           topList.add(0,area)
        }
        
        def map = [list: results, total: results.totalCount,topList:topList]
    
        
        render(view: "/admin/area/list",model:map)
        
        
    }
    
    
    def toAdd(){
        
        def p_name;
        if(params.pId=='0'){
            p_name = "无"
        }else{
            p_name = Area.get(params.pId).name;
        }

        def map = [p_name:p_name]
        render(view: "/admin/area/add",model:map)
    }
    
    def toUpdate(){
        def area = Area.get(params.id)
        def p_name;
        if(area.pId=='0'){
            p_name = "无"
        }else{
            p_name = Area.get(area.pId).name;
        }

        def map = [p_name:p_name,area:area]
        render(view: "/admin/area/update",model:map)
    }
    
    def doAdd(){
        
        def area = new Area(params);
        area.id = getAreaId(params.pId,params.level)
        area.add_user = session.loginPOJO.user.id
        area.save(flush:true);
        SysInitParams.getInstance().initAreaMap()
        flash.message = "添加地区成功"
        flash.messageClass=this.success
        render(view:this.commonSuccess)
    }
    
    def doUpdate(){

        def area = Area.get(params.id)
        area.properties = params
        area.add_user = session.loginPOJO.user.id
        area.save(flush:true)
        SysInitParams.getInstance().initAreaMap()
        flash.message = "修改地区成功"
        flash.messageClass=this.success
        render(view:this.commonSuccess)
    }
    
    def doDelete(){
        def area = Area.get(params.id)
        area.delete(flush:true);
        SysInitParams.getInstance().initAreaMap()
        flash.message = "删除地区成功"
        flash.messageClass=this.success
        render(view:this.commonSuccess)
    }
    
    public String getAreaId(pId,level){
        def area_id = Area.executeQuery("select max(a.id) from Area a where a.pId like concat(?,'%') and level = ?", [pId,level])[0]
        if(!area_id){
			return pId+"100";
		}else{
                        print area_id
			return "0"+(Integer.parseInt(area_id)+1);
		}
    }
}

