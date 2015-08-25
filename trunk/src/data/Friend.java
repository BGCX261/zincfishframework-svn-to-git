package data;

public class Friend {
	private String name;
	private String mid;
	private String group;
	private String descript;
	private String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Friend)) {
			return false;
		}
		return mid.equals(((Friend) obj).mid);
	}

}
