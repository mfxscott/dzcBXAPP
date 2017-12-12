package com.bixian365.dzc.utils.Gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
/**
 * Json解析Gson工具类
 */
public class GsonUtils {
    /**
     * 返回对象Json数据解析
     * @param clazz 实体对象
     * @param json  json数据
     * @return
     */
    public static Object jsonToObject(Class<?> clazz, String json)
    {
        Object obj = null;
        Gson gson = new Gson();
        obj = gson.fromJson(json, clazz);
        return obj;
    }
    // 将Json数组解析成相应的映射对象列表
    public static <T> List<T> parseJsonArrayWithGson(String jsonData,
                                                     Class<T> type) {
        Gson gson = new Gson();
        List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
        return result;
    }
    /**
     * 返回集合数据列表Json数据解析
     * @param json json数据
     * @param clazz  实体对象
     * @return
     */
    public static  List<?> jsonToList(String json, Class<?> clazz)
    {
        Gson gson = new Gson();
        List<?> ps = gson.fromJson(json, new TypeToken<List<?>>(){}.getType());
        for(int i = 0; i < ps.size() ; i++)
        {
            ps.get(i);
        }
        return ps;
    }
    public static  List<?> JsonArr(String jsonArr , Class<?> clazz) {
        /**
         * 解析没有数据头的纯数组
         */
        //拿到本地JSON 并转成String

        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(jsonArr).getAsJsonArray();

        Gson gson = new Gson();
        List<Object> userBeanList = new ArrayList<>();
        //加强for循环遍历JsonArray
        for (JsonElement user : jsonArray) {
            //使用GSON，直接转成Bean对象
            Object obj = gson.fromJson(user, clazz);
            userBeanList.add((obj));
        }
        return userBeanList;
    }

}
