package com.gdn.onestop.web.controller;

import com.gdn.onestop.entity.Audio;
import com.gdn.onestop.entity.Book;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.LibraryService;
import com.gdn.onestop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
public class LibraryController {

    @Autowired
    LibraryService libraryService;

    @Autowired
    UserService userService;

    @PostMapping("/book/upload")
    Response<Boolean> uploadBook(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title
    ){

        return ResponseHelper.isOk(
                libraryService.storeBook(file, title)
        );
    }

    @GetMapping("/book")
    Response<List<Book>> getBookAfterDate(
            @RequestParam("after_time") Long time
    ){
        return ResponseHelper.isOk(
                libraryService.getBookAfterDate(time)
        );
    }

    @PostMapping("/audio/upload")
    Response<Boolean> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title){

        return ResponseHelper.isOk(
                libraryService.storeAudio(file, title)
        );
    }

    @GetMapping("/audio")
    Response<List<Audio>> getAudioAfterDate(
            @RequestParam("after_time") Long time
    ){
        return ResponseHelper.isOk(
                libraryService.getAudioAfterDate(time)
        );
    }

    @PostMapping("/book/{id}/finish")
    Response<Boolean> finishBook(@PathVariable("id") String bookId){
        libraryService.setBookFinished(
                userService.getUserBySession(),
                bookId
        );

        return ResponseHelper.isOk(true);
    }

    @PostMapping("/audio/{id}/finish")
    Response<Boolean> finishAudio(@PathVariable("id") String audioId){
        libraryService.setAudioFinished(
                userService.getUserBySession(),
                audioId
        );

        return ResponseHelper.isOk(true);
    }


}
