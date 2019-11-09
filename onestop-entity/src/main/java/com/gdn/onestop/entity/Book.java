package com.gdn.onestop.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class Book {

    @Id
    String id;

    String title;
    Integer totalPages;

    String path;
    String thumbnail;

    Long fileSize;

    @CreatedDate
    Date createdAt;

}
