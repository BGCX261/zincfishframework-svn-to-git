package ui.combo;

import zincfish.zincdom.SNSComboBoxDOM;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.ui.IKeyInputHandler;
import com.mediawoz.akebono.ui.UPanel;

public abstract class AbstractComboPanel extends UPanel implements
		IKeyInputHandler {

	/** 用户选中的要返回的值 */
	protected String value = null;

	/** 相关联的ComBoxDOM */
	protected SNSComboBoxDOM dom = null;

	public AbstractComboPanel(int iAnimTickCount, int iX, int iY, int iWidth,
			int iHeight) {
		super(iAnimTickCount, iX, iY, iWidth, iHeight);
	}

	protected void drawCurrentFrame(CRGraphics g) {

	}

}
