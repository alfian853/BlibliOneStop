package com.gdn.onestop.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

public interface FileStorageService {

    enum PathCategory{
        BOOKS("/books"), AUDIOS("/audios"), BOOK_THUMBNAIL("/book_thumbnails");

        String path;

        PathCategory(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    String BASE_PATH = "onestop-web/src/main/resources/public";

    HashMap<PathCategory,String> path = new HashMap<PathCategory,String>(){{
        put(PathCategory.BOOKS, BASE_PATH+"/books/");
        put(PathCategory.AUDIOS, BASE_PATH+ "/audios/");
        put(PathCategory.BOOK_THUMBNAIL, BASE_PATH+"/book_thumbnails/");
    }};


    boolean storeFile(MultipartFile file, String fileName, PathCategory pathCategory);

    boolean deleteFile(String fileName, PathCategory pathCategory);

    boolean createThumbnailFromDocument(PDDocument document, String resultName, PathCategory pathCategory);
}
