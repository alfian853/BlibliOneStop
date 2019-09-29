package com.gdn.onestop.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentDto {
    String username;
    String text;
    Integer likes;
}
