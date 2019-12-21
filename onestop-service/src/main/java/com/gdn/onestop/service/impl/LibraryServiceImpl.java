package com.gdn.onestop.service.impl;

import com.gdn.onestop.entity.Audio;
import com.gdn.onestop.entity.Book;
import com.gdn.onestop.entity.User;
import com.gdn.onestop.repository.AudioRepository;
import com.gdn.onestop.repository.BookRepository;
import com.gdn.onestop.service.FileStorageService;
import com.gdn.onestop.service.GameService;
import com.gdn.onestop.service.LibraryService;
import org.apache.commons.io.FilenameUtils;
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

import static com.gdn.onestop.service.FileStorageService.PathCategory.*;

@Service
public class LibraryServiceImpl implements LibraryService {


    private Long bookLastUpload = 0L;

    private Long audioLastUpload = 0L;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AudioRepository audioRepository;

    @Autowired
    GameService gameService;

    @PostConstruct
    public void setLastUpdate() {
        bookLastUpload = bookRepository.getLastUpdate().getTime();
        audioLastUpload = audioRepository.getLastUpdate().getTime();
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
            bookLastUpload = Math.max(bookLastUpload, book.getCreatedAt().getTime());
        }

        return isSuccess;
    }

    @Override
    public boolean storeAudio(MultipartFile file, String title) {
        String baseFilename = title+"-"+UUID.randomUUID().toString().substring(0,5);
        String filename = baseFilename +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
        boolean isSuccess = fileStorageService.storeFile(file, filename, AUDIOS);

        if(isSuccess){
            Audio audio = new Audio();
            audio.setTitle(title);
            audio.setPath(AUDIOS.getPath()+"/"+filename);
            audio.setFileSize(file.getSize());

            audioRepository.save(audio);
            audioLastUpload = Math.max(audioLastUpload, audio.getCreatedAt().getTime());
        }

        return isSuccess;
    }

    @Override
    public List<Book> getBookAfterDate(Long time) {
        if(time < bookLastUpload){
            return bookRepository.getBookAfterTime(new Date(time));
        }
        else{
            return Collections.emptyList();
        }
    }

    @Override
    public List<Audio> getAudioAfterDate(Long time) {
        if(time < audioLastUpload){
            return audioRepository.getAudioAfterTime(new Date(time));
        }
        else{
            return Collections.emptyList();
        }
    }

    @Override
    public void setBookFinished(User user, String bookId) {
        gameService.onBookReadFinished(user, bookId);

    }

    @Override
    public void setAudioFinished(User user, String audioId) {
        gameService.onAudioListened(user, audioId);
    }

}
