package com.softarex.kuzmich.questportal.service.user;

import com.softarex.kuzmich.questportal.dto.ChangePasswordDTO;
import com.softarex.kuzmich.questportal.dto.EditProfileDTO;
import com.softarex.kuzmich.questportal.dto.FileDTO;
import com.softarex.kuzmich.questportal.dto.UserDTO;
import com.softarex.kuzmich.questportal.entity.Role;
import com.softarex.kuzmich.questportal.entity.Token;
import com.softarex.kuzmich.questportal.entity.User;
import com.softarex.kuzmich.questportal.exception.NoEntityException;
import com.softarex.kuzmich.questportal.exception.UserInfoNotChangedException;
import com.softarex.kuzmich.questportal.repository.RedisRepository;
import com.softarex.kuzmich.questportal.repository.RoleRepository;
import com.softarex.kuzmich.questportal.repository.UserRepository;
import com.softarex.kuzmich.questportal.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RedisRepository redisRepository;
    private MailService mailService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           RedisRepository redisRepository,
                           MailService mailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.redisRepository = redisRepository;
        this.mailService = mailService;
    }

    @Override
    public void changePassword(Integer id, ChangePasswordDTO data) {
        User user = findUserById(id);
        if (bCryptPasswordEncoder.matches(data.getPassword(),
                user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(data.getNewPassword()));
            userRepository.save(user);
            mailService.sendEmail(user,
                    "Change password notification",
                    "You have successfully changed password on Questionnaire portal!");
        } else {
            throw new UserInfoNotChangedException();
        }
    }

    @Override
    public EditProfileDTO getUserProfileInfo(Integer id) {
        User user = findUserById(id);
        EditProfileDTO profile = new EditProfileDTO();
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setLogin(user.getLogin());
        profile.setPhoneNumber(user.getPhoneNumber());
        return profile;
    }

    @Override
    public void editProfile(Integer id, EditProfileDTO editProfile) {
        User user = findUserById(id);
        if (!editProfile.getFirstName().isEmpty()) {
            user.setFirstName(editProfile.getFirstName());
        }
        if (!editProfile.getLastName().isEmpty()) {
            user.setLastName(editProfile.getLastName());
        }
        if (!editProfile.getLogin().isEmpty()) {
            user.setLogin(editProfile.getLogin());
        }
        if (!(editProfile.getPhoneNumber() == null)) {
            user.setPhoneNumber(editProfile.getPhoneNumber());
        }
        userRepository.save(user);
    }

    @Override
    public void setInvalidTokenStatus(String tokenId) {
        redisRepository.findById(tokenId).ifPresentOrElse(token -> {
            token.setStatus(Token.Status.INVALID);
            redisRepository.save(token);
        }, () -> {
            throw new NoEntityException();
        });
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(NoEntityException::new);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByType("user");
        user.setRoles(singleton(userRole));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void createUser(User user) {
        if (!userExists(user.getLogin())) {
            saveUser(user);
            mailService.sendEmail(user,
                    "Registration notification",
                    "You have successfully registrated on Questionnaire portal!");
        } else throw new NoEntityException();
    }

    @Override
    public User updateUser(Integer id, User updateUser) throws NoEntityException {
        User user = userRepository.findById(id).orElseThrow(NoEntityException::new);

        boolean loginUpdated = !(user.getLogin().equalsIgnoreCase(updateUser.getLogin()));
        boolean userWithLoginUpdatedNotExists = loginUpdated && !userRepository.existsByLogin(updateUser.getLogin());

        if (loginUpdated && !userWithLoginUpdatedNotExists) {
            throw new NoEntityException("Email not free");
        } else if (userWithLoginUpdatedNotExists) {
            user.setLogin(updateUser.getLogin());
        }

        if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
            if (!bCryptPasswordEncoder.matches(updateUser.getPassword(), user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(updateUser.getPassword()));
            }
        }
        userRepository.save(user);

        return user;
    }

    @Override
    public User saveAdmin(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByType("admin");
        user.setRoles(singleton(userRole));
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(int id) throws NoEntityException {
        return userRepository.findById(id).orElseThrow(NoEntityException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean userExists(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public List<UserDTO> findAllForAccess() {
        return findAll().stream()
                .map(x -> new UserDTO(x.getId(),
                        x.getFirstName(),
                        x.getLastName())).collect(Collectors.toList());
    }
}
