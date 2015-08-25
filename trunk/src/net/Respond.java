/*
 * Response.java
 *
 * Created on 2007年8月22日, 下午11:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net;

/**
 * 
 * @author Admin
 */
public class Respond {
	/** 返回状态控制器 **/
	public int ResponseState;
	/** 返回数据 **/
	public byte[] ResponseData;
	// public int ResponseDataStart = 0;
	// public String ResponseFileName = null;
	// public String ResponseFileType = null;
	/** 请求 **/
	public Request request;

	/**
	 * Creates a new instance of Response
	 * 
	 * @param ResponseState
	 *            int 网络返回数据状态
	 * @param request
	 *            Request 当次请求
	 * @param ResponseData
	 *            byte[] 数据
	 */
	public Respond(int ResponseState, Request request, byte[] ResponseData,
			String ResponseFileName, String ResponseFileType,
			int ResponseDataStart) {
		this.ResponseState = ResponseState;
		this.request = request;
		this.ResponseData = ResponseData;
		// this.ResponseFileName = ResponseFileName;
		// this.ResponseFileType = ResponseFileType;
		// this.ResponseDataStart = ResponseDataStart;
	}

	/**
	 * 返回一个错误的数据
	 * 
	 * @param ResponseType
	 *            int
	 * @param request
	 *            Request
	 */
	public Respond(int ResponseState, Request request) {
		this(ResponseState, request, null, null, null, 0);
	}
}
