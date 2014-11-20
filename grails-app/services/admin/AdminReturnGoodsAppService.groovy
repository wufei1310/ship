package admin
import ship.DiffGoods
import ship.DaiFaGoods
import util.StringUtil
import ship.GoodsLog
import ship.User
import ship.TranLog
import ship.ReturnGoodsApp
class AdminReturnGoodsAppService {
    def mailService

    def doCheckReturnGoods(params,checkUser) {
        def returnGoodsApp =  ReturnGoodsApp.findByIdAndStatus(params.id,"0")
        if(!returnGoodsApp || checkUser.email != 'superquan'){
             throw new RuntimeException("操作出错")
        }
        returnGoodsApp.status = params.status
        returnGoodsApp.checkUser = checkUser
            if(params.status == '1'){ //审核通过
                def  goods = returnGoodsApp.daiFaGoods
                //修改商品状态
                goods.status = '10'
                goods.tuihuo_price = returnGoodsApp.tuihuo_price
                goods.tuihuo_num = returnGoodsApp.tuihuo_num
                //退钱
                 def  addUser = User.get(goods.add_user)
                 def fee = goods.tuihuo_num*goods.tuihuo_price
                 def account = addUser.account
                 account.lock()
                BigDecimal memberamount = account.amount;
                 account.amount = fee +account.amount
                 
                  //插入资金流水表（商品）
                    def tranLog = new TranLog();
                    tranLog.amount = fee
                memberamount = memberamount + fee;
                tranLog.memberamount = memberamount
                    tranLog.direction = "1"
                    tranLog.type = "10"
                    tranLog.orderSN = returnGoodsApp.orderSN
                    tranLog.goods_id = goods.id
                    tranLog.order_user = goods.daiFaOrder.add_user
                    tranLog.num = goods.num;
                    tranLog.save()
                    
                    //发邮件
                    Thread.start {
                        mailService.sendMail {
                            async true 
                            from "service@findyi.com"
                            to addUser.email     
                            subject "金士代发退货退款通知"     
                            body " 您订单号"+returnGoodsApp.orderSN+" 货号"+goods.goods_sn+"的商品已被退回\n 退货件数："+returnGoodsApp.tuihuo_num+"   单件退款金额："+returnGoodsApp.tuihuo_price
                         }
                         print "发送邮件成功========================="
                    }
            }
    }
}
