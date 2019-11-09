package com.gdn.onestop.service;

import com.gdn.onestop.entity.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LibraryService {
    boolean storeBook(MultipartFile file, String title);
    List<Book> getBookAfterDate(Long time);
}
