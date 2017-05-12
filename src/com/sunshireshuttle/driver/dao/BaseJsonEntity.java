package com.sunshireshuttle.driver.dao;

import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import net.iaf.framework.exception.JsonServerException;
import net.iaf.framework.exception.ServerException;
import net.iaf.framework.util.GsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

public abstract class BaseJsonEntity<T> implements Serializable, Cloneable {

    public static final String CODE_SUCCESS = "0";

    private static final long serialVersionUID = 1L;

    private Class<T> entityClass;

    //--------------------------------------------------------------------
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;
    //--------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public T parseJson2Entity(String jsonStr) throws ServerException {
//Loger.v(jsonStr);
        T jsonEntity;
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (!jsonObject.getString("code").equals(CODE_SUCCESS)) {
                throw new ServerException(jsonObject.getString("code"), jsonObject.getString("message"));
            }
        } catch (JSONException e) {
            throw new JsonServerException();
        }
        try {
            entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            jsonEntity = GsonUtil.jsonToBean(jsonStr, entityClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JsonServerException();
        }
        return jsonEntity;
    }

    /**
     * 请求的url
     */
    public abstract String getUrl();

    /**
     * 缓存时间
     */
    public abstract int getCacheTime();


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
