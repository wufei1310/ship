/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package member

import ship.AlipayRemit
import ship.TranLog
import ship.User
/**
 *
 * @author DELL
 */
class MemberAlipayService {
	def chongzhi(params,orderSN){
               
                //插入资金流水表（会员充值（收入））
                def tranLog = new TranLog();
                tranLog.shouru_type = "1"
                tranLog.amount = new BigDecimal(params.total_fee)
                tranLog.direction = "0"
                tranLog.type = "18"
                tranLog.orderSN = orderSN
                tranLog.order_user = Long.valueOf(params.body)
                tranLog.flat = '2'
                tranLog.save()
                
                //插入资金流水表（会员充值（支出））
                def tranLog1 = new TranLog();
                tranLog1.amount = new BigDecimal(params.total_fee)
                tranLog1.direction = "1"
                tranLog1.type = "19"
                tranLog1.orderSN = orderSN
                tranLog1.order_user = Long.valueOf(params.body)
                tranLog1.flat = '2'
                tranLog1.save()
                
                def user = User.get(params.body)
                //支付宝表
                def alipayRemit = new AlipayRemit()
                alipayRemit.remitSN = orderSN
                alipayRemit.user = user
                alipayRemit.amount = new BigDecimal(params.total_fee)
                alipayRemit.save()
                
                //用户充值
                def account = user.account
                account.lock()
                account.amount = alipayRemit.amount +account.amount
                account.save()
        }
}

