package com.wheelys.pay.config;

/**
 * 支付宝支付配置
 */
public interface AliPayConfigure {
	
	public static final String APPID = "2016113003642153";
	
	public static final String SELLERID = "2088521333373572";
	
	public static final String PUBLICKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIv"+
					   "YISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwC"+
					   "tzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/"+
					   "FCEa3/cNMW0QIDAQAB";
	
	public static final String PRIVATEKEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALE4tfvOxnxN5s19"+
						"83OXWGgTtVlkCNmtOA+DtQ60nnHz7Tq729nQe3TltUfq4W1mdTQpQMlQyfuMYXS8"+
						"iMWUo4dwkktGd+wsy4bKmQUbARIIxTKsWGZDI3QUi6/GlhVs2z4zquCrQ+kFPr7s"+
						"gz7f6Oag8W3zPtI6N9zCTM5o8tzXAgMBAAECgYBs8zJd5db8YF/+AOqEK4z8HvGk"+
						"TO1r9SO71fqfWcb2w0EalJcqJxNe4EE8P1frC8wODzZ3DSW/caQQQj/LR//npA7N"+
						"RQUMAkSntiBBn40zOO3SFMwglBsTn5R4PHsbBtpENBvoju6cguO6/slQ7x15Z6fk"+
						"UsnMN91KZ4/gtrH28QJBANg14szFib1IZVpRX062pJKM/6GrcD5+bIUtqu7FXAIa"+
						"5NzrF0Tl0bMnM893nbRcEPMy5kv0GuXxjzDDPnUb7SMCQQDR1fnQ/3OBsWZstYwA"+
						"lBCURFq5dVR2J0yzCaPMub0nQ62W8te6oEB1jt77ax5GvgyICCJNzWJ+Xb9Pq7Gj"+
						"La69AkAactUQuaV02zNSzht7lIojR05KJ5+udWavD/wWA1yTAVk30SZijj6NzODg"+
						"o0BOfOix52sWpYDY2ijrDSdJyiXDAkBfi2/Zw0dD6ZP6sduSs9isukcZJRyVRCW/"+
						"bAdCg9xmEYHsNp0WJDvVi31qu4aorYalNhbwmDFd2q/uxf33kCh9AkBjFRqTnDm7"+
						"Cpm043BsP7DPNns3nEncx1PVjIlM5Yt4vZTiouLoj7wCyIyHVc8LYwWLpxChe+Dw"+
						"716G0wB6IX8t";
	
	public static final String GATEWAY = "https://openapi.alipay.com/gateway.do";
	
	public static final String PAYURL = "alipay.trade.wap.pay";
	
	public static final String APP_PAYURL = "alipay.trade.app.pay";
	
	public static final String QUERYURL = "alipay.trade.query";
	
	public static final String PRECREATEURL = "alipay.trade.precreate";

	public static final String NOTIFYURL = "notify/aliwappay.xhtml";
	
	public static final String RETURNURL = "return/aliwappay.xhtml";
	
	

}
