package member

import taobao.TaobaoUtil
import ship.DaiFaOrder
class MemberTaoBaoOrderController {

    def index() { }
    
    
    def toAdd(){
        if(params.tid)
            session.loginPOJO.tid = params.tid
        
        if(params.top_session)
            session.loginPOJO.top_session = params.top_session
            
        if(!session.loginPOJO.top_session){
            redirect(url: "http://container.api.taobao.com/container?appkey=21705358")
            return;
        }
        TreeMap<String, String> apiparamsMap = new TreeMap<String, String>();
        apiparamsMap.method = "taobao.trade.fullinfo.get"
        apiparamsMap.session = session.loginPOJO.top_session
        apiparamsMap.fields = "orders.num,orders.payment,orders.sku_properties_name,orders.title,orders.total_fee,post_fee,receiver_address,receiver_city,receiver_district,receiver_mobile,receiver_name,receiver_state,orders.outer_iid"
        apiparamsMap.tid = session.loginPOJO.tid
        def result = new TaobaoUtil().getResultStr(apiparamsMap) 
        
        if(!result){
            session.loginPOJO.top_session = null;
            flash.message = "同步淘宝订单数据失败，请重试或者手动创建订单"
            redirect(controller:"memberDaiFaOrder",action: "list")
        }else if(result == "error session"){
            redirect(url: "http://container.api.taobao.com/container?appkey=21705358")
            return;
        }else{

            //解析xml
            def products = new XmlParser().parseText(result)
          //  DaiFaOrder daiFaOrder = new DaiFaOrder();
            def orderMap = [:]
            List goodsList = new ArrayList();
            products.trade.each {
          
                orderMap.reperson = it.get('receiver_name').text()
                orderMap.contphone = it.get('receiver_mobile').text()
                orderMap.address = it.get('receiver_state').text()+" "+it.get('receiver_city').text()+" "+it.get('receiver_district').text()+" "+it.get('receiver_address').text()
                orderMap.shipFee = it.get('post_fee').text()           
                
                
                it.orders.each {
                    
                    it.order.each {
                        def goodsMap = [:]
                        goodsMap.goods_sn = it.get('outer_iid').text()
                        goodsMap.spec = it.get('sku_properties_name').text()
                        goodsMap.num = it.get('num').text()
                        def price = new BigDecimal(it.get('total_fee').text()).divide(new BigDecimal(goodsMap.num)).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
                        goodsMap.price = price
                        goodsList.add(goodsMap)
                    }
                    
                }
           }
           orderMap.goodsList = goodsList
           flash.message = "同步淘宝数据完成，请仔细核对订单数据"
           render(view: "/member/daiFaOrder/taobaoAdd",model:orderMap)

        }
        
    }
    
}
