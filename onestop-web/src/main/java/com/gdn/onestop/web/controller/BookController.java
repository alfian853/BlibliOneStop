package com.gdn.onestop.web.controller;


import com.gdn.onestop.entity.Book;
import com.gdn.onestop.response.Response;
import com.gdn.onestop.response.ResponseHelper;
import com.gdn.onestop.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    LibraryService libraryService;

    @PostMapping("/upload")
    Response<Boolean> uploadBook(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title){

        return ResponseHelper.isOk(
                libraryService.storeBook(file, title)
        );
    }

    @GetMapping
    Response<List<Book>> getBookAfterDate(
            @RequestParam("after_time") Long time
    ){
        return ResponseHelper.isOk(
                libraryService.getBookAfterDate(time)
        );
    }

}
