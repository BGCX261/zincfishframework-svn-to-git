package zincfish.zincwidget.focus;

import screen.BrowserScreen;

import com.mediawoz.akebono.coreservice.utils.CSDevice;
import com.mediawoz.akebono.ui.IKeyInputHandler;
import zincfish.zinccss.model.Alignment;
import zincfish.zincscript.ZincScript;
import zincfish.zincwidget.AbstractSNSComponent;
import zincfish.zincwidget.SNSBodyComponent;

/**
 * <code>FocusManager</code>对View Tree的焦点进行统一管理
 * 
 * @author Jarod Yv
 */
public class FocusManager implements IKeyInputHandler {
	/** {@link FocusManager}的唯一实例 */
	private static final FocusManager instance = new FocusManager();

	/** FocusManager负责处理焦点的View Tree */
	private AbstractSNSComponent root = null;

	/** 当前获得焦点的组件 */
	private AbstractSNSComponent focusedComponent;

	/** 标示焦点切换是否可循环 */
	private boolean isLoop = false;

	/**
	 * 获取{@link FocusManager}
	 * 
	 * @return {@link FocusManager}的唯一实例
	 */
	public static FocusManager getFocusManager() {
		return instance;
	}

	/**
	 * 获取{@link FocusManager}
	 * 
	 * @param rootWidget
	 *            需要焦点管理的View Tree
	 * @return {@link FocusManager}的唯一实例
	 */
	public static FocusManager getFocusManager(AbstractSNSComponent rootWidget) {
		instance.setRootWidget(rootWidget);
		return instance;
	}

	/**
	 * 获取{@link FocusManager}
	 * 
	 * @param root
	 *            需要焦点管理的View Tree
	 * @param loop
	 *            是否循环查找焦点
	 * @return {@link FocusManager}的唯一实例
	 */
	public static FocusManager getFocusManager(AbstractSNSComponent root,
			boolean loop) {
		instance.setRootWidget(root);
		instance.setLoop(loop);
		return instance;
	}

	/**
	 * 是否循环获得焦点
	 * 
	 * @return {@link #isLoop}
	 */
	public boolean isLoop() {
		return isLoop;
	}

	/**
	 * 设置是否循环获得焦点
	 * 
	 * @param loop
	 *            是否循环获得焦点
	 */
	public void setLoop(boolean loop) {
		this.isLoop = loop;
	}

	/**
	 * 设置需要管理焦点的View Tree
	 * 
	 * @param root
	 *            需要管理焦点的View Tree根节点
	 */
	public void setRootWidget(AbstractSNSComponent root) {
		this.root = root;
	}

	/**
	 * 返回当前获得焦点的组件.
	 * 
	 * @return {@link #focusedComponent}
	 */
	public AbstractSNSComponent getFocusedWidget() {
		return focusedComponent;
	}

	/**
	 * 让传入的组件获得焦点
	 * 
	 * @param component
	 *            即将获得焦点的组件
	 */
	public void requestFocus(AbstractSNSComponent component) {
		if (focusedComponent != component) {
			// 当前组件失去焦点
			if (focusedComponent != null) {
				focusedComponent.setFocus(false);
			}
			// 切换获得焦点的组件
			focusedComponent = component;
			// 当前组件获得焦点
			if (focusedComponent != null) {
				focusedComponent.setFocus(true);
			}
		}
	}

	/**
	 * 让焦点落在第一个能获得焦点的组件上
	 */
	public void requestFirstFocus() {
		requestFocus(null);
		requestOtherFocus(true, null);
	}

	/**
	 * 让焦点落在最后一个能获得焦点的组件上
	 */
	public void requestLastFocus() {
		requestFocus(null);
		requestOtherFocus(false, null);
	}

	/**
	 * 从<code>startComponent</code>开始，寻找上一个或下一个焦点。
	 * 
	 * @param startComponent
	 *            当前获得焦点的组件
	 * @param forward
	 *            <code>true</code>-向下搜索,<code>false</code>-向上搜索
	 * @param direction
	 *            搜索的方向-上,下,左,右四个方向；若为null则整棵树搜索
	 * @param loopCount
	 *            循环次数
	 */
	private void requestOtherFocus(AbstractSNSComponent startComponent,
			boolean forward, Alignment direction, int loopCount) {
		if (loopCount > 1) {// 强制只能循环搜索一次
			return;
		}
		AbstractSNSComponent otherFocusComponent = null;
		try {
			otherFocusComponent = ((startComponent == null) ? root
					: startComponent).getOtherFocus(root, startComponent,
					null, forward, direction, true, true, true);
		} catch (FocusException e) {
			otherFocusComponent = e.getFocusComponent();
			e = null;
		}
		if (otherFocusComponent != null) {
			// 处理滚屏
			if (direction != null)
				((SNSBodyComponent) root).scroll(otherFocusComponent);
			requestFocus(otherFocusComponent);
		} else if (!isLoop && focusedComponent != null) {
			// 处理跳屏
			((SNSBodyComponent) root).handleEged(forward);
		} else if (isLoop) {
			requestFocus(null);
			requestOtherFocus(null, forward, direction, ++loopCount);
		}
		otherFocusComponent = null;
	}

	/**
	 * 从<code>startComponent</code>开始，寻找上一个或下一个焦点。
	 * 
	 * @param startComponent
	 *            当前获得焦点的组件
	 * @param direction
	 *            搜索的方向-上,下,左,右四个方向；若为null则整棵树搜索
	 */
	public void requestOtherFocus(AbstractSNSComponent startComponent,
			boolean forward, Alignment direction) {
		requestOtherFocus(startComponent, forward, direction, 0);
	}

	/**
	 * 默认从当前获得焦点的组件开始，沿着<code>direction</code>方向搜索下一个获得焦点的组件
	 * 
	 * @param direction
	 *            搜索的方向-上,下,左,右四个方向；若为null则整棵树搜索
	 */
	public void requestOtherFocus(boolean forward, Alignment direction) {
		requestOtherFocus(focusedComponent, forward, direction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mediawoz.akebono.ui.IKeyInputHandler#keyPressed(int)
	 */
	public boolean keyPressed(int iKeyCode) {
		// 首先给当前获得组件的焦点以机会，让他处理自身内部的按键响应
		if (focusedComponent != null) {
			if (focusedComponent.keyPressed(iKeyCode)) {
				return true;
			}
		}
		if(iKeyCode == CSDevice.NK_RSOFT ){
			BrowserScreen.getInstance().getBuffer().prev();
			return true;
		}
		// 然后切换焦点
		iKeyCode = CSDevice.getGameAction(iKeyCode);
		switch (iKeyCode) {
		case CSDevice.KEY_FIRE:
			ZincScript.getZincScript().executeDynamicScript(
					focusedComponent.getDom().onClick);
			return true;
		case CSDevice.KEY_UP:
			requestOtherFocus(false, Alignment.TOP);
			return true;
		case CSDevice.KEY_LEFT:
			requestOtherFocus(false, Alignment.LEFT);
			return true;
		case CSDevice.KEY_DOWN:
			requestOtherFocus(true, Alignment.BOTTOM);
			return true;
		case CSDevice.KEY_RIGHT:
			requestOtherFocus(true, Alignment.RIGHT);
			return true;
		}

		return false;
	}

	public boolean keyReleased(int iKeyCode) {
		return false;
	}

	public boolean keyRepeated(int iKeyCode) {
		return false;
	}
}
