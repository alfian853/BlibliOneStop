package com.gdn.onestop.response;

public class ResponseHelper{

    private static String OK_STATUS = "OK";
    private static int OK_CODE = 200;

    public static <T> Response<T> isOk(T data){
        return Response.<T>builder()
                .code(OK_CODE)
                .status(OK_STATUS)
                .data(data)
                .build();
    }
}
