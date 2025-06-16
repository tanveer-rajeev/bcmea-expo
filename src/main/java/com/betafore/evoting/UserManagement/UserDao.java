package com.betafore.evoting.UserManagement;

import com.betafore.evoting.Exception.CustomException;

import java.util.List;

public interface UserDao {

    User findById(Long id) throws CustomException;

    User registration(UserRequestDto user,Long expoId) throws CustomException;

    List<User> all(Long expoId) throws CustomException;

    User updateByUser(UserRequestDto userDto,Long id) throws CustomException;

    void removeById(Long id) throws CustomException;

    void removeAll(Long expoId) throws CustomException;
}
