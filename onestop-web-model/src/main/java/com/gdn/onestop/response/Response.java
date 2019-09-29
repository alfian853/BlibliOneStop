package com.gdn.onestop.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response<T> {
    private Integer code;
    private String status;
    private T data;

    public Response() {
    }

    public Response(Integer code, String status, T data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }
}
