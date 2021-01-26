package com.softarex.kuzmich.questportal.service.response;

import com.softarex.kuzmich.questportal.dto.AnswerDTO;
import com.softarex.kuzmich.questportal.dto.ResponseDTO;
import com.softarex.kuzmich.questportal.dto.WorksheetDTO;
import com.softarex.kuzmich.questportal.entity.Response;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import com.softarex.kuzmich.questportal.repository.FieldRepository;
import com.softarex.kuzmich.questportal.repository.ResponseRepository;
import com.softarex.kuzmich.questportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ResponseServiceImpl implements ResponseService {

    private final FieldRepository fieldRepository;
    private final ResponseRepository responseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ResponseServiceImpl(FieldRepository fieldRepository, ResponseRepository responseRepository, UserRepository userRepository) {
        this.fieldRepository = fieldRepository;
        this.responseRepository = responseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public WorksheetDTO addResponse(final ResponseDTO data, final String email) {
        int maxWorksheetId = Optional.ofNullable(responseRepository.findMaxWorksheetId())
                .map(existingWorksheetId -> existingWorksheetId + 1)
                .orElse(1);

        final Response response = new Response();
        for (int i = 0; i < data.getResponse().size(); i++) {
            response.setAnswer(data.getResponse().get(i).getAnswer());
            response.setWorksheetId(maxWorksheetId);
            fieldRepository.findById(data.getResponse().get(i).getId()).ifPresent(response::setFieldId);
            response.setId(response.getFieldId().getId());
            userRepository.findByLogin(email).ifPresent(response::setUserId);
            responseRepository.save(response);
        }

        WorksheetDTO worksheetDTO = new WorksheetDTO();
        worksheetDTO.setWorksheetId(response.getWorksheetId());
        List<AnswerDTO> answerDTOS = responseRepository.findAllByWorksheetId(response.getWorksheetId())
                .stream().map(Response::toDto).collect(Collectors.toList());
        worksheetDTO.setAnswerDTOS(answerDTOS);

        return worksheetDTO;
    }

    @Override
    public List<WorksheetDTO> findAllResponses(int userId) {
        final List<WorksheetDTO> worksheets = new ArrayList<>();
        final User user = userRepository.findById(userId).orElseThrow(NoEntityException::new);
        final List<Response> responses = responseRepository.findAllByUserId(user);
        for (Response response : responses) {
            WorksheetDTO worksheetDTO = new WorksheetDTO();
            worksheetDTO.setWorksheetId(response.getWorksheetId());
            List<AnswerDTO> answerDTOS = responseRepository.findAllByWorksheetId(response.getWorksheetId())
                    .stream().map(Response::toDto).collect(Collectors.toList());
            worksheetDTO.setAnswerDTOS(answerDTOS);
            worksheets.add(worksheetDTO);
        }
        return worksheets.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Response saveResponse(Response response) {
        return null;
    }

    @Override
    public List<Response> findAll(int id) {
        return null;
    }

    @Override
    public Response findResponseById(int id) throws NoEntityException {
        return null;
    }

    @Override
    public void editResponse(Integer id, Response updateResponse) {

    }

    @Override
    public void deleteResponse(Integer id) {

    }
}
