package com.softarex.kuzmich.questportal.controller;

import com.softarex.kuzmich.questportal.dto.*;
import com.softarex.kuzmich.questportal.entity.File;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import com.softarex.kuzmich.questportal.exception.UserAlreadyExistsException;
import com.softarex.kuzmich.questportal.repository.FileRepository;
import com.softarex.kuzmich.questportal.service.awss3.AWSS3Service;
import com.softarex.kuzmich.questportal.service.file.FileService;
import com.softarex.kuzmich.questportal.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/member")
public class MemberController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MemberService memberService;
    private final AWSS3Service awss3Service;
    private FileRepository fileRepository;
    private final FileService fileService;

    @Autowired
    public MemberController(SimpMessagingTemplate simpMessagingTemplate,
                            MemberService memberService,
                            AWSS3Service awss3Service,
                            FileRepository fileRepository,
                            FileService fileService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.memberService = memberService;
        this.awss3Service = awss3Service;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    @PostMapping(value = "/join")
    public void joinChat(@RequestParam("userId") int userId, @RequestParam("role") String role) throws UserAlreadyExistsException {
        MemberDTO memberDTO = memberService.joinChat(userId, role);
        simpMessagingTemplate.convertAndSend("/topic/join", memberDTO);
    }

    @DeleteMapping(value = "/leave")
    public void leaveChat(@RequestParam Integer userId) {
        MemberDTO memberDTO = memberService.leaveChat(userId);
        simpMessagingTemplate.convertAndSend("/topic/leave", memberDTO);
    }

    @PostMapping(value = "/raise_hand")
    public void raiseHand(@RequestParam Integer userId) {
        MemberDTO memberDTO = memberService.raiseHand(userId);
        simpMessagingTemplate.convertAndSend("/topic/raise_hand", memberDTO);
    }

    @GetMapping
    public List<MemberDTO> viewAllMembers() {
        return memberService.findAllMembers();
    }


    @PostMapping("/upload")
    public void uploadFile(@ModelAttribute FileUploadDTO file) {
        FileDTO fileDTO = memberService.uploadFile(file);
        simpMessagingTemplate.convertAndSend("/topic/upload", fileDTO);
    }

    @PostMapping("/give_access")
    public void giveAccess(@RequestBody AccessDTO accessDTO) {
        fileService.giveAccess(accessDTO);
    }

    @GetMapping("/download")
    public void downloadFile(@RequestParam("fileName") String fileName) throws IOException {
        awss3Service.download(fileName);
    }

    @DeleteMapping("/delete")
    public void deleteFile(@RequestParam Integer fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(NoEntityException::new);
        awss3Service.deleteFile(file.getName());
        fileRepository.delete(file);
        simpMessagingTemplate.convertAndSend("/topic/delete", file.getId());
    }

    @GetMapping("/list")
    public List<FileDTO> allFiles() {
        return fileService.findAll();
    }

    @PostMapping("/rate")
    public void setRate(@RequestBody RateDTO rateDTO) {
        fileService.setRate(rateDTO);
        simpMessagingTemplate.convertAndSend("/topic/rate", rateDTO);
    }

    @PostMapping("/comment")
    public void comment(@RequestBody CommentDTO commentDTO) {
        ResponseCommentDTO responseCommentDTO = fileService.comment(commentDTO);
        simpMessagingTemplate.convertAndSend("/topic/comment", responseCommentDTO);
    }

    @GetMapping("/comment")
    public List<ResponseCommentDTO> comment(@RequestParam Integer fileId) {
        return fileService.getCommentsForFile(fileId);
    }
}
