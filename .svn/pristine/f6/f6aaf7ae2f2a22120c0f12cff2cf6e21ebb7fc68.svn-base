/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin
import ship.Area
import ship.Express
import ship.AreaShip
import sysinit.SysInitParams
/**
 *
 * @author DELL
 */
class AdminAreaShipController extends BaseController{
	def expressList() { 
       
        def list = Express.list()
        
        def map = [list: list]
    
        
        render(view: "/admin/areaShip/expressList",model:map)
        
        
    }
    
    def shipList(){
        def express = Express.get(params.id)
        def areaList = Area.findAllByLevel("1")
        def shipList = AreaShip.findAllByExpress(express)
        def shipMap = [:]
        shipList.each{
            shipMap.put(it.area_id,it)
        }
        
         def map = [areaList: areaList,shipMap:shipMap,express:express]
    
        
        render(view: "/admin/areaShip/shipList",model:map)
    }
    
    def doUpdate(){
        def express = Express.get(params.express_id)
        AreaShip.executeUpdate("delete AreaShip d where d.express = :express", [express:express])
        params.area_id.eachWithIndex{it, i -> 
            if(params.f_price[i] && params.x_price[i]){
                def areaShip = new AreaShip()
                areaShip.area_id = params.area_id[i]
                areaShip.f_price = new BigDecimal(params.f_price[i]) 
                areaShip.x_price = new BigDecimal(params.x_price[i])
                areaShip.express = express
                areaShip.save()
            }
        }
        
        flash.message="设置地区运费成功";
        render(view:this.commonSuccess)
    }
    
    
}

