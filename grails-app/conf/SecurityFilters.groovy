import util.StringUtil
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wufei
 */
class SecurityFilters {
    def filters = {
        
        
	    
        
        loginCheck(controller:'*', action:'*') {



            before = {

                request.setCharacterEncoding("utf-8");
                
//                String serverName = request.getServerName()
//                if(serverName.endsWith("kingsdf.cn")){
//                   //String basePath = request.getScheme()+"://kingsdf.com:"+request.getServerPort()+path+"/";
//                    redirect(url: "http://kingsdf.com")
//                    return 
//                }
                
               checkParam(params)
               
                if(controllerName.startsWith('member') && !actionName.equals("alinotify")){
                    if(controllerName!="memberNewProduct"){//
                        if(!session.loginPOJO?.user){
                            redirect(controller: '/',params:[reqAction:"reqLogin"])
                            return false
                        }
                        if(session.loginPOJO.user.user_type=='admin'){
                            redirect(controller:"adminUser",action: "reqLogin")
                            return false
                        }
                    }

                }



                if((!session.loginPOJO?.user || !session.loginPOJO.user.user_type.equals("admin"))
                    && controllerName.startsWith('admin') && !actionName.equals("reqLogin") && !actionName.equals("doLogin")) {//请求后台页面
                    redirect(controller:"adminUser",action: "reqLogin" ,params:[sessionFail:"sessionFail"])
                    return false
                }
                //如果是访问图片服务器 则必须登录
               if(controllerName == "image"
                       && !session.loginPOJO?.user
               ){
                   return false
               }

                println   "请求路径:" + controllerName + "/"+ actionName
                    
            }
            after={
                response.setCharacterEncoding("utf-8");
            }
        }
	
	
    }
    
    private void checkParam(Map param){
//        Map<String,String> oldMap = new HashMap<String,String>();
//        List<String> list =new ArrayList()
//        param.each { key, value ->
//            if(key.startsWith("_old_param_")){
//                oldMap.put(key.substring(11),StringUtil.getSafeStringXSS(value))
//                list.add(key)
//            }else{
//                param.put(key,StringUtil.getSafeStringXSS(value))
//            } 
//        } 
//        for(int i=0;i<list.size();i++){
//            param.remove(list.get(i))
//        }
//        oldMap.each{ key, value ->
//            if(!param.containsKey(key)){
//                param.put(key,value)
//            }
//        }
        
        param.each { key, value ->
                if(value instanceof String){
                     param.put(key,StringUtil.getSafeStringXSS(value))
                }else{
                    Class c=value.getClass();
                    if(c.isArray()){//如果是数组
                        List list = new ArrayList();
                         for(it in value)
                         {
                            it = StringUtil.getSafeStringXSS(it)
                            list.add(it)
                        }
                        param.put(key,list.toArray(new String[0]))
                    }
                }

        } 

    }
}

