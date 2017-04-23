package com.wheelys.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.HttpResult;
import com.wheelys.util.HttpUtils;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.ValidateUtil;
import com.wheelys.util.WebLogger;

public abstract class SendMessageUtils {
	
	private static final transient WheelysLogger dbLogger = WebLogger.getLogger(SendMessageUtils.class);

    private static String MTURL = "http://esms100.10690007.net/sms/mt";

    public static ResultCode sendMessage(String mobile, String content){
    	if(StringUtils.isBlank(content) || !ValidateUtil.isMobile(mobile)){
    		return ResultCode.getFailure("content:"+content+",mobile:"+mobile);
    	}
        String msg = getEncodeHexMsg(content);
        HashMap<String, String> smsParams = new HashMap<String, String>();
        smsParams.put("command", "MT_REQUEST");
        smsParams.put("spid", "9879");
        smsParams.put("sppassword", "uzC8B8hS");
        smsParams.put("da", "86"+mobile);
        smsParams.put("sm", msg);
        smsParams.put("dc", "15");
        HttpResult reuslt = HttpUtils.postUrlAsString(MTURL, smsParams);
        if(reuslt.isSuccess()){
            HashMap<String,String> pp = parseResStr(reuslt.getResponse());
            if(StringUtils.equals(pp.get("mtstat"), "ACCEPTD") && StringUtils.equals(pp.get("mterrcode"), "000")){
            	dbLogger.warn("result:"+JsonUtils.writeObjectToJson(pp));
            	return ResultCode.SUCCESS; 
            }else{
            	dbLogger.warn("error:"+reuslt.getResponse());
            }
        }
        return ResultCode.getFailure(reuslt.getResponse());
    }
    
    private static String getEncodeHexMsg(String content){
		try {
			String msg = new String(Hex.encodeHex(content.getBytes("GBK")));
			return msg;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
    }
    
    private static HashMap<String,String> parseResStr(String resStr) {
        HashMap<String,String> pp = new HashMap<String,String>();
        try {
            String[] ps = resStr.split("&");
            for(int i=0;i<ps.length;i++){
                int ix = ps[i].indexOf("=");
                if(ix!=-1){
                   pp.put(ps[i].substring(0,ix),ps[i].substring(ix+1)); 
                }
            }
        } catch (Exception e) {
			dbLogger.error("parseResStr:" + resStr);
        }
        return pp;
    }

}
