/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util
import org.codehaus.groovy.grails.web.util.WebUtils  
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 *
 * @author DELL
 */
class RemoteFileUtil {
	private static String img_addresse=SysPropertUtil.getString("img_addresse");
        private static String img_url=SysPropertUtil.getString("img_url");    
    
        private static long exceptionTime = 5000;
        
        

	/**
	 * 复制缓存图片到实际保存路径中，并返回真实路径
	 * @param request
	 * @param tempPath
	 * @return
	 * @throws Exception 
	 */
	public static String remoteFileCopy(request,tempPath){ 
                if(!tempPath)
                    return null
		//上传图片服务器
		if(tempPath.startsWith(img_url)){
			return tempPath;
		}
		
                def _session = request.getSession()
                
        
		String user_id = _session.loginPOJO.user.id
		
		String contextPath = _session.getServletContext().getRealPath("/");
		String deltePath = tempPath;
                

        
		String fileName = tempPath.substring(tempPath.lastIndexOf("/")+1);
                
                String file_type = tempPath.substring(tempPath.lastIndexOf("."));
                //判断是文件还是图片
                String _url;
                String _addresse
  
                    _url = img_url
                    _addresse = img_addresse
       
        
		String backPath = _url+user_id+"/"+fileName;
		String uplodaPath = _addresse+user_id+"/"+fileName;
		File uploadedFile = new File(contextPath+"/"+deltePath);
		if(!uploadedFile.exists()){
                    print uploadedFile.getPath()
			System.out.print("此文件"+uploadedFile.getPath()+"不存在，略过~~~~~~~~~~~~~~~~~~~~~");
			return backPath;
		}
		
		boolean flat= UploadDownloadUtil.smbPut(uplodaPath, contextPath+"/"+deltePath);
		long time =0;
		while (!flat){
			if(time>exceptionTime){
				throw new RuntimeException("上传图片超时，请检查图片服务器配置");
			}
			try {
				Thread.sleep(500);
				time = time+500;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flat= UploadDownloadUtil.smbPut(uplodaPath, contextPath+"/"+deltePath);
		}
		deleteFile(contextPath+"/"+deltePath);
		print backPath
		return backPath;
		
	}
        
   public static void deleteFile(String path) {
		File file = new File(path);
		if(file.exists()) {
			file.delete(); 
		}
	}
        
    
}

