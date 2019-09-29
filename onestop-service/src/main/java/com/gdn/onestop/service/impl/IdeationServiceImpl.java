package com.gdn.onestop.service.impl;

import com.gdn.onestop.dto.CommentDto;
import com.gdn.onestop.dto.IdeaPostDto;
import com.gdn.onestop.entity.IdeaPost;
import com.gdn.onestop.repository.AdvancedQuery;
import com.gdn.onestop.repository.enums.IdeaEntitiyField;
import com.gdn.onestop.request.IdeationCommentRequest;
import com.gdn.onestop.request.IdeationRequest;
import com.gdn.onestop.service.IdeationService;
import com.gdn.onestop.repository.IdeationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IdeationServiceImpl implements IdeationService {

    @Autowired
    IdeationRepository ideationRepository;

    private IdeaPostDto mapPostToDto(IdeaPost post){
        return IdeaPostDto.builder()
                .id(post.getId())
                .username(post.getUsername())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .totalComment(post.getTotalComment())
                .voteUp(post.getVoteUp())
                .voteDown(post.getVoteDown())
                .build();

    }

    @Override
    public IdeaPostDto addIdeation(IdeationRequest request) {
        IdeaPost post = new IdeaPost();
        post.setVoteUp(0);
        post.setVoteDown(0);
        post.setComments(new LinkedList<>());
        post.setContent(request.getContent());
        post.setTotalComment(0);
        post.setUsername("user");
        ideationRepository.save(post);

        return mapPostToDto(post);
    }

    @Override
    public List<IdeaPostDto> getIdeations(int page, int itemPerPage) {
        Page<IdeaPost> pageIdea = ideationRepository.findByQuery(
                AdvancedQuery.builder()
                        .direction(Sort.Direction.DESC)
                        .sortBy(IdeaEntitiyField.CREATED_AT)
                        .page(page)
                        .size(itemPerPage)
                        .search("user")
                        .build()
        );

        return pageIdea.getContent()
                .stream()
                .map(this::mapPostToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(IdeationCommentRequest request) {
        return null;
    }
}
