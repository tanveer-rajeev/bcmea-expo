package com.betafore.evoting.other_services;

import com.betafore.evoting.Exception.CustomException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileStorageServiceImp implements FileStorageService {
    private final Path root = Paths.get("uploads");

    @Override
    public void initStorage() {
        if (!Files.exists(root)) {
            try {
                Files.createDirectory(root);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public String saveFile(MultipartFile file) throws CustomException {
        try {
            String filename = System.currentTimeMillis() + "." + Objects.requireNonNull(file.getOriginalFilename()).substring(
                file.getOriginalFilename().lastIndexOf(".") + 1
            );
            if (!isValidImage(file.getOriginalFilename())) {
                throw new CustomException("Image file required");
            }
            if (fileDoesExist(file.getName())) {
                deleteFile(file.getName());
            }
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(filename)));
            return filename;
        } catch (CustomException | IOException e) {
            throw new CustomException(e.getMessage());
        }
    }

    public static boolean isValidImage(String img)  {
        String extension = FilenameUtils.getExtension(img);

        if (extension == null || extension.isEmpty()) {
            return false;
        }

        return (extension.equals("jpeg") || extension.equals("png") || extension.equals("gif") || extension.equals("jpg")
            || extension.equals("webp")|| extension.equals("bmp")|| extension.equals("svg") || extension.equals("tiff"));
    }

    @Override
    public Boolean fileDoesExist(String filename) {
        Path file = root.resolve(filename);
        return Files.exists(file);
    }

    @Override
    public byte[] loadFile(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            try (InputStream inputStream = resource.getInputStream()) {
                return inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException("Error reading file: " + filename, e);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid file URL: " + filename, e);
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            Path file = root.resolve(filename);
            Files.delete(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
