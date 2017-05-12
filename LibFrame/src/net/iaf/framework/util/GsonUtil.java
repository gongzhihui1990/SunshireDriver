package net.iaf.framework.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {
    }

    /**
     * 将对象转换成json格式
     *
     * @param ts
     * @return
     */
    public static String objectToJson(Object ts) {
        String jsonStr = null;
        if (gson != null) {
            jsonStr = gson.toJson(ts);
        }
        return jsonStr;
    }

    /**
     * 将对象转换成json格式(并自定义日期格式)
     */
    public static String objectToJsonDateSerializer(Object obj,
                                                    final String dateformat) throws JsonSyntaxException {
        String jsonStr = null;
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(
                        Date.class
                        ,
                        new JsonSerializer<Date>() {
                            public JsonElement serialize(Date src,
                                                         Type typeOfSrc,
                                                         JsonSerializationContext context) {
                                SimpleDateFormat format = new SimpleDateFormat(
                                        dateformat);
                                return new JsonPrimitive(format.format(src));
                            }
                        }
                )
                .setDateFormat(dateformat)
                .create();
        if (gson != null) {
            jsonStr = gson.toJson(obj);
        }
        return jsonStr;
    }

    /**
     * 将json格式转换成list对象
     *
     * @param jsonStr
     * @return
     */
    public static List<?> jsonToList(String jsonStr)
            throws JsonSyntaxException {
        List<?> objList = null;
        if (gson != null) {
            java.lang.reflect.Type type =
                    new com.google.gson.reflect.TypeToken<List<?>>() {
                    }.getType();
            objList = gson.fromJson(jsonStr, type);
        }
        if (null == objList) {
            throw new JsonSyntaxException("");
        }
        return objList;
    }

    /**
     * 将json格式转换成list对象，并准确指定类型
     *
     * @param jsonStr
     * @param type
     * @return
     */
    public static List<?> jsonToList(String jsonStr, java.lang.reflect.Type type)
            throws JsonSyntaxException {
        List<?> objList = null;
        if (gson != null) {
            objList = gson.fromJson(jsonStr, type);
        }
        if (null == objList) {
            throw new JsonSyntaxException("");
        }
        return objList;
    }

    /**
     * 将json格式转换成map对象
     *
     * @param jsonStr
     * @return
     */
    public static Map<?, ?> jsonToMap(String jsonStr) throws JsonSyntaxException {
        Map<?, ?> objMap = null;
        if (gson != null) {
            java.lang.reflect.Type type =
                    new com.google.gson.reflect.TypeToken<Map<?, ?>>() {
                    }.getType();
            objMap = gson.fromJson(jsonStr, type);
        }
        if (null == objMap) {
            throw new JsonSyntaxException("");
        }
        return objMap;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(String jsonStr, Class<T> clas)
            throws JsonSyntaxException {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonStr, clas);
        }
        if (null == obj) {
            throw new JsonSyntaxException("");
        }
        return (T) obj;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonElement
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBean(JsonElement jsonElement, Class<T> clas)
            throws JsonSyntaxException {
        Object obj = null;
        if (gson != null) {
            obj = gson.fromJson(jsonElement, clas);
        }
        if (null == obj) {
            throw new JsonSyntaxException("");
        }
        return (T) obj;
    }

    /**
     * 将json转换成bean对象
     *
     * @param jsonStr
     * @param cl
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToBeanDateSerializer(String jsonStr, Class<T> clas,
                                                 final String dateformat) throws JsonSyntaxException {
        Object obj = null;
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class,
                        new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                                    throws JsonParseException {
                                SimpleDateFormat format = new SimpleDateFormat(dateformat);
                                String dateStr = json.getAsString();
                                try {
                                    return format.parse(dateStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        }
                )
                .setDateFormat(dateformat)
                .create();
        if (gson != null) {
            obj = gson.fromJson(jsonStr, clas);
        }
        if (null == obj) {
            throw new JsonSyntaxException("");
        }
        return (T) obj;
    }

    /**
     * 根据key取json对应Map相关的value值
     *
     * @param jsonStr
     * @param key
     * @return
     */
    public static Object getJsonValue(String jsonStr, String key) throws JsonSyntaxException {
        Object rulsObj = null;
        Map<?, ?> rulsMap = jsonToMap(jsonStr);
        if (rulsMap != null && rulsMap.size() > 0) {
            rulsObj = rulsMap.get(key);
        }
        return rulsObj;
    }

}  
