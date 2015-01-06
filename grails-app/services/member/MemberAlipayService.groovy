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
                def user = User.get(params.body)
                //用户充值
                def account = user.account
                account.lock()
                BigDecimal memberamount = account.amount;

                //插入资金流水表（会员充值（收入））
                def tranLog = new TranLog();
                tranLog.shouru_type = "1"
                tranLog.amount = new BigDecimal(params.total_fee)
                memberamount = memberamount + new BigDecimal(params.total_fee)
                tranLog.memberamount =  memberamount
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
                

                //支付宝表
                def alipayRemit = new AlipayRemit()
                alipayRemit.remitSN = orderSN
                alipayRemit.user = user
                alipayRemit.amount = new BigDecimal(params.total_fee)
                alipayRemit.save()
                

                account.amount = alipayRemit.amount +account.amount
                account.save()
        }
}

