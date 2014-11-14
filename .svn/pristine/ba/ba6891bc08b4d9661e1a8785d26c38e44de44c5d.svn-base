/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package member
import common.CommonParams
import ship.DaiFaOrder
import ship.Market
/**
 *
 * @author DELL
 */

class MemberFindyiController {
	def toAdd(){
            def vvic_market_name = request.getParameterValues("vvic_market_name")
            def vvic_goods_sn = request.getParameterValues("vvic_goods_sn")
            def floor = request.getParameterValues("floor")
            def stalls = request.getParameterValues("stalls")
            def img_url = request.getParameterValues("img_url")
            def goods_spec = request.getParameterValues("goods_spec")
            def single_goods_price = request.getParameterValues("single_goods_price")
            def goods_num = request.getParameterValues("goods_num")
            def market = Market.list().market_name
            def result = new ArrayList()

                vvic_goods_sn.eachWithIndex{it, i -> 
                   if(it){
                       if(market.contains(vvic_market_name[i])){
                           def goodsMap = [:]
                            goodsMap.market = vvic_market_name[i]
                            goodsMap.floor = floor[i]
                            goodsMap.stalls = stalls[i]
                            goodsMap.goods_sn = vvic_goods_sn[i]
                            goodsMap.spec = goods_spec[i]
                            goodsMap.num = goods_num[i]
                            goodsMap.price = single_goods_price[i]
                            goodsMap.attach_id = img_url[i]
                            result.add(goodsMap)
                       }
                       
                   }
                }
                
                def daiFaOrder = DaiFaOrder.findByAdd_user(session.loginPOJO.user.id,[max: 1, sort: "dateCreated", order: "desc", offset: 0])
               if(!daiFaOrder){
                   daiFaOrder = new DaiFaOrder()
               }

               

               def map = [daiFaOrder:daiFaOrder,market:market,goodsList:result]
                render(view: "/member/daiFaOrder/add",model:map)
            }
            

}

