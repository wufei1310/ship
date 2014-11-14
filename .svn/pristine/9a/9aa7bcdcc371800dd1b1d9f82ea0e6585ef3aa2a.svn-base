package admin
import ship.Remit
import ship.User
import ship.Account
import ship.TranLog
class AdminRemitService {

    def doCheck(id,status,reason,checkUser) {
         def remit = Remit.lock(id)
         if(remit.status != '0')
            return "页面过期！"
         remit.status = status;
         remit.check_user = User.get(checkUser.id)
         remit.check_date = new Date()
         remit.reason=reason
         if(status == '1'){
            
            //插入资金流水表（会员充值（收入））
                def tranLog = new TranLog();
                tranLog.shouru_type = "2"
                tranLog.amount = remit.bank_amount
                tranLog.direction = "0"
                tranLog.type = "18"
                tranLog.orderSN = remit.bank_order
                tranLog.order_user = remit.submit_user.id
                tranLog.flat = '2'
                tranLog.save()
                
                //插入资金流水表（会员充值（支出））
                def tranLog1 = new TranLog();
                tranLog1.amount = remit.bank_amount
                tranLog1.direction = "1"
                tranLog1.type = "19"
                tranLog1.orderSN = remit.bank_order
                tranLog1.order_user = remit.submit_user.id
                tranLog1.flat = '2'
                tranLog1.save()
            
             //资金划拨
             def user = remit.submit_user
             def account = user.account
             account.lock()
             account.amount = remit.bank_amount +account.amount
         }
         return null
    }
}
