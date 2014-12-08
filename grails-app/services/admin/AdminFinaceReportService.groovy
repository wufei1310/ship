package admin

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import view.Qianshouview

import java.text.SimpleDateFormat
import ship.TranLog
import ship.DaiFaOrder
import ship.DaiFaGoods
import ship.ReturnOrder
import ship.User
import grails.converters.JSON
class AdminFinaceReportService {
    
    def mailService
    def groovyPageRenderer
    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    def isPro = properties.getProperty("isPro");
    def sendEmail(params){
        
        
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    String nowDate = df.format(new Date()-1)

        def start_time = nowDate + " 00:00:00"
        def end_time = nowDate + " 23:59:59"

        def startDate = Date.parse("yyyy-MM-dd HH:mm:ss",nowDate+" 00:00:00")
        def endDate = Date.parse("yyyy-MM-dd HH:mm:ss",nowDate+" 23:59:59")
        
        if(params){
            if(params.start_date){
                startDate = Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 00:00:00")
                endDate = Date.parse("yyyy-MM-dd HH:mm:ss",params.start_date+" 23:59:59")

                 start_time = params.start_date + " 00:00:00"
                 end_time = params.start_date + " 23:59:59"
            }
            if(params.end_date){
                endDate = Date.parse("yyyy-MM-dd HH:mm:ss",params.end_date+" 23:59:59")
                end_time = params.end_date + " 23:59:59"
            }
        }
        
        /**
         *用于统计每日 凡衣需向金士代发拔款多少
         *数据将从下面统计出的map中提取
         */
        def t_shipped_actual_price = new BigDecimal(0)//已发货订单的实际拿货款
        def t_shipped_actual_shipfee = new BigDecimal(0)//已发货订单的实际发货运费 
        def t_shipped_actualhuanhuo_shipfee = new BigDecimal(0)//已发货的换货订单的发货运费
        def t_return_goodsFee = new BigDecimal(0)//已退货完成的档口退货回款
        /**
         *用于统计每日 凡衣需向金士代发拔款多少end
         */
        
        //====================================
        //完整统计所有收支流水记录
        def tranList = TranLog.findAllByDateCreatedBetween(startDate,endDate)
        def shouruzhichutongjireportData = renderReportData(tranList)



        shouruzhichutongjireport(shouruzhichutongjireportData,startDate,endDate)
        
        
        
        
        
        
        //===============================
        //退货报表统计
        def tuiOrderList = ReturnOrder.findAllByStatusAndCheck_timeBetween('2',startDate,endDate)


        def tuiTranList = TranLog.findAllByTypeInListAndOrderSNInList(['10','12','16'],tuiOrderList.orderSN)
        def tuitongjireportData = renderReportData(tuiTranList)//


        t_return_goodsFee = tuitongjireportData.tui_huikuan//已退货完成的档口退货回款

        def qianshousql  =  "select sum(d.num) as num,sum(d.amount) as amount,sum(d.tuiamount) as tuiamount,sum(d.tuinum) as tuinum,d.checktime,d.is_qianshou,d.daifa_user,d.qianshou_user,d.qianshou_user_id,d.qianshoutime from Qianshouview d " +
                " where  d.checktime between '" + start_time + "' and '" + end_time + "'"
        def qianshouview = Qianshouview.executeQuery(qianshousql)
        t_return_goodsFee =  new BigDecimal(qianshouview[0][2]!=null?qianshouview[0][2]:0)


        tuitongjireportData.orderCount = tuiOrderList.size();
        // tuitongjireport(tuitongjireportData,startDate,endDate)
        
        
        //===============================
        //换货报表统计
        def huanOrderList = ReturnOrder.findAllByStatusAndShipTimeBetween('5',startDate,endDate)
        def huanTranList = TranLog.findAllByTypeInListAndOrderSNInList(['10','15','17','12','13','14'],huanOrderList.orderSN)
        def huantongjireportData = renderReportData(huanTranList)//


        t_shipped_actualhuanhuo_shipfee = huantongjireportData.huan_ship//已发货的换货订单的发货运费


        huantongjireportData.orderCount = huanOrderList.size();
        //huatongjireport(huantongjireportData,startDate,endDate)
        
        
        //===============================
        //已发货订单报表统计
        def shippedOrderList = DaiFaOrder.findAllByStatusInListAndShip_timeBetween(['shipped','kill'],startDate,endDate)

        def shippedTranList = TranLog.findAllByTypeInListAndOrderSNInList(['1','2','5','6','11','7','8','9','3','4','22','23','24','25','28'],shippedOrderList.orderSN)



        def shippedtongjireportData = renderReportData(shippedTranList)//

        t_shipped_actual_price = shippedtongjireportData.yan_goods//已发货订单的实际拿货款
        t_shipped_actual_shipfee = shippedtongjireportData.fa_ship//已发货订单的实际发货运费 
        
        
        
        shippedtongjireportData.t_shipped_actual_price = t_shipped_actual_price
        shippedtongjireportData.t_shipped_actual_shipfee = t_shipped_actual_shipfee
        shippedtongjireportData.t_shipped_actualhuanhuo_shipfee = t_shipped_actualhuanhuo_shipfee
        shippedtongjireportData.t_return_goodsFee = t_return_goodsFee
        
        shippedtongjireportData.orderCount = shippedOrderList.size();
        shippedtongjireport(shippedtongjireportData,tuitongjireportData,huantongjireportData,startDate,endDate)
    }
    
    
    
