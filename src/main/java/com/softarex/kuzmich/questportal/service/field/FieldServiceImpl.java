package com.softarex.kuzmich.questportal.service.field;

import com.softarex.kuzmich.questportal.dto.AllFieldDTO;
import com.softarex.kuzmich.questportal.dto.FieldDTO;
import com.softarex.kuzmich.questportal.entity.Field;
import com.softarex.kuzmich.questportal.entity.Option;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import com.softarex.kuzmich.questportal.repository.FieldRepository;
import com.softarex.kuzmich.questportal.repository.OptionRepository;
import com.softarex.kuzmich.questportal.repository.TypeRepository;
import com.softarex.kuzmich.questportal.repository.UserRepository;
import com.softarex.kuzmich.questportal.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class FieldServiceImpl implements FieldService {

    private FieldRepository fieldRepository;
    private TypeRepository typeRepository;
    private UserService userService;
    private OptionRepository optionRepository;
    private UserRepository userRepository;

    @Autowired
    public FieldServiceImpl(FieldRepository fieldRepository,
                            TypeRepository typeRepository,
                            UserService userService,
                            OptionRepository optionRepository,
                            UserRepository userRepository) {
        this.fieldRepository = fieldRepository;
        this.typeRepository = typeRepository;
        this.userService = userService;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AllFieldDTO addField(FieldDTO data) {
        Field field = new Field();
        field.setLabel(data.getLabel());
        typeRepository.findById(data.getType()).ifPresent(field::setType);

        field.setActive(data.isActive());
        field.setRequired(data.isRequired());
        field.setUser(userService.findUserById(data.getUserId()));
        field = fieldRepository.save(field);

        for (int i = 0; i < data.options.length; i++) {
            Option option = new Option();
            option.setOption(data.options[i]);
            option.setField(field);
            optionRepository.save(option);
        }

        AllFieldDTO fieldDTO = new AllFieldDTO();
        fieldDTO.setFieldId(field.getId());
        fieldDTO.setLabel(field.getLabel());
        fieldDTO.setActive(field.isActive());
        fieldDTO.setRequired(field.isRequired());
        fieldDTO.setType(field.getType().getId());
        return fieldDTO;
    }

    @Override
    public Field saveField(Field field) {
        return fieldRepository.save(field);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AllFieldDTO> findAll(int userId) {
        User user = userRepository.findById(userId).orElseThrow(NoEntityException::new);

        return fieldRepository.findAllByUser(user).stream()
                .map(x -> new AllFieldDTO(x.getId(),
                        x.getLabel(),
                        x.getType().getId(),
                        x.isRequired(),
                        x.isActive())).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Field findFieldById(int id) throws NoEntityException {
        return fieldRepository.findById(id).orElseThrow(NoEntityException::new);
    }

    @Override
    public void editField(Integer id, FieldDTO data) {
        Field field = findFieldById(id);

        field.setLabel(data.getLabel());
        typeRepository.findById(data.getType()).ifPresent(field::setType);
        field.setActive(data.isActive());
        field.setRequired(data.isRequired());
        field.setUser(userService.findUserById(data.getUserId()));
        field = fieldRepository.save(field);

        optionRepository.deleteAllByField(field);
        for (int i = 0; i < data.options.length; i++) {
            Option option = new Option();
            option.setOption(data.options[i]);
            option.setField(field);
            optionRepository.save(option);
        }


    }

    @Override
    public void deleteField(Integer id) {
        fieldRepository.deleteById(id);
    }

    @Override
    public List<FieldDTO> findAllForForm(int userId) {
        List<FieldDTO> allFields = new ArrayList<>();

        User user = userRepository.findById(userId).orElse(new User());
        List<Field> fields = fieldRepository.findAllByUser(user);
        for (Field field : fields) {
            if (field.isActive()) {
                List<Option> options = optionRepository.findAllByField(field);
                String[] optionArray = new String[options.size()];
                for (int j = 0; j < options.size(); j++) {
                    optionArray[j] = options.get(j).getOption();
                }
                FieldDTO fieldDTO = new FieldDTO();
                fieldDTO.setId(field.getId());
                fieldDTO.setLabel(field.getLabel());
                fieldDTO.setType(field.getType().getId());
                fieldDTO.setRequired(field.isRequired());
                fieldDTO.setActive(field.isActive());
                fieldDTO.setOptions(optionArray);
                fieldDTO.setUserId(user.getId());
                allFields.add(fieldDTO);
            }
        }
        return allFields;
    }
}
