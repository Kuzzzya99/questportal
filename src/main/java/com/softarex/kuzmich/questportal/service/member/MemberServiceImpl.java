package com.softarex.kuzmich.questportal.service.member;

import com.softarex.kuzmich.questportal.dto.FileDTO;
import com.softarex.kuzmich.questportal.dto.FileUploadDTO;
import com.softarex.kuzmich.questportal.dto.MemberDTO;
import com.softarex.kuzmich.questportal.dto.UserIdDTO;
import com.softarex.kuzmich.questportal.entity.File;
import com.softarex.kuzmich.questportal.entity.Member;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import com.softarex.kuzmich.questportal.exception.UserAlreadyExistsException;
import com.softarex.kuzmich.questportal.exception.YouDidntJoinException;
import com.softarex.kuzmich.questportal.repository.FileRepository;
import com.softarex.kuzmich.questportal.repository.MemberRepository;
import com.softarex.kuzmich.questportal.repository.UserRepository;
import com.softarex.kuzmich.questportal.service.awss3.AWSS3Service;
import com.softarex.kuzmich.questportal.service.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberServiceImpl implements MemberService {
    private final UserService userService;
    private final MemberRepository memberRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private FileRepository fileRepository;
    private final AWSS3Service awss3Service;

    @Autowired
    public MemberServiceImpl(UserService userService,
                             MemberRepository memberRepository,
                             ModelMapper modelMapper,
                             UserRepository userRepository,
                             FileRepository fileRepository,
                             AWSS3Service awss3Service) {
        this.userService = userService;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.awss3Service = awss3Service;
    }

    @Override
    public FileDTO uploadFile(FileUploadDTO file) {
        File newFile = new File();
        newFile.setName(file.getFile().getOriginalFilename());
        Integer userId = file.getUserId();
        User user = userRepository.findById(userId).orElseThrow(NoEntityException::new);
        newFile.setOwner(user);
        fileRepository.save(newFile);

        List<Integer> canWatch = new ArrayList<>();
        FileDTO fileDTO = new FileDTO(newFile.getId(),
                newFile.getName(),
                newFile.getRate(),
                newFile.getOwner().getId(),
                canWatch
        );
        awss3Service.uploadFile(file.getFile());

        return fileDTO;
    }

    @Override
    public List<MemberDTO> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberDTO> memberDTOS = new ArrayList<>();
        members.forEach(el -> {
            memberDTOS.add(modelMapper.map(el, MemberDTO.class));
        });
        return memberDTOS;
    }

    @Override
    public MemberDTO joinChat(int userId, String role) {
        User user = userService.findUserById(userId);
        if (memberRepository.findByUser(user).isPresent()) {
            throw new UserAlreadyExistsException();
        } else {
            String firstName = userService.findUserById(userId).getFirstName();
            String lastName = userService.findUserById(userId).getLastName();
            Member member = new Member();
            member.setFirstName(firstName);
            member.setLastName(lastName);
            member.setRaisedHand(false);
            member.setRole(role);
            member.setUser(user);
            memberRepository.save(member);

            return modelMapper.map(member, MemberDTO.class);
        }
    }

    @Override
    public MemberDTO raiseHand(Integer userId) {
        User user = userService.findUserById(userId);
        Member member = memberRepository.findByUser(user).orElseThrow(YouDidntJoinException::new);
        member.setRaisedHand(!member.isRaisedHand());
        memberRepository.save(member);
        return modelMapper.map(member, MemberDTO.class);
    }

    @Override
    public MemberDTO leaveChat(Integer userId) {
        User user = userService.findUserById(userId);
        Member member = memberRepository.findByUser(user).orElseThrow(YouDidntJoinException::new);
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        memberRepository.delete(member);
        return memberDTO;
    }

}
