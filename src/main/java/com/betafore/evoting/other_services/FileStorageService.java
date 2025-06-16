package com.betafore.evoting.other_services;

import com.betafore.evoting.Exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    void initStorage();
    String saveFile(MultipartFile file) throws CustomException;
    Boolean fileDoesExist(String filename);
    byte[] loadFile(String filename);
    void deleteFile(String filename);

}
