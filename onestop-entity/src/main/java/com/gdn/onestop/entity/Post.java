package com.gdn.onestop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    String id;

    String username;
    String content;

    Integer totalComment;

    @CreatedDate
    Date createdAt;

}


