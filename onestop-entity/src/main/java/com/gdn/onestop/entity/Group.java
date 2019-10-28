package com.gdn.onestop.entity;

import com.gdn.onestop.model.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("group")
public class Group {

    @Id
    String id;
    String name;
    String groupCode;

    GroupType type;

    List<String> members;


}
