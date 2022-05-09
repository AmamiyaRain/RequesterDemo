package com.javaweb.base.common;


import lombok.Data;

import java.io.Serializable;

/**
 * @author NXY666&YBW
 */
@Data

public final class CommonResponse<T> implements Serializable {


    /**
     * 响应码
     */
    private Integer code;


    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static <T> CommonResponse<T> createSuccess(T data) {
        return CommonResponse.create(data, "success");
    }

    public static <T> CommonResponse<T> createFail(T data) {
        return CommonResponse.create(data, "fail");
    }

    public static <T> CommonResponse<T> success() {
        return CommonResponse.create(null, "success");
    }

    public static <T> CommonResponse<T> fail() {
        return CommonResponse.create(null, "fail");
    }


    public static <T> CommonResponse<T> create(T data, String message) {
        return CommonResponse.create(200, data, message);
    }

    public static <T> CommonResponse<T> create(Integer code, T data, String message) {
        CommonResponse<T> commonResponse = new CommonResponse<>();
        commonResponse.setCode(code);
        commonResponse.setMessage(message);
        commonResponse.setData(data);
        return commonResponse;
    }
}
