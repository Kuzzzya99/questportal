package com.softarex.kuzmich.questportal.service.response;

import com.softarex.kuzmich.questportal.dto.ResponseDTO;
import com.softarex.kuzmich.questportal.dto.WorksheetDTO;
import com.softarex.kuzmich.questportal.entity.Response;
import com.softarex.kuzmich.questportal.exception.NoEntityException;

import java.util.List;

public interface ResponseService {

    WorksheetDTO addResponse(ResponseDTO data, String email);

    List<WorksheetDTO> findAllResponses(int userId);

    Response saveResponse(Response response);

    List<Response> findAll(int id);

    Response findResponseById(int id) throws NoEntityException;

    void editResponse(Integer id, Response updateResponse);

    void deleteResponse(Integer id);
}
