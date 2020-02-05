package com.gdn.onestop.entity;

import com.gdn.onestop.model.GroupChatModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class GroupChat {

    @Id
    String id;

    List<GroupChatModel> chats;

}
