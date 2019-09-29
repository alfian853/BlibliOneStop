package com.gdn.onestop.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PagedResponse<T> extends Response<T> {

    @JsonProperty("data_list")
    List<T> dataList;
    
    private Integer page;

    @JsonProperty("total_page")
    private Integer totalPage;

    @JsonProperty("item_per_page")
    private Integer itemPerPage;

    @JsonProperty("total_item")
    private Integer totalItem;
}
