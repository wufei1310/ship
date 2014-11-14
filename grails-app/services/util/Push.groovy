package util
import ship.PushPOJO
import com.yiyu.push.BaiduPush
import com.yiyu.push.AndroidPushNotification
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
class Push {
	 private final static String API_KEY_LSS = "0T66q3GLE03pCzwmFfonEY7z";
	 private final static String SECRIT_KEY_LSS = "6ZMC5g7VzG7SKfG5DNOaNW1WfcltkPyu";
	
         
         public void pushByStore(PushPOJO pushPOJO){
		  
		def pushMsgList = pushPOJO.pushMsgList
                pushMsgList.each { pushMsg ->
                     Thread.start {
                        Map<String,String> pushKeyMap = getPushKey(pushMsg.apk_type);
                        AndroidPushNotification baiduPush = new AndroidPushNotification();
                        String result = baiduPush.push(pushPOJO.title, pushPOJO.content, pushMsg.push_user_id);
//                        print result+"-================================================";
                     }
                }
		
	}
         
         private Map<String,String> getPushKey(String apk_type) {
		Map<String,String> pushKeyMap = new HashMap<String,String>();
		if("cg_daifa_android_native".equals(apk_type)) {
                        pushKeyMap.put("SECRIT_KEY", SECRIT_KEY_LSS);
			pushKeyMap.put("API_KEY", API_KEY_LSS);
		} 
		return pushKeyMap;
	}
}

