package com.gdn.onestop.service;

import com.gdn.onestop.dto.CommentDto;
import com.gdn.onestop.dto.IdeaPostDto;
import com.gdn.onestop.entity.IdeaComment;
import com.gdn.onestop.request.IdeationRequest;

import java.util.List;
import java.util.Map;

public interface IdeationService {

    IdeaPostDto addIdea(IdeationRequest request);
    List<IdeaPostDto> getIdeas(int page, int itemPerPage);

    CommentDto addComment(String id, String comment);
    List<IdeaComment.CommentUnit> getComments(String postId, int page, int itemPerPage);

    boolean voteIdea(String id, boolean isVoteUp);

    // map<username,isVoteUp>
    Map<String, Boolean> getIdeaVoter(String id);
}
