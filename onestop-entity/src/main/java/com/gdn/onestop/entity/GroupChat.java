package com.gdn.onestop.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class GroupChat {

    @Id
    String id;

    List<Chat> chats;

}
