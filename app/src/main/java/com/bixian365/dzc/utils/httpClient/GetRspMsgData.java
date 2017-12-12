package com.bixian365.dzc.utils.httpClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * ***************************
 * 解析获得返回数据
 * @author mfx
 * 深圳市优讯信息技术有限公司
 * 16/10/29 下午3:15
 * ***************************
 */
public class GetRspMsgData {
    /**
     * 检查升级版本
     * @param jsonobj
     */
    public static Map<String,String> getUpdateVer(String jsonobj) {

        Map<String,String> map = new HashMap<String, String>();
        JSONObject repmsg;
        JSONObject object;
        try {
            repmsg = new JSONObject(jsonobj.toString());
            object = repmsg.getJSONObject("responseData");
            map.put("verCode",object.getString("forceUpdate"));
            map.put("verContent",object.getString("description"));
            map.put("verUrl",object.getString("downloadPageUrl"));
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

}
