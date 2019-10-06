package com.gdn.onestop.service;

import com.gdn.onestop.dto.IdeaPostDto;
import com.gdn.onestop.request.IdeationRequest;

import java.util.List;
import java.util.Map;

public interface IdeationService {

    IdeaPostDto addIdea(IdeationRequest request);
    List<IdeaPostDto> getIdeas(int page, int itemPerPage);
    boolean addComment(String id, String comment);
    boolean voteIdea(String id, boolean isVoteUp);

    // map<username,isVoteUp>
    Map<String, Boolean> getIdeaVoter(String id);
}
