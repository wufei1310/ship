package taobao
import java.security.MessageDigest
import java.text.SimpleDateFormat
import sun.misc.BASE64Decoder
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Administrator
 */
class TaobaoUtil {
	
    /**
         * 新的md5签名，首尾放secret。
         * @param secret 分配给您的APP_SECRET
         */
         public static String md5Signature(TreeMap<String, String> params, String secret) {
                 String result = null;
                 StringBuffer orgin = getBeforeSign(params, new StringBuffer(secret));
                 if (orgin == null)
                 return result;
                 orgin.append(secret);
                 try {
                         MessageDigest md = MessageDigest.getInstance("MD5");
                         result = byte2hex(md.digest(orgin.toString().getBytes("utf-8")));
                 } catch (Exception e) {
                         throw new java.lang.RuntimeException("sign error !");
                 }
             return result;
         }
         /**
         * 二行制转字符串
         */
         private static String byte2hex(byte[] b) {
                 StringBuffer hs = new StringBuffer();
                 String stmp = "";
                 for (int n = 0; n < b.length; n++) {
                         stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
                         if (stmp.length() == 1)
                                 hs.append("0").append(stmp);
                         else
                                 hs.append(stmp);
                 }
                 return hs.toString().toUpperCase();
         }
         /**
         * 添加参数的封装方法
         */
         private static StringBuffer getBeforeSign(TreeMap<String, String> params, StringBuffer orgin) {
                 if (params == null)
                         return null;
                 Map<String, String> treeMap = new TreeMap<String, String>();
                 treeMap.putAll(params);
                 Iterator<String> iter = treeMap.keySet().iterator();
                 while (iter.hasNext()) {
                         String name = (String) iter.next();
                         orgin.append(name).append(params.get(name));
                 }
                 return orgin;
         }
         /**连接到TOP服务器并获取数据*/
         public static String getResult(String urlStr, String content) {
                 URL url = null;
                 HttpURLConnection connection = null;
 
                 try {
                         url = new URL(urlStr);
                         connection = (HttpURLConnection) url.openConnection();
                         connection.setDoOutput(true);
                         connection.setDoInput(true);
                         connection.setRequestMethod("POST");
                         connection.setUseCaches(false);
                         connection.setConnectTimeout(20000);
  						 connection.setReadTimeout(20000);
                         connection.connect();
                         
                        
 
                         DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                         out.write(content.getBytes("utf-8"));
                         out.flush();
                         out.close();
 
                         BufferedReader reader =
                         new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
                         StringBuffer buffer = new StringBuffer();
                         String line = "";
                         while ((line = reader.readLine()) != null) {
                                 buffer.append(line);
                         }
                         reader.close();
                         return buffer.toString();
                 } catch (IOException e) {
                     e.printStackTrace();
                     return null;
                 } finally {
                         if (connection != null) {
                                 connection.disconnect();
                         }
                 }
         }
         
         public  String getResultStr(TreeMap<String, String> apiparamsMap){
		 String testUrl = "http://gw.api.taobao.com/router/rest";//正式环境
	         apiparamsMap.put("format", "xml");
	         //apiparamsMap.put("method", "taobao.item.get");
	         apiparamsMap.put("sign_method","md5");
	         apiparamsMap.put("app_key","21705358");
	         apiparamsMap.put("v", "2.0");
	         //apiparamsMap.put("session",request.getParameter("session"));他用型需要sessionkey
	         String timestamp =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	         apiparamsMap.put("timestamp",timestamp);
	        // apiparamsMap.put("fields","title,props_name,price,item_img,item_weight,cid,property_alias");//需要获取的字段
	        // apiparamsMap.put("num_iid",id);
	         //生成签名
	         String sign = md5Signature(apiparamsMap,"bce1cb63dcaf1d1d7171e27b1e1a630d");
	         apiparamsMap.put("sign", sign);
	         StringBuilder param = new StringBuilder();
	         for (Iterator<Map.Entry<String, String>> it = apiparamsMap.entrySet()
	         .iterator(); it.hasNext();) {
	             Map.Entry<String, String> e = it.next();
	             param.append("&").append(e.getKey()).append("=").append(e.getValue());
	         }
	         
	         String result = getResult(testUrl,param.toString().substring(1));
                 print result
	         if( result==null || result.indexOf("error_response")>-1){
	             if(result.indexOf("Missing session")>-1 || result.indexOf("Invalid session")>-1){
                         return "error session"
                     }else{
                        return null; 
                     }	
                        
	         }else{
	        	 return result; 
	         }
	         
	}       
        
        public static Map<String, String> convertBase64StringtoMap(String str) { 



                if (str == null) 


                return null; 


                String keyvalues = null; 


                try { 


                BASE64Decoder decoder = new BASE64Decoder(); 


                keyvalues = new String(decoder.decodeBuffer(str)); 


           


                } catch (IOException e) { 


                return null; 


                } 


                if (keyvalues == null || keyvalues.length() == 0) 


                return null; 


                String[] keyvalueArray = keyvalues.split("&"); 


                Map<String, String> map = new HashMap<String, String>(); 


                for (String keyvalue : keyvalueArray) { 


                String[] s = keyvalue.split("="); 


                if (s == null || s.length != 2) 


                return null; 


                map.put(s[0], s[1]); 


                } 


                return map; 


                } 


    
}

