/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.WriteRender;
import com.mortennobel.imagescaling.ResampleOp;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 创建缩略图
 * 
 * @author Administrator
 * 
 */
public class ImageManagerUtil {

	private static final float defualtQuality = 0.92f; 
	
	private double newImgWideth;
	private double newImgHeight;
	private double changeToWideth;
	private double changeToHeight;
	private boolean flat = true;

	public boolean isFlat() {
		return flat;
	}

	public void setFlat(boolean flat) {
		this.flat = flat;
	}
        
        public double getWidth(){
            return newImgWideth
        }
        public double getHeight(){
            return newImgHeight
        }
	
	 //过滤图像
    public BufferedImage applyFilter(float[] data,BufferedImage bufImage,int type) {
		Kernel kernel = new Kernel(3, 3, data);  
		ConvolveOp imageOp=new ConvolveOp(kernel,ConvolveOp.EDGE_NO_OP, null);	//创建卷积变换操作对象
		BufferedImage filteredBufImage = null;
		for(int i=0;i<=type;i++){
			filteredBufImage = new BufferedImage((int)getWidth(bufImage),(int)getHeight(bufImage),BufferedImage.TYPE_INT_RGB);	//过滤后的缓冲区图像
			imageOp.filter(bufImage, filteredBufImage);//过滤图像，目标图像在filteredBufImage
			bufImage = filteredBufImage;
		}
		return filteredBufImage;
	}
	
	//模糊图像
 	public float[] blur() {
		
		def data = [
				0.0625f, 0.125f, 0.0625f,
				0.125f,	0.025f, 0.125f,
				0.0625f, 0.125f, 0.0625f 
		]
		return data;
	}
 	
 	//马赛克效果
 	public BufferedImage masaic(BufferedImage img,int masaicSize) throws Exception {

		int imageWidth = img.getWidth(null);
		int imageHeight = img.getHeight(null);

		BufferedImage mosaicPic = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

		int x = 0;// 矩形绘制点的x坐标
		int y = 0; // 矩形绘制点的y坐标
		int xCnt = 0;// 矩形绘制x方向个数
		int yCnt = 0;// 矩形绘制y方向个数
		if (imageWidth % masaicSize == 0) {
			xCnt = imageWidth / masaicSize;
		} else {
			xCnt = imageWidth / masaicSize + 1;
		}
		if (imageHeight % masaicSize == 0) {
			yCnt = imageHeight / masaicSize;
		} else {
			yCnt = imageHeight / masaicSize + 1;
		}

		// 绘制矩形并填充颜色
		Graphics gs = mosaicPic.getGraphics();
		for (int i = 0; i < xCnt; i++) {
			for (int j = 0; j < yCnt; j++) {
				// 计算矩形宽高
				int mosaicWidth = masaicSize;
				int mosaicHeight = masaicSize;
				if (i == xCnt - 1) {
					mosaicWidth = imageWidth - x;
				}
				if (j == yCnt - 1) {
					mosaicHeight = imageHeight - y;
				}
				// 矩形颜色取中心像素点RGB值
				int centerX = x;
				int centerY = y;
				if (mosaicWidth % 2 == 0) {
					centerX += mosaicWidth / 2;
				} else {
					centerX += (mosaicWidth - 1) / 2;
				}
				if (mosaicHeight % 2 == 0) {
					centerY += mosaicHeight / 2;
				} else {
					centerY += (mosaicHeight - 1) / 2;
				}
				Color color = new Color(img.getRGB(centerX, centerY));
				gs.setColor(color);
				gs.fillRect(x, y, mosaicWidth, mosaicHeight);
				y = y + masaicSize;// 计算下一个矩形的y坐标
			}
			y = 0;// 还原y坐标
			x = x + masaicSize;// 计算x坐标
		}
		gs.dispose();
		return mosaicPic;
	}

 	
 	public BufferedImage readImageEffectImg(File file) throws IOException{
 		BufferedImage inn =  ImageIO.read(file);
 		BufferedImage originalBufImage = new BufferedImage(in.getWidth(),in.getHeight(),BufferedImage.TYPE_INT_RGB);
 		Graphics2D g = originalBufImage.createGraphics();
 		g.drawImage((Image)inn,0,0,null);
 		return originalBufImage;
 	}
 	
