package zincfish.zincwidget;

import javax.microedition.lcdui.game.Sprite;
import net.Net;
import net.NetController;
import screen.BrowserScreen;
import utils.ArrayList;
import utils.DrawUtils;
import utils.ImageUtils;
import zincfish.zinccss.model.Alignment;
import zincfish.zinccss.model.Insets;
import zincfish.zinccss.model.Metrics;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSImageDOM;
import zincfish.zinclib.DOMLib;
import zincfish.zincscript.ZSException;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.coreservice.utils.CSDevice;
import config.Config;
import config.Resources;

/**
 * <code>SNSImageComponent</code> 是界面上的图片元素
 * 
 * @author Jarod Yv
 */
public class SNSImageComponent extends AbstractSNSComponent {
	private static final int LOADING_SIZE = 16;// 载入动画的尺寸
	private static final int BG_STEP = 3;// 图片载入时拉伸的步长
	private static final int BG_COLOR = 0x434343;// 背景色

	/** 正在下载图片 */
	public static final byte IMAGE_LOADING = 0x01;
	/** 图片加载成功 */
	public static final byte IMAGE_OK = 0x02;
	/** 图片加载失败 */
	public static final byte IMAGE_FAILED = 0x03;
	/** 图片下载完成 */
	public static final byte IMAGE_LOADED = 0x04;
	/**
	 * 组件当前的状态
	 * 
	 * @see {@link IMAGE_LOADING}
	 * @see {@link IMAGE_OK}
	 * @see {@link IMAGE_FAILED}
	 * @see {@link IMAGE_LOADED}
	 */
	public byte status = IMAGE_LOADING;

	private CRImage image = null;// 图片
	private CRImage loading = null;// 装载动画
	private CRImage corner = null;// 背景的圆角

	private int offest = 0;// 加载动画的偏移
	private int spriteX = 0;
	private int spriteY = 0;
	private int imageWidth = 0;// 图片宽度
	private int imageHeight = 0;// 图片高度
	private int imageBGWidth = 0;// 图片背景宽度
	private int imageBGHeight = 0;// 图片背景高度

	private int scaleW = -1; // 缩放后的图片宽度
	private int scaleH = -1; // 缩放后的图片高度

	public void init(AbstractSNSDOM dom) {
		super.init(dom);
		SNSImageDOM imageDOM = (SNSImageDOM) this.dom;
		setSrc(imageDOM.src);
		if (image == null) {
			imageBGWidth = LOADING_SIZE;
			imageBGHeight = LOADING_SIZE;
			if (imageDOM.alt != null) {
				imageBGWidth = Config.PLAIN_SMALL_FONT
						.stringWidth(imageDOM.alt);
				imageBGWidth = imageBGWidth < LOADING_SIZE ? LOADING_SIZE
						: imageBGWidth;
				imageBGHeight += Config.PLAIN_SMALL_FONT.getHeight()
						+ LOADING_SIZE;
			}
		}
		imageDOM = null;
		corner = Resources.getInstance().getImageCorner();
	}

	public Metrics getPreferredSize(int preferredWidth, int nextLineWidth) {
		Metrics metrics = null;
		if (needToComputePreferredSize(preferredWidth)) {
			metrics = super.getPreferredSize(preferredWidth, nextLineWidth);
			Metrics minSize = dom.getStyleSet().getMinSize();
			if (image == null) {
				metrics.width += Math.max(minSize.width, imageBGWidth);
				metrics.height += Math.max(minSize.height, imageBGHeight);
			} else {
				metrics.width += Math.max(minSize.width, image.getWidth());
				metrics.height += Math.max(minSize.height, image.getHeight());
			}
		} else {
			metrics = getCachedMetrics();
		}
		return metrics;
	}

	protected void layout() {
		StyleSet style = dom.getStyleSet();
		Insets insets = style.getInsets();
		spriteX = insets.left;
		spriteY = insets.top;
		Alignment alignment = style.getAlign();
		if (alignment != null) {
			boolean rotate = isRotate();
			spriteX += alignment.alignX(
					getWidth() - insets.left - insets.right,
					rotate ? imageBGHeight : imageBGWidth);
			spriteY += alignment.alignY(getHeight() - insets.top
					- insets.bottom, rotate ? imageBGWidth : imageBGHeight);
		}
		insets = null;
		alignment = null;
		style = null;
	}

