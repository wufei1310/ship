/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package member

import org.springframework.web.multipart.commons.CommonsMultipartFile
import sysinit.SysInitParams
import util.GoodsExcelImporter
import ship.Market
import ship.DaiFaOrder
/**
 *
 * @author DELL
 */
class MemberImportGoodsController {
    
        def toGoodsImport(){
            render(view:"/member/daiFaOrder/goodsImport")
        }
    
	def goodsImport(){
            CommonsMultipartFile file = request.getFile("goods_path")
            GoodsExcelImporter goodsExcelImporter =new GoodsExcelImporter(file.getInputStream())
            List list =  goodsExcelImporter.getGoods()
            def markets = Market.list().market_name
            def floors = ['1楼','2楼','3楼','4楼','5楼','6楼','7楼','8楼','9楼']
            StringBuffer s = new StringBuffer()
            list.eachWithIndex{it, i -> 
                def market = it.market
                def floor = it.floor
                def num = it.num
                def price = it.price
                if(!markets.contains(market)){
                    s.append("第"+(i+1)+"行 市场‘"+market+"’填写错误，只能填写以下内容("+markets+")<br/>");
                }
                if(!floors.contains(floor)){
                    s.append("第"+(i+1)+"行 楼层‘"+floor+"’填写错误，只能填写以下内容("+floors+")<br/>");
                   
                }
                try{
                    Double.valueOf(num)
                }catch(Exception e){
                    s.append("第"+(i+1)+"行 件数‘"+num+"’填写错误，只能填写数字<br/>");
                }
                
                try{
                    Double.valueOf(price)
                }catch(Exception e){
                    s.append("第"+(i+1)+"行 单件价格‘"+price+"’填写错误，只能填写数字<br/>");
                }
            }
            if(s.length()>0){
                flash.message=s.toString()
            }else{
                flash.message="导入成功，请仔细检查数据是否正确"
            }
             def daiFaOrder = DaiFaOrder.findByAdd_user(session.loginPOJO.user.id,[max: 1, sort: "dateCreated", order: "desc", offset: 0])
               if(!daiFaOrder){
                   daiFaOrder = new DaiFaOrder()
               }


        def areaChild = SysInitParams.getAreaChildMap().get("0100");
               def map = [daiFaOrder:daiFaOrder,market:markets,goodsList:list,lot:"lot",areaChild:areaChild]
                render(view: "/member/daiFaOrder/add",model:map)
        }
}

