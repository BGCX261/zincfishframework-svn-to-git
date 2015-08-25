package zincfish.zincdom;

import zincfish.zinccss.ICSSConstants;
import zincfish.zinccss.model.Alignment;
import data.MenuData;

/**
 * <code>SNSBodyDOM</code> 代表根据&lt;body&gt;标签解析出来的DOM节点
 * 
 * @author Jarod Yv
 */
public class SNSBodyDOM extends SNSDivDOM {

	private AbstractSNSDOM currentDOM = null;// 缓存当前选中的节点，用于从上一页返回时光标能够继续停在上次停留的组件上
	private int offsetY = 0;// 页面偏移
	private MenuData leftMenu = null;
	private MenuData rightMenu = null;

	public AbstractSNSDOM bindDOMNorth = null;
	public AbstractSNSDOM bindDOMSouth = null;
	public AbstractSNSDOM bindDOMWest = null;
	public AbstractSNSDOM bindDOMEast = null;

	public SNSBodyDOM() {
		this.type = TYPE_BODY;
		canFocus = false;
	}

	public void addBind(AbstractSNSDOM dom) {
		switch (dom.bind) {
		case AbstractSNSDOM.BIND_NORTH:
			bindDOMNorth = dom;
			break;
		case AbstractSNSDOM.BIND_EAST:
			bindDOMEast = dom;
			break;
		case AbstractSNSDOM.BIND_SOUTH:
			bindDOMSouth = dom;
			break;
		case AbstractSNSDOM.BIND_WEST:
			bindDOMWest = dom;
			break;
		}
	}

	/**
	 * @return the offsetY
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * @param offsetY
	 *            the offsetY to set
	 */
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	/**
	 * @return the currentDOM
	 */
	public AbstractSNSDOM getCurrentDOM() {
		return currentDOM;
	}

	/**
	 * @param currentDOM
	 *            the currentDOM to set
	 */
	public void setCurrentDOM(AbstractSNSDOM currentDOM) {
		this.currentDOM = currentDOM;
	}

	public void setMenu(MenuData menu) {
		if (menu == null)
			return;
		switch (menu.type) {
		case MenuData.LEFT_MENU:
			this.leftMenu = menu;
			break;
		case MenuData.RIGHT_MENU:
			this.rightMenu = menu;
			break;
		default:
			this.leftMenu = menu;
			break;
		}
	}

	public MenuData getMenu(int type) {
		switch (type) {
		case MenuData.LEFT_MENU:
			return leftMenu;
		case MenuData.RIGHT_MENU:
			return rightMenu;
		default:
			return leftMenu;
		}
	}

	public Object getDefaultStylePropertyValue(String name) {
		if (ICSSConstants.ALIGN_STYLE_PROPERTY.equals(name)) {
			return Alignment.FILL;
		}
		return super.getDefaultStylePropertyValue(name);
	}

	public void setInvalidated(boolean isInvalidated) {
		super.setInvalidated(isInvalidated);
		if (bindDOMNorth != null)
			bindDOMNorth.setInvalidated(isInvalidated);
		if (bindDOMSouth != null)
			bindDOMSouth.setInvalidated(isInvalidated);
		if (bindDOMWest != null)
			bindDOMWest.setInvalidated(isInvalidated);
		if (bindDOMEast != null)
			bindDOMEast.setInvalidated(isInvalidated);
	}
}
