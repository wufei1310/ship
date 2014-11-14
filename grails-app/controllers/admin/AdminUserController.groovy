package admin

import ship.LoginPOJO
import ship.User
import ship.Menu
import ship.Role
import ship.MobileMessage
import grails.converters.JSON
import ship.Pager
import ship.Account
import ship.Market
import ship.UserCheck

class AdminUserController extends BaseController{
    def jcaptchaService
    def adminUserService
    def index() { }
    
    
    def list(){
        if (!params.max) {
            params.max = 10  
        }else{
            params.max = new Long(params.max)
        }
        if (!params.offset) params.offset = 0  
        if (!params.sort) params.sort = "dateCreated"  
        if (!params.order) params.order = "desc" 
        
        def searchClosure =  {
            if(params.email){
                like('email','%'+params.email+'%')
            }
            if(params.role){
                eq('role',params.role)
            }
            if(params.userid){
                eq('id',params.userid as Long)
            }
        }
        
        def o = User.createCriteria();
        def results = o.list(params,searchClosure)
        
        def map = [list: results, total: results.totalCount]
    
        
        if(params.mobile=="mobile"){
            //封装分页传给手机端
            def pager = new Pager(max:params.max,"offset":params.offset,"totalPage":(results.totalCount/params.max + 1) as int ,total:results.totalCount)
            map.put("pager",pager)
            
            def mm = new MobileMessage()
            mm.message = "获取订单列表成功"
            mm.result = "success"
            mm.model = map
            
            render mm as JSON
        }else{
            render(view: "/admin/user/list",model:map)
        }
    }


    def cancleBind(){

        def userCheck = UserCheck.findByEmail(params.email1);
        if(userCheck){
            userCheck.delete()

        }
        redirect(action: "list",params: params)

    }

    def isCanPrint(){


        def user = User.get(params.id)
        user.isCanPrint = params.isCanPrint
        flash.message = "操作成功"
        flash.messageClass=this.success
        render(view:this.commonSuccess)
    }

    
    def reqLogin(){
        render(view:"/admin/login")
    }
    
    def doLogin(){
        //验证码不正确0，登录成功1,用户名密码不正确2
        try{
             if (!jcaptchaService.validateResponse("imageCaptcha", session.id, params.loginValidCode)){  

                flash.message="验证码不正确";
                redirect(action: "reqLogin")
                return;
             }
        }catch(Exception e){
                flash.message="验证码不正确";
                redirect(action: "reqLogin")
                return;
        }
       

        def user = User.findByEmailAndPasswordAndUser_type(params.email, params.password.encodeAsPassword(),'admin')
        if(!user){
            flash.message="帐号或者密码不正确";
            redirect(action: "reqLogin")
            return;
        }else{
            
            if(user.status == '1'){
                 flash.message = "用户被冻结" 
                 redirect(action: "reqLogin")
                 return;
            }
           
            def searchClosure =  {
            
           role{eq("id",user.role_id.id)}
        }
        
        def o = Menu.createCriteria();
        def menu = o.list(params,searchClosure)

            def menuList = new ArrayList();
            def roleMenuList = new ArrayList();
            menu.each { it ->
                if(it.pId == 0){
                   menuList.add(it) 
                }
                if(it.menuPath){
                    roleMenuList.add(it.menuPath)
                }     
            }
            menuList.each{it ->
                menu.each{ i ->
                    if(i.pId == it.id)
                        it.childrenMenu.add(i)
                }
            }
            
            def loginPOJO = new LoginPOJO();
            loginPOJO.user = user;
            loginPOJO.menuList = menuList;
            loginPOJO.roleMenuList = roleMenuList//验证菜单权限
            session.loginPOJO = loginPOJO;
            redirect(controller:"adminIndex",action: "index")
            return;
        }
    }
    
    def toAddDaiFaUser(){
        def role = Role.list()
        def market = Market.list().market_name
        def map = [role:role,marketList:market]
        render(view:"/admin/user/addDaiFaUser",model:map)
    }
    
    def toUpdateDaiFaUser(){
        def user = User.get(params.id)
        def role = Role.findAllByType(user.role)
        def market = Market.list().market_name
        def user_market = user.market.market_name
        def map = [user:user,role:role,marketList:market,user_market:user_market]
        render(view:"/admin/user/updateDaiFaUser",model:map)
    }
    
    def doUpdateDaiFaUser(){
        def user = User.get(params.id)
        user.role_id = Role.get(params.role_key)
        user.market = null
        if(params.marketList instanceof String ){
            user.addToMarket(Market.findByMarket_name(params.marketList))
        }else{
            params.marketList.eachWithIndex{it, i -> 
                user.addToMarket(Market.findByMarket_name(params.marketList[i]))
            }
        }
        flash.message="分配角色市场成功";
        render(view:this.commonSuccess)
    }
    
    def doAddDaiFaUser(){
        def msg = adminUserService.doAddDaiFaUser(params,User.get(session.loginPOJO.user.id))
        if(msg){
            flash.message = msg 
            flash.messageClass=this.error
        }else{
            flash.message = "添加用户成功" 
            flash.messageClass=this.success
        }
        render(view:this.commonSuccess)
    }
    
    def dongjie(){
        if(session.loginPOJO.user.role == 'admin'){
            def user = User.get(params.id)
            user.status = '1'
            flash.message = "冻结成功" 
            flash.messageClass=this.success
            render(view:this.commonSuccess)
        }
        
    }
    
    def noDongjie(){
        if(session.loginPOJO.user.role == 'admin'){
            def user = User.get(params.id)
            user.status = '0'
            flash.message = "取消冻结状态成功" 
            flash.messageClass=this.success
            render(view:this.commonSuccess)
        }
    }
    
}
