package ui.combo;

import screen.BrowserScreen;
import ui.component.ScrollComponent;
import zincfish.zincdom.SNSComboBoxDOM;
import com.mediawoz.akebono.corefilter.CFMotion;
import com.mediawoz.akebono.corerenderer.CRDisplay;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.coreservice.utils.CSDevice;
import com.mediawoz.akebono.filters.motion.FMLinear;
import config.Config;
import config.Resources;

/**
 * <code>SingleSelectedPanel</code>实现了单选的下拉菜单组件，主要用于下拉框组件的选项选择
 * 
 * @author Jarod Yv
 */
public class SingleSelectedPanel extends AbstractComboPanel {

	private static final int MAX_ITEM = 5;// 下拉列表的最大长度

	private static final int SPACE = 2;// 选项之间的间距

	private static final int ITEM_HEIGHT = Config.PLAIN_SMALL_FONT.getHeight()
			+ SPACE;// 选项高度

	private String[] options = null;// 选项列表

	private int currentIndex = 0;// 当前选中的项的索引

	private int offsetNum = 0;// 偏移的项数

	private int offsetY = 0;// 选项纵坐标偏移量

	private CFMotion motion = null;// 滚动的动画

	private int showY = 0;// 组件纵坐标偏移量

	private CFMotion showMotion = null;// 滚动的动画

	private ScrollComponent scroll = null;// 滚动条

	public SingleSelectedPanel(int ix, int iy, int width, int height,
			String[] options, SNSComboBoxDOM dom) {
		super(1, ix, iy, width, height);
		this.dom = dom;
		setCurrentIndex(this.dom.index);
		if (options != null) {
			setItems(options);
		}
	}

	protected void drawCurrentFrame(CRGraphics g) {
		g.setClip(0, 0, iWidth, iHeight);
		g.setColor(0xF8F8F8);
		g.fillRect(0, showY, iWidth, iHeight);
		g.setColor(0xB7BABC);
		g.drawRect(0, showY, iWidth, iHeight);
		if (options != null) {
			for (int i = 0; i < options.length; i++) {
				if (ITEM_HEIGHT * i + 2 - offsetY < iHeight
						|| ITEM_HEIGHT * (i + 1) + 2 - offsetY > 0) {
					g.setColor(0xB7BABC);
					if (i == currentIndex) {
						g.setColor(0xE1E1E1);
						g.fillRect(1, ITEM_HEIGHT * i + offsetY + showY,
								iWidth - 2, ITEM_HEIGHT);
						g.setColor(0x000000);
					}
					if (i == dom.index) {
						Resources.getInstance().getFormTick().draw(
								g,
								2,
								ITEM_HEIGHT * i + 2 + ITEM_HEIGHT / 2 + showY
										+ offsetY, 6);
					}
					Config.PLAIN_SMALL_FONT.drawString(g, options[i], 14,
							ITEM_HEIGHT * i + 2 + offsetY + showY, 20);
				}
			}
		}
		if (scroll != null) {
			scroll.paintCurrentFrame(g, scroll.iX, scroll.iY + showY);
		}
		g.setClip(0, 0, CRDisplay.getWidth(), CRDisplay.getHeight());
	}

	protected boolean animate() {
		boolean ret = false;
		if (motion != null) {
			if (motion.isFinished()) {
				detachAnimator(motion);
				motion = null;
			} else {
				offsetY = motion.getCurY();
			}
			ret = true;
		}
		if (showMotion != null) {
			if (showMotion.isFinished()) {
				detachAnimator(showMotion);
				showMotion = null;
				showY = 0;
			} else {
				showY = showMotion.getCurY();
			}
			if (scroll != null) {
				scroll.setVisible(false);
			}
			ret = true;
		}
		return ret;
	}

	/**
	 * 设置下拉菜单选项
	 * 
	 * @param items
	 *            菜单项
	 */
	public void setItems(String[] items) {
		this.options = items;
		if (items.length <= MAX_ITEM) {
			iHeight = ITEM_HEIGHT * items.length + 2;
		} else {
			iHeight = ITEM_HEIGHT * MAX_ITEM + 2;
			scroll = new ScrollComponent(iWidth - 7, SPACE, 7, iHeight - SPACE
					* 2);
			scroll.setContainerHeight((iHeight - SPACE * 2) * MAX_ITEM
					/ items.length);
			scroll.setVisible(false);
			scroll.iY -= offsetY * scroll.getHeight() / iHeight;
			addComponent(scroll);
		}
	}

	/**
	 * 设置当前选中的项的索引
	 * 
	 * @param currentIndex
	 *            当前选中项的索引
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
		if (currentIndex >= MAX_ITEM) {
			offsetNum = currentIndex - MAX_ITEM + 1;
			offsetY = -offsetNum * ITEM_HEIGHT;
		}
	}

	public boolean keyPressed(int iKeyCode) {
		int keyCode = CSDevice.getGameAction(iKeyCode);
		if (iKeyCode == CSDevice.NK_LSOFT) {
			keyCode = CSDevice.KEY_FIRE;
		} else if (iKeyCode == CSDevice.NK_LSOFT) {
			BrowserScreen.getInstance().hideDropDownMenu();
			return true;
		}
		switch (keyCode) {
		case CSDevice.KEY_FIRE:
			dom.index = currentIndex;
			BrowserScreen.getInstance().hideDropDownMenu();
			return true;
		case CSDevice.KEY_UP:
			currentIndex = --currentIndex < 0 ? 0 : currentIndex;
			if (currentIndex < offsetNum) {
				offsetNum--;
				setMotion(ITEM_HEIGHT);
			}
			return true;
		case CSDevice.KEY_DOWN:
			currentIndex = ++currentIndex >= options.length ? options.length - 1
					: currentIndex;
			if (currentIndex - offsetNum >= MAX_ITEM) {
				offsetNum++;
				setMotion(-ITEM_HEIGHT);
			}
			return true;
		default:
			return true;
		}
	}

	/*
	 * 设置动画
	 * 
	 * @param dist 滚动距离
	 */
	private void setMotion(int dist) {
		if (motion != null) {
			detachAnimator(motion);
			motion = null;
		}
		motion = new FMLinear(1, FMLinear.PULLBACK, 0, offsetY, 0, offsetY
				+ dist, 15, 0, dist / 3);
		if (scroll != null) {
			scroll.scroll(motion);
		}
		attachAnimator(motion);
	}

	public void setShowMotion(CFMotion motion) {
		this.showMotion = motion;
		attachAnimator(this.showMotion);
	}

	public boolean keyReleased(int iKeyCode) {
		return false;
	}

	public boolean keyRepeated(int arg0) {
		return false;
	}

	/**
	 * 释放资源
	 */
	public void release() {
		options = null;
		motion = null;
		showMotion = null;
		dom = null;
		if (scroll != null) {
			scroll.release();
			scroll = null;
		}
	}
}
