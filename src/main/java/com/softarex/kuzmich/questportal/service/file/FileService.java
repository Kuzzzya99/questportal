package com.softarex.kuzmich.questportal.service.file;

import com.softarex.kuzmich.questportal.dto.*;

import java.util.List;

public interface FileService {
    List<FileDTO> findAll();

    void setRate(RateDTO rateDTO);

    ResponseCommentDTO comment(CommentDTO commentDTO);

    List<ResponseCommentDTO> getCommentsForFile(Integer fileId);

    void giveAccess(AccessDTO accessDTO);
}
