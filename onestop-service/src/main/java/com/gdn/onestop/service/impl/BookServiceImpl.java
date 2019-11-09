package com.gdn.onestop.service.impl;

import com.gdn.onestop.entity.Book;
import com.gdn.onestop.repository.BookRepository;
import com.gdn.onestop.service.FileStorageService;
import com.gdn.onestop.service.LibraryService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.gdn.onestop.service.FileStorageService.PathCategory.BOOKS;
import static com.gdn.onestop.service.FileStorageService.PathCategory.BOOK_THUMBNAIL;

@Service
public class BookServiceImpl implements LibraryService {


    private Long lastUpload = 0L;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    BookRepository bookRepository;

    @PostConstruct
    public void setLastUpdate() {
        lastUpload = bookRepository.getLastUpdate().getTime();
    }

    @Override
    public boolean storeBook(MultipartFile file, String title) {

        String baseFilename = title+"-"+UUID.randomUUID().toString().substring(0,5);
        String filename = baseFilename+".pdf";
        boolean isSuccess = fileStorageService.storeFile(file, filename, BOOKS);
        PDDocument document = null;
        try {
            document = PDDocument.load(file.getInputStream());
            isSuccess |= fileStorageService.createThumbnailFromDocument(
                    document, baseFilename+".jpg",
                    FileStorageService.PathCategory.BOOK_THUMBNAIL
            );
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        if(isSuccess){
            Book book = new Book();
            book.setTitle(title);
            book.setPath(BOOKS.getPath()+"/"+filename);
            book.setThumbnail(BOOK_THUMBNAIL.getPath()+"/"+baseFilename+".jpg");
            book.setFileSize(file.getSize());
            book.setTotalPages(document.getNumberOfPages());
            bookRepository.save(book);
            lastUpload = Math.max(lastUpload, book.getCreatedAt().getTime());
        }

        return isSuccess;
    }

    @Override
    public List<Book> getBookAfterDate(Long time) {
        if(time < lastUpload){
            return bookRepository.getBookAfterTime(new Date(time));
        }
        else{
            return Collections.emptyList();
        }
    }

}
