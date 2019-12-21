package com.gdn.onestop.repository;

import com.gdn.onestop.repository.enums.MongoEntityField;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;

@Data
public class AdvancedQuery {
    int page;
    int size;

    List<SortField> sortFields = new LinkedList<>();
    List<SearchField> searchFields = new LinkedList<>();

    public static AdvancedQuery create(){
        return new AdvancedQuery();
    }

    public AdvancedQuery page(int page){
        this.page = page;
        return this;
    }

    public AdvancedQuery size(int size){
        this.size = size;
        return this;
    }

    @Data
    class SortField {
        MongoEntityField field;
        Sort.Direction direction = Sort.Direction.ASC;

        SortField(MongoEntityField field, Sort.Direction direction){
            this.field = field;
            this.direction = direction;
        }

        SortField(MongoEntityField field){
            this.field = field;
        }
    }

    @Data
    class SearchField{

        MongoEntityField field;
        Object search;
        boolean isRegex = false;

        SearchField(MongoEntityField field, Object search, boolean isRegex){
            this.field = field;
            this.search = search;
            this.isRegex = isRegex;
        }
    }

    public AdvancedQuery addSearch(MongoEntityField field, Object search){
        return addSearch(field, search, false);
    }

    public AdvancedQuery addSearch(MongoEntityField field, Object search, boolean isRegex){
        this.searchFields.add(new SearchField(field, search, isRegex));
        return this;
    }

    public AdvancedQuery addSort(MongoEntityField field){
        addSort(field, Sort.Direction.ASC);

        return this;
    }

    public AdvancedQuery addSort(MongoEntityField field, Sort.Direction direction){
        this.sortFields.add(new SortField(field, direction));

        return this;
    }
}
