package com.gdn.onestop.repository;

import com.gdn.onestop.entity.Audio;
import com.gdn.onestop.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AudioRepository extends MongoRepository<Audio, String>, AudioRepositoryExtension {
}
