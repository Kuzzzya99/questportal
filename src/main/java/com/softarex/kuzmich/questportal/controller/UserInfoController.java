package com.softarex.kuzmich.questportal.controller;

import com.softarex.kuzmich.questportal.dto.ChangePasswordDTO;
import com.softarex.kuzmich.questportal.dto.EditProfileDTO;
import com.softarex.kuzmich.questportal.dto.UserDTO;
import com.softarex.kuzmich.questportal.dto.UserNameDTO;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserInfoController {

    private final UserService userService;

    @Autowired
    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public UserNameDTO findById(@PathVariable Integer id) {

        User user = userService.findUserById(id);
        UserNameDTO userName = new UserNameDTO();
        userName.setFirstName(user.getFirstName());
        return userName;
    }

    @PostMapping(value = "/{id}/change_password")
    public void changePassword(@RequestBody ChangePasswordDTO data, @PathVariable Integer id) {

        userService.changePassword(id, data);
    }

    @GetMapping(value = "/{id}/edit_profile")
    public EditProfileDTO getUserProfileInfo(@PathVariable Integer id) {

        return userService.getUserProfileInfo(id);
    }


    @PostMapping(value = "/{id}/edit_profile")
    public void editProfile(@RequestBody EditProfileDTO editProfile, @PathVariable Integer id) {

        userService.editProfile(id, editProfile);
    }

    @PostMapping(value = "/{id}/logout")
    public void logout(@RequestBody String tokenId, @PathVariable Integer id) {

        userService.setInvalidTokenStatus(tokenId);
    }

    @GetMapping
    public List<UserDTO> findAll(){
        return userService.findAllForAccess();
    }

}
