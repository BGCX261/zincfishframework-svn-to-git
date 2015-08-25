package config;

import zincfish.zincparser.zmlparser.ZMLTag;

public class SystemProperty {
	private static final byte UNKNOWN = 0x00;
	private static final byte SUPPORTED = 0x01;
	private static final byte UNSUPPORTED = 0x02;

	private static byte isSupportJSR75 = UNKNOWN;
	private static byte isSupportRecording = UNKNOWN;
	private static byte isSupportCamera = UNKNOWN;

	/**
	 * 判断是否支持JSR75
	 * 
	 * @return <ul>
	 *         <li><code>true</code> - 支持录音
	 *         <li><code>false</code> - 不支持录音
	 *         </ul>
	 */
	public static boolean isSupportJSR75() {
		if (isSupportJSR75 == UNKNOWN) {
			String support = System.getProperty("supports.audio.capture")
					.toLowerCase();
			isSupportRecording = (support != null && support
					.equals(ZMLTag.TRUE_VALUE)) ? SUPPORTED : UNSUPPORTED;
			support = null;
		}
		return isSupportJSR75 == SUPPORTED;
	}

	/**
	 * 判断是否支持录音
	 * 
	 * @return <ul>
	 *         <li><code>true</code> - 支持录音
	 *         <li><code>false</code> - 不支持录音
	 *         </ul>
	 */
	public static boolean isSupportRecording() {
		if (isSupportRecording == UNKNOWN) {
			String support = System.getProperty("supports.audio.capture")
					.toLowerCase();
			isSupportRecording = (support != null && support
					.equals(ZMLTag.TRUE_VALUE)) ? SUPPORTED : UNSUPPORTED;
			support = null;
		}
		return isSupportRecording == SUPPORTED;
	}

	/**
	 * 判断是否支持拍照
	 * 
	 * @return <ul>
	 *         <li><code>true</code> - 支持录音
	 *         <li><code>false</code> - 不支持录音
	 *         </ul>
	 */
	public static boolean isSupportCamera() {
		if (isSupportCamera == UNKNOWN) {
			String support = System.getProperty("supports.video.capture")
					.toLowerCase();
			isSupportCamera = (support != null && support
					.equals(ZMLTag.TRUE_VALUE)) ? SUPPORTED : UNSUPPORTED;
			support = null;
		}
		return isSupportCamera == SUPPORTED;
	}
}
