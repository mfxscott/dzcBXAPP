package com.bixian365.dzc.utils.httpClient;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.bixian365.dzc.entity.GoodsTypeEntity;
import com.bixian365.dzc.entity.GsonResponseDataEntity;
import com.bixian365.dzc.entity.ResponseDataUserInfoEntity;
import com.bixian365.dzc.entity.UserInfoEntity;
import com.bixian365.dzc.utils.Logs;
import com.bixian365.dzc.utils.SXUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 解析返回数据
 * @time  2017/8/2 11:17
 */
public class ResponseData {
    private static ResponseData mInstance;
    private Context mContext;

    private ResponseData(Context context) {
        this.mContext = context.getApplicationContext();
    }
    public static ResponseData getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SXUtils.class) {
                if (mInstance == null) {
                    mInstance = new ResponseData(context);
                }
            }
        }
        return mInstance;
    }
    /**
     * 生活模块特产商品
     * @param jsonobj  返回数据
     * @return
     */
    public static List<Map<String,String>> getHotSearchData(String jsonobj) {
        List<Map<String,String>> spList = new ArrayList<Map<String,String>>();
        Map<String,String> hotSearch;
        try {
            Object isobj = jsonobj;
//            Object isobj = msgRspJson.get("localSearchGoods");
            if (isobj instanceof JSONArray) {
                JSONArray arr = (JSONArray) isobj;
                for (int i = 0; i < arr.length(); i++) {
                    hotSearch = new HashMap<>();
                    JSONObject objdetail = arr.getJSONObject(i);
//                    spinfo.setShopName(objdetail.getString("shopname"));
//                    spinfo.setGoodsImg(objdetail.getString("goodsimage"));
//                    spinfo.setMarketPrice(objdetail.getString("marketprice"));
//                    spinfo.setPrice(objdetail.getString("price_money"));
//                    spinfo.setGoodsId(objdetail.getString("goodsid"));
//                    spinfo.setGoodsName(objdetail.getString("goodsname"));
//                    spinfo.setSales(objdetail.getString("sales"));
                    spList.add(hotSearch);
                }
            } else {
                JSONObject objdetail = (JSONObject) isobj;
                hotSearch = new HashMap<>();
//                spinfo.setShopName(objdetail.getString("shopname"));
//                spinfo.setGoodsImg(objdetail.getString("goodsimage"));
//                spinfo.setMarketPrice(objdetail.getString("marketprice"));
//                spinfo.setPrice(objdetail.getString("price_money"));
//                spinfo.setGoodsId(objdetail.getString("goodsid"));
//                spinfo.setGoodsName(objdetail.getString("goodsname"));
//                spinfo.setSales(objdetail.getString("sales"));
                spList.add(hotSearch);
            }
            return spList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return spList;
    }
    /**
     * 首页商品一，而级分类
     * @param jsonobj  返回数据
     * @return
     */
    public  List<GoodsTypeEntity> getGoodsTypeData(Object jsonobj) throws JSONException {
        List<GoodsTypeEntity> goodsTypeList = new ArrayList<GoodsTypeEntity>();
        List<GoodsTypeEntity> childrengoodsTypeList= null;
        GoodsTypeEntity goodsType;
        Logs.i("解析返回json====="+jsonobj);
        Object obj;
        try {
            obj =  (JSONObject) jsonobj;
        }catch (ClassCastException e){
            obj =  (JSONArray) jsonobj;
        }

//        JSONObject aaa = (JSONObject) jsonobj;
//            Object isobj = msgRspJson.get("localSearchGoods");
        if (obj instanceof JSONArray) {
            JSONArray arr = (JSONArray) obj;
            for (int i = 0; i < arr.length(); i++) {
                goodsType = new GoodsTypeEntity();
                JSONObject objdetail = arr.getJSONObject(i);
                goodsType.setCatNo(objdetail.getString("catNo"));
                goodsType.setName(objdetail.getString("name"));
                goodsType.setParentId(objdetail.getString("parentId"));
                goodsType.setId(objdetail.getString("id"));
                if(objdetail.get("children") != null){
                    childrengoodsTypeList = new ArrayList<GoodsTypeEntity>();
                    GoodsTypeEntity CgoodsType;

                    Object cobj = objdetail.get("children");
                    if(cobj instanceof String){
                        //空字符串什么都不做
                    }else if(cobj instanceof JSONObject){
                        CgoodsType = new GoodsTypeEntity();
                        JSONObject Cobjdetail = (JSONObject) cobj;
                        CgoodsType.setCatNo(Cobjdetail.getString("catNo"));
                        CgoodsType.setName(Cobjdetail.getString("name"));
                        CgoodsType.setParentId(Cobjdetail.getString("parentId"));
                        CgoodsType.setId(Cobjdetail.getString("id"));
                        childrengoodsTypeList.add(CgoodsType);
                    }else if(cobj instanceof JSONArray){
                        JSONArray childrenarr = objdetail.getJSONArray("children");
                        for (int c = 0; c < childrenarr.length(); c++) {
                            CgoodsType = new GoodsTypeEntity();
                            JSONObject Cobjdetail = childrenarr.getJSONObject(c);
                            CgoodsType.setCatNo(Cobjdetail.getString("catNo"));
                            CgoodsType.setName(Cobjdetail.getString("name"));
                            CgoodsType.setParentId(Cobjdetail.getString("parentId"));
                            CgoodsType.setId(Cobjdetail.getString("id"));
                            childrengoodsTypeList.add(CgoodsType);
                        }
                    }
                }
                goodsType.setGoodsTypeList(childrengoodsTypeList);
                goodsTypeList.add(goodsType);
            }
        } else if(obj instanceof JSONObject){
            JSONObject objdetail = (JSONObject) obj;
            goodsType = new GoodsTypeEntity();
            goodsType.setCatNo(objdetail.getString("catNo"));
            goodsType.setName(objdetail.getString("name"));
            goodsType.setParentId(objdetail.getString("parentId"));
            goodsType.setId(objdetail.getString("id"));

            if(objdetail.get("children") != null){
                childrengoodsTypeList = new ArrayList<GoodsTypeEntity>();
                GoodsTypeEntity CgoodsType;
                JSONArray childrenarr = objdetail.getJSONArray("children");
                for (int c = 0; c < childrenarr.length(); c++) {
                    CgoodsType = new GoodsTypeEntity();
                    JSONObject Cobjdetail = childrenarr.getJSONObject(c);
                    CgoodsType.setCatNo(Cobjdetail.getString("catNo"));
                    CgoodsType.setName(Cobjdetail.getString("name"));
                    CgoodsType.setParentId(Cobjdetail.getString("parentId"));
                    CgoodsType.setId(Cobjdetail.getString("id"));
                    childrengoodsTypeList.add(CgoodsType);
                }
            }
            goodsType.setGoodsTypeList(childrengoodsTypeList);
            goodsTypeList.add(goodsType);
        }
        return goodsTypeList;
    }

    /**
     * 解析用户个人信息
     * @param jsonObj
     * @return
     * @throws JSONException
     */
    public UserInfoEntity  getUserInfo(Object jsonObj) throws JSONException {
        Gson gson = new Gson();
        return gson.fromJson(jsonObj.toString(),UserInfoEntity.class);

    }
    public ResponseDataUserInfoEntity getGYSUserInfo(Object jsonObj) throws JSONException {
        Gson gson = new Gson();
        return gson.fromJson(jsonObj.toString(),ResponseDataUserInfoEntity.class);

    }
    public GsonResponseDataEntity getDataGson(Object jsonObj) throws JSONException {
        Gson gson = new Gson();
        return gson.fromJson(jsonObj.toString(),GsonResponseDataEntity.class);

    }
    // 将Json数据解析成相应的映射对象
    public  <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Logs.i("输出GSON数据=====",jsonData);
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }
    // 将Json数组解析成相应的映射对象列表
    public  <T> List<T> parseJsonArrayWithGson(String jsonData,Class<T> type) {
        Logs.i("输出GSON数据=======",jsonData);
        Gson gson = new Gson();
        List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
        }.getType());
        return result;
    }

    /**
     * 解析返回数据数组json数据
     * @param strByJson
     * @param type
     * @param <T>
     * @return
     */
    public  <T> List<T> parseJsonArray(String strByJson,Class<T> type) {
        List<T> userBeanList = new ArrayList<>();
        try {
            //拿到本地JSON 并转成String
            //Json的解析类对象
            JsonParser parser = new JsonParser();
            //将JSON的String 转成一个JsonArray对象
            JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();

            Gson gson = new Gson();


            //加强for循环遍历JsonArray
            for (JsonElement user : jsonArray) {
                //使用GSON，直接转成Bean对象
                T userBean = gson.fromJson(user, type);
                userBeanList.add(userBean);
            }
        }catch (Exception e){
            return userBeanList;
        }
        return userBeanList;
    }

}
