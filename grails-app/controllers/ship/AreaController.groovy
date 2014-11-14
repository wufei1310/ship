package ship

import ship.Area
import sysinit.SysInitParams
import grails.converters.JSON

class AreaController {

    def index() { }
    
    def reqAreaField(){
        render(view:"/commons/areaField",model:params)
    }
    
    def reqAreaPannel(){
        render(view:"/commons/area",model:params)
    }
       
    def initChildArea(){
        def areaChild = SysInitParams.getAreaChildMap().get(params.pId);
        if(areaChild){
            render areaChild as JSON
        }else{
            render ""
        }
    }
}
