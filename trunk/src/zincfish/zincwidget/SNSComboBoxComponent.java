package zincfish.zincwidget;

import com.mediawoz.akebono.coreservice.utils.CSDevice;
import zincfish.zinccss.model.Metrics;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSComboBoxDOM;
import zincfish.zincdom.SNSLabelDOM;

/**
 * <code>SNSComboBoxComponent</code>代表下拉框组件<br>
 * 逻辑上它继承自{@link SNSTextComponent}, 可以看做是特殊的文字组件，只要在文字周围画上背景和边框, 点击后弹出下拉框即可.<br>
 * <code>SNSComboBoxComponent</code>支持4中类型的下拉框, 分别是:单选下拉框、多选下拉框、日期选择框和文件选择框。
 * 
 * @author Jarod Yv
 */
public class SNSComboBoxComponent extends SNSTextComponent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincwidget.SNSTextComponent#init(zincfish.zincdom.AbstractSNSDOM
	 * )
	 */
	public void init(AbstractSNSDOM dom) {
		this.dom = dom;
		setVisible(dom.isVisible);
		SNSComboBoxDOM comboBoxDOM = (SNSComboBoxDOM) this.dom;
		wrap = SNSLabelDOM.WRAP_OMIT;
		displayedString = comboBoxDOM.text;
		comboBoxDOM = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zincfish.zincwidget.AbstractSNSComponent#getPreferredSize(int, int)
	 */
	public Metrics getPreferredSize(int preferredWidth, int nextLineWidth) {
		Metrics metrics = null;
		if (needToComputePreferredSize(preferredWidth)) {
			metrics = super.getPreferredSize(preferredWidth, preferredWidth);
			metrics.width = preferredWidth;
		} else {
			metrics = getCachedMetrics();
		}
		return metrics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see zincfish.zincwidget.AbstractSNSComponent#keyPressed(int)
	 */
	public boolean keyPressed(int iKeyCode) {
		int keyAction = CSDevice.getGameAction(iKeyCode);
		switch (keyAction) {
		case CSDevice.KEY_FIRE:
			// TODO 弹出下来菜单或选择框
			return true;
		}
		return super.keyPressed(iKeyCode);
	}

}