	private void setSrc(String src) {
		if (src == null) {// 获取默认图片
			// TODO 载入默认图片image = Resources.getInstance().getDefault_album();
			status = IMAGE_FAILED;
		} else {
			if (src.startsWith("http")) {// 网络图片
				if (loading == null)// 获取载入动画的图片
					loading = Resources.getInstance().getLoadingImage();
				status = IMAGE_LOADING;
				String imgKey = "GetImg_" + this.toString();
				Net.getInstance().addRequest(src, imgKey, false, false, true);
				if (NetController.getInstance().netImgTable != null) {
					NetController.getInstance().netImgTable.put(imgKey, this);
				}
				imgKey = null;
			} else {// 本地图片
				image = CRImage.loadFromResource(src);
				resizeImage();
				status = IMAGE_OK;
			}
		}
	}

	/**
	 * 把从网络加载的图片设置到组件里面
	 * 
	 * @param img
	 */
	public void setNetImgOK(CRImage img) {
		this.image = img;
		resizeImage();
		status = IMAGE_LOADED;
	}

	/**
	 * 对图片进行缩放
	 */
	private void resizeImage() {
		if (image != null) {
			if (scaleW != -1 || scaleH != -1)
				image = ImageUtils.resizeImage(image, scaleW == -1 ? image
						.getWidth() : scaleW, scaleH == -1 ? image.getHeight()
						: scaleH);
			boolean rotation = isRotate();
			imageWidth = rotation ? image.getHeight() : image.getWidth();
			imageHeight = rotation ? image.getWidth() : image.getHeight();
		}
	}

	/**
	 * 设置下载图片失败
	 */
	public void setNetImgFail() {
		status = IMAGE_FAILED;
	}

	public CRImage getImg() {
		return image;
	}

	public void init(AbstractSNSDOM dom, int w, int h) {
		scaleW = w;
		scaleH = h;
		init(dom);
	}

	protected void paintImpl(CRGraphics g) {
		if (status == IMAGE_LOADING || status == IMAGE_LOADED) {// 处于加载状态，则绘制加载动画
			if (status == IMAGE_LOADED && image != null) {// 图片显示动画
				imageBGWidth = imageBGWidth < imageWidth ? imageBGWidth
						+ BG_STEP : imageWidth;
				imageBGHeight = imageBGHeight < imageHeight ? imageBGHeight
						+ BG_STEP : imageHeight;
				if (imageBGWidth >= image.getWidth()
						&& imageBGHeight >= image.getHeight()) {
					status = IMAGE_OK;
					// 重新排版
					((DOMLib) DOMLib.getInstance())._zsdRefresh(null);
				}
			}
			// 绘制背景
			drawBG(g);
			// 绘制alt
			if (((SNSImageDOM) dom).alt != null) {
				g.setColor(0xFFFFFF);
				Config.PLAIN_SMALL_FONT.drawString(g, ((SNSImageDOM) dom).alt,
						spriteX
								+ (imageBGWidth - Config.PLAIN_SMALL_FONT
										.stringWidth(((SNSImageDOM) dom).alt))
								/ 2, spriteY + 2, 20);
			}
			// 绘制载入动画
			int loadingX = spriteX + (imageBGWidth - LOADING_SIZE) / 2;
			int loadingY = spriteY
					+ (((SNSImageDOM) dom).alt == null ? 0
							: (Config.PLAIN_SMALL_FONT.getHeight() + 4));

			int cX = g.getClipX();
			int cY = g.getClipY();
			int cW = g.getClipWidth();
			int cH = g.getClipHeight();
			offest += LOADING_SIZE;
			if (offest >= 192)
				offest = 0;

			if (loading != null) {
				g = DrawUtils.intersectG(g, loadingX, loadingY, LOADING_SIZE,
						LOADING_SIZE);
				loading.draw(g, loadingX - offest, loadingY, 20);
				g.setClip(cX, cY, cW, cH);
			}
		} else if (status == IMAGE_OK) {// 图片载入成功，绘制图片
			if (image != null) {
				image.draw(g, iWidth / 2, iHeight / 2, 3);
				if (dom.name != null) {
					if (isFocused) {
						g.setColor(0x2A00FF);
					} else {
						g.setColor(DrawUtils.iTextColor);
					}
					Config.PLAIN_SMALL_FONT.drawString(g, dom.name,
							(iWidth - Config.PLAIN_SMALL_FONT
									.stringWidth(dom.name)) / 2, image
									.getHeight(), DrawUtils.TOP_LEFT);
					if (dom.onClick != null) { // 绘制下划线
						g.drawLine((iWidth - Config.PLAIN_SMALL_FONT
								.stringWidth(dom.name)) / 2, image.getHeight()
								+ Config.PLAIN_SMALL_FONT.getHeight(),
								(iWidth + Config.PLAIN_SMALL_FONT
										.stringWidth(dom.name)) / 2, image
										.getHeight()
										+ Config.PLAIN_SMALL_FONT.getHeight());
					}
				}
			}
		}
		// 绘制左右箭头
		// if (canFocus && isFocused) {
		// g.drawRect(0, 0, iWidth, iHeight);
		// if (prevImageURL != null) {
		// Resources.getInstance().getBigRightArrow().draw(g, 4,
		// iHeight / 2, CRImage.FLIP_MIRROR, 6);
		// }
		// if (nextImageURL != null) {
		// Resources.getInstance().getBigRightArrow().draw(g, iWidth - 4,
		// iHeight / 2, 10);
		// }
		// }
	}

