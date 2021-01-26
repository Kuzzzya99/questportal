package com.softarex.kuzmich.questportal.controller;

import com.softarex.kuzmich.questportal.dto.AllFieldDTO;
import com.softarex.kuzmich.questportal.dto.FieldDTO;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import com.softarex.kuzmich.questportal.service.field.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/fields")
public class FieldController {

    private final FieldService fieldService;

    @Autowired
    public FieldController(FieldService fieldServiceService) {
        this.fieldService = fieldServiceService;
    }

    @GetMapping
    public List<AllFieldDTO> findAll(@RequestParam int userId) {
        return fieldService.findAll(userId);
    }

    @PostMapping
    public AllFieldDTO addField(@RequestBody FieldDTO data) throws NoEntityException {

        return fieldService.addField(data);
    }

    @GetMapping(value = "/{id}")
    public void fieldInfo(@PathVariable Integer id) {
        fieldService.findFieldById(id);
    }

    @PutMapping(value = "/{id}")
    public void editField(@RequestBody FieldDTO editField, @PathVariable Integer id) {
        fieldService.editField(id, editField);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteField(@PathVariable Integer id) {

        fieldService.deleteField(id);
    }
}
