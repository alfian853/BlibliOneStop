package com.gdn.onestop.service.impl;

import com.gdn.onestop.service.FileStorageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {


    public boolean storeFile(MultipartFile file, String fileName, PathCategory pathCategory){
        Path target;
        try{
            if(fileName.contains("..")) {
                return false;
            }
            target = Paths.get(this.path.get(pathCategory)+"/"+fileName).toAbsolutePath().normalize();
            Files.copy(file.getInputStream(),target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFile(String fileName,PathCategory pathCategory){
        try{
            Path target = Paths.get(this.path.get(pathCategory)+"/"+fileName).toAbsolutePath();
            Files.delete(target);
            return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createThumbnailFromDocument(PDDocument document, String resultName, PathCategory pathCategory) {
        try {
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage image = renderer.renderImage(0);
            ImageIO.write(image, "JPEG",
                    new File(
                            path.get(pathCategory)+"/"+resultName)
            );
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
