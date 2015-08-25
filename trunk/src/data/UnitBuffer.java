package data;

import utils.DOMUtil;
import zincfish.zincdom.AbstractSNSDOM;
import zincfish.zincdom.SNSUnitDOM;

/**
 * <code>UnitBuffer</code>是{@link SNSUnitDOM}的缓存。
 * 
 * @author Jarod Yv
 */
public final class UnitBuffer {
	/** 缓存的最大长度 */
	public static final int BUFFER_SIZE = 5;

	/* 缓存的唯一实例 */
	private static UnitBuffer instance = null;

	/* DOM Tree缓存 */
	private SNSUnitDOM[] buffer = null;

	/* 当前索引 */
	private int index = -1;

	/* 用于监听DOM Tree变化的监听器 */
	private IDOMChangeListener domChangeListener = null;

	/*
	 * 构造函数
	 */
	private UnitBuffer() {
		buffer = new SNSUnitDOM[BUFFER_SIZE];
	}

	/**
	 * 获取DOMBuffer的唯一实例
	 * 
	 * @return DOMBuffer的唯一实例
	 */
	public static UnitBuffer getInstance() {
		if (instance == null)
			instance = new UnitBuffer();
		return instance;
	}

	public void updateView() {
		domChangeListener.updateView(true);
	}

	/**
	 * 获取当前的缓存
	 * 
	 * @return 当前的缓存
	 */
	public SNSUnitDOM getCurrentBuffer() {
		if (index < 0 || index >= buffer.length)
			return null;
		return buffer[index];
	}

	/**
	 * 获取指定索引位置上的缓存
	 * 
	 * @param index
	 *            指定索引位置
	 * @return 索引位置上的缓存。如果没有则返回null
	 */
	public SNSUnitDOM getBufferByIndex(int index) {
		if (index >= 0 && index < BUFFER_SIZE)
			return buffer[index];
		return null;
	}

	/**
	 * 获取具有指定id的缓存
	 * 
	 * @param domid
	 *            指定ID
	 * @return 具有指定ID的缓存。如果没有则返回null
	 */
	public SNSUnitDOM getBufferByDOMID(String domID) {
		if (domID == null)
			return null;
		for (int i = 0; i < BUFFER_SIZE; i++) {
			if (buffer[i] != null) {
				if (domID.equals(buffer[i].id)) {
					index = i;
					return buffer[index];
				}
			}
		}
		return null;
	}

	private static boolean isAnotherBuffer = true;

	/**
	 * 当前的页面是否是新加载的页面(不是刷新当前)
	 * 
	 * @return
	 */
	public static boolean isAnotherBuffer() {
		return isAnotherBuffer;
	}

	/**
	 * 向缓存中加入一个缓存。如果缓存已满，会将最老的缓存清除。
	 * 
	 * @param root
	 *            要加入的缓存
	 */
	public void addBuffer(SNSUnitDOM root) {

		if (root == null || root.children == null) // 如果unit DOM 没有孩子节点直接返回
			return;

		SNSUnitDOM currentUnit = getCurrentBuffer();// 当前界面
		if (root.children != null && currentUnit != null
				&& currentUnit.equals(root)) {// 如果两个Unit定义的是同一个界面，则更新当前的Unit
			isAnotherBuffer = false;
			for (int i = 0; i < root.children.size(); i++) {
				AbstractSNSDOM subtree = (AbstractSNSDOM) root.children.get(i);
				DOMUtil.replaceSubtree(currentUnit, subtree);
				subtree = null;
			}
			// 要重新排版
			DOMUtil.invalidateAlldom(currentUnit);
		} else {
			isAnotherBuffer = true;
			if (index == BUFFER_SIZE - 1) {// 缓存已满
				// 释放最老的缓存
				buffer[0].release();
				buffer[0] = null;
				// 移动缓存
				for (int i = 0; i < BUFFER_SIZE - 1; i++) {
					buffer[i] = buffer[i + 1];
				}
				// 加入新的unit到缓存
				buffer[BUFFER_SIZE - 1] = root;
			} else {// 缓存未满
				index++;
				// 清除当前数组索引(包括)后的所有缓存
				for (int i = index; i < BUFFER_SIZE; i++) {
					if (buffer[i] != null) {
						buffer[i].release();
						buffer[i] = null;
					}
				}
				// 加入新的unit到缓存
				buffer[index] = root;
			}
			// //System.out.println("222222");
		}
		domChangeListener.updateView(true);
	}

	public void prev() {
		if (index > 0) {
			index--;

			buffer[index].getComponent().init(buffer[index]);
			domChangeListener.updateView(false);
		}
	}

	public void next() {
		if (index < BUFFER_SIZE - 1) {
			index++;
			if (buffer[index] != null)
				domChangeListener.updateView(true);
			else
				index--;
		}
	}

	/**
	 * 获取当恰的索引
	 * 
	 * @return 当前索引
	 */
	public int getCurrentIndex() {
		return index;
	}

	/**
	 * @return the domChangeListener
	 */
	public IDOMChangeListener getDomChangeListener() {
		return domChangeListener;
	}

	/**
	 * @param domChangeListener
	 *            the domChangeListener to set
	 */
	public void setDomChangeListener(IDOMChangeListener domChangeListener) {
		this.domChangeListener = domChangeListener;
	}

	// public void removeNode(AbstractDOM dom) {
	// Unit unit = getCurrentBuffer();
	// unit.removeNode(dom);
	// unit = null;
	// domChangeListener.updateView(true);
	// }
}
