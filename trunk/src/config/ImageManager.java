package config;

import java.io.InputStream;
import java.util.Hashtable;
import com.mediawoz.akebono.corerenderer.CRImage;

/**
 * <code>ImageManager</code>用于统一管理图片资源
 * 
 * @author Jarod Yv
 * @since Fingerling
 */
public final class ImageManager {
	/** 唯一实例 */
	public static final ImageManager instance = new ImageManager();

	// 图片缓存
	private final Hashtable images = new Hashtable();

	/**
	 * 根据图片的路径获取图片对象
	 * 
	 * @param path
	 *            图片路径
	 * @return 图片对象
	 */
	public CRImage getImage(String path) {
		// 验证路径
		if ((path == null) || (path.length() == 0)) {
			return null;
		}

		// 如果图片已经生成，则直接返回图片对象
		if (images.containsKey(path)) {
			return (CRImage) images.get(path);
		}

		// 加载图片，生成图片对象
		CRImage image = null;
		try {
			image = CRImage.loadFromResource(path);
		} catch (Exception ioe) {
		}
		if (image == null) {
			try {
				InputStream is = getClass().getResourceAsStream(path);
				if (is != null) {
					image = CRImage.createImage(is);
					is.close();
					is = null;
				}
			} catch (Exception e) {
			}
		}

		// 将图片加入缓存
		if (image != null) {
			images.put(path, image);
		} else {
			System.out.println("Error loading : " + path);
		}

		return image;
	}

	/**
	 * 释放指定图片对象
	 * 
	 * @param path
	 *            图片路径
	 */
	public void releaseImage(String path) {
		if (images.containsKey(path)) {
			images.remove(path);
		}
	}

	/**
	 * 释放所有图片
	 */
	public void releaseAll() {
		images.clear();
		// System.gc();
	}
}
