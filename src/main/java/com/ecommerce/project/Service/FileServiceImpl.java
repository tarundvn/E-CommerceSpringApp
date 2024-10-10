package com.ecommerce.project.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        // File names of current / original file
        String originalFilename = image.getOriginalFilename();
        // Generate a unique file name
        String randomID = UUID.randomUUID().toString();
        // mat.jpg -> 1234.jpg
        String fileName = randomID.concat(originalFilename.substring(originalFilename.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;
        // Check if path exists
        File folder = new File(path);
        if(!folder.exists()) {
            folder.mkdir();
        }
        Files.copy(image.getInputStream(), Paths.get(filePath));
        return fileName;
    }
}
