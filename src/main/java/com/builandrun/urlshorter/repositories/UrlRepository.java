package com.builandrun.urlshorter.repositories;

import com.builandrun.urlshorter.entities.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {
}
