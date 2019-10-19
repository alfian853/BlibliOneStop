package com.gdn.onestop.repository;

import com.gdn.onestop.entity.GroupPost;

public interface GroupRepositoryExtension {

    GroupPost getPaginatedPost(String groupId, int page, int itemPerPage);
}
