package member

import grails.converters.deep.JSON
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import ship.XmlGoods

class MemberXmlGoodsService {

    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    private static List<XmlGoods> xmlGoodsList = new ArrayList<XmlGoods>();
    private static Map<String,XmlGoods> xmlGoodsMap = new HashMap<String,XmlGoods>()
    private static Map<String,XmlGoods> xmlGoodsSNMap = new HashMap<String,XmlGoods>()
    private static int size =0 ;
    private static Map<String,List<XmlGoods>> storeGoodsListMap = new HashMap<String,List<XmlGoods>>();
    private static Map<String,List<XmlGoods>> stallGoodsListMap = new HashMap<String,List<XmlGoods>>();
    private static Map<String,List<XmlGoods>> marketGoodsListMap = new HashMap<String,List<XmlGoods>>();
    private static Map<String,List<XmlGoods>> marketfloorGoodsListMap = new HashMap<String,List<XmlGoods>>();

    def getXmlGoodsList(){
        return xmlGoodsList;
    }


    def initStallGoodsListMap(resultlist,params){
        resultlist = stallGoodsListMap.get(params.stall)

        if(!resultlist){
            xmlGoodsList.each{
                if(it.stall==params.stall){
                    if(stallGoodsListMap.containsKey(params.stall)){
                        stallGoodsListMap.get(params.stall).add(it)
                    }else{
                        stallGoodsListMap.put(params.stall,new ArrayList<XmlGoods>()) ;
                    }
                }
            }
            resultlist = stallGoodsListMap.get(params.stall)
        }

        return resultlist;

    }

    def initStoreGoodsListMap(resultlist,params){
        resultlist = storeGoodsListMap.get(params.store_name)

        if(!resultlist){
            xmlGoodsList.each{

                if(it.store_name==params.store_name){
                    if(storeGoodsListMap.containsKey(params.store_name)){
                        storeGoodsListMap.get(params.store_name).add(it)
                    }else{
                        storeGoodsListMap.put(params.store_name,new ArrayList<XmlGoods>()) ;
                    }
                }
            }
            resultlist = storeGoodsListMap.get(params.store_name)
        }

        return resultlist;

    }

    def initMarketGoodsListMap(resultlist,params){
        resultlist = marketGoodsListMap.get(params.market)
        if(!resultlist){
            xmlGoodsList.each{

                if(it.market==params.market){
                    if(marketGoodsListMap.containsKey(params.market)){
                        marketGoodsListMap.get(params.market).add(it)
                    }else{
                        marketGoodsListMap.put(params.market,new ArrayList<XmlGoods>()) ;
                    }
                }
            }
            resultlist = marketGoodsListMap.get(params.market)
        }

        return resultlist;
    }

    def initMarketfloorGoodsListMap(resultlist,params){
        def marketfloorkey = params.market + params.floor
        resultlist = marketfloorGoodsListMap.get(marketfloorkey)
        if(!resultlist){
            resultlist = initMarketGoodsListMap(resultlist,params)


            resultlist.each{
                if(it.floor==params.floor){

                    if(marketfloorGoodsListMap.containsKey(marketfloorkey)){
                        marketfloorGoodsListMap.get(marketfloorkey).add(it)
                    }else{
                        marketfloorGoodsListMap.put(marketfloorkey,new ArrayList<XmlGoods>()) ;
                    }
                }
            }
            resultlist = marketfloorGoodsListMap.get(marketfloorkey)
        }

        return resultlist;
    }

    def getXmlGoodsList(params,p){
        def List<XmlGoods> resultlist;
        if(xmlGoodsList.size()==0){
            init();

        }
        resultlist = xmlGoodsList;




        if(params.store_name){
            resultlist = initStoreGoodsListMap(resultlist,params)
        }else if(params.goods_sn){
            def xmlGoods =  getXmlGoodsSN(params.goods_sn);
            resultlist = []
            resultlist.add(xmlGoods)
        }else if(params.stall){
            resultlist = initStallGoodsListMap(resultlist,params)
        }else if(params.floor){
            resultlist = initMarketfloorGoodsListMap(resultlist,params)
        }else if(params.market){
            resultlist = initMarketGoodsListMap(resultlist,params)
        }



        if(!resultlist){
            resultlist = new ArrayList<XmlGoods>();
        }
        size = resultlist.size();


        int d = 16
        int s = (p-1)*d;
        int m = d;
        if(s>size-1){
            return []
        }else if(s+d>size-1){
            m = size-1;
        }else{
            m = s + d -1
        }




        return resultlist[s..m];
    }

    def getXmlGoods(id){
        return  xmlGoodsMap.get(id)
    }

    def getXmlGoodsSN(sn){

        return  xmlGoodsSNMap.get(sn)
    }

    def init() {
        xmlGoodsList.clear();
        xmlGoodsMap.clear();

        File dir = new File(properties.getProperty("hotgoodspath"));
        File[] files = dir.listFiles();

        for (xmlfile in files) {
            println xmlfile.absolutePath
            def storePOJO = new XmlSlurper().parse(xmlfile)
            for (goods in storePOJO.goodslist) {
                XmlGoods xmlGoods = new XmlGoods();
                xmlGoods.setGoods_name(goods.goods_name.text())
                xmlGoods.setFloor(goods.floor.text())
                xmlGoods.setMarket(goods.market.text())
                xmlGoods.setPic(goods.pic.text())
                xmlGoods.setPrice(goods.price.text())
                xmlGoods.setQq(goods.qq.text())
                xmlGoods.setStall(goods.stall.text())
                xmlGoods.setStore_name(goods.store_name.text())
                xmlGoods.setTel(goods.tel.text())
                xmlGoods.setUrl(goods.url.text())
                xmlGoods.setGoods_id(goods.goods_id.text())
                xmlGoods.setTaobao_id(goods.taobao_id.text())
                xmlGoods.setSkusize(goods.skusize.text())
                xmlGoods.setSkucolor(goods.skucolor.text())
                xmlGoods.setContent(goods.content.text())
                xmlGoods.setGoods_sn(goods.goods_sn.text())

                xmlGoodsList.add(xmlGoods);
                xmlGoodsMap.put(goods.goods_id.toString(),xmlGoods)
                xmlGoodsSNMap.put(goods.goods_sn.toString(),xmlGoods)
            }
        }


        Collections.shuffle(xmlGoodsList);

    }
}
