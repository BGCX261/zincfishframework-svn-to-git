package zincfish.zincwidget;

import com.mediawoz.akebono.coreservice.utils.CSDevice;

import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSCheckBoxDOM;
import zincfish.zincscript.ZincScript;

/**
 * <code>SNSCheckBoxComponent</code> 是界面上的复选框/单选框组件.<br>
 * 逻辑上它继承自{@link SNSTextComponent}, 可以看做是特殊的文字组件，只是在文字组件上加入了选择控制状态.<br>
 * 可以使用CSS的<strong>selected</strong>和<strong>hover</strong>伪类来实现选中和聚焦的样式.
 * 
 * @author Jarod Yv
 */
public class SNSCheckBoxComponent extends SNSTextComponent {

	/* (non-Javadoc)
	 * @see zincfish.zincwidget.SNSTextComponent#init(zincfish.zincdom.AbstractSNSDOM)
	 */
	public void init(AbstractSNSDOM dom) {
		this.dom = dom;
		setVisible(dom.isVisible);
		SNSCheckBoxDOM checkBoxDOM = (SNSCheckBoxDOM) this.dom;
		isSelected = checkBoxDOM.isSelected;
		wrap = checkBoxDOM.wrap;
		displayedString = checkBoxDOM.text;
		checkBoxDOM = null;
	}

	/* (non-Javadoc)
	 * @see zincfish.zincwidget.AbstractSNSComponent#keyPressed(int)
	 */
	public boolean keyPressed(int keyCode) {
		int keyAction = CSDevice.getGameAction(keyCode);
		switch (keyAction) {
		case CSDevice.KEY_FIRE:
			isSelected = !isSelected;
			dom.invalidateStylePropertiesCache(true);// 当选中状态发生变化时，需要重新绑定样式
			if (dom.onClick != null)
				ZincScript.getZincScript().executeDynamicScript(dom.onClick);
			return true;
		}
		return super.keyPressed(keyCode);
	}
}
