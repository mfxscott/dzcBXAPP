package com.bixian365.dzc.utils.httpClient.ssl;

import android.app.Activity;

import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * des
 * @author mfx  16/10/14 20:27
 *  深圳市优讯信息技术有限公司
 */
public class https {
    public static String TAG="https";
    // 证书数据
    private static List<byte[]> CERTIFICATES_DATA = new ArrayList<byte[]>();
    /**
     * 添加https证书
     * @param inputStream
     */
//    public synchronized static void addCertificate(InputStream inputStream) {
//        Log.i(TAG,"#addCertificate inputStream = " + inputStream);
//        if (inputStream != null) {
//
//            try {
//                int ava = 0;// 数据当次可读长度
//                int len = 0;// 数据总长度
//                ArrayList<byte[]> data = new ArrayList<byte[]>();
//                while ((ava = inputStream.available()) > 0) {
//                    byte[] buffer = new byte[ava];
//                    inputStream.read(buffer);
//                    data.add(buffer);
//                    len += ava;
//                }
//
//                byte[] buff = new byte[len];
//                int dstPos = 0;
//                for (byte[] bytes:data) {
//                    int length = bytes.length;
//                    System.arraycopy(bytes, 0, buff, dstPos, length);
//                    dstPos += length;
//                }
//
//                CERTIFICATES_DATA.add(buff);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
    /**
     * 添加证书
     */

    public  static SSLSocketFactory getSocketFactory(Activity context) {
        try {
            CertificateFactory certificateFactory= CertificateFactory.getInstance("X.509");
            KeyStore keyStore= KeyStore.getInstance("PKCS12","BC");
            keyStore.load(null);
//            try {
//                InputStream  is = context.getBaseContext().getResources().openRawResource(R.raw.https); // 证书文件
//                Certificate cer=certificateFactory.generateCertificate(is);
//                keyStore.load(null,null);
//                keyStore.setCertificateEntry("trust", cer);
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
