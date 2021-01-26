package com.softarex.kuzmich.questportal.service.file;

import com.softarex.kuzmich.questportal.dto.*;
import com.softarex.kuzmich.questportal.entity.Comment;
import com.softarex.kuzmich.questportal.entity.File;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import com.softarex.kuzmich.questportal.repository.CommentRepository;
import com.softarex.kuzmich.questportal.repository.FileRepository;
import com.softarex.kuzmich.questportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;
    private CommentRepository commentRepository;
    private UserRepository userRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository,
                           CommentRepository commentRepository,
                           UserRepository userRepository) {
        this.fileRepository = fileRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<FileDTO> findAll() {
        List<Integer> canWatchIds = new ArrayList<>();
        List<File> files = fileRepository.findAll();
        files.forEach(file -> {
            file.getCanWatch().forEach(user -> {
                canWatchIds.add(user.getId());
            });
        });

        return fileRepository.findAll().stream()
                .map(x -> new FileDTO(x.getId(),
                        x.getName(),
                        x.getRate(),
                        x.getOwner().getId(),
                        canWatchIds)).collect(Collectors.toList());
    }

    @Override
    public void setRate(RateDTO rateDTO) {
        File file = fileRepository.findById(rateDTO.getFileId()).orElseThrow(NoEntityException::new);
        file.setRate(rateDTO.getRate());
        fileRepository.save(file);
    }

    @Override
    public ResponseCommentDTO comment(CommentDTO commentDTO) {
        File file = fileRepository.findById(commentDTO.getFileId()).orElseThrow(NoEntityException::new);

        Comment comment = new Comment();
        User user = userRepository.findById(commentDTO.getUserId()).orElseThrow(NoEntityException::new);
        comment.setUser(user);
        comment.setComment(commentDTO.getComment());
        comment.setFile(file);
        commentRepository.save(comment);

        return new ResponseCommentDTO(user.getFirstName(), comment.getComment());
    }

    @Override
    public List<ResponseCommentDTO> getCommentsForFile(Integer fileId) {
        List<Comment> comments = commentRepository
                .findByFile(fileRepository.findById(fileId).orElseThrow(NoEntityException::new));

        return comments.stream().map(comment ->
                new ResponseCommentDTO(comment.getUser().getFirstName(),
                        comment.getComment())).collect(Collectors.toList());
    }

    @Override
    public void giveAccess(AccessDTO accessDTO) {
        File file = fileRepository.findById(accessDTO.getFileId()).orElseThrow(NoEntityException::new);
        List<User> users = new ArrayList<>();
        accessDTO.getUserIds().forEach(el -> {
            User user = userRepository.findById(el).orElseThrow(NoEntityException::new);
            users.add(user);
        });
        file.setCanWatch(users);
        fileRepository.save(file);
    }
}
