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
        boolean emptyCriteria = true;
        if(requestQuery.getSearch() != null){
            getFieldList().forEach(field -> {
                if(field.isSearchable()){
                    andCriteria.add(Criteria.where(field.getField())
                            .regex(requestQuery.getSearch(),"i"));
                }
            });
            emptyCriteria = false;
        }

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

        if(requestQuery.getSortBy() != null){
            if(!requestQuery.getSortBy().isSortable()){
                throw new RuntimeException(
                        "Field "+requestQuery.getSortBy().getField()+" is not sortable"
                );
            }
            Sort sort = new Sort(requestQuery.getDirection(), requestQuery.getSortBy().getField());
            query.with(sort);
        }

        List<ENTITY> list = this.getMongoTemplate().find(query, this.getEntityClass());
        return PageableExecutionUtils.getPage(list, pageable,
            ()-> getMongoTemplate().count(query, this.getEntityClass()));
    }
}
