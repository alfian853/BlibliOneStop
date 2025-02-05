package com.gdn.onestop.service.impl;

import com.gdn.onestop.dto.CommentDto;
import com.gdn.onestop.dto.IdeaPostDto;
import com.gdn.onestop.entity.IdeaComment;
import com.gdn.onestop.entity.IdeaPost;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.repository.AdvancedQuery;
import com.gdn.onestop.repository.IdeaCommentRepository;
import com.gdn.onestop.repository.IdeationRepository;
import com.gdn.onestop.repository.enums.IdeaEntitiyField;
import com.gdn.onestop.request.IdeationRequest;
import com.gdn.onestop.service.GameService;
import com.gdn.onestop.service.IdeationService;
import com.gdn.onestop.service.UserService;
import com.gdn.onestop.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IdeationServiceImpl implements IdeationService {

    @Autowired
    IdeationRepository ideationRepository;

    @Autowired
    IdeaCommentRepository commentRepository;

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;

    private IdeaPostDto mapPostToDto(User user, IdeaPost post){
        String username = user.getUsername();
        int stat = 0;
        Boolean isVoteUp = post.getVoter().getOrDefault(username,null);
        if(isVoteUp != null){
            stat = (isVoteUp)? 2 : 1;
        }
        return IdeaPostDto.builder()
                .id(post.getId())
                .username(post.getUsername())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .commentCount(post.getTotalComment())
                .upVoteCount(post.getUpVoteCount())
                .downVoteCount(post.getDownVoteCount())
                .isMeVoteUp(stat == 2)
                .getIsMeVoteDown(stat == 1)
                .build();

    }

    @Override
    public IdeaPostDto addIdea(User user, IdeationRequest request) {
        String username = user.getUsername();

        IdeaPost post = new IdeaPost();
        post.setUpVoteCount(0);
        post.setDownVoteCount(0);
        post.setContent(request.getContent());
        post.setTotalComment(0);
        post.setUsername(username);
        ideationRepository.save(post);
        commentRepository.save(IdeaComment.builder().id(post.getId()).comments(new LinkedList<>()).build());

        gameService.onIdeationPosted(user);

        return mapPostToDto(user, post);
    }

    @Override
    public List<IdeaPostDto> getIdeas(User user, int page, int itemPerPage) {
        Page<IdeaPost> pageIdea = ideationRepository.findByQuery(
                AdvancedQuery.create()
                        .page(page)
                        .size(itemPerPage)
                        .addSort(IdeaEntitiyField.CREATED_AT, Sort.Direction.DESC)
        );

        return pageIdea.getContent()
                .stream()
                .map(post -> this.mapPostToDto(user, post))
                .collect(Collectors.toList());
    }

    private CommentDto mapToCommentDto(String postId, IdeaComment.CommentUnit ideaComment){
        return CommentDto.builder()
                .postId(postId)
                .username(ideaComment.getUsername())
                .text(ideaComment.getText())
                .date(ideaComment.getDate())
                .build();
    }

    @Override
    public CommentDto addComment(User user, String id, String comment) {
        String username = user.getUsername();

        IdeaPost ideaPost = ideationRepository.findById(id).orElseThrow(NotFoundException::new);
        IdeaComment ideaComment = commentRepository.findById(id)
                .orElseGet( ()-> IdeaComment.builder().id(ideaPost.getId()).build() );

        ideaPost.setTotalComment(ideaPost.getTotalComment()+1);

        IdeaComment.CommentUnit commentUnit = IdeaComment.CommentUnit.builder()
                .username(username).date(new Date())
                .text(comment)
                .build();
        ideaComment.getComments().addFirst(commentUnit);

        ideationRepository.save(ideaPost);
        commentRepository.save(ideaComment);

        gameService.onIdeationCommented(user);

        return mapToCommentDto(id, commentUnit);
    }

    @Override
    public List<IdeaComment.CommentUnit> getComments(String postId, int page, int itemPerPage) {
        return commentRepository.getPaginatedCommentById(postId, page, itemPerPage);
    }

    @Override
    public boolean voteIdea(User user, String id, boolean isVoteUp) {
        IdeaPost ideaPost = ideationRepository.findById(id).orElseThrow(NotFoundException::new);

        String username = user.getUsername();

        if(ideaPost.getVoter().containsKey(username)){
            boolean isUpVoteBefore = ideaPost.getVoter().get(username);

            if(isUpVoteBefore){
                ideaPost.setUpVoteCount(ideaPost.getUpVoteCount()-1);
            }
            else{
                ideaPost.setDownVoteCount(ideaPost.getDownVoteCount()-1);
            }

            if(isVoteUp == isUpVoteBefore){
                ideaPost.getVoter().remove(username);
                ideationRepository.save(ideaPost);
                return true;
            }
        }

        if(isVoteUp){
            ideaPost.setUpVoteCount(ideaPost.getUpVoteCount()+1);
            ideaPost.getVoter().put(username, true);
        }
        else{
            ideaPost.setDownVoteCount(ideaPost.getDownVoteCount()+1);
            ideaPost.getVoter().put(username, false);
        }

        ideationRepository.save(ideaPost);

        return true;
    }

    @Override
    public Map<String, Boolean> getIdeaVoter(String id) {
        IdeaPost ideaPost = ideationRepository.findById(id).orElseThrow(NotFoundException::new);
        return ideaPost.getVoter();
    }

    public List<IdeaPostDto> getTopPostByUsername(User user, String targetUsername) {
        Page<IdeaPost> pageIdea = ideationRepository.findByQuery(
                AdvancedQuery.create()
                        .page(0)
                        .size(10)
                        .addSearch(IdeaEntitiyField.USERNAME, targetUsername)
                        .addSort(IdeaEntitiyField.UP_VOTE, Sort.Direction.DESC)
        );

        return pageIdea.getContent()
                .stream()
                .map(post -> this.mapPostToDto(user, post))
                .collect(Collectors.toList());
    }
}
