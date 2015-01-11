package member

import com.alipay.config.*
import com.alipay.util.*
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import java.text.DecimalFormat
import java.util.HashMap
import java.util.Map
import ship.DaiFaOrder
import ship.DiffOrder
import ship.DaiFaGoods
import ship.DiffGoods
import grails.converters.JSON
import util.Push
import ship.PushPOJO
import ship.PushMsg
import util.StringUtil
import ship.TranLog
import ship.ReturnOrder
import ship.ReturnGoods
import ship.AlipayRemit
import ship.User

class MemberAlipayController {

    def memberAlipayService

    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    def shouxufee = properties.getProperty("AlipayConfig.shouxufee")
    
    def index() { }
    
    def alinotify(){
        println "1================"
        println params
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
            : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
		
		
        println "2================"
		
        if(!AlipayNotify.verify(params)){//验证不通过,后面的业务将都不执行
            render "fail"
            return;
        }
		
        
        
        
        def trade_status = params.trade_status;
		
        if(trade_status=="TRADE_SUCCESS"){
            
            println  params.out_trade_no
            
            def out_trade_no = params.out_trade_no.split("#")
            def total_fee = params.total_fee;
            def orderSN = out_trade_no[0];
            def payType = out_trade_no[1];
            
            //如果是正常支付订单
            if(payType == '0'){
                def daiFaOrder = DaiFaOrder.findByOrderSN(orderSN)
                daiFaOrder.payTime = new Date()
                daiFaOrder.status = "waitaccept"
                daiFaOrder.save();
                println "订单支付成功，回调执行修改订单状态："+ (daiFaOrder as JSON)
                
                
                def shichang = ""
                def goodsNum = 0;
                //商品
                def goodsList = daiFaOrder.daiFaGoods
                goodsList.eachWithIndex{it, i -> 
                    if(shichang.indexOf(it.market)<0){
                        shichang = shichang + it.market+","
                    }
                    goodsNum = goodsNum + it.num
                }
                
                //推送
//                def searchClosure =  {
//                    eq("user_type", "admin")
//                }
//
//                def o = PushMsg.createCriteria();
//                def results = o.list(searchClosure)
//
//
//
//                PushPOJO pushPOJO = new PushPOJO();
//                pushPOJO.title = shichang+"有人下单啦，快去代发挣钱呀";
//                pushPOJO.content = "订单号："+daiFaOrder.orderSN+"#拿货数量："+goodsNum+
//                                        "#拿货市场："+shichang+"#订单状态："+StringUtil.getOrderStatus(daiFaOrder.status)+"#支付时间："+new Date().format('yyyy-MM-dd HH:mm:ss')
//
//                println pushPOJO.content + "================================"
//                pushPOJO.pushMsgList = results
//                new Push().pushByStore(pushPOJO)


                def shouxu ;
                if(params.extra_common_param){
                    shouxu = new BigDecimal(params.extra_common_param)
                }else{
                    shouxu = 0;
                }

                //插入资金流水表（商品）
                def tranLog = new TranLog();
                tranLog.shouru_type = "1"
                tranLog.amount = daiFaOrder.goodsFee
                tranLog.direction = "0"
                tranLog.type = "7"
                tranLog.num = goodsNum
                tranLog.orderSN = daiFaOrder.orderSN
                tranLog.order_user = daiFaOrder.add_user
                tranLog.save()


                def shouxuLog = new TranLog();
                shouxuLog.shouru_type = "1"
                shouxuLog.amount =  shouxu
                shouxuLog.direction = "0"
                shouxuLog.type = "29"
                shouxuLog.orderSN = daiFaOrder.orderSN
                shouxuLog.order_user = daiFaOrder.add_user
                shouxuLog.flat = '2'
                shouxuLog.save()

                //插入资金流水表（运费）
                def tranLogShip = new TranLog();
                tranLogShip.shouru_type = "1"
                tranLogShip.amount = daiFaOrder.shipFee
                tranLogShip.direction = "0"
                tranLogShip.type = "8"
                tranLog.num = goodsNum
                tranLogShip.orderSN = daiFaOrder.orderSN
                tranLogShip.order_user = daiFaOrder.add_user
                tranLogShip.save()
                
                 //插入资金流水表（代发费）
                def tranLogDaiFa = new TranLog();
                tranLogDaiFa.shouru_type = "1"
                tranLogDaiFa.amount = daiFaOrder.serviceFee
                tranLogDaiFa.direction = "0"
                tranLogDaiFa.type = "9"
                tranLog.num = goodsNum
                tranLogDaiFa.orderSN = daiFaOrder.orderSN
                tranLogDaiFa.order_user = daiFaOrder.add_user
                tranLogDaiFa.save()
                
                if(daiFaOrder.regardsFee>0){
                    //插入资金流水表（礼品费）
                    def tranLogRegards = new TranLog();
                    tranLogRegards.shouru_type = "1"
                    tranLogRegards.amount = daiFaOrder.regardsFee
                    tranLogRegards.direction = "0"
                    tranLogRegards.type = "20"
                    tranLogRegards.orderSN = daiFaOrder.orderSN
                    tranLogRegards.order_user = daiFaOrder.add_user
                    tranLogRegards.num = goodsNum
                    tranLogRegards.save()
                 }
                
                
            }else if(payType == '1'){//支付差额
                orderSN = orderSN.substring(1)
                def diffGoods = DiffGoods.findAllByOrderSNAndStatus(orderSN, '0')
                def goodsNum = 0 ;//商品数量
                diffGoods.each{
                    it.status = '1'
                    it.payTime = new Date()
                    it.daiFaGoods.status = '1' //已受理
                    it.daiFaGoods.price = it.daiFaGoods.price + it.daiFaGoods.diffFee;
                    goodsNum = goodsNum + it.daiFaGoods.num
                }
                
                def daiFaOrder = DaiFaOrder.findByOrderSN(orderSN)
                
                def order_status = StringUtil.getOrderStatusByGoods(daiFaOrder.daiFaGoods.status);
                daiFaOrder.status = order_status
                daiFaOrder.totalFee = daiFaOrder.totalFee + new BigDecimal(total_fee);
                daiFaOrder.goodsFee = daiFaOrder.goodsFee + new BigDecimal(total_fee);
              
                println "差额支付成功，回调执行修改订单状态："+ (daiFaOrder as JSON)
                
                //支付差额后推送
                //补差额成功后 给推送
//                def searchClosure =  {
//                    eq("id", daiFaOrder.processing_user_id)
//                }
//
//                def o = PushMsg.createCriteria();
//                def results = o.list(searchClosure)
//
//
//
//                def shichang = ""
//                //商品
//                def goodsList = daiFaOrder.daiFaGoods
//                goodsList.eachWithIndex{it, i ->
//                    if(shichang.indexOf(it.market)<0)
//                    shichang = shichang + it.market+","
//                }
//
//
//                PushPOJO pushPOJO = new PushPOJO();
//                pushPOJO.title = shichang+"有订单补齐差额，快去代发挣钱呀";
//                pushPOJO.content = "订单号："+daiFaOrder.orderSN+"差额补款已付款,快去拿货吧";
//                pushPOJO.pushMsgList = results
//                new Push().pushByStore(pushPOJO)
                
                 //插入资金流水表
                def tranLog = new TranLog();
                tranLog.shouru_type = "1"
                tranLog.amount = new BigDecimal(total_fee) 
                tranLog.direction = "0"
                tranLog.type = "3"
                tranLog.orderSN = daiFaOrder.orderSN
                tranLog.order_user = daiFaOrder.add_user
                tranLog.num = goodsNum
                tranLog.save()
                
            }else if(payType == '2'){//支付运费差额
                orderSN = orderSN.substring(1)
                def daiFaOrder = DaiFaOrder.findByOrderSNAndStatus(orderSN,"diffship")
                
                
                def goodsNum = 0
                //商品
                def goodsList = daiFaOrder.daiFaGoods
                goodsList.eachWithIndex{it, i -> 
                    goodsNum = goodsNum + it.num
                }
                
                
                daiFaOrder.status = "allaccept"
                daiFaOrder.totalFee = daiFaOrder.totalFee + daiFaOrder.diffShip
                daiFaOrder.shipFee = daiFaOrder.shipFee + daiFaOrder.diffShip
                
//                 def poSearchClosure =  {
//                    eq("user_type", "admin")
//                    eq("role", "admin")
//                }
//                def po = PushMsg.createCriteria();
//                def results = po.list(poSearchClosure)
//
//
//                PushPOJO pushPOJO = new PushPOJO();
//                pushPOJO.title = "订单号："+daiFaOrder.orderSN+"运费差额已付款";
//                pushPOJO.content = "订单号："+daiFaOrder.orderSN+"运费差额补款已付款,快去发货吧";
//                println "=================="
//                println pushPOJO.content
//                pushPOJO.pushMsgList = results
//                new Push().pushByStore(pushPOJO)
                
                //插入资金流水表
                def tranLog = new TranLog();
                tranLog.shouru_type = "1"
                tranLog.amount = daiFaOrder.diffShip
                tranLog.direction = "0"
                tranLog.type = "4"
                tranLog.orderSN = daiFaOrder.orderSN
                tranLog.order_user = daiFaOrder.add_user
                tranLog.num = goodsNum
                tranLog.save()
            }else if(payType == '3'){//支付退货费用
                
                
                
                
                
                def returnOrder = ReturnOrder.findByIdAndStatus(params.body,"0")
                
                
                
                
                def goodsNum = 0;
                //商品
                def goodsList = returnOrder.returnGoods
                goodsList.eachWithIndex{it, i -> 
                    goodsNum = goodsNum + it.return_num
                }
                
                
                returnOrder.status = '1'
                returnOrder.payTime =  new Date()
               // returnOrder.daiFaOrder.type = '1'
                //插入资金流水表（退货服务费）
                def tranLog = new TranLog();
                tranLog.shouru_type = "1"
                tranLog.amount = returnOrder.serviceFee
                tranLog.direction = "0"
                tranLog.type = "12"
                tranLog.num = goodsNum
                tranLog.orderSN = returnOrder.orderSN
                tranLog.order_user = returnOrder.add_user
                
                 if(returnOrder.flat == '1'){ //如果是非平台订单则记录一下
                    tranLog.flat = '1'
                }
                
                tranLog.save()
                
                //如果是换货 记录运费和差价
                if(returnOrder.type == '1'){
                    def tranLog1 = new TranLog();
                    tranLog1.shouru_type = "1"
                    tranLog1.amount = returnOrder.shipFee
                    tranLog1.direction = "0"
                    tranLog1.type = "14"
                    tranLog1.num = goodsNum
                    tranLog1.orderSN = returnOrder.orderSN
                    tranLog1.order_user = returnOrder.add_user
                    tranLog1.save()
                    
                    if(returnOrder.chajia){
                        def tranLog2 = new TranLog();
                        tranLog2.shouru_type = "1"
                        tranLog2.amount = returnOrder.chajia
                        tranLog2.direction = "0"
                        tranLog2.type = "13"
                        tranLog2.num = goodsNum
                        tranLog2.orderSN = returnOrder.orderSN
                        tranLog2.order_user = returnOrder.add_user
                        tranLog2.save()
                    }
            }
                
            }else if(payType == '4'){
                print '===============================================================================充值成功：'+orderSN
                memberAlipayService.chongzhi(params,orderSN)
            }
            
            

			
            render "success"
            return;
        }
		
		
		
		
    }
	
    
    def alireturn(){
        
        def out_trade_no = params.out_trade_no.split("#")
        def orderSN = out_trade_no[0];
        def payType = out_trade_no[1];
        
        String totalFee = params.total_fee
        
            
            
        //        def daiFaOrder = DaiFaOrder.findByOrderSN(orderSN)
        //        totalFee = daiFaOrder.totalFee;
        def map = [totalFee:totalFee,orderSN:orderSN]
        if(payType=='4'){
            render(view: "/member/alipayRemit/paySuccess", model:map)
        }else{
            render(view: "/member/daiFaOrder/paySuccess", model:map)
        }
        
    }
        
    
    
    
    
    

