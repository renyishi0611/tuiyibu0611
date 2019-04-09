package com.none.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
public class TestJson
{
    // json字符串转
    private static String diskListContent =
        "[{\"staff\":\"xxx\",\"userName\":\"xxx\",\"bu\":\"xxx\",\"email\":\"example@163.com\",\"other\":\"xxx\"}]";
    /***
     * json字符串转java List
     * @param rsContent
     * @return
     * @throws Exception
     */
    private static List<Map<String, String>> jsonStringToList(String rsContent) throws Exception
    {
        JSONArray arry = JSONArray.fromObject(rsContent);
        System.out.println("json字符串内容如下");
        System.out.println(arry);
        List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < arry.size(); i++)
        {
            JSONObject jsonObject = arry.getJSONObject(i);
            Map<String, String> map = new HashMap<String, String>();
            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();)
            {
                String key = (String) iter.next();
                String value = jsonObject.get(key).toString();
                map.put(key, value);
            }
            rsList.add(map);
        }
        return rsList;
    }
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        List<Map<String, String>> list1 = jsonStringToList(diskListContent);
        System.out.println("json字符串成map");
        for (Map<String, String> m : list1)
        {
            System.out.println(m);
        }
        System.out.println("map转换成json字符串");
        for (Map<String, String> m : list1)
        {
            JSONArray jsonArray = JSONArray.fromObject(m);
            System.out.println(jsonArray.toString());
        }
        System.out.println("list转换成json字符串");
        JSONArray jsonArray2 = JSONArray.fromObject(list1);
        System.out.println(jsonArray2.toString());
    }
}
