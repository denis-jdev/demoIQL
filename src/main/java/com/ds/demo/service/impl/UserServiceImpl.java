package com.ds.demo.service.impl;

import com.ds.demo.dto.EmailDto;
import com.ds.demo.dto.PhoneDto;
import com.ds.demo.dto.SearchUserDto;
import com.ds.demo.dto.UserDto;
import com.ds.demo.entity.User;
import com.ds.demo.exception.NotFoundException;
import com.ds.demo.exception.WrongPasswordException;
import com.ds.demo.repository.UserRepository;
import com.ds.demo.security.JwtTokenUtil;
import com.ds.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ds.demo.repository.Specification.UserSpecification.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String verifyEmailPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmailsEmail(email);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User by email not found");
        }

        User storedUser = userOptional.get();

        if (!passwordEncoder.matches(password, storedUser.getPassword())) {
            log.error(String.format("Введен не верный пароль для пользователя c email '%s'", email));
            throw new WrongPasswordException("Неверный пароль");
        }

        return jwtTokenUtil.generateToken(storedUser.getId());
    }

    @Override
    public Page<UserDto> userSearch(SearchUserDto searchUserDto, Pageable pageable) {
        Page<User> userPage = findBySearchDto(searchUserDto, pageable);
        return new PageImpl<>(userPage.getContent()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList()), pageable, (int) userPage.getTotalElements());
    }

    private Page<User> findBySearchDto(SearchUserDto searchUserDto, Pageable pageable) {
        if (searchUserDto.getName() != null) {
            return userRepository.findAll(nameLike(searchUserDto.getName()), pageable);
        }
        if (searchUserDto.getPhone() != null) {
            return userRepository.findAll(phoneEqual(searchUserDto.getPhone()), pageable);
        }
        if (searchUserDto.getEmail() != null) {
            return userRepository.findAll(emailEqual(searchUserDto.getEmail()), pageable);
        }
        if (searchUserDto.getDateOfBirth() != null) {
            return userRepository.findAll(greaterThenDateOfBirth(searchUserDto.getDateOfBirth()), pageable);
        }
        return Page.empty();
    }

    private UserDto entityToDto(User entity) {
        List<EmailDto> emailDtos = entity.getEmails()
                .stream()
                .map(email -> new EmailDto(email.getId(), email.getEmail()))
                .collect(Collectors.toList());

        List<PhoneDto> phoneDtos = entity.getPhones()
                .stream()
                .map(phone -> new PhoneDto(phone.getId(), phone.getPhone()))
                .collect(Collectors.toList());

        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setEmails(emailDtos);
        dto.setPhones(phoneDtos);

        return dto;
    }

}
