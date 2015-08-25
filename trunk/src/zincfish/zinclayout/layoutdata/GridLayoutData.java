package zincfish.zinclayout.layoutdata;

/**
 * @author Jarod Yv
 */
public class GridLayoutData implements ILayoutData {
	/** */
	private int[] splits = null;

	public int[] getSplits() {
		return splits;
	}

	public void setSplits(int[] splits) {
		this.splits = splits;
	}

	public GridLayoutData(int[] splits) {
		this.splits = splits;
	}
}
