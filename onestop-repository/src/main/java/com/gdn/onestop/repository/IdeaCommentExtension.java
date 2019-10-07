package com.gdn.onestop.repository;

import com.gdn.onestop.entity.IdeaComment;

import java.util.List;

public interface IdeaCommentExtension {
    List<IdeaComment.CommentUnit> getPaginatedCommentById(String id, int page, int itemPerPage);
}
