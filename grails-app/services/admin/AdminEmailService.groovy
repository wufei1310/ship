/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package admin

import java.text.SimpleDateFormat
import ship.TranLog
import ship.DaiFaOrder
import ship.User
import ship.ReturnOrder
import util.StringUtil
import grails.converters.JSON
/**
 *
 * @author DELL
 */
class AdminEmailService {
    def mailService
    def groovyPageRenderer
    
    def sendEmail(params){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	String nowDate = df.format(new Date()-1)
        
        def startDate = Date.parse("yyyy-MM-dd HH:mm:ss",nowDate+" 00:00:00")
        def endDate = Date.parse("yyyy-MM-dd HH:mm:ss",nowDate+" 23:59:59")
        
        if(params){
            if(params.start_date){
                startDate = Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 00:00:00")
                //endDate = Date.parse("yyyy-MM-dd HH:mm:ss",params.date+" 23:59:59")
            }
            if(params.end_date){
                endDate = Date.parse("yyyy-MM-dd HH:mm:ss",params.end_date+" 23:59:59")
            }
        }
        
        def t_ali_goods = new BigDecimal(0)
        
        def t_ali_ship = new BigDecimal(0)
        def t_ali_service = new BigDecimal(0)
        def t_ali_bugoods = new BigDecimal(0)
        def t_ali_buship = new BigDecimal(0)
        
        def h_ali_goods = new BigDecimal(0)
        def h_ali_ship = new BigDecimal(0)
        def h_ali_service = new BigDecimal(0)
        def h_ali_bugoods = new BigDecimal(0)
        def h_ali_buship = new BigDecimal(0)
        
        def t_yu_goods = new BigDecimal(0)
        
        def t_yu_ship = new BigDecimal(0)
        def t_yu_service = new BigDecimal(0)
        def t_yu_bugoods = new BigDecimal(0)
        def t_yu_buship = new BigDecimal(0)
        
        def h_yu_goods = new BigDecimal(0)
        def h_yu_ship = new BigDecimal(0)
        def h_yu_service = new BigDecimal(0)
        def h_yu_bugoods = new BigDecimal(0)
        def h_yu_buship = new BigDecimal(0)
        
        def t_yan_goods = new BigDecimal(0)
        def t_yan_goods_count = new BigDecimal(0)
        def t_fa_ship = new BigDecimal(0)  
        def t_cancle_goods = new BigDecimal(0)   
        def t_cancle_goods_count = new BigDecimal(0)       
        def t_cancle_allgoods  = new BigDecimal(0)
        def t_tui  = new BigDecimal(0)
        def t_kong = new BigDecimal(0)
        
        def h_yan_goods = new BigDecimal(0)
        def h_yan_goods_count = new BigDecimal(0)
        def h_fa_ship = new BigDecimal(0)  
        def h_cancle_goods = new BigDecimal(0)   
        def h_cancle_goods_count = new BigDecimal(0)       
        def h_cancle_allgoods  = new BigDecimal(0)
        def h_tui  = new BigDecimal(0)
        def h_kong = new BigDecimal(0)
        
        
        def tui_ali_goods = new BigDecimal(0) //退换货ali支付换货差价
        def tui_yu_goods = new BigDecimal(0) //退换货余额支付换货差价
        
        def tui_ali_ship = new BigDecimal(0) //退换货ali支付换货运费
        def tui_yu_ship = new BigDecimal(0) //退换货余额支付换货运费
        
        def tui_ali_service = new BigDecimal(0)  //阿里支付退换货服务费
        def tui_yu_service = new BigDecimal(0) //余额支付退换货服务费
        def tui_huikuan = new BigDecimal(0) //档口回款
        
        def newCount = new BigDecimal(0)
        def oldCount = new BigDecimal(0)


        def tuiyunfeifromadmin = new BigDecimal(0)
        def tuigoodsfeefromadmin = new BigDecimal(0)
        
        def tranList = TranLog.findAllByDateCreatedBetween(startDate,endDate)


        //查询今日订单
        def searchGoods =  {

            ge('dateCreated',startDate)
            le('dateCreated',endDate)
            ne("status","delete")
            ne("status","waitpay")


        }
        def todayOrder = DaiFaOrder.createCriteria().list(searchGoods).orderSN
        
        tranList.each{




            //商品收入
            if(it.type == '7'){
               
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        t_yu_goods = t_yu_goods + it.amount
                    }else if(it.shouru_type == '1'){
                        t_ali_goods = t_ali_goods + it.amount
                    }
                    
                    newCount++
                }else{
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        h_yu_goods = h_yu_goods + it.amount
                    }else if(it.shouru_type == '1'){
                        h_ali_goods = h_ali_goods + it.amount
                    }
                    
                    oldCount++
                }
                
                
             
            }else if(it.type == '26'){
                tuiyunfeifromadmin = tuiyunfeifromadmin + it.amount
            }else if(it.type == '27'){
                tuigoodsfeefromadmin = tuigoodsfeefromadmin + it.amount
            }else if(it.type == '13'){//支付换货差价
                if(it.shouru_type == '0'){
                    tui_yu_goods = tui_yu_goods + it.amount
                }else if(it.shouru_type == '1'){
                    tui_ali_goods = tui_ali_goods + it.amount
                }
            }else if(it.type == '14'){//支付换货运费
                if(it.shouru_type == '0'){
                    tui_yu_ship = tui_yu_ship + it.amount
                }else if(it.shouru_type == '1'){
                    tui_ali_ship = tui_ali_ship + it.amount
                }
            }
            
            else if(it.type == '12'){//支付退换货服务费
                if(it.shouru_type == '0'){
                    tui_yu_service = tui_yu_service + it.amount
                }else if(it.shouru_type == '1'){
                    tui_ali_service = tui_ali_service + it.amount
                }
            }
            else if(it.type == '16'){//退货完成档口回款
                tui_huikuan = tui_huikuan + it.amount
            }
            
            
            
            //支付运费   
            else if(it.type == '8'){
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        t_yu_ship = t_yu_ship + it.amount
                    }else if(it.shouru_type == '1'){
                        t_ali_ship = t_ali_ship + it.amount
                    }
                }else{
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        h_yu_ship = h_yu_ship + it.amount
                    }else if(it.shouru_type == '1'){
                        h_ali_ship = h_ali_ship + it.amount
                    }
                }
                //服务费   
            }else if(it.type == '9'){
               
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        t_yu_service = t_yu_service + it.amount
                    }else if(it.shouru_type == '1'){
                        t_ali_service = t_ali_service + it.amount
                    }
                }else{
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        h_yu_service = h_yu_service + it.amount
                    }else if(it.shouru_type == '1'){
                        h_ali_service = h_ali_service + it.amount
                    }
                }
             
                //验收商品（支出）
            }else if(it.type == '1'){
                
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    t_yan_goods = t_yan_goods + it.amount
                   
                    t_yan_goods_count++ 
                }else{
                    h_yan_goods = h_yan_goods + it.amount
                   
                    h_yan_goods_count++ 
                }
                
                //发货运费（支出）
            }else if(it.type == '2'){
                
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    t_fa_ship = t_fa_ship + it.amount
                }else{
                    h_fa_ship = h_fa_ship + it.amount
                }
                
                //商品补款（收入）
            }else if(it.type == '3'){
              
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        t_yu_bugoods = t_yu_bugoods + it.amount
                    }else if(it.shouru_type == '1'){
                        t_ali_bugoods = t_ali_bugoods + it.amount
                    }
                }else{
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        h_yu_bugoods = h_yu_bugoods + it.amount
                    }else if(it.shouru_type == '1'){
                        h_ali_bugoods = h_ali_bugoods + it.amount
                    }
                }
               
                //运费补款（收入）
            }else if(it.type == '4'){
                
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        t_yu_buship = t_yu_buship + it.amount
                    }else if(it.shouru_type == '1'){
                        t_ali_buship = t_ali_buship + it.amount
                    }
                }else{
                    //如果是余额支付
                    if(it.shouru_type == '0'){
                        h_yu_buship = h_yu_buship + it.amount
                    }else if(it.shouru_type == '1'){
                        h_ali_buship = h_ali_buship + it.amount
                    }
                }
                //取消商品退款
            }else if(it.type == '5'){
               
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    t_cancle_goods = t_cancle_goods + it.amount
                   
                    t_cancle_goods_count++
                }else{
                    h_cancle_goods = h_cancle_goods + it.amount
                   
                    h_cancle_goods_count++
                }
                //运费退款 
            }else if(it.type == '6'){
                
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    t_cancle_allgoods = t_cancle_allgoods + it.amount
                }else{
                    h_cancle_allgoods = h_cancle_allgoods + it.amount
                }
                
                //退货退款（另算）
            }
            //            else if(it.type == '10'){
            //                
            //                 //如果是今天的订单
            //               if(todayOrder.contains(it.orderSN)){
            //                   t_tui = t_tui + it.amount
            //               }else{
            //                   h_tui = h_tui + it.amount
            //               }
            //               
            //                //空包退还代发费用
            //            }
            else if(it.type == '11'){
                
                //如果是今天的订单
                if(todayOrder.contains(it.orderSN)){
                    t_kong = t_kong + it.amount
                }else{
                    h_kong = h_kong + it.amount
                }
                
            }
            
        }
        
        def zhichu_tranList = TranLog.findAllByDateCreatedBetweenAndTypeInList(startDate,endDate,['1','2','5','6','11'])//退换货另算
        
        //已发货的订单
        def ship_zhichu_tranList = TranLog.findAllByDateCreatedBetweenAndType(startDate,endDate,"2").orderSN
        
        def orderShipMap = new HashMap()
        def orderNoShipMap = new HashMap()
        def orderAmountMap = new HashMap()
        
        
        zhichu_tranList.each{
            
            def tem_map
            //如果是未发货订单
            if(!ship_zhichu_tranList.contains(it.orderSN)){
                tem_map = orderNoShipMap
            }else{
                tem_map = orderShipMap
            }
            if(tem_map.containsKey(it.orderSN)){
                def orderList = tem_map.get(it.orderSN)
                orderList.add(it)
                
                def order_amount = orderAmountMap.get(it.orderSN)
                order_amount = order_amount + it.amount
                orderAmountMap.put(it.orderSN,order_amount)
            }else{
                def orderList = new ArrayList()
                orderList.add(it)
                tem_map.put(it.orderSN,orderList)
                
                def order_amount = it.amount
                orderAmountMap.put(it.orderSN,order_amount)
            }
            
            
        }
        
        def mapParam = [:]
          
        mapParam.t_ali_goods = t_ali_goods
        mapParam.t_ali_ship = t_ali_ship
        mapParam.t_ali_service = t_ali_service
        mapParam.t_ali_bugoods = t_ali_bugoods
        mapParam.t_ali_buship = t_ali_buship
        
        mapParam.h_ali_goods = h_ali_goods
        mapParam.h_ali_ship = h_ali_ship
        mapParam.h_ali_service = h_ali_service
        mapParam.h_ali_bugoods = h_ali_bugoods
        mapParam.h_ali_buship = h_ali_buship
        
        mapParam.t_yu_goods = t_yu_goods
        mapParam.t_yu_ship = t_yu_ship
        mapParam.t_yu_service = t_yu_service
        mapParam.t_yu_bugoods = t_yu_bugoods
        mapParam.t_yu_buship = t_yu_buship
        
        mapParam.h_yu_goods = h_yu_goods
        mapParam.h_yu_ship = h_yu_ship
        mapParam.h_yu_service = h_yu_service
        mapParam.h_yu_bugoods = h_yu_bugoods
        mapParam.h_yu_buship = h_yu_buship
        
        
        
        mapParam.t_yan_goods = t_yan_goods
        mapParam.t_yan_goods_count = t_yan_goods_count
        mapParam.t_fa_ship = t_fa_ship 
        mapParam.t_cancle_goods = t_cancle_goods   
        mapParam.t_cancle_goods_count = t_cancle_goods_count       
        mapParam.t_cancle_allgoods  = t_cancle_allgoods
        mapParam.t_tui  = t_tui
        mapParam.t_kong = t_kong
        
        mapParam.h_yan_goods = h_yan_goods
        mapParam.h_yan_goods_count = h_yan_goods_count
        mapParam.h_fa_ship = h_fa_ship 
        mapParam.h_cancle_goods = h_cancle_goods 
        mapParam.h_cancle_goods_count = h_cancle_goods_count       
        mapParam.h_cancle_allgoods  = h_cancle_allgoods
        mapParam.h_tui  = h_tui
        mapParam.h_kong = h_kong
        
        mapParam.tui_ali_goods = tui_ali_goods
        mapParam.tui_yu_goods = tui_yu_goods
        mapParam.tui_ali_ship = tui_ali_ship
        mapParam.tui_yu_ship = tui_yu_ship
        
        mapParam.tui_ali_service = tui_ali_service
        mapParam.tui_yu_service = tui_yu_service
        mapParam.tui_huikuan = tui_huikuan
        
        
        
        
        mapParam.newCount = newCount
        mapParam.oldCount = oldCount
        
      
        def t_shipped_actual_price = new BigDecimal(0) //实际拿货价格
        def t_shipped_actual_shipfee = new BigDecimal(0) //实际发货运费
        def t_shipped_actualhuanhuo_shipfee = new BigDecimal(0) //实际换货发货运费
        def t_return_goodsFee = new BigDecimal(0) //档口退回款



        
        
        def shipOrder = DaiFaOrder.findAllByShip_timeBetween(startDate,endDate)
        def shipGoodsMap = [:];
        def returnGoodsMap = [:]
        shipOrder.each{
            def shipGoods = it.daiFaGoods
            def shipGoodsAmount = 0
            def returnGoodsAmount = 0
            shipGoods.each{ i ->
                if(i.actual_price){
                    shipGoodsAmount = shipGoodsAmount + i.actual_price*i.num
                    
                    t_shipped_actual_price = t_shipped_actual_price + i.actual_price*i.num 
                    
                }else{
                    // shipGoodsAmount = shipGoodsAmount + i.price*i.num
                    returnGoodsAmount = returnGoodsAmount + i.price*i.num
                }
                
            }
            shipGoodsMap.put(it.orderSN,shipGoodsAmount)
            returnGoodsMap.put(it.orderSN,returnGoodsAmount)
            
            t_shipped_actual_shipfee = t_shipped_actual_shipfee + it.actual_shipfee
        }
        
        
        def returnOrder = ReturnOrder.findAllByLastUpdatedBetweenAndStatusInList(startDate,endDate,['1','2','4','5'])
        
        returnOrder.each{
            if(it.status == '2' || it.status == '4'){
                t_return_goodsFee = t_return_goodsFee + it.goodsFee
            }
            if(it.status == '5'){
                t_shipped_actualhuanhuo_shipfee = t_shipped_actualhuanhuo_shipfee + it.actual_shipFee
            }
        }
        mapParam.t_shipped_actual_price = t_shipped_actual_price
        mapParam.t_shipped_actual_shipfee = t_shipped_actual_shipfee
        mapParam.t_shipped_actualhuanhuo_shipfee = t_shipped_actualhuanhuo_shipfee//当天换货发货运费金额
        mapParam.t_return_goodsFee = t_return_goodsFee//当天退货退款金额


        mapParam.tuiyunfeifromadmin  =  tuiyunfeifromadmin
        mapParam.tuigoodsfeefromadmin = tuigoodsfeefromadmin



        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String f_Date = df1.format(new Date())
        mapParam.startDate = df1.format(startDate)
        mapParam.endDate = df1.format(endDate)
        
        
        //会员余额
        def searchClosure =  {
            account{
                gt("amount",new BigDecimal(0))
            }
        }

        def o = User.createCriteria();
        def user = o.list(searchClosure)
        def all_amount = new BigDecimal(0)
            
        def str = "<br/><br/><br/>（"+f_Date+"）会员余额情况"
        
        user.each{
            str = str + "<br/>"+it.email+"："+it.account.amount
            all_amount = all_amount+it.account.amount
        }
        
        str = str + "<br/>小计："+all_amount
        mapParam.yue_str = str
        
        
      
        print "===============开始发邮件了============================"
        
        
        
        
