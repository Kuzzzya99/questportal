package com.softarex.kuzmich.questportal.service.user;

import com.softarex.kuzmich.questportal.dto.ChangePasswordDTO;
import com.softarex.kuzmich.questportal.dto.EditProfileDTO;
import com.softarex.kuzmich.questportal.dto.UserDTO;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.exception.NoEntityException;

import java.util.List;

public interface UserService {
    void changePassword(Integer id, ChangePasswordDTO data);

    void setInvalidTokenStatus(String tokenId);

    User findUserByLogin(String login);

    void createUser(User user);

    User saveUser(User user);

    User saveAdmin(User user);

    List<User> findAll();

    User findUserById(int id) throws NoEntityException;

    boolean userExists(String login);

    User updateUser(Integer id, User updateUser) throws NoEntityException;

    EditProfileDTO getUserProfileInfo(Integer id);

    void editProfile(Integer id, EditProfileDTO editProfile);

    List<UserDTO> findAllForAccess();
}
