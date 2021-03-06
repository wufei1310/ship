package admin
import ship.User
import ship.Account
import ship.Role
import ship.Market
class AdminUserService {

    def doAddDaiFaUser(params,adduser) {
        def user = User.findByEmail(params.email,[lock: true])
        if(user){
             //flash.message = "账号已存在" 
            // flash.messageClass=this.error
            // render(view:this.commonSuccess)
             return "账号已存在"
        }
         def newUser = new User(email:params.email,password:params.password.encodeAsPassword())
         newUser.user_type= "admin" 
         newUser.role = params.role
         newUser.phone = params.phone
         newUser.adduser = adduser
         newUser.status = '0'
         newUser.role_id = Role.get(params.role_key)
         
        if(params.marketList instanceof String ){
            newUser.addToMarket(Market.findByMarket_name(params.marketList))
        }else{
            params.marketList.eachWithIndex{it, i -> 
                newUser.addToMarket(Market.findByMarket_name(params.marketList[i]))
            }
        }
        
         def account = new Account();
         account.amount = new BigDecimal(0);
         newUser.account = account
         newUser.save();
         return null
    }

    //根据角色查询用户
    def selUser(role){

        def user = User.findAllByRoleAndStatus(role,"0");

        return user
    }
}