    //已发货订单报表统计
    def shippedtongjireport(mapParam,tuitongjireportData,huantongjireportData,startDate,endDate){
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String f_Date = df1.format(new Date())//当前发件时间 
        mapParam.startDate = df1.format(startDate)
        mapParam.endDate = df1.format(endDate)





        Thread.start {
            mailService.sendMail {
                async true 
                from "service@findyi.com"
                if(isPro=="true"){
                    to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com"
                }else{
                    to "wufei1310@126.com"
                }

                subject "已完成订单收入支出统计报表${mapParam.startDate}~${mapParam.endDate}"    
                html groovyPageRenderer.render(template:"/mailTemplate/shippedtongjireportTemplate",model:[shippedmap:mapParam,tuimap:tuitongjireportData,huanmap:huantongjireportData])
        
            }
        }
    }
    
    
    //收入统计支出报表
    def shouruzhichutongjireport(mapParam,startDate,endDate){
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String f_Date = df1.format(new Date())//当前发件时间 
        mapParam.startDate = df1.format(startDate)
        mapParam.endDate = df1.format(endDate)
        
        Thread.start {
            mailService.sendMail {
                async true 
                from "service@findyi.com"
                if(isPro=="true"){
                    to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com"
                }else{
                    to "wufei1310@126.com"
                }
                subject "金士代发财务收入支出统计报表${mapParam.startDate}~${mapParam.endDate}"  
                html groovyPageRenderer.render(template:"/mailTemplate/shouruzhichutongjiTemplate",model:mapParam)
            }
        }
        
    }
    
    
    //换货报表统计
    def huatongjireport(mapParam,startDate,endDate){
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String f_Date = df1.format(new Date())//当前发件时间 
        mapParam.startDate = df1.format(startDate)
        mapParam.endDate = df1.format(endDate)
        
        
        mailService.sendMail {
            async true 
            from "service@findyi.com"
            //          to "wufei1310@126.com"
            //                    to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com","425501056@qq.com"
            subject f_Date+"已完成换货订单收入支出统计报表"    
            html groovyPageRenderer.render(template:"/mailTemplate/huantongjireportTemplate",model:mapParam)
        }
    }
    
    
    //退货报表统计
    def tuitongjireport(mapParam,startDate,endDate){
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String f_Date = df1.format(new Date())//当前发件时间 
        mapParam.startDate = df1.format(startDate)
        mapParam.endDate = df1.format(endDate)
        
        
        mailService.sendMail {
            async true 
            from "service@findyi.com"
            //          to "wufei1310@126.com"
            //                    to "findyi@icloud.com","mail3298@gmail.com","wufei1310@126.com","425501056@qq.com"
            subject f_Date+"已完成退货订单收入支出统计报表"    
            html groovyPageRenderer.render(template:"/mailTemplate/tuitongjireportTemplate",model:mapParam)
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //生成报表数据
    def renderReportData(tranList){
        def mapParam = [:]
        
        
        
        
        /**
         *
         *=========================================
         *收入
         */
        
        def t_ali_goods = new BigDecimal(0)//会员支付商品
        def t_ali_ship = new BigDecimal(0)//会员支付拿货运费
        def t_yu_goods = new BigDecimal(0) //余额支付商品
        def t_yu_ship = new BigDecimal(0)//余额支付拿货运费 
        def t_ali_service = new BigDecimal(0)//会员支付服务费
        def t_yu_service = new BigDecimal(0)//余额支付服务费
        def t_ali_bugoods = new BigDecimal(0)//会员补款
        def t_yu_bugoods = new BigDecimal(0)//会员余额支付补款
        
        def t_ali_buship = new BigDecimal(0)//会员对运费补款
        def t_yu_buship = new BigDecimal(0)//会员余额对运费补款
        
        
        def tui_ali_service = new BigDecimal(0)//会员支付退货服务费
        def tui_yu_service = new BigDecimal(0)//会员余额支付退货服务费
        def tui_ali_goods =  new BigDecimal(0)//会员支付换货差价
        def tui_yu_goods =  new BigDecimal(0)//会员余额支付换货差价
        
        
        def tui_ali_ship = new BigDecimal(0)//会员支付换货运费
        def tui_yu_ship = new BigDecimal(0)//会员余额支付换货运费


        def ali_changestall = new BigDecimal(0)//会员支付换货运费
        def yu_changestall = new BigDecimal(0)//会员余额支付换货运费


        def tui_huikuan = new BigDecimal(0)//

        def t_yu_gift =   new BigDecimal(0)//
        def t_ali_gift =  new BigDecimal(0)//
        
        def aliru = new BigDecimal(0)//会员通过支付宝充值


        def killru = new BigDecimal(0)//会员紧急中止订单，收付服务费
        
        /**
         *
         *=========================================
         *支出
         */
        
        def yan_goods = new BigDecimal(0)//验收商品
        def fa_ship = new BigDecimal(0)//发货运费
        def cancle_goods = new BigDecimal(0)//会员取消商品退款
        def cancle_allgoods = new BigDecimal(0)//会员取消商品退运费
        def cancle_order_giftfee = new BigDecimal(0)//会员取消商品订单退礼品费
        def tui_goods = new BigDecimal(0)//退货退款
        def kong = new BigDecimal(0)//空包退还代发费用
        def huan_ship = new BigDecimal(0)//发货运费(换货)
        def cha_goods = new BigDecimal(0)//换货差价支出
        
        def yanshougoodsnum = 0 ;//验收商品数量 
        def shipgoodsnum = 0;//发货运费商品数量
        def canclegoodsnum = 0;//会员取消商品退款商品数量
        def canclegoodsnumship = 0;//会员取消商品退运费商品数量
        def tuigoodsnum = 0;//会员退货退款至会员账户商品数量
        def huanshipgoodsnum = 0;//发货运费(换货)商品数量
        def chagoodsnum = 0;//换货差价支出商品数量
        def konggoodsnum = 0; //空包
        
        def paygoodsnum = 0 ;//会员支付商品商品数量
        def payshipgoodsnum = 0;//会员支付拿货运费商品数量
        def payservicegoodsnum = 0;//会员支付服务费商品数量
        def paybugoodsnum = 0;//会员对商品补款商品数量
        def paybushipgoodsnum = 0;//会员对运费补款 商品数量
        def payhuangoodsnum = 0;//会员支付换货差价商品数量
        def paytuiservicegoodsnum = 0;//会员支付退货服务费商品数量
        def payhuanshipgoodsnum = 0;//会员支付换货运费商品数量


        def changestallgoodsnum = 0;//会员更换档口超过2次支付费用


        def huikangoodsnum = 0;//退货档口回款商品数量
        
        def alichu = new BigDecimal(0)//会员通过支付宝充值


        def changeStallchu = new BigDecimal(0)//会员更换档口退货款
        
        def killchu = new BigDecimal(0)// 会员紧急中止订单，退回货款
        def killchuship = new BigDecimal(0)// 会员紧急中止订单，退回运费

        tranList.each{
            
            
            //=======================================
            //支出
            
            if(it.type == '1'){//验收商品
                yan_goods = yan_goods + it.amount
                if(it.num){
                    yanshougoodsnum = yanshougoodsnum + it.num
                }
                
            }else if(it.type == '2'){//发货运费
                fa_ship = fa_ship + it.amount
                if(it.num){
                    shipgoodsnum = shipgoodsnum + it.num
                }

            }else if(it.type == '25'){//发货运费
                killchu = killchu + it.amount

            }else if(it.type == '28'){//发货运费
                killchuship = killchuship + it.amount

            }else if(it.type == '5'){//会员取消商品退款
                cancle_goods = cancle_goods + it.amount
                if(it.num){
                    canclegoodsnum = canclegoodsnum + it.num
                }
                
            }else if(it.type == '6'){//会员取消商品退运费
                cancle_allgoods = cancle_allgoods + it.amount
                if(it.num){
                    canclegoodsnumship = canclegoodsnumship + it.num
                }
                
            }else if(it.type == '10'){//退货退款
                tui_goods = tui_goods + it.amount
                if(it.num){
                    tuigoodsnum = tuigoodsnum + it.num
                }
                
            }else if(it.type == '11'){//空包退还代发费用
                kong = kong + it.amount
                if(it.num){
                    konggoodsnum = konggoodsnum + it.num
                }
                
            }else if(it.type == '15'){//发货运费(换货)
                huan_ship = huan_ship + it.amount
                if(it.num){
                    huanshipgoodsnum = huanshipgoodsnum + it.num
                }
                
            }else if(it.type == '17'){//换货差价支出
                cha_goods = cha_goods + it.amount
                if(it.num){
                    chagoodsnum = chagoodsnum + it.num
                }
                
            }else if(it.type == '19'){//会员支付宝充值支出
                alichu = alichu + it.amount
                
            }else if(it.type == '21'){//会员支付宝充值支出
                cancle_order_giftfee = cancle_order_giftfee + it.amount

            } else if(it.type == '22'){//会员更换档口退货款
                changeStallchu = changeStallchu + it.amount
            }
            
            
            
            
            
            
            //=======================================
            //收出
            if(it.type == '7'){
                
                //如果是余额支付
                if(it.shouru_type == '0'){
                    t_yu_goods = t_yu_goods + it.amount
                }else if(it.shouru_type == '1'){
                    t_ali_goods = t_ali_goods + it.amount
                }
                
                if(it.num){
                    paygoodsnum = paygoodsnum + it.num
                }
                
                
            } else if(it.type == '18'){//会员支付宝充值支出
                aliru = aliru + it.amount
                
            }else if(it.type == '24'){//会员紧急中止订单收入服务费
                killru = killru + it.amount

            }else if(it.type == '16'){//退货完成档口回款
                tui_huikuan = tui_huikuan + it.amount
                if(it.num){
                    huikangoodsnum = huikangoodsnum + it.num
                }
                
            }else if(it.type == '14'){//会员支付换货运费
                //如果是余额支付
                if(it.shouru_type == '0'){
                    tui_yu_ship = tui_yu_ship + it.amount
                }else if(it.shouru_type == '1'){
                    tui_ali_ship = tui_ali_ship + it.amount
                }
                if(it.num){
                    payhuanshipgoodsnum = payhuanshipgoodsnum + it.num
                }
                
            }else if(it.type == '23'){//会员支付换货运费
                //如果是余额支付
                if(it.shouru_type == '0'){
                    yu_changestall = yu_changestall + it.amount
                }else if(it.shouru_type == '1'){
                    ali_changestall = ali_changestall + it.amount
                }
                if(it.num){
                    changestallgoodsnum = changestallgoodsnum + it.num
                }

            }


            else if(it.type == '12'){//会员支付退货服务费
                //如果是余额支付
                if(it.shouru_type == '0'){
                    tui_yu_service = tui_yu_service + it.amount
                }else if(it.shouru_type == '1'){
                    tui_ali_service = tui_ali_service + it.amount
                }
                if(it.num){
                    paytuiservicegoodsnum = paytuiservicegoodsnum + it.num
                }
                
            }else if(it.type == '13'){//会员换货支付差价 
                //如果是余额支付
                if(it.shouru_type == '0'){
                    tui_yu_goods = tui_yu_goods + it.amount
                }else if(it.shouru_type == '1'){
                    tui_ali_goods = tui_ali_goods + it.amount
                }
                if(it.num){
                    payhuangoodsnum = payhuangoodsnum + it.num
                }
                
            }else if(it.type == '4'){//支付运费   
                //如果是余额支付
                if(it.shouru_type == '0'){
                    t_yu_buship = t_yu_buship + it.amount
                }else if(it.shouru_type == '1'){
                    t_ali_buship = t_ali_buship + it.amount
                }
                if(it.num){
                    paybushipgoodsnum = paybushipgoodsnum + it.num
                }
                
            }
            else if(it.type == '8'){//支付运费   
                //如果是余额支付
                if(it.shouru_type == '0'){
                    t_yu_ship = t_yu_ship + it.amount
                }else if(it.shouru_type == '1'){
                    t_ali_ship = t_ali_ship + it.amount
                }
                if(it.num){
                    payshipgoodsnum = payshipgoodsnum + it.num
                }
                
            }
            else if(it.type == '9'){//会员支付服务费
                //如果是余额支付
                if(it.shouru_type == '0'){
                    t_yu_service = t_yu_service + it.amount
                }else if(it.shouru_type == '1'){
                    t_ali_service = t_ali_service + it.amount
                }
                if(it.num){
                    payservicegoodsnum = payservicegoodsnum + it.num
                }
                
            }else if(it.type == '3'){//会员补款
                //如果是余额支付
                if(it.shouru_type == '0'){
                    t_yu_bugoods = t_yu_bugoods + it.amount
                }else if(it.shouru_type == '1'){
                    t_ali_bugoods = t_ali_bugoods + it.amount
                }
                if(it.num){
                    paybugoodsnum = paybugoodsnum + it.num
                }
                
            } else if(it.type == '20'){//会员补款
                //如果是余额支付
                if(it.shouru_type == '0'){
                    t_yu_gift = t_yu_gift + it.amount
                }else if(it.shouru_type == '1'){
                    t_ali_gift = t_ali_gift + it.amount
                }


            }
        }





        mapParam.killru = killru
        mapParam.killchu = killchu
        mapParam.killchuship = killchuship
        
        mapParam.t_ali_goods = t_ali_goods
        mapParam.t_ali_ship = t_ali_ship
        
        mapParam.t_yu_goods = t_yu_goods
        mapParam.t_yu_ship = t_yu_ship
        
        mapParam.t_ali_service = t_ali_service
        mapParam.t_yu_service = t_yu_service
        
        mapParam.t_ali_bugoods = t_ali_bugoods
        mapParam.t_yu_bugoods = t_yu_bugoods
        
        mapParam.t_ali_buship = t_ali_buship
        mapParam.t_yu_buship = t_yu_buship

        mapParam.t_yu_gift = t_yu_gift
        mapParam.t_ali_gift = t_ali_gift
        
        
        mapParam.tui_ali_service = tui_ali_service
        mapParam.tui_yu_service = tui_yu_service
        mapParam.tui_ali_goods = tui_ali_goods
        mapParam.tui_yu_goods = tui_yu_goods
        
        mapParam.tui_ali_ship = tui_ali_ship
        mapParam.yu_changestall = yu_changestall


        mapParam.ali_changestall = ali_changestall
        mapParam.tui_yu_ship = tui_yu_ship

        mapParam.tui_huikuan = tui_huikuan
        
        
        mapParam.yan_goods = yan_goods
        mapParam.fa_ship = fa_ship
        mapParam.cancle_goods = cancle_goods
        mapParam.cancle_allgoods = cancle_allgoods
        mapParam.cancle_order_giftfee = cancle_order_giftfee
        
        mapParam.tui_goods = tui_goods
        mapParam.kong = kong
        mapParam.huan_ship = huan_ship
        mapParam.cha_goods = cha_goods
        
        mapParam.yanshougoodsnum = yanshougoodsnum
        mapParam.shipgoodsnum = shipgoodsnum
        mapParam.canclegoodsnum = canclegoodsnum
        mapParam.canclegoodsnumship = canclegoodsnumship

        mapParam.tuigoodsnum = tuigoodsnum
        mapParam.huanshipgoodsnum = huanshipgoodsnum
        mapParam.chagoodsnum = chagoodsnum
        mapParam.konggoodsnum = konggoodsnum
        
        mapParam.aliru = aliru
        mapParam.alichu = alichu

        mapParam.changeStallchu = changeStallchu
        
        
        mapParam.paygoodsnum = paygoodsnum ;//会员支付商品商品数量
        mapParam.payshipgoodsnum = payshipgoodsnum;//会员支付拿货运费商品数量
        mapParam.payservicegoodsnum = payservicegoodsnum;//会员支付服务费商品数量
        mapParam.paybugoodsnum = paybugoodsnum;//会员对商品补款商品数量
        mapParam.paybushipgoodsnum = paybushipgoodsnum;//会员对运费补款 商品数量
        mapParam.payhuangoodsnum = payhuangoodsnum;//会员支付换货差价商品数量
        mapParam.paytuiservicegoodsnum = paytuiservicegoodsnum;//会员支付退货服务费商品数量
        mapParam.payhuanshipgoodsnum = payhuanshipgoodsnum;//会员支付换货运费商品数量
        mapParam.changestallgoodsnum = changestallgoodsnum;//会员支付换货运费商品数量
        mapParam.huikangoodsnum = huikangoodsnum;//退货档口回款商品数量
        
        
        
        
        
        
        //会员余额
        def searchClosure =  {
            account{
                gt("amount",new BigDecimal(0))
            }
        }

        def o = User.createCriteria();
        def user = o.list(searchClosure)
        mapParam.user = user
        
        return mapParam;
        
    }
}
