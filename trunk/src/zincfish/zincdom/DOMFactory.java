package zincfish.zincdom;

/**
 * <code>DOMFactory</code> 是DOM节点的简单工厂。它根据DOM节点的类型，生成相应的DOM节点。
 * 
 * @author Jarod Yv
 * @since fingerling
 */
public final class DOMFactory {

	/**
	 * 根据类型创建相应的DOM节点
	 * 
	 * @param type
	 *            DOM节点类型
	 * @return DOM节点
	 */
	public static AbstractSNSDOM createDOM(byte type) {
		switch (type) {
		case AbstractSNSDOM.TYPE_LABEL:// <label>
			return new SNSLabelDOM();
		case AbstractSNSDOM.TYPE_IMAGE:// <img>
			return new SNSImageDOM();
		case AbstractSNSDOM.TYPE_DIV:// <div>
			return new SNSDivDOM();
		case AbstractSNSDOM.TYPE_UNIT:// <unit>
			return new SNSUnitDOM();
		case AbstractSNSDOM.TYPE_BODY:// <body>
			return new SNSBodyDOM();
		case AbstractSNSDOM.TYPE_PAGING:// <paging>
			return new SNSPagingDOM();
		default:
			return null;
		}
	}
}
