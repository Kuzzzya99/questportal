package com.softarex.kuzmich.questportal.service.field;

import com.softarex.kuzmich.questportal.dto.AllFieldDTO;
import com.softarex.kuzmich.questportal.dto.FieldDTO;
import com.softarex.kuzmich.questportal.entity.Field;
import com.softarex.kuzmich.questportal.exception.NoEntityException;

import java.util.List;

public interface FieldService {

    AllFieldDTO addField(FieldDTO field);

    Field saveField(Field field);

    List<AllFieldDTO> findAll(int userId);

    List<FieldDTO> findAllForForm(int userId);

    Field findFieldById(int id) throws NoEntityException;

    void editField(Integer id, FieldDTO updateField);

    void deleteField(Integer id);

}

