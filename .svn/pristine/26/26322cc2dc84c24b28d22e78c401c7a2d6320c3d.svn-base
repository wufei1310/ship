package ship

class RegisterController {
    def jcaptchaService
    def index() { }
    
    
    def doRegister(){
        def returnCode = "" ;//验证码不正确0，注册成功1,用户名已存在2
        try{
            if (!jcaptchaService.validateResponse("registerImageCaptcha", session.id, params.registerImageCaptcha)){  
        
                flash.message = "验证码不正确" 
                redirect(controller: 'index',action:"index",params:[reqAction:"reqRegister"])
                return
             }
        }catch(Exception e){
                flash.message = "验证码不正确" 
                redirect(controller: 'index',action:"index",params:[reqAction:"reqRegister"])
                return
        }
        
        if(!params.email.endsWith("@qq.com")){
          params.email = params.email+"@qq.com"
        }
        def user = User.findByEmail(params.email)
        if(user){
            flash.message = "邮箱已存在" 
            redirect(controller: 'index',action:"index",params:[reqAction:"reqRegister"])
            return
        }
        
        def newUser = new User(email:params.email,password:params.password.encodeAsPassword())
        newUser.user_type= "member"
        newUser.role= "member"
        newUser.status = '0'
         def account = new Account();
         account.amount = new BigDecimal(0);
        newUser.account = account
        newUser.save();
       

        
        def loginPOJO = new LoginPOJO();
        loginPOJO.user = newUser;
        session.loginPOJO = loginPOJO;
        
        redirect(controller: 'index',action:"index")
        return
        
    }
    
    
}
