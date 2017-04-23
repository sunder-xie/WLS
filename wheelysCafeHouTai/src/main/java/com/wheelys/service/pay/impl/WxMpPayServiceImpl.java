package com.wheelys.service.pay.impl;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wheelys.Config;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.WebLogger;
import com.wheelys.pay.WeiXinPayUtil;
import com.wheelys.pay.config.WeiXinPayConfigure;
import com.wheelys.service.pay.WxMpPayService;

@Service("wxMpPayService")
public class WxMpPayServiceImpl implements WxMpPayService, InitializingBean {
	
	private static final transient WheelysLogger dbLogger = WebLogger.getLogger(WxMpPayServiceImpl.class);

	@Autowired@Qualifier("config")
	private Config config;
	
	@SuppressWarnings("deprecation")
	@Override
	public void afterPropertiesSet() throws Exception {
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        String certDir = config.getString("cerDir");
        File file = new File(certDir+"wxmp_apiclient_cert.p12");
        if(!file.exists()) return;
        FileInputStream instream = null;
        try {
        	instream = new FileInputStream(file);
            keyStore.load(instream, WeiXinPayConfigure.MCH_ID.toCharArray());
        } catch (Exception e) {
        	dbLogger.warn("wxmp_apiclient_cert init " + e.getMessage());
		} finally {
			if(instream != null){
				instream.close();
			}
        }
        
        SSLContext sslcontext = org.apache.http.conn.ssl.SSLContexts.custom()
                .loadKeyMaterial(keyStore, WeiXinPayConfigure.MCH_ID.toCharArray())
                .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		WeiXinPayUtil.sslConnfactorys.put(WeiXinPayConfigure.MCH_ID, sslsf);
	}

}
