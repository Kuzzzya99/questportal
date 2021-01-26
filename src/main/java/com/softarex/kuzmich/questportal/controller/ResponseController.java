package com.softarex.kuzmich.questportal.controller;

import com.softarex.kuzmich.questportal.dto.FieldDTO;
import com.softarex.kuzmich.questportal.dto.ResponseDTO;
import com.softarex.kuzmich.questportal.dto.WorksheetDTO;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import com.softarex.kuzmich.questportal.repository.UserRepository;
import com.softarex.kuzmich.questportal.service.field.FieldService;
import com.softarex.kuzmich.questportal.service.response.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ResponseController {

    private final FieldService fieldService;
    private final ResponseService responseService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private UserRepository userRepository;

    @Autowired
    public ResponseController(FieldService fieldService, ResponseService responseService,
                              SimpMessagingTemplate simpMessagingTemplate,
                              UserRepository userRepository) {
        this.fieldService = fieldService;
        this.responseService = responseService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/responses/form")
    public List<FieldDTO> findAllFieldsForForm(@RequestParam String email) {
        User user = userRepository.findByLogin(email).orElseThrow(NoEntityException::new);
        return fieldService.findAllForForm(user.getId());
    }

    @PostMapping(value = "/responses/add")
    public void addResponse(@RequestBody ResponseDTO data, @RequestParam String email) throws NoEntityException {
        simpMessagingTemplate.convertAndSend("/topic/response", responseService.addResponse(data, email));
    }

    @GetMapping(value = "/responses/all")
    @PreAuthorize("hasAuthority('user')")
    public List<WorksheetDTO> findAllResponses(@RequestParam int userId) {
        return responseService.findAllResponses(userId);
    }
}