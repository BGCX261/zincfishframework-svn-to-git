package screen;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import log.Log;
import ui.combo.DatePickerPanel;
import ui.combo.SingleSelectedPanel;
import ui.component.ActiveButtonComponent;
import utils.DOMUtil;
import zincfish.zincdom.SNSUnitDOM;
import zincfish.zincparser.zmlparser.ZMLParser;
import zincfish.zincscript.ZincScript;
import zincfish.zincwidget.SNSUnitComponent;
import zincfish.zincwidget.focus.FocusManager;
import com.mediawoz.akebono.components.window.CMessageBox;
import com.mediawoz.akebono.coreanimation.CAAnimator;
import com.mediawoz.akebono.corefilter.CFMotion;
import com.mediawoz.akebono.corerenderer.CRDisplay;
import com.mediawoz.akebono.corerenderer.CRImage;
import com.mediawoz.akebono.coreservice.utils.CSDevice;
import com.mediawoz.akebono.events.EAnimationEventListener;
import com.mediawoz.akebono.filters.motion.FMLinear;
import com.mediawoz.akebono.ui.UComponent;
import com.mediawoz.akebono.ui.UScreen;
import data.IDOMChangeListener;
import data.UnitBuffer;

/**
 * <code>BrowserScreen</code>
 * 
 * @author Jarod Yv
 */
