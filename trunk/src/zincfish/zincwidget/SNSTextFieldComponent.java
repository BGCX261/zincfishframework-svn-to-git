package zincfish.zincwidget;

import zincfish.zinccss.model.Insets;
import zincfish.zinccss.model.Metrics;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSLabelDOM;
import zincfish.zincdom.SNSTextFieldDOM;
import com.mediawoz.akebono.corerenderer.CRFont;
import com.mediawoz.akebono.coreservice.utils.CSDevice;

/**
 * <code>SNSTextFieldComponent</code>是界面上的输入框组件.<br>
 * 逻辑上它继承自{@link SNSTextComponent}, 可以看做是特殊的文字组件，只要在文字周围画上背景和边框, 点击后进入输入状态.<br>
 * <code>SNSComboBoxComponent</code>分为2中表现形式, 分别是:单行输入框和多行输入框.
 * 
 * @author Jarod Yv
 */
public class SNSTextFieldComponent extends SNSTextComponent {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * zincfish.zincwidget.AbstractSNSComponent#init(zincfish.zincdom.AbstractSNSDOM
	 * )
	 */
	public void init(AbstractSNSDOM dom) {
		this.dom = dom;
		setVisible(dom.isVisible);
		SNSTextFieldDOM textFieldDOM = (SNSTextFieldDOM) this.dom;
		wrap = textFieldDOM.isMulti ? SNSLabelDOM.WRAP_BREAK
				: SNSLabelDOM.WRAP_OMIT;
		displayedString = textFieldDOM.value;
		textFieldDOM = null;
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
			if (wrap == SNSLabelDOM.WRAP_OMIT) {
				int maxSize = ((SNSTextFieldDOM) dom).size;
				if (maxSize == 0) {
					metrics.width = preferredWidth;
				} else {
					StyleSet style = dom.getStyleSet();
					Insets insets = style.getInsets();
					CRFont font = style.getFont();
					metrics.width = insets.left + font.charWidth('宽') * maxSize
							+ insets.right;
					font = null;
					insets = null;
					style = null;
				}
				if (metrics.width > preferredWidth)
					metrics.width = preferredWidth;
			}
		} else {
			metrics = getCachedMetrics();
		}
		return metrics;
	}

	/* (non-Javadoc)
	 * @see zincfish.zincwidget.AbstractSNSComponent#keyPressed(int)
	 */
	public boolean keyPressed(int keyCode) {
		int keyAction = CSDevice.getGameAction(keyCode);
		switch (keyAction) {
		case CSDevice.KEY_FIRE:
			// TODO 弹出输入框
			return true;
		}
		return super.keyPressed(keyCode);
	}
}
