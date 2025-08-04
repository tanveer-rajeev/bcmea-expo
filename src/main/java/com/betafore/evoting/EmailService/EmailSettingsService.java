package com.betafore.evoting.EmailService;

import com.betafore.evoting.Exception.CustomException;

public interface EmailSettingsService {

    EmailSettings create(Long expoId,EmailDto emailSettings) throws CustomException;
    void update(EmailDto emailSettings,Long id) throws CustomException;
    void delete(Long id);
    EmailSettings getById(Long id) throws CustomException;
    EmailSettings getByExpoId(Long id) throws CustomException;

}
