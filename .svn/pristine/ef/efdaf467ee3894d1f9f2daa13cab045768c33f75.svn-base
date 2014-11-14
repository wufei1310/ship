/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ship
import sysinit.SysInitParams
/**
 *
 * @author DELL
 */
class AreaNameTagLib {
	def areaName = { attrs ->
        if(!attrs.area_id || attrs.area_id.length() <=4){
            out << "中国"
        }else{
            String str = ""
            String area_id = attrs.area_id
            while(area_id.length()>4){
                def area = SysInitParams.getAreaMap().get(area_id)
                str =  area.name+"-"+str
                area_id = area.pId
            }
            out << str
        }
    }

    def areaProvice = {  attrs ->
        if(!attrs.area_id || attrs.area_id.length() <7){
            out << "中国"
        }else{
            String area_id = attrs.area_id
            def area = SysInitParams.getAreaMap().get(area_id.substring(0,7))
            out << area.name
        }
    }
}

