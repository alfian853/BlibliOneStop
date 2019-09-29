package com.gdn.onestop.repository;

import com.gdn.onestop.repository.enums.MongoEntityField;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
@Builder
public class AdvancedQuery {
    int page;
    int size;
    MongoEntityField sortBy;
    Sort.Direction direction = Sort.Direction.ASC;
    String search = "";
}
