/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package common

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils
/**
 *
 * @author DELL
 */
class CommonParams {
	public static Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("sysSetting.properties"))
        public static final String daifa_free = properties.getProperty("daifa.daifa_free")
        public static final String return_free = properties.getProperty("daifa.return_free")
        public static final Map regardsMap = ["1":1,"2":2,"3":3,"4":1.5,"5":1]
}

