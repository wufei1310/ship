package admin

import grails.converters.JSON
import ship.Area
import ship.MobileMessage

class AdminAreaService {

    def serviceMethod() {

    }

    def mobileList(params){
        def searchClosure =  {

            if(params.pId){
                eq('pId',params.pId)
            }else{
                eq('pId',"0100")
            }
        }
        def o = Area.createCriteria();
        def results = o.list(params,searchClosure)

        return  results
    }
}
