package admin
import sysinit.SysInitParams
class BaseController {

    public static final String error = 'error'
    public static final String success = 'success'
    public static final String commonSuccess = '/commons/success'
    
   def beforeInterceptor = {
       //如果是登录状态下
       if(session.loginPOJO?.user && session.loginPOJO.user.user_type == 'admin'){
            String path = controllerName +"/"+ actionName
            def roleMenuList = session.loginPOJO.roleMenuList
            

            def menuList = SysInitParams.getMenuList()

            if(menuList.contains(path)){
                if(!roleMenuList.contains(path)){
                    render "非法请求"
                    return false
                }
            }
         }
    }
}
