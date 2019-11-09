package com.gdn.onestop.repository;

import com.gdn.onestop.entity.Book;

import java.util.Date;
import java.util.List;

public interface BookRepositoryExtension {
    Date getLastUpdate();
    List<Book> getBookAfterTime(Date afterTime);
}