    def reqPay(){
        String baseUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
        String total_fee = ""
        def orderSN = params.orderSN;
        def payType = params.payType;

        def extra_common_param = params.shouxu_fee
        
        //订单描述

        String body = params.body  //存储唯一标示 一般是一张表的id ╮(╯▽╰)╭暂时这样吧
        
        //正常支付订单   
        if(payType == '0'){
            def daiFaOrder = DaiFaOrder.findByOrderSN(orderSN)
            //商户订单号
            //商户网站订单系统中唯一订单号，必填
            //订单名称
            //付款金额
            total_fee = daiFaOrder.totalFee;
            def shouxu = util.DecimalUtil.mul(new Double(total_fee), new Double(shouxufee))
            if(shouxu<0.5)shouxu=0.5

            extra_common_param = shouxu as String

            total_fee = util.DecimalUtil.add(new Double(total_fee), new Double(shouxu))
            DecimalFormat df = new DecimalFormat("#.00");
            total_fee = df.format(total_fee as double)
        }else if(payType == '1'){//支付差额订单
            def daiFaOrder = DaiFaOrder.findByOrderSN(orderSN)
            def diffGoodsList = DaiFaGoods.findAllByDaiFaOrderAndStatus(daiFaOrder, '4')
            
            BigDecimal decimal = new BigDecimal(0);
            
            diffGoodsList.each{
                 
                
                decimal = decimal + it.diffFee * it.num
            }
             
            total_fee = decimal.toString()
            orderSN = "B"+orderSN
        }else if(payType == '2'){//支付运费差额
            def daiFaOrder = DaiFaOrder.findByOrderSN(orderSN)
            total_fee = daiFaOrder.diffShip;
            orderSN = "S"+orderSN
        }else if(payType == '3'){//支付退货费用
            def returnOrder = ReturnOrder.findByIdAndStatus(params.body,"0")
            total_fee = returnOrder.totalFee
            orderSN = orderSN + "T"+body
        }else if(payType == '4'){ //会员支付宝充值
            total_fee = params.total_fee
        }
        
        
        
        String out_trade_no = orderSN + "#" +payType
        
        String subject = orderSN;
        
		
		
        //支付类型
        String payment_type = "1";
        //必填，不能修改
        //服务器异步通知页面路径
        String notify_url = baseUrl+"${createLink(action: 'alinotify')}";
        //需http://格式的完整路径，不能加?id=123这类自定义参数
        
        println notify_url
        //页面跳转同步通知页面路径
        String return_url = baseUrl+"${createLink(action: 'alireturn')}"
        println return_url

        //需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

        //卖家支付宝帐户
        String seller_email = grailsApplication.getConfig().AlipayConfig.seller_email;
        //必填

		
        //必填

		
        //必填

        
        //商品展示地址
        String show_url = "";
        //需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html

        //防钓鱼时间戳
        String anti_phishing_key = "";
        //若要使用请调用类文件submit中的query_timestamp函数

        //客户端的IP地址
        String exter_invoke_ip = "";
        //非局域网的外网IP地址，如：221.0.0.1
		
		
        //////////////////////////////////////////////////////////////////////////////////
		
        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", payment_type);
        sParaTemp.put("notify_url", notify_url);
        sParaTemp.put("return_url", return_url);
        sParaTemp.put("seller_email", seller_email);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("body", body);
        sParaTemp.put("show_url", show_url);
        sParaTemp.put("anti_phishing_key", anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
        sParaTemp.put("extra_common_param", extra_common_param);

		
        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
        //		out.println(sHtmlText);
		
        render sHtmlText
    }
}
