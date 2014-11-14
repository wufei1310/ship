/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.text.SimpleDateFormat
import grails.converters.JSON
/**
 *
 * @author DELL
 */

class FileUtil {
    
    public static final String ACCESS_KEY = SysPropertUtil.getString("QN.ACCESS_KEY")
    public static final String SECRET_KEY = SysPropertUtil.getString("QN.SECRET_KEY")
    
    def static doUpload(extMap,bucketName,fileName,inputStream){
        Config.ACCESS_KEY = ACCESS_KEY;
        Config.SECRET_KEY = SECRET_KEY;
        Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        // 请确保该bucket已经存在
       // String bucketName = "trjmc";
        PutPolicy putPolicy = new PutPolicy(bucketName);
        
        //设定类型限制
        if(extMap){
            StringBuffer s = new StringBuffer(); 
            extMap.each { key, value ->
                String[] vals = value.split(",")
                vals.each{
                    s.append(";").append(key+"/"+it)
                }
            }
            putPolicy.mimeLimit = s.toString().substring(1)
            print putPolicy.mimeLimit
        }
        
        String uptoken = putPolicy.token(mac);
        PutExtra extra = new PutExtra();
        
        PutRet ret = IoApi.putStream(uptoken, fileName, inputStream, extra);
        print ret as JSON
        if(ret.statusCode==200){
            return true
        }else{
            return false;
        }
    }
    
    def static String getNewFileName(id){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
	String newFileName = df.format(new Date()) + "_"+id+"_" + UUID.randomUUID().toString()
        return newFileName
    }
    
    
    
}

