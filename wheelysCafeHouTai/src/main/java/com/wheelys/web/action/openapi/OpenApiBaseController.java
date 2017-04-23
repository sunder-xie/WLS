package com.wheelys.web.action.openapi;

import com.wheelys.api.vo.ResultCode;
import com.wheelys.util.WheelysLogger;
import com.wheelys.util.JsonUtils;
import com.wheelys.util.WebLogger;

public class OpenApiBaseController {

	protected final transient WheelysLogger dbLogger = WebLogger.getLogger(getClass());
	
	protected String successJsonResult(Object data) {
		ResultCode result = ResultCode.getSuccessReturn(data);
		return JsonUtils.writeObjectToJson(result, true);
	}

	protected String errorJsonResult(String msg) {
		ResultCode result = ResultCode.getFailure(msg);
		return JsonUtils.writeObjectToJson(result, true);
	}


}