	public BufferedImage readImage(File file) {
		BufferedImage  buff = null;
		InputStream inputStream = null;
		ByteArrayOutputStream baos = null;
		ReadRender rr = null;
		WriteRender wr = null;
		InputStream fileInput = null;
			try {
				fileInput = new FileInputStream(file);
				baos = new ByteArrayOutputStream();
				rr = new ReadRender(fileInput, true);
				wr = new WriteRender(rr, baos);
				wr.render();
				inputStream = new ByteArrayInputStream(baos.toByteArray());
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SimpleImageException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally{
                            if(fileInput!=null){
                                try {
                                    fileInput.close()
                                 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
                                 }
                            }
                        }
		try {
			buff =  ImageIO.read(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				//fileInput.close();
				inputStream.close();
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buff;
	}

	public double getHeight(BufferedImage image) {
		return image.getHeight();
	}

	public double getWidth(BufferedImage image) {
		return image.getWidth();
	}

	public void writeImage(BufferedImage image, String formatName, File file)
			throws IOException {
		if (image != null && formatName != null && !"".equals(formatName)
				&& file != null) {
			if(flat){
				// 创建一个和要求同大小的新空白图片
				BufferedImage bimage = new BufferedImage((int)newImgWideth,
						(int)newImgHeight, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = bimage.createGraphics();
				g.setColor(Color.white);   
			    g.fillRect(0, 0, (int)newImgWideth, (int)newImgHeight);
				// 画原图
				if (changeToWideth < newImgWideth) {
					g.drawImage((Image) image, (int)(newImgWideth - changeToWideth) / 2,
							0, (int)changeToWideth, (int)changeToHeight, null);
				} else {
					g.drawImage((Image) image, 0,
							(int)(newImgHeight - changeToHeight) / 2, (int)changeToWideth,
							(int)changeToHeight, null);
				}
				g.dispose();
				image = bimage;
			}
			FileOutputStream out = null;
			try {
				// String newWaterFile =
				// filePath.substring(0,filePath.lastIndexOf("."))+"_water."+filePath.substring(filePath.lastIndexOf(".")+1);
				out = new FileOutputStream(file);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				JPEGEncodeParam param = encoder
						.getDefaultJPEGEncodeParam(image);
				param.setQuality(defualtQuality, true);
				encoder.encode(image, param);
			} catch (Exception e) {
				System.out.println("---生成失败---");
			} finally {
				if (out != null) {
					try {
						out.close();
						out = null;
					} catch (Exception e) {
					}
				}
			}
//			System.out.println("生成成功");
		}
	}

	//flat ture添加白色底色固定宽和高大小   false只固定宽度不添加底色
	public BufferedImage zoom(BufferedImage image, String newWideth,
			String newHeight) {
		if(newWideth!=null && newHeight!=null){
			this.newImgWideth = Double.parseDouble(newWideth);
			this.newImgHeight = Double.parseDouble(newHeight);
			double imageWideth =  getWidth(image);
			double imageHeight =  getHeight(image);
			if (imageWideth > 0 && imageHeight > 0) {
				if(flat){
					// 原图比例 宽/高
					double oldImgTran = imageWideth / imageHeight;
					// 新图要求比例
					double newImgTran = newImgWideth / newImgHeight;
					if (oldImgTran > newImgTran) {
						// 重新计算高
						changeToHeight = newImgWideth * imageHeight / imageWideth;
						changeToWideth = newImgWideth;
					} else {
						// 重新计算宽
						changeToHeight = newImgHeight;
						changeToWideth = newImgHeight * imageWideth / imageHeight;
					}
				}else{
					// 重新计算高 固定宽度 高度等比例
					changeToHeight = newImgWideth * imageHeight / imageWideth;
					changeToWideth = newImgWideth;
				}
			}
		}else{
			changeToHeight = newImgHeight= getHeight(image);
			changeToWideth = newImgWideth= getWidth(image);
		}
		ResampleOp resampleOp = new ResampleOp((int)changeToWideth, (int)changeToHeight);
		BufferedImage tag = resampleOp.filter(image, null);
		return tag;

	}

}


