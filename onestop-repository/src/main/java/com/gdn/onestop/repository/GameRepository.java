package com.gdn.onestop.repository;

import com.gdn.onestop.entity.UserGame;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<UserGame, String> {
}
