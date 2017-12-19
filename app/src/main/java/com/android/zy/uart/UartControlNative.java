package com.android.zy.uart;

public class UartControlNative {
	static {
		try {
			System.loadLibrary("dartsUart");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ������;
	 * 
	 * @param dev �����ļ�
	 * @param bandRate ������
	 * @return true ��ʼ���ɹ��� false ��ʼ��ʧ�ܣ�
	 */
	public static native int initUartNative(String dev, int bandRate);

	/**
	 * �رմ���;
	 * @return true ��ʼ���ɹ��� false ��ʼ��ʧ�ܣ�
	 */
	public static native int closeUartNative();

	/**
	 * ���ڷ�����Ϣ;
	 * 
	 * @param message ��Ҫ���͵���Ϣ��
	 * @return true ���ͳɹ��� false ����ʧ�ܣ�
	 */
	public static native int uartSendMessageNative(byte[] message);

	/**
	 * ���ڶ���Ϣ;
	 * @return ���������ݣ�
	 */
	public static native byte[] uartReadMessageNative();

}