public class BrowserScreen extends UScreen implements EAnimationEventListener,
		IDOMChangeListener {

	/* 当前Screen的唯一实例 */
	private static BrowserScreen instance = null;
	/* 当前的Unit */
	private SNSUnitComponent currentUnit = null;
	/* 上一个的Unit */
	private SNSUnitComponent prevUnit = null;
	/* 新载入的Unit */
	private SNSUnitComponent newUnit = null;
	/* zml解析引擎 */
	private ZMLParser parse = null;
	/* ZincScript解释执行引擎 */
	private ZincScript zinc = null;
	/* 页面缓存 */
	private UnitBuffer buffer = null;
	/* 左软键菜单 */
	// private MenuContainer menu = null;
	/* 左软键按钮 */
	private ActiveButtonComponent leftMenuButton = null;
	/* 右软件按钮 */
	private ActiveButtonComponent rightMenuButton = null;
	/* 页面切换的动画 */
	private CFMotion motion = null;
	/* 下拉框列表 */
	private SingleSelectedPanel dropDownMenu = null;

	private FocusManager focusManager = FocusManager.getFocusManager();;

	private static Log log = Log.getLog("BrowserScreen");

	/**
	 * 获取唯一的实例
	 * 
	 * @return 主浏览页面唯一实例{@link BrowserScreen}
	 */
	public static BrowserScreen getInstance() {
		if (instance == null)
			instance = new BrowserScreen();
		return instance;
	}

	/*
	 * 构造函数
	 */
	private BrowserScreen() {
		super(30);
		init();
		initMenu();
		buffer = UnitBuffer.getInstance();
		buffer.setDomChangeListener(this);
		parse = ZMLParser.getSNSParser();
		zinc = ZincScript.getZincScript();
	}

	/*
	 * 初始化背景
	 */
	private void init() {
		setBgColor(0x2d68ad);// 蓝
		// setBgColor(0xad2d98);// 红
		// FFColorOverlay colorOverlay = new FFColorOverlay(
		// FFColorOverlay.MODE_OVERLAY, 0xad2d98, 70);
		// CRImage bg = CRImage.loadFromResource("/img/bg.png");
		// colorOverlay.setImage(bg);
		// bg = colorOverlay.getTransformedImage();
		// colorOverlay = null;
		// setBgImage(bg, BGPOSMODE_CENTER);
		// bg = null;
		CRImage bgImage = CRImage.loadFromResource("/img/light.png");
		CRImage fgImage = CRImage.loadFromResource("/img/leftmenu.png");
		leftMenuButton = new ActiveButtonComponent(1, 0, getHeight()
				- bgImage.getHeight(), bgImage.getWidth(), bgImage.getHeight(),
				bgImage, fgImage);
		addComponent(leftMenuButton);
		fgImage = null;
		fgImage = CRImage.loadFromResource("/img/rightmenu.png");
		rightMenuButton = new ActiveButtonComponent(1, getWidth()
				- bgImage.getWidth(), getHeight() - bgImage.getHeight(),
				bgImage.getWidth(), bgImage.getHeight(), bgImage, fgImage);
		addComponent(rightMenuButton);
		fgImage = null;
		bgImage = null;

		// NetController netController = NetController.getInstance();
		// netController.start();
	}

	/*
	 * 初始化左软键菜单
	 */
	private void initMenu() {
		// 添加背景图片组件
		// CRImage imgTL = CRImage.loadFromResource("/menu/tl.png");
		// CRImage imgBL = CRImage.loadFromResource("/menu/bl.png");
		// CRImage imgTR = CRImage.loadFromResource("/menu/tr.png");
		// CRImage imgBR = CRImage.loadFromResource("/menu/br.png");
		// CRImage upTrig = CRImage.loadFromResource("/menu/up.png");
		// CRImage downTrig = CRImage.loadFromResource("/menu/down.png");
		// menu = new MenuContainer(0, 0, getWidth(), getHeight(), null, this);
		// menu.setMenuImg(imgTL, imgBL, imgTR, imgBR, upTrig, downTrig);
		// this.addComponent(menu);
		// imgTL = null;
		// imgBL = null;
		// imgTR = null;
		// imgBR = null;
		// upTrig = null;
		// downTrig = null;
	}

	/**
	 * 载入页面数据
	 * 
	 * @param data
	 *            页面的数据流
	 * @param url
	 *            跳转的url
	 */
	public void loadUnit(byte[] data, String url) {
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		parseData(is, url);
		is = null;
	}

	/**
	 * 载入页面数据
	 * 
	 * @param path
	 *            页面的路径
	 * @param url
	 *            跳转的URL
	 */
	public void loadUnit(String path, String url) {
		// SNSUnitDOM root = FileUtils.readFromBuffer(url);
		// if (root == null) {
		InputStream is = this.getClass().getResourceAsStream(path);
		parseData(is, url);
		is = null;
		// }
	}

	/*
	 * 解析页面数据
	 * 
	 * @param is 页面数据流
	 * 
	 * @param url 跳转的URL
	 */
	private void parseData(InputStream is, String url) {
		try {
			parse.setInput(is, "UTF-8");// 载入页面数据
			parse.parse();// 解析
			SNSUnitDOM unit = parse.getResult();// 获取解析结果
			if (unit.onload == null)
				unit.onload = url;// 设置指定的跳转URL

			UnitBuffer.getInstance().addBuffer(unit);// 将新页面加入buffer
			unit = null;
		} catch (Exception e) {
			log.debug("parser exception:\n" + e.getMessage());
		} finally {
			try {
				parse.release();
				is.close();
			} catch (IOException e) {
			} finally {
				is = null;
			}
		}
	}

	/** 触摸屏版本 */
	public void pointerPressed(int iiX, int iiY) {
		if (iiX < 30 && iiY > (getHeight() - 26)) {
			keyPressed(CSDevice.NK_LSOFT);
		}
		if (iiX > getWidth() - 30 && iiY > (getHeight() - 26)) {
			keyPressed(CSDevice.NK_RSOFT);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.ui.UScreen#keyPressed(int)
	 */
	public void keyPressed(int iKeyCode) {
		if (iKeyCode == CSDevice.NK_LSOFT || iKeyCode == -21 /* moto v8 */) {
			leftMenuButton.setActive(true);
			rightMenuButton.setActive(false);
		} else if (iKeyCode == CSDevice.NK_RSOFT || iKeyCode == -22 /* moto v8 */) {
			// 移除日期弹出框
			if (DatePickerPanel.getInstance() != null) {
				if (indexOfComponent(DatePickerPanel.getInstance()) > -1) {
					removeComponent(DatePickerPanel.getInstance());
					return;
				}
			}
			rightMenuButton.setActive(true);
			leftMenuButton.setActive(false);
		}
		if (dropDownMenu != null) {
			if (dropDownMenu.keyPressed(iKeyCode)) {
				return;
			}
		}
		if (pop != null) {
			if (pop.keyPressed(iKeyCode)) {
				pop.dismiss();
				removeComponent(pop);
				pop = null;
				return;
			}
		}
		// if (menu.keyPressed(iKeyCode)) {
		// return;
		// }
		try {
			focusManager.keyPressed(iKeyCode);
			// AbstractSNSComponent body = currentUnit
			// .getChildComponent(currentUnit.getChildrenNum() - 1);
			// iKeyCode = CSDevice.getGameAction(iKeyCode);
			// if (iKeyCode == CSDevice.KEY_DOWN) {
			// body.offsetY += 10;
			// } else if (iKeyCode == CSDevice.KEY_UP) {
			// body.offsetY -= 10;
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.ui.UScreen#keyReleased(int)
	 */
	public void keyReleased(int iKeyCode) {
		if (iKeyCode == CSDevice.NK_LSOFT || iKeyCode == -21 /* moto v8 */) {
			leftMenuButton.setActive(false);
		} else if (iKeyCode == CSDevice.NK_RSOFT || iKeyCode == -22 /* moto v8 */) {
			rightMenuButton.setActive(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.IDOMChangeListener#updateView(boolean)
	 */
	public void updateView(boolean isNext) {
		SNSUnitDOM root = buffer.getCurrentBuffer();
		layout(root);
		if (newUnit == null) {
			log.debug("生成View Tree失败!");
			return;
		}
		// 加载另外一个Unit页面
		if (currentUnit != newUnit) {
			loadAnotherUnit(isNext);
			prevUnit = currentUnit;
			currentUnit = newUnit;
		} else {
			currentUnit = newUnit;
		}
		focusManager.setRootWidget(currentUnit.getChildComponent(currentUnit
				.getChildrenNum() - 1));
		focusManager.requestFirstFocus();// 重新获得焦点
		newUnit = null;
		// 更新菜单
		// updateMenu((SNSBodyDOM) currentUnit.currentBody.getDom());
		root = null;
		updateScreen();
	}

	/**
	 * 发起排版
	 * 
	 * @param root
	 *            DOM Tree的根节点
	 */
	public void layout(SNSUnitDOM root) {
		// 将DOM Tree转换成View Tree
		newUnit = (SNSUnitComponent) DOMUtil.DOMTree2ViewTree(root);
		newUnit.loadCSS();
		root.setComponent(newUnit);
		// 开始排版
		newUnit.setBounds(0, 0, getWidth(), getHeight());
		newUnit.loadScript();
	}

	/**
	 * 更新菜单
	 * 
	 * @param root
	 */
	// public void updateMenu(SNSBodyDOM body) {
	// this.removeComp(leftMenuButton);
	// this.removeComp(menu);
	// this.removeComp(rightMenuButton);
	// MenuData menuData = body.getMenu(MenuData.LEFT_MENU);
	// if (menuData != null) {
	// if (menuData.hasSubMemu()) {
	// menu.generateMenu(menuData);
	// if (menu.Root != null)
	// menu.setPosition(0, getHeight() - 20);
	// this.addComp(menu);
	// this.addComp(leftMenuButton);
	// }
	// }
	// menuData = null;
	// menuData = body.getMenu(MenuData.RIGHT_MENU);
	// }

	/**
	 * 准备页面切换的动画
	 * 
	 * @param isNext
	 *            表示是切换到前一页还是后一页
	 *            <ul>
	 *            <li><code>true</code> - 却换到后一页
	 *            <li><code>false</code> - 切换到前一页
	 *            </ul>
	 */
	private void loadAnotherUnit(boolean isNext) {
		newUnit.iX = isNext ? CRDisplay.getWidth() : -CRDisplay.getWidth();
		addComponent(newUnit); // 
		// newUnit.findFirstFocus();
		motion = new FMLinear(1, FMLinear.CACCEL, isNext ? CRDisplay.getWidth()
				: -CRDisplay.getWidth(), 0, 0, 0, 10, isNext ? 20 : -20, 0);
		motion.setAnimationEventListener(this);
		newUnit.attachAnimator(motion);
		if (currentUnit != null)
			currentUnit.attachAnimator(motion);
	}

	public void removeComp(UComponent com) {
		if (indexOfComponent(com) > -1) {
			removeComponent(com);
		}
	}

	public void addComp(UComponent com) {
		if (indexOfComponent(com) > -1) {
			removeComponent(com);
			addComponent(com);
		} else {
			addComponent(com);
		}
	}

	CMessageBox pop = null;

	public void showWarningPop(String msg) {
		pop = new CMessageBox(getWidth() * 4 / 5, getHeight() / 2,
				CMessageBox.TYPE_WARNING, CMessageBox.BUTTON_OK, msg, 0x000000,
				0xFFFFFF, "/messagebox/");
		this.addComponent(pop);
	}

	public void showQuestionPop(String msg) {
		pop = new CMessageBox(getWidth() * 4 / 5, getHeight() / 2,
				CMessageBox.TYPE_QUESTION, CMessageBox.BUTTON_RETRY
						| CMessageBox.BUTTON_CANCEL, msg, 0x000000, 0xFFFFFF,
				"/messagebox/");
		this.addComponent(pop);
		// pop.getSelectedButton(); TODO 处理retry
	}

	public void onFinish(CAAnimator animator) {
		if (prevUnit != null) {
			this.prevUnit.detachAnimator(motion);
			this.removeComponent(prevUnit);
			// this.currentUnit.release(); // 深度释放会报空指针异常
			this.prevUnit = null;
		}
		if (motion != null && currentUnit != null)
			currentUnit.detachAnimator(motion);
		prevUnit = null;
		motion = null;
		ZincScript.getZincScript().executeDynamicScript(
				((SNSUnitDOM) currentUnit.getDom()).onload);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mediawoz.akebono.events.EAnimationEventListener#onProgress(com.mediawoz
	 * .akebono.coreanimation.CAAnimator)
	 */
	public void onProgress(CAAnimator animator) {
		if (currentUnit != null)
			currentUnit.iX = motion.getCurX();
		if (prevUnit != null) {
			prevUnit.iX = motion.getCurX() > 0 ? -CRDisplay.getWidth() - 2
					+ motion.getCurX() : CRDisplay.getWidth() - 2
					+ motion.getCurX();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mediawoz.akebono.events.EAnimationEventListener#onStart(com.mediawoz
	 * .akebono.coreanimation.CAAnimator)
	 */
	public void onStart(CAAnimator animator) {
	}

	/**
	 * 获取页面缓存
	 * 
	 * @return {@link #buffer}
	 */
	public UnitBuffer getBuffer() {
		return buffer;
	}

	/**
	 * 获取ZincScript执行引擎
	 * 
	 * @return {@link #zinc}
	 */
	public ZincScript getZinc() {
		return zinc;
	}

	/**
	 * 显示下拉菜单
	 * 
	 * @param menu
	 */
	public void showDropDownMenu(SingleSelectedPanel menu) {
		if (dropDownMenu != null) {
			dropDownMenu.release();
			dropDownMenu = null;
		}
		dropDownMenu = menu;
		CFMotion motion = null;
		if (dropDownMenu.iY + dropDownMenu.getHeight() > getHeight()) {
			dropDownMenu.iY -= (dropDownMenu.getHeight() + 28);
			motion = new FMLinear(1, FMLinear.CSPEED, 0, dropDownMenu
					.getHeight(), 0, 0, 5, 0, 0);
		} else {
			motion = new FMLinear(1, FMLinear.CSPEED, 0, -dropDownMenu
					.getHeight(), 0, 0, 5, 0, 0);
		}
		dropDownMenu.setShowMotion(motion);
		motion = null;
		addComponent(dropDownMenu);
	}

	/**
	 * 隐藏下拉框菜单
	 */
	public void hideDropDownMenu() {
		if (dropDownMenu != null) {
			dropDownMenu.release();
			removeComponent(dropDownMenu);
			dropDownMenu = null;
		}
	}

	public SNSUnitComponent getCurrentUnit() {
		return currentUnit;
	}
}
