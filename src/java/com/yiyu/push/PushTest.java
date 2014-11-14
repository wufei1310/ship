package com.yiyu.push;

public class PushTest {
	
	private final static String USERID = "1151633511600470806";
	private final static String CHANNELID = "3771856989221427189";
	
	private final static String API_KEY = "I6PeNRqxutvTOwa7dAjGVPVh";
	private final static String SECRIT_KEY = "ueRDmVHoUVAwrUv6lpqKlRuBlduqZSWT";
	
	public static void main(String[] args) {
		 
		BaiduPush baiduPush = new BaiduPush(BaiduPush.HTTP_METHOD_POST, SECRIT_KEY, API_KEY);
		
		//System.out.format("PUSH: %s\n", baiduPush.QueryBindlist(USERID, CHANNELID));
		//System.out.format("PUSH: %s\n", baiduPush.VerifyBind(USERID, null));
		
		//System.out.format("PUSH: %s\n", baiduPush.SetTag("tag1"));
		//System.out.format("PUSH: %s\n", baiduPush.SetTag("tag2"));
		//System.out.format("PUSH: %s\n", baiduPush.SetTag("tag3"));
		
		//System.out.format("PUSH: %s\n", baiduPush.QueryUserTag(USERID));
		//System.out.format("PUSH: %s\n", baiduPush.FetchTag());
		
		//System.out.format("PUSH: %s\n", baiduPush.PushMessage("hello world"));
		//System.out.format("PUSH: %s\n", baiduPush.PushMessage("hello world", USERID));
		//System.out.format("PUSH: %s\n", baiduPush.PushTagMessage("hello", "tag1"));
		
		System.out.format("PUSH: %s\n", baiduPush.PushNotify("Hello", "testAndroid", USERID));
		
	}
	
	//189  455
}
