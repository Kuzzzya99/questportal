package com.softarex.kuzmich.questportal.service.member;

import com.softarex.kuzmich.questportal.dto.FileDTO;
import com.softarex.kuzmich.questportal.dto.FileUploadDTO;
import com.softarex.kuzmich.questportal.dto.MemberDTO;
import com.softarex.kuzmich.questportal.dto.UserIdDTO;

import java.util.List;

public interface MemberService {
    MemberDTO joinChat(int userId, String role);

    MemberDTO leaveChat(Integer userId);

    MemberDTO raiseHand(Integer memberId);

    List<MemberDTO> findAllMembers();

    FileDTO uploadFile(FileUploadDTO file);
}
