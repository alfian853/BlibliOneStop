package com.gdn.onestop.repository.impl;

import com.gdn.onestop.entity.Book;
import com.gdn.onestop.repository.BookRepositoryExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Date;
import java.util.List;

import static com.gdn.onestop.repository.enums.BookEntityField.CREATED_AT;
import static com.gdn.onestop.repository.enums.BookEntityField.TITLE;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class BookRepositoryExtensionImpl implements BookRepositoryExtension {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Date getLastUpdate() {

        TypedAggregation<Book> agg = newAggregation(Book.class,
                sort(Sort.Direction.DESC, CREATED_AT.getField()),
                limit(1)
        );
        AggregationResults<Book> result = mongoTemplate.aggregate(agg, Book.class);

        Book book = result.getUniqueMappedResult();

        if(book == null){
            return new Date();
        }
        return book.getCreatedAt();
    }

    @Override
    public List<Book> getBookAfterTime(Date afterTime) {
        TypedAggregation<Book> agg = newAggregation(Book.class,
                match(Criteria.where(CREATED_AT.getField()).gt(afterTime))
        );

        AggregationResults<Book> result = mongoTemplate.aggregate(agg, Book.class);

        return result.getMappedResults();
    }
}
