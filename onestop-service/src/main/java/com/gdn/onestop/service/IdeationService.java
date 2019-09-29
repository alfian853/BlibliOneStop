package com.gdn.onestop.service;

import com.gdn.onestop.dto.CommentDto;
import com.gdn.onestop.dto.IdeaPostDto;
import com.gdn.onestop.request.IdeationCommentRequest;
import com.gdn.onestop.request.IdeationRequest;

import java.util.List;

public interface IdeationService {

    IdeaPostDto addIdeation(IdeationRequest request);
    List<IdeaPostDto> getIdeations(int page, int itemPerPage);
    CommentDto addComment(IdeationCommentRequest request);
}
