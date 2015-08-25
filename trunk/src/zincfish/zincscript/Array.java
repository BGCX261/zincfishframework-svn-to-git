package zincfish.zincscript;

import utils.ArrayList;

public class Array {
	int dimension;// 维度
	ArrayList data = null;// 数据

	public Array(int di) {
		dimension = di;
		data = new ArrayList();
	}

	public void setValue(ArrayList index, Object value) {
		if (index == null)
			return;
		dimension = index.size();
		Integer in = (Integer) index.remove(0);
		int m = in.intValue();
		if (index.size() > 0) {
			if (m >= data.size()) {
				for (int i = data.size(); i <= m; i++)
					data.add(null);
			}

			Array a = (Array) data.get(m);
			if (a == null)
				a = new Array(index.size());
			data.set(m, a);
			a.setValue(index, value);
			a = null;
		} else {
			if (m >= data.size()) {
				for (int i = data.size(); i <= m; i++) {
					data.add(null);
				}
			}
			data.set(m, value);
		}
		in = null;
	}

	public Object getValue(ArrayList index) {
		if (index == null)
			return null;
		Integer in = (Integer) index.remove(0);
		int m = in.intValue();
		if (index.size() > 0) {
			if (m < data.size()) {
				Array a = (Array) data.get(m);
				return a.getValue(index);
			} else {
				return null;
			}
		} else {
			if (m < data.size()) {
				return data.get(m);
			} else {
				return null;
			}
		}
	}
}
