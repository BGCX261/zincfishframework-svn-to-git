package zincfish.zincwidget;

import zincfish.zincdom.AbstractSNSDOM;

/**
 * <code>ComponentFactory</code> 是SNS UI组件的简单工厂。它根据DOM节点的类型，生成相应的View节点。
 * 
 * @author Jarod Yv
 */
public final class ComponentFactory {

	/**
	 * 根据DOM生成相应的组件
	 * 
	 * @param dom
	 *            DOM节点
	 * @return 与DOM相对应的组件
	 */
	public static AbstractSNSComponent createComponent(AbstractSNSDOM dom) {
		if (dom == null)
			return null;
		AbstractSNSComponent component = null;
		switch (dom.type) {
		case AbstractSNSDOM.TYPE_LABEL:
			component = new SNSTextComponent();
			break;
		case AbstractSNSDOM.TYPE_DIV:
			component = new SNSDivComponent();
			break;
		case AbstractSNSDOM.TYPE_IMAGE:
			component = new SNSImageComponent();
			break;
		case AbstractSNSDOM.TYPE_UNIT:
			component = new SNSUnitComponent();
			break;
		case AbstractSNSDOM.TYPE_BODY:
			component = new SNSBodyComponent();
			break;
		case AbstractSNSDOM.TYPE_TEXT_FIELD:
			component = new SNSTextFieldComponent();
			break;
		case AbstractSNSDOM.TYPE_PAGING:
			component = new SNSPagingComponent();
			break;
		case AbstractSNSDOM.TYPE_LIST:
			component = new SNSHListComponent();
			break;
		case AbstractSNSDOM.TYPE_COMBOX:
			component = new SNSComboBoxComponent();
			break;
		case AbstractSNSDOM.TYPE_CHECKBOX:
			component = new SNSCheckBoxComponent();
			break;
		case AbstractSNSDOM.TYPE_VOTE_ITEM:
			component = new SNSRateComponent();
			break;
		}

		if (component != null)
			component.setDom(dom);
		return component;
	}
}
