package com.gdn.onestop.response;

public class ResponseHelper{

    private static String OK_STATUS = "OK";
    private static String NOT_OK_STATUS = "NOT_OK";
    private static int CODE = 200;

    public static <T> Response<T> isOk(T data){
        return Response.<T>builder()
                .code(CODE)
                .status(OK_STATUS)
                .data(data)
                .build();
    }

    public static <T> Response<T> isNotOk(){
        return Response.<T>builder()
                .code(CODE)
                .status(NOT_OK_STATUS)
                .build();

    }
}
