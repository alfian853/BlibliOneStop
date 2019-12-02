package com.gdn.onestop.repository.impl;

import com.gdn.onestop.entity.Audio;
import com.gdn.onestop.repository.AudioRepositoryExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Date;
import java.util.List;

import static com.gdn.onestop.repository.enums.AudioEntityField.CREATED_AT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class AudioRepositoryExtensionImpl implements AudioRepositoryExtension {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public Date getLastUpdate() {

        TypedAggregation<Audio> agg = newAggregation(Audio.class,
                sort(Sort.Direction.DESC, CREATED_AT.getField()),
                limit(1)
        );
        AggregationResults<Audio> result = mongoTemplate.aggregate(agg, Audio.class);

        Audio audio = result.getUniqueMappedResult();

        if(audio == null){
            return new Date();
        }
        return audio.getCreatedAt();
    }

    @Override
    public List<Audio> getAudioAfterTime(Date afterTime) {
        TypedAggregation<Audio> agg = newAggregation(Audio.class,
                match(Criteria.where(CREATED_AT.getField()).gt(afterTime))
        );

        AggregationResults<Audio> result = mongoTemplate.aggregate(agg, Audio.class);

        return result.getMappedResults();
    }
}
