package zincfish.zincwidget;

import zincfish.zinccss.model.Alignment;
import zincfish.zinccss.model.Coordinates;
import zincfish.zinccss.model.Insets;
import zincfish.zinccss.model.Metrics;
import zincfish.zinccss.style.StyleSet;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSLabelDOM;
import zincfish.zincdom.SNSPagingDOM;
import zincfish.zincdom.SNSTextFieldDOM;
import zincfish.zincparser.zmlparser.ZMLTag;
import com.mediawoz.akebono.corerenderer.CRGraphics;
import com.mediawoz.akebono.coreservice.utils.CSDevice;

/**
 * <code>SNSPagingComponent</code> 是界面中的分页组件
 * 
 * @author Jarod Yv
 */
public class SNSPagingComponent extends AbstractSNSComponent {
	private static final String PREV_PAGE = "[上页]";
	private static final String NEXT_PAGE = "[下页]";
	private static final String TURN_PAGE = "[跳转]";
	private SNSTextComponent prevPage = null;
	private SNSTextComponent nextPage = null;
	private SNSTextComponent turnPage = null;
	private SNSTextComponent indexString = null;
	private SNSTextFieldComponent textField = null;// 跳页输入框
	private int totlePageNum = 0;// 总页数
	private int currentPageNum = 0;// 当前页数
	private int index = 0;

	public Metrics getPreferredSize(int preferredWidth, int nextLineWidth) {
		Metrics metrics = null;
		if (needToComputePreferredSize(preferredWidth)) {
			metrics = super.getPreferredSize(preferredWidth, nextLineWidth);
			metrics.width = preferredWidth;
			int height = 0;
			Metrics tmpMetrics = textField.getPreferredSize(preferredWidth,
					nextLineWidth);
			height = tmpMetrics.height;
			tmpMetrics = null;

			tmpMetrics = prevPage.getPreferredSize(preferredWidth,
					nextLineWidth);
			height = Math.max(height, tmpMetrics.height);
			tmpMetrics = null;

			tmpMetrics = nextPage.getPreferredSize(preferredWidth,
					nextLineWidth);
			height = Math.max(height, tmpMetrics.height);
			tmpMetrics = null;

			tmpMetrics = turnPage.getPreferredSize(preferredWidth,
					nextLineWidth);
			height = Math.max(height, tmpMetrics.height);
			tmpMetrics = null;

			tmpMetrics = indexString.getPreferredSize(preferredWidth,
					nextLineWidth);
			height = Math.max(height, tmpMetrics.height);
			tmpMetrics = null;

			tmpMetrics = textField.getPreferredSize(preferredWidth,
					nextLineWidth);
			metrics.height += Math.max(height, tmpMetrics.height);
			tmpMetrics = null;
		}
		return metrics;
	}

	protected void layout() {
		StyleSet style = dom.getStyleSet();
		Insets insets = style.getInsets();
		Coordinates gap = style.getGap();

		int leftWidth = iWidth;
		Metrics metrics = nextPage.getCachedMetrics();
		int x = insets.left;
		int y = Alignment.CENTER.alignY(iHeight, metrics.height);
		nextPage.setBounds(x, y, metrics.width, metrics.height);
		metrics = null;
		leftWidth -= nextPage.iX + nextPage.getWidth();

		metrics = prevPage.getCachedMetrics();
		x += nextPage.getWidth() + gap.X;
		y = Alignment.CENTER.alignY(iHeight, metrics.height);
		prevPage.setBounds(x, y, metrics.width, metrics.height);
		metrics = null;
		leftWidth -= insets.left + prevPage.getWidth();

		metrics = indexString.getCachedMetrics();
		x += prevPage.getWidth() + gap.X;
		y = Alignment.CENTER.alignY(iHeight, metrics.height);
		indexString.setBounds(x, y, metrics.width, metrics.height);
		metrics = null;
		leftWidth -= indexString.getWidth();
		((SNSLabelDOM) indexString.dom).text = currentPageNum + "/"
				+ totlePageNum;
		indexString.displayedString = ((SNSLabelDOM) indexString.dom).text;

		metrics = turnPage.getCachedMetrics();
		x = iWidth - metrics.width - insets.right;
		y = Alignment.CENTER.alignY(iHeight, metrics.height);
		turnPage.setBounds(x, y, metrics.width, metrics.height);
		metrics = null;
		leftWidth -= turnPage.getWidth() + insets.right;

		metrics = textField.getCachedMetrics();
		leftWidth -= gap.X * 4;
		x = turnPage.iX - gap.X - leftWidth;
		y = Alignment.CENTER.alignY(iHeight, metrics.height);
		textField.setBounds(x, y, leftWidth, metrics.height);
		metrics = null;
		gap = null;
		insets = null;
		style = null;
	}

