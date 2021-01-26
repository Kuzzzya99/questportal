package com.softarex.kuzmich.questportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDTO {
    private MultipartFile file;
    private Integer userId;
}
