package data;

import utils.ArrayList;

public class MenuData {
	/** 左软键菜单 */
	public static final byte LEFT_MENU = 0x00;
	/** 右软件菜单 */
	public static final byte RIGHT_MENU = 0x01;
	/** 弹出菜单 */
	public static final byte POP_MENU = 0x02;

	/** 菜单的类型 */
	public int type = LEFT_MENU;
	/** 菜单ID */
	public String id;
	/** 菜单项显示的文字 */
	public String text = null;
	/** 点击后响应的函数 */
	public String onClick = null;
	/** 菜单项的功能码 */
	public int code = 0;

	private ArrayList childrenMenu = null;

	private MenuData father = null;

	public void addSubMenu(MenuData subMenu) {
		if (childrenMenu == null)
			childrenMenu = new ArrayList(5);
		childrenMenu.add(subMenu);
		subMenu.setFather(this);
	}

	public ArrayList getSubMemu() {
		return childrenMenu;
	}

	public void setFather(MenuData father) {
		this.father = father;
	}

	public MenuData getFather() {
		return father;
	}

	public boolean hasSubMemu() {
		return childrenMenu != null && childrenMenu.size() > 0;
	}
}
