package com.softarex.kuzmich.questportal.controller;

import com.amazonaws.services.rekognition.model.Image;
import com.softarex.kuzmich.questportal.dto.*;
import com.softarex.kuzmich.questportal.entity.Token;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.exception.UserAlreadyExistsException;
import com.softarex.kuzmich.questportal.security.jwt.JwtTokenProvider;
import com.softarex.kuzmich.questportal.service.TokenService;
import com.softarex.kuzmich.questportal.service.awss3.AWSCompareFaces;
import com.softarex.kuzmich.questportal.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class AuthController {

    private ModelMapper modelMapper;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private TokenService tokenService;
    private AWSCompareFaces awsCompareFaces;

    @Autowired
    public AuthController(ModelMapper modelMapper,
                          AuthenticationManager authenticationManager,
                          JwtTokenProvider jwtTokenProvider,
                          UserService userService,
                          TokenService tokenService,
                          AWSCompareFaces awsCompareFaces) {
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.tokenService = tokenService;
        this.awsCompareFaces = awsCompareFaces;
    }

    @PostMapping("/auth")
    public UserAuthenticationResponseDTO login(@RequestBody UserAuthenticationDTO data) throws AuthenticationException {

        String login = data.getLogin();
        String password = data.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        User user = userService.findUserByLogin(login);
        String token = jwtTokenProvider.createToken(login, user.getRoles());
        Token newToken = tokenService.createActiveToken(token);

        UserAuthenticationResponseDTO response = new UserAuthenticationResponseDTO();
        response.setTokenId(newToken.getId());
        response.setToken(token);
        response.setId(user.getId());

        return response;
    }


    @PostMapping("/registration")
    public void registration(@RequestBody UserRegisterDTO requestDto) throws UserAlreadyExistsException {

        User user = modelMapper.map(requestDto, User.class);
        userService.createUser(user);
    }

    @PostMapping("/photoAuth")
    public Object detectModerationLabels(@ModelAttribute RecognitionDTO recognitionDTO){
        return awsCompareFaces.compareFaces(recognitionDTO);
    }
}
