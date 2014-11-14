
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sysinit
import grails.converters.JSON
import ship.Menu
import ship.Area
/**
 *
 * @author DELL
 */
class SysInitParams {

        private static List<String> menuList = null
        private static SysInitParams sysInitParams
        private static Map<String,Area> areaMap = null
        private static Map<String,List<Area>> areaChildMap = null
        
        private SysInitParams(){

            initMenuList()
            initAreaMap()
        }
        
       
        public static SysInitParams getInstance(){
		if(sysInitParams==null){
			sysInitParams = new SysInitParams();
		}
		return sysInitParams;
	}
        
 
       
        public void initMenuList(){
           def menu = Menu.list()
           List result = new ArrayList()
           menu.each{
               if(it.menuPath)
                  result.add(it.menuPath)
           }
           menuList = result
       }
       
    public void initAreaMap(){
        def area = Area.list()
        areaMap = new HashMap<String,Area>();
        areaChildMap = new HashMap<String,List<Area>>();
        area.each{
            areaMap.put(it.id,it)
            
            if(areaChildMap.keySet().contains(it.pId)){
                areaChildMap.get(it.pId).add(it);
            }else{
                List<Area> list = new ArrayList<Area>();
                list.add(it)
                areaChildMap.put(it.pId,list)
            }
            
            
        }
           
    }
       
      
       public static List getMenuList(){
           return menuList
       }
        public static Map<String,Area> getAreaMap(){
        return areaMap
    }
    
    public static Map<String,List<Area>> getAreaChildMap(){
        return areaChildMap
    }
  
}

