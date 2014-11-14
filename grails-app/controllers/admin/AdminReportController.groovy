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

class AdminReportController extends BaseController{
        
    
    
    def orderExport() { 
        
        def searchClosure =  {
            
           
            if(params.start_date){
                ge('payTime',Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 00:00:00"))
            }
            if(params.end_date){
                le('payTime',Date.parse("yyyy-MM-dd HH:mm:ss",params.end_date+" 23:59:59"))
            }
            sort:"payTime"
            order:"asc"
            eq("status","shipped")

        }
        
        
        def o = DaiFaOrder.createCriteria();
        def results = o.list(searchClosure)
        
        Map<String,List> dateMap = new TreeMap<String,List>();
        Map<String,Integer> countMap = new HashMap<String,Integer>();
        
        results.each{
            if(it.payTime){
                String payTime = it.payTime.toString().substring(0,10)
                if(dateMap.containsKey(payTime)){
                    
                    dateMap.get(payTime).add(it)

                    def count = countMap.get(payTime) + it.daiFaGoods.size()
                    countMap.put(payTime,count)
                }else{
                    List orderList = new ArrayList()
                    orderList.add(it)
                    dateMap.put(payTime,orderList)

                    countMap.put(payTime,it.daiFaGoods.size())
                }
            }
            
        }
        
        print countMap
        def map = [dateMap:dateMap,countMap:countMap]
        render(view: "/admin/report/orderExport",model:map)
        
    }
    
    def list(){
        render(view: "/admin/report/list")
    }
    
    
}
