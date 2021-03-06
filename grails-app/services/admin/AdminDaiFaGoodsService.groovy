package admin

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import ship.DiffGoods
import ship.DaiFaGoods
import ship.User
import util.StringUtil
import ship.GoodsLog
import ship.TranLog

import java.text.SimpleDateFormat

class AdminDaiFaGoodsService {
    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    def isPro = properties.getProperty("isPro");
    def mailService;
    def doBukuan(params,addUser) {
        def o = DaiFaGoods.createCriteria();
        def goods = o.get{eq("status", "3") eq("id", new Long(params.id)) lock true}
        if(!goods ){
           throw new RuntimeException("补款出错") ;
        }else{
            goods.status = "4" //补款
            //goods.diffFee = new BigDecimal(params.diffFee) //补款数据
            
            goods.diffFee = goods.actual_price - goods.price //补差单价
            
            def order = goods.daiFaOrder
            def goodsStatusList = order.daiFaGoods.status
            def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
            if(!o_status)
                throw new RuntimeException("补款出错") ;
            order.status = o_status
            //order.totalFee = order.totalFee + goods.diffFee 
            //添加补款数据
            
            def diffGoods = new DiffGoods();
            diffGoods.diffFee = goods.diffFee
            diffGoods.status = '0'
            diffGoods.addUser = addUser
            diffGoods.addTime = new Date()
            diffGoods.orderSN = order.orderSN
            goods.addToDiffGoods(diffGoods)    
            
            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = addUser
            goodsLog.logdesc = "确认补款，单件差额"+goods.diffFee
            goodsLog.save()



            def orderUser = User.get(order.add_user)
            Thread.start {
                mailService.sendMail {
                    from "service@findyi.com"
                    if(isPro=="true"){
                        to orderUser.email
                    }else{
                        to "wufei1310@126.com"
                    }

                    subject "金士代发补款通知"
                    html "您在金士代发订单(订单号："+ order.orderSN + ")经我们实地档口拿货需要补款，为不影响您的发货，请尽快补款!<a href='http://kingsdf.cn' target='_blank' >去金士代发</a>"

                }
            }
        }
        return goods
    }


    def buhege(params,checkUser){
        def o = DaiFaGoods.createCriteria();
        def goods = o.get{eq("status", "2") eq("id", new Long(params.id))}
        def map = [:]



        if(!goods || (checkUser.role != 'admin' && checkUser.role != 'wuliu')){
            throw new RuntimeException("验收出错") ;
        }else{

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String time = df.format(new Date())

            goods.yanshoudesc = "该商品验收不合格，次日将安排人员重新拿货，验收时间："+  time;
            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = checkUser
            goodsLog.logdesc = "该商品验收不合格，次日将安排人员重新拿货"
            goodsLog.save()

        }

        return goods;
    }
    
    def doYanshou(params,checkUser){
        def o = DaiFaGoods.createCriteria();
        def goods = o.get{eq("status", "2") eq("id", new Long(params.id))}
        def map = [:]
        if(!goods || (checkUser.role != 'admin' && checkUser.role != 'wuliu')){
            throw new RuntimeException("验收出错") ;
        }else{
            goods.status = "7"
            goods.checktime = new Date()
            goods.check_user = checkUser
            
            def order = goods.daiFaOrder
            def goodsStatusList = order.daiFaGoods.status
            def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
            if(!o_status)
                throw new RuntimeException("验收出错") ;
            order.status = o_status
            
            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = checkUser
            goodsLog.logdesc = "已验收"
            goodsLog.save()
           
            //插入资金流水表
            def tranLog = new TranLog();
            tranLog.amount = goods.actual_price*goods.num
            tranLog.direction = "1"
            tranLog.type = "1"
            tranLog.num = goods.num
            tranLog.orderSN = order.orderSN
            tranLog.goods_id = goods.id
            tranLog.order_user = order.add_user
            tranLog.save()
        }
        return goods
    }
    
    def doQuehuo(params,quehuoUser){
        def o = DaiFaGoods.createCriteria();
        def goods = o.get{eq("status", "8") eq("id", new Long(params.id))}




        def map = [:]
        if(!goods ){
            throw new RuntimeException("确认缺货出错") ;
        }else{
            goods.status = "5"
            goods.shuoming = params.shuoming
            //goods.quehuo_user = quehuoUser
           // goods.quehuo_time = new Date();
            
            def order = goods.daiFaOrder
            def goodsStatusList = order.daiFaGoods.status
            def o_status = StringUtil.getOrderStatusByGoods(goodsStatusList)
            if(!o_status)
                throw new RuntimeException("确认缺货出错") ;
            order.status = o_status
           
            def goodsLog = new GoodsLog();
            goodsLog.daiFaGoods = goods
            goodsLog.addUser = quehuoUser
            goodsLog.logdesc = "确认缺货"
            goodsLog.save()


            def orderUser = User.get(order.add_user)
            Thread.start {
                mailService.sendMail {
                    from "service@findyi.com"
                    if(isPro=="true"){
                        to orderUser.email
                    }else{
                        to "wufei1310@126.com"
                    }

                    subject "金士代发缺货通知"
                    html "您在金士代发订单(订单号："+ order.orderSN + ")经我们实地档口拿货后确认缺货，为不影响您的发货，请尽快更换档口或者取消商品!<a href='http://kingsdf.cn' target='_blank' >去金士代发</a>"

                }
            }
        }
        return goods
    }
}
