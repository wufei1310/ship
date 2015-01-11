/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package member

import ship.AlipayRemit
import ship.TranLog
import ship.User
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
import util.DecimalUtil

/**
 *
 * @author DELL
 */
class MemberAlipayService {


    def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
    def shouxufee = properties.getProperty("AlipayConfig.shouxufee")


    def shouxufee() {
        return new BigDecimal(shouxufee)
    }

    def chongzhi(params, orderSN) {


        def user = User.get(params.body)
        //用户充值
        def account = user.account
        account.lock()
        BigDecimal memberamount = account.amount;


        def shouxu =new BigDecimal(params.extra_common_param)
        def chongzhi = DecimalUtil.sub(new Double(params.total_fee),new Double(shouxu))
        //插入资金流水表（会员充值（收入））
        def tranLog = new TranLog();
        tranLog.shouru_type = "1"
        tranLog.amount =  chongzhi

        tranLog.direction = "0"
        tranLog.type = "18"
        tranLog.orderSN = orderSN
        tranLog.order_user = Long.valueOf(params.body)
        tranLog.flat = '2'
        tranLog.save()



        def shouxuLog = new TranLog();
        shouxuLog.shouru_type = "1"
        shouxuLog.amount =  shouxu
        shouxuLog.direction = "0"
        shouxuLog.type = "29"
        shouxuLog.orderSN = orderSN
        shouxuLog.order_user = Long.valueOf(params.body)
        shouxuLog.flat = '2'
        shouxuLog.save()


        //插入资金流水表（会员充值（支出））
        def tranLog1 = new TranLog();
        tranLog1.amount = new BigDecimal(chongzhi)
        memberamount = memberamount + new BigDecimal(chongzhi)
        tranLog1.memberamount = memberamount
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
        alipayRemit.amount = new BigDecimal(chongzhi)
        alipayRemit.save()


        account.amount = chongzhi + account.amount
        account.save()
    }
}