//        mailService.sendMail {
//            async true 
//            from "service@findyi.com"
//            to "wufei1310@126.com"
////            to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com","425501056@qq.com"
//            subject f_Date+"金士代发财务收入报表"     
//            // body new File("src/groovy/report.html").getText()
//            html groovyPageRenderer.render(template:"/mailTemplate/dayWaterTemplate",model:mapParam)
//        }
//                
//        mailService.sendMail {
//            async true 
//            from "service@findyi.com"
//            to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com","425501056@qq.com"
//            subject f_Date+"金士代发财务支出统计报表"     
//            // body new File("src/groovy/report.html").getText()
//            html groovyPageRenderer.render(template:"/mailTemplate/dayOutWaterTemplate",model:mapParam)
//        }
//                
//        mailService.sendMail {
//            async true 
//            from "service@findyi.com"
//            to "wufei1310@126.com"
////            to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com","425501056@qq.com"
//            subject f_Date+"金士代发财务支出明细报表"     
//            // body new File("src/groovy/report.html").getText()
//            html groovyPageRenderer.render(template:"/mailTemplate/dayOutWaterDetailTemplate",model:[orderShipMap:orderShipMap,orderNoShipMap:orderNoShipMap,orderAmountMap:orderAmountMap,mapParam:mapParam])
//        }
//                
        mailService.sendMail {
            async true 
            from "service@findyi.com"
            to "wufei1310@126.com"
           // to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com","425501056@qq.com"
            subject "已发货订单收入支出对照明细报表"+mapParam.startDate+"~"+mapParam.endDate     
            // body new File("src/groovy/report.html").getText()
            html groovyPageRenderer.render(template:"/mailTemplate/dayInOutWaterTemplate",model:[shipOrder:shipOrder,shipGoodsMap:shipGoodsMap,returnGoodsMap:returnGoodsMap,mapParam:mapParam])
        }
//                
//        mailService.sendMail {
//            async true 
//            from "service@findyi.com"
//            to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com","425501056@qq.com"
//            subject f_Date+"退换货申请收入支出对照明细报表"     
//            // body new File("src/groovy/report.html").getText()
//            html groovyPageRenderer.render(template:"/mailTemplate/dayReturnOrderInOutWaterTemplate",model:[returnOrderList:returnOrder,mapParam:mapParam])
//        }
                
        

        print "===================邮件发完啦========================"
    }
}

