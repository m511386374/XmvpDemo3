package com.huitouwuyou.huitou.xmvpdemo.JsonCallback;

import com.google.gson.stream.JsonReader;
import com.lzy.okgo.convert.Converter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/9/11
 * 描    述：
 * -
 * -
 * -
 * -该类主要用于 OkRx 调用，直接解析泛型，返回数据对象，若不用okrx，可以删掉该类
 * -
 * -
 * -
 * 修订历史：
 * ================================================
 */
public class JsonConvert<T> implements Converter<T> {


    @Override
    public T convertResponse (Response response) throws Exception {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        if (!(type instanceof ParameterizedType)) throw new IllegalStateException("没有填写泛型参数");
        Type rawType = ((ParameterizedType) type).getRawType();
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        JsonReader jsonReader = new JsonReader(response.body().charStream());
        if (typeArgument == Void.class) {
            SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
            response.close();
            return (T) simpleResponse.toLzyResponse();
        } else if (rawType == LzyResponse.class) {
            LzyResponse lzyResponse = Convert.fromJson(jsonReader, type);
            response.close();
            boolean code = lzyResponse.code;
            if (!code) {
                return (T) lzyResponse;
            } else {
                throw new IllegalStateException("错误代码：");
            }
        } else {
            response.close();
            throw new IllegalStateException("基类错误无法解析!");
        }
    }


}