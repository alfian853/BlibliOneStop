package com.gdn.onestop.repository;

import com.gdn.onestop.repository.enums.MongoEntityField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

public interface RepositoryExtension<ENTITY> {

    MongoTemplate getMongoTemplate();
    Class<ENTITY> getEntityClass();
    List<? extends MongoEntityField> getFieldList();

    default Page<ENTITY> findByQuery(AdvancedQuery requestQuery, Criteria... additionalCriteria) {
        Pageable pageable = PageRequest.of(requestQuery.getPage(), requestQuery.getSize());
        Query query = new Query();

        List<Criteria> andCriteria = new ArrayList<>();
        boolean emptyCriteria = requestQuery.getSearchFields().size() == 0;

        requestQuery.getSearchFields().forEach( searchField -> {
            if(searchField.isRegex){
                andCriteria.add(Criteria.where(searchField.getField().getField()).is(searchField.search));
            }
            else {
                andCriteria.add(Criteria.where(searchField.getField().getField())
                        .regex(searchField.getSearch().toString(), "i"));
            }
        });

        Criteria orCriteria = new Criteria().orOperator(andCriteria.toArray(new Criteria[0]));

        if(additionalCriteria.length > 0){
            for (Criteria additionalCriterion : additionalCriteria) {
                orCriteria.andOperator(additionalCriterion);
            }
            emptyCriteria = false;
        }
        if(!emptyCriteria){
            query.addCriteria(orCriteria);
        }
        query.with(pageable);

        if(!requestQuery.getSortFields().isEmpty()){
            AdvancedQuery.SortField sortField = requestQuery.getSortFields().get(0);
            Sort sort = new Sort(sortField.getDirection(), sortField.field.getField());

            int len = requestQuery.getSortFields().size();
            if(len > 1){
                for(int i=1; i < len; ++i){
                    sortField = requestQuery.getSortFields().get(i);
                    sort.and(new Sort(sortField.getDirection(), sortField.field.getField()));
                }
            }

            query.with(sort);
        }

        List<ENTITY> list = this.getMongoTemplate().find(query, this.getEntityClass());
        return PageableExecutionUtils.getPage(list, pageable,
            ()-> getMongoTemplate().count(query, this.getEntityClass()));
    }
}
