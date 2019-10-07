package com.gdn.onestop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class CommentDto {
    String username;
    String text;
    Date date;
}
