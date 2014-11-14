/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

public class UploadDownloadUtil {

    public static boolean smbPut(String remoteUrl, String localFilePath) {

        File localFile = new File(localFilePath);
        FileInputStream inn = null;
        SmbFileOutputStream out = null;
        // try{
        SmbFile f = new SmbFile(remoteUrl);
        String filePath = remoteUrl.substring(0, remoteUrl.lastIndexOf("/"));
        SmbFile fileSmb = new SmbFile(filePath);
        if (!fileSmb.exists()) {
            fileSmb.mkdirs();
        }
        inn = new FileInputStream(localFile);
        out = new SmbFileOutputStream(f);

        long t0 = System.currentTimeMillis();

        byte[] b = new byte[8192];
        int n, tot = 0;
        while ((n = inn.read(b)) > 0) {
            out.write(b, 0, n);
            tot += n;
            System.out.print('#');
        }

        long t = System.currentTimeMillis() - t0;

        System.out.println();
        System.out.println(tot + " bytes transfered in " + (t / 1000) + " seconds at " + ((tot / 1000) / Math.max(1, (Double) (t / 1000))) + "Kbytes/sec");
        //  }catch(IOException e){
//		    	e.printStackTrace();
//		    	System.out.print("上传服务器失败");
//		    	return false;
//		    }finally{
//		    	try {
//					inn.close();
//					out.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//					System.out.print("上传服务器失败");
//			    	return false;
//				}
//		    }
        return true;
    }

    public static boolean smbPut(String remoteUrl, FileInputStream inn) {


        SmbFileOutputStream out = null;
        try {
            SmbFile f = new SmbFile(remoteUrl);
            out = new SmbFileOutputStream(f);

            long t0 = System.currentTimeMillis();

            byte[] b = new byte[8192];
            int n, tot = 0;
            while ((n = inn.read(b)) > 0) {
                out.write(b, 0, n);
                tot += n;
                System.out.print('#');
            }

            long t = System.currentTimeMillis() - t0;

            System.out.println();
            System.out.println(tot + " bytes transfered in " + (t / 1000) + " seconds at " + ((tot / 1000) / Math.max(1, (t / 1000))) + "Kbytes/sec");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("上传服务器失败");
            return false;
        } finally {
            try {
                inn.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.print("上传服务器失败");
                return false;
            }
        }
        return true;
    }


    public static boolean deleteFile(String remoteUrl) {
        try {
            SmbFile f = new SmbFile(remoteUrl);
            if (f.exists()) {
                f.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
            return false;
        }
        return true;
    }

    public static boolean creatFile(String remoteUrl) {
        try {
            SmbFile f = new SmbFile(remoteUrl);
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (SmbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static InputStream getFileInputStream(String remoteUrl) {
        def inputStream = null
        try {
            SmbFile f = new SmbFile(remoteUrl);
            inputStream = f.getInputStream()
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (SmbException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

