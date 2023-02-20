package com.ds.demo.service.impl;

import com.ds.demo.entity.Email;
import com.ds.demo.entity.User;
import com.ds.demo.exception.UserEmailException;
import com.ds.demo.repository.EmailRepository;
import com.ds.demo.repository.UserRepository;
import com.ds.demo.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void deleteEmail(Long emailId) {
        Long userId = getUserIdByContext();

        List<Email> emailsByUserId = emailRepository.findByUserId(userId);

        Optional<Email> storedEmailOptional = emailsByUserId.stream()
                .filter(email -> email.getId().equals(emailId))
                .findFirst();

        checkIsUserEmail(storedEmailOptional.isPresent(), emailId, userId);
        checkIsLastEmail(emailsByUserId, userId);

        emailRepository.deleteById(emailId);
    }


    @Override
    public void updateEmail(Long emailId, String emailToUpdate) {
        Long userId = getUserIdByContext();

        Optional<Email> optionalEmail = emailRepository.findByEmail(emailToUpdate);
        checkIsEmailPresent(optionalEmail);

        List<Email> emailsByUserId = emailRepository.findByUserId(userId);

        Optional<Email> storedEmailOptional = emailsByUserId.stream()
                .filter(email -> email.getId().equals(emailId))
                .findFirst();

        checkIsUserEmail(storedEmailOptional.isPresent(), emailId, userId);


        Email email = storedEmailOptional.get();
        email.setEmail(emailToUpdate);

        emailRepository.save(email);
    }

    @Override
    public void addEmail(String email) {
        Long userId = getUserIdByContext();

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Email> optionalEmail = emailRepository.findByEmail(email);

        checkIsEmailPresent(optionalEmail);

        User user = userOptional.get();
        Email entity = new Email();
        entity.setUser(user);
        entity.setEmail(email);

        emailRepository.save(entity);
    }

    private void checkIsEmailPresent(Optional<Email> optionalEmail) {
        if (optionalEmail.isPresent()) {
            String msg = String.format("Email '%s' уже зарегистрирован в системе", optionalEmail.get().getEmail());
            log.error(msg);
            throw new UserEmailException(msg);
        }
    }

    private void checkIsUserEmail(boolean isStoredEmail, Long emailId, Long userId) {
        if (!isStoredEmail) {
            log.error(String.format("Email c id '%s' не принадлежит пользователю с id '%s'", emailId, userId));
            throw new UserEmailException("Передан некорректный email id.");
        }
    }

    private void checkIsLastEmail(List<Email> emailsByUserId, Long userId) {
        if (emailsByUserId.size() == 1) {
            log.error(String.format("Невозможно удалить единственный email для пользователя с id '%s'", userId));
            throw new UserEmailException("Невозможно удалить единственный email для пользователя");
        }
    }


    private Long getUserIdByContext() {
        return (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
