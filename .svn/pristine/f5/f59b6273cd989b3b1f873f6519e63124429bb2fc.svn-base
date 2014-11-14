import java.security.MessageDigest
import sun.misc.BASE64Encoder
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wufei
 */
class PasswordCodec {
    def encode = {  
	str ->  
        
        
        getMD5(str.getBytes())
//	//用 SHA 加密密码  
//	MessageDigest md = MessageDigest.getInstance('SHA')  
//	md.update(str.getBytes('UTF-8'))  
//	//把SHA加密产生的字节数组交给BASE64在加密一把，并返回该字符串  
//	return(new BASE64Encoder()).encode(md.digest())
    }  
    
    
    
    public String getMD5(byte[] paramArrayOfByte)
    {
        String str = null;
        def arrayOfChar1 = [ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' ];
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar2 = new char[32];
            int i = 0;
            for (int j = 0; j < 16; j++)
            {
                int k = arrayOfByte[j];
                arrayOfChar2[(i++)] = arrayOfChar1[(k >>> 4 & 0xF)];
                arrayOfChar2[(i++)] = arrayOfChar1[(k & 0xF)];
            }
            str = new String(arrayOfChar2);
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return str;
    }
}

