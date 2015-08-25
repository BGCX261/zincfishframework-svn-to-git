/**
 * License  3G门户版权所有 2008-2009
 */
package utils;

import com.mediawoz.akebono.corerenderer.CRImage;

/**
 * @作者 江威
 * @Email weijiang8410@163.com/jiang-wei@3g.net.cn
 */
public class ImageUtils {

	public static long runTime;
	
	public static CRImage resizeImage(CRImage src, int destW, int destH) {
		runTime = System.currentTimeMillis();
		int srcW = src.getWidth();  // 原图片宽度
		int srcH = src.getHeight(); // 原图片高度
		
		int[] destPixels = new int[destW * destH];
		int[] srcPixels = new int[srcW * srcH]; // 
		
		src.getARGB(srcPixels, 0, srcW, 0, 0, srcW, srcH);
		
		for (int destY = 0; destY < destH; ++destY) {
			for (int destX = 0; destX < destW; ++destX) {
				int srcX = (destX * srcW) / destW;
				int srcY = (destY * srcH) / destH;
				destPixels[destX + destY * destW] = srcPixels[srcX + srcY
						* srcW];
			}
		}
		CRImage destImg = CRImage.createImage(destPixels, destW, destH, 0, true);
		runTime = System.currentTimeMillis() - runTime;
		System.out.println("创建图片消耗时间:" + runTime);
		return destImg;
	}
}
