package ship

class PushMsg {

    static constraints = {
        
    }
    
 static mapping = {
  id generator:'assigned'
}
   Long id
   String appid;
   String channel_id;
   String push_user_id;
   String apk_type;
   String user_type
   String role  //角色
   
    Date dateCreated
    Date lastUpdated
}
