/**
 * License  3G门户版权所有 2008-2009
 */
package domain;

/**
 * @作者 江威
 * @Email weijiang8410@163.com/jiang-wei@3g.net.cn
 */
public class A_File {

	public String fileName; // 文件名称
	public String path; // 文件路径
	public boolean isDirectory; // 是否是目录
	public A_File father; // 父目录
	
	public boolean isroot = false; // 是否是根目录
}
