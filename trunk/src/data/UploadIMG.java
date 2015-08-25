/**
 * License  3G门户版权所有 2008-2009
 */
package data;

/**
 * @作者 江威
 * @Email weijiang8410@163.com/jiang-wei@3g.net.cn
 */
public class UploadIMG {

	public String picpath;
	public String picname;
	public String picdescribe;
	public int picalbum;
	public String picfri;
	/**
	 * @return the picpath
	 */
	public String getPicpath() {
		if(picpath == null) {
			picpath = "";
		}
		return picpath;
	}
	/**
	 * @return the picname
	 */
	public String getPicname() {
		if(picname == null) {
			return "";
		}
		return picname;
	}
	/**
	 * @return the picdescribe
	 */
	public String getPicdescribe() {
		if(picdescribe == null) {
			return "";
		}
		return picdescribe;
	}

	/**
	 * @return the picfri
	 */
	public String getPicfri() {
		if(picfri == null) {
			return "";
		}
		return picfri;
	}
	
}