	public void init(AbstractSNSDOM dom) {
		super.init(dom);
		SNSPagingDOM pageDOM = (SNSPagingDOM) this.dom;
		totlePageNum = pageDOM.totalPage;
		currentPageNum = pageDOM.currentPage;
		pageDOM = null;

		SNSLabelDOM labelDOM = new SNSLabelDOM();
		labelDOM.text = NEXT_PAGE;
		nextPage = (SNSTextComponent) ComponentFactory
				.createComponent(labelDOM);
		nextPage.init(labelDOM);
		labelDOM.setComponent(nextPage);
		this.addComponent(nextPage);
		labelDOM = null;

		labelDOM = new SNSLabelDOM();
		labelDOM.text = PREV_PAGE;
		prevPage = (SNSTextComponent) ComponentFactory
				.createComponent(labelDOM);
		prevPage.init(labelDOM);
		labelDOM.setComponent(prevPage);
		this.addComponent(prevPage);
		labelDOM = null;

		SNSTextFieldDOM textFieldDOM = new SNSTextFieldDOM();
		textFieldDOM.tagName = ZMLTag.INPUT_TAG;
		textFieldDOM.value = String.valueOf(currentPageNum);
		textField = (SNSTextFieldComponent) ComponentFactory
				.createComponent(textFieldDOM);
		textField.init(textFieldDOM);
		textFieldDOM.setComponent(textField);
		this.addComponent(textField);
		textFieldDOM = null;

		labelDOM = new SNSLabelDOM();
		labelDOM.text = TURN_PAGE;
		turnPage = (SNSTextComponent) ComponentFactory
				.createComponent(labelDOM);
		turnPage.init(labelDOM);
		labelDOM.setComponent(turnPage);
		this.addComponent(turnPage);
		labelDOM = null;

		labelDOM = new SNSLabelDOM();
		labelDOM.text = "/" + totlePageNum;
		indexString = (SNSTextComponent) ComponentFactory
				.createComponent(labelDOM);
		indexString.init(labelDOM);
		labelDOM.setComponent(indexString);
		this.addComponent(indexString);
		labelDOM = null;

		handleClasses();
	}

	protected void paintImpl(CRGraphics g) {
		nextPage.paintCurrentFrame(g, nextPage.iX, nextPage.iY);
		prevPage.paintCurrentFrame(g, prevPage.iX, prevPage.iY);
		textField.paintCurrentFrame(g, textField.iX, textField.iY);
		indexString.paintCurrentFrame(g, indexString.iX, indexString.iY);
		turnPage.paintCurrentFrame(g, turnPage.iX, turnPage.iY);
	}

	public void setMotion(int motionX, int motionY) {

	}

	public void setFocus(boolean isFocused) {
		super.setFocus(isFocused);
		if (isFocused) {
			index = 0;
			if (currentPageNum >= totlePageNum) {
				index++;
			}
			handleClasses();
		}
		AbstractSNSComponent com = (AbstractSNSComponent) componentAt(index);
		com.setFocus(isFocused);
		com = null;
	}

	private void handleClasses() {
		if (currentPageNum >= totlePageNum) {
			nextPage.dom.canFocus = false;
			nextPage.dom.classes = null;
		} else {
			nextPage.dom.canFocus = true;
			nextPage.dom.classes = "link";
		}

		if (currentPageNum <= 1) {
			prevPage.dom.canFocus = false;
			prevPage.dom.classes = null;
		} else {
			prevPage.dom.canFocus = true;
			prevPage.dom.classes = "link";
		}

		turnPage.dom.canFocus = true;
		turnPage.dom.classes = "link";
	}

	public boolean keyPressed(int keyCode) {
		int keyAction = CSDevice.getGameAction(keyCode);
		switch (keyAction) {
		case CSDevice.KEY_FIRE:
			switch (index) {
			case 0:
				currentPageNum--;
				break;
			case 1:
				currentPageNum++;
				break;
			case 2:
				return textField.keyPressed(keyCode);
			case 3:
				currentPageNum = Integer.parseInt(textField.getDom().value);
				break;
			}
			// TODO 翻页
			handleClasses();
			return true;
		case CSDevice.KEY_LEFT:
			int oldIndex = index;
			index--;
			if (currentPageNum == 1 && index == 1)
				index--;
			if (currentPageNum == totlePageNum && index == 0)
				index--;
			if (index < 0) {
				index = 0;
			} else {
				AbstractSNSComponent com = (AbstractSNSComponent) componentAt(oldIndex);
				com.setFocus(false);
				com = null;
				com = (AbstractSNSComponent) componentAt(index);
				com.setFocus(true);
				com = null;
				return true;
			}
			break;
		case CSDevice.KEY_RIGHT:
			oldIndex = index;
			index++;
			if (currentPageNum == 1 && index == 1)
				index++;
			if (index > 3) {
				index = 3;
			} else {
				AbstractSNSComponent com = (AbstractSNSComponent) componentAt(oldIndex);
				com.setFocus(false);
				com = null;
				com = (AbstractSNSComponent) componentAt(index);
				com.setFocus(true);
				com = null;
				return true;
			}
			break;
		}
		return false;
	}
}
