/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

/**
 *
 * @author DELL
 */
class SysPropertUtil {
	public static properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties")) 
        
        public static String  getString(String key){
            return properties.getProperty(key)
        }
}

