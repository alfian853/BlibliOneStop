package com.gdn.onestop.repository;

import com.gdn.onestop.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String>, BookRepositoryExtension {
}
