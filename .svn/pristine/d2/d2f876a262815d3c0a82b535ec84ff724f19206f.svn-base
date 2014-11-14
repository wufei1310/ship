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
import ship.ReturnGoodsApp

class AdminReturnGoodsAppController extends BaseController{
        
    def adminReturnGoodsAppService
    
    def doAdd(){
        
            flash.message="退换货申请已由会员提交，现已不支持管理员提交申请，请安装最新客户端程序包！";
            flash.messageClass=this.error
            
            def mm = new MobileMessage()
            mm.message = flash.message
            mm.result = "fail"
            mm.model.put("ordergoods",null)
            render mm as JSON
        
//        def o = DaiFaGoods.createCriteria();
//        def goods = o.get{eq("status", "9") eq("id", new Long(params.id))}
//        
//        
//        if(!goods || session.loginPOJO.user.role != 'admin'){
//            flash.message="提交申请出错";
//            flash.messageClass=this.error
//
//        }else if(ReturnGoodsApp.findByDaiFaGoodsAndStatus(goods,"0")){
//             flash.message="已经提交过申请，请勿重复提交";
//             flash.messageClass=this.error
//
//        }else{
//            def returnGoodsApp = new ReturnGoodsApp()
//           returnGoodsApp.orderSN = goods.daiFaOrder.orderSN
//           returnGoodsApp.tuihuo_price = new BigDecimal(params.tuihuo_price)
//           returnGoodsApp.daiFaGoods = DaiFaGoods.get(params.id)
//           returnGoodsApp.tuihuo_num = new Long(params.tuihuo_num)
//           returnGoodsApp.status = "0"
//           returnGoodsApp.addUser = User.get(session.loginPOJO.user.id)
//           returnGoodsApp.save()
//           
//            flash.message="提交退货退款申请成功";
//            flash.messageClass=this.success
//
//            
//            def goodsLog = new GoodsLog();
//            goodsLog.daiFaGoods = returnGoodsApp.daiFaGoods
//            goodsLog.addUser = returnGoodsApp.addUser
//            goodsLog.logdesc = "提交退货退款申请"
//            goodsLog.save()
//        }
//
//           
//
//            
//           
//        
//        if(params.mobile=="mobile"){    
//            
//            def paramsMap = new HashMap()
//            goods.properties.each{k,v->
//                if(k!='daiFaOrder' && k!='daifa_user' && k!='check_user')
//                paramsMap.put(k,v)
//            }
//            paramsMap.put("orderSN",goods.daiFaOrder.orderSN)
//            paramsMap.put("id",goods.id)
//            
//            
//            def mm = new MobileMessage()
//            mm.message = flash.message
//            mm.result = "success"
//            mm.model.put("ordergoods",paramsMap)
//            render mm as JSON
//        }else{
//            render(view:this.commonSuccess)
//            // redirect(action: "list", params: params)
//            return 
//        }
    }
    
    
    def list() { 
        
        
        
        if (!params.max) {
            params.max = 10  
        }else{
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0  
        params.sort = "dateCreated"  
        params.order = "desc" 
        
        def searchClosure =  {
            
           
            if(params.goods_sn){
                daiFaGoods{
                    like('goods_sn',"%"+params.goods_sn+"%")
                }
                
            }
            if(params.status){
                eq("status",params.status)
            }
            if(params.orderSN){
                like('orderSN',"%"+params.orderSN+"%")
            }
            
            //如果不是超级管理员则只能看自己
             if(session.loginPOJO.user.email != 'superquan'){
                 addUser{
                     eq("id",session.loginPOJO.user.id)
                 }
             }
        }
        
        def o = ReturnGoodsApp.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
        
        if(params.mobile=="mobile"){
           
            //封装分页传给手机端
            def pager = new Pager(max:params.max,"offset":params.offset,"totalPage":((results.totalCount-1)/params.max + 1) as int ,total:results.totalCount)
            map.put("pager",pager)
            
            def mm = new MobileMessage()
            mm.message = "获取退款申请列表成功"
            mm.result = "success"
            mm.model = map
            
            render mm as JSON
        }else{
            render(view: "/admin/returnGoodsApp/list",model:map)
        }
        
    }
    
    def  checkReturnGoods(){
        adminReturnGoodsAppService
        // try{
            adminReturnGoodsAppService.doCheckReturnGoods(params,User.get(session.loginPOJO.user.id))
            flash.message = "操作成功"
            flash.messageClass=this.success
            
       // }catch(Exception e){
       //     flash.message = e.getMessage()
       //     flash.messageClass=this.error
       // }
         render(view:this.commonSuccess)
            // redirect(action: "list", params: params)
            return 
    }
    
}