	public boolean animate() {
		if (status == IMAGE_LOADING || status == IMAGE_LOADED) {
			return true;
		}
		return false;
	}

	/**
	 * 绘制背景
	 * 
	 * @param g
	 *            图像上下文
	 */
	private void drawBG(CRGraphics g) {
		g.setColor(BG_COLOR);
		g.fillRect(spriteX + 2, spriteY + 2, imageBGWidth - 4,
				imageBGHeight - 4);
		corner.draw(g, spriteX, spriteY, 20);
		corner
				.draw(g, imageBGWidth - spriteX, spriteY, CRImage.FLIP_MIRROR,
						24);
		corner.draw(g, spriteX, imageBGHeight - spriteY, CRImage.FLIP_ROT270,
				36);
		corner.draw(g, imageBGWidth - spriteX, imageBGHeight - spriteY,
				CRImage.FLIP_ROT180, 40);
		g.fillRect(spriteX + 3, spriteY, imageBGWidth - 6, 2);
		g.fillRect(spriteX + 3, imageBGHeight - spriteY - 2, imageBGWidth - 6,
				2);
		g.fillRect(spriteX, spriteY + 3, 2, imageBGHeight - 6);
		g.fillRect(imageBGWidth - spriteX - 2, spriteY + 3, 2,
				imageBGHeight - 6);
	}

	/**
	 * 设置图片
	 * 
	 * @param image
	 *            图片
	 */
	public void setImage(CRImage image) {
		if (image == null) {
			this.image = null;
			status = IMAGE_FAILED;
		} else {
			this.image = image;
			resizeImage();
			status = IMAGE_OK;
		}
	}

	public void setMotion(int motionX, int motionY) {

	}

	public boolean keyPressed(int keyCode) {
		int keyAction = CSDevice.getGameAction(keyCode);
		switch (keyAction) {
		case CSDevice.KEY_LEFT:
		case CSDevice.KEY_RIGHT:
			SNSImageDOM imageDOM = (SNSImageDOM) dom;
			if (imageDOM.prevImageURL == null || imageDOM.nextImageURL == null) {
				return false;
			} else {
				ArrayList para = null;
				if (keyAction == CSDevice.KEY_LEFT
						&& imageDOM.prevImageURL != null) {
					para = new ArrayList(1);
					para.add(imageDOM.prevImageURL);
				} else if (keyAction == CSDevice.KEY_RIGHT
						&& imageDOM.nextImageURL != null) {
					para = new ArrayList(1);
					para.add(imageDOM.nextImageURL);
				}
				imageDOM = null;
				// 请求下一张图片
				if (para != null) {
					try {
						BrowserScreen.getInstance().getZinc().callFunction(
								"_zssSwitch", para);
					} catch (ZSException e) {
						e.printStackTrace();
					}
					para = null;
				}
				return true;
			}
		default:
			return false;
		}
	}

	public boolean keyReleased(int keyCode) {
		return false;
	}

	public boolean keyRepeated(int keyCode) {
		return false;
	}

	public void release() {
		loading = null;
		corner = null;
		image = null;
		super.release();
	}

	/**
	 * 判断图片是否被旋转，使宽高交换
	 * 
	 * @return 如果组件通过<code>transform</code>属性旋转了图片，令图片的宽高交换则返回<code>true</code>
	 */
	private boolean isRotate() {
		int rotation = ((SNSImageDOM) dom).rotation;
		return rotation == Sprite.TRANS_ROT90
				|| rotation == Sprite.TRANS_ROT270
				|| rotation == Sprite.TRANS_MIRROR_ROT90
				|| rotation == Sprite.TRANS_MIRROR_ROT270;
	}

}
