package com.ds.demo.service.impl;

import com.ds.demo.entity.Phone;
import com.ds.demo.entity.User;
import com.ds.demo.exception.UserPhoneException;
import com.ds.demo.repository.PhoneRepository;
import com.ds.demo.repository.UserRepository;
import com.ds.demo.service.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PhoneServiceImpl implements PhoneService {

    @Autowired
    private PhoneRepository phoneRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void deletePhone(Long phoneId) {
        Long userId = getUserIdByContext();

        List<Phone> phonesByUserId = phoneRepository.findByUserId(userId);

        Optional<Phone> storedPhoneOptional = phonesByUserId.stream()
                .filter(phone -> phone.getId().equals(phoneId))
                .findFirst();

        checkIsUserPhone(storedPhoneOptional.isPresent(), phoneId, userId);
        checkIsLastPhone(phonesByUserId, userId);

        phoneRepository.deleteById(phoneId);
    }


    @Override
    public void updatePhone(Long phoneId, String phoneToUpdate) {
        Long userId = getUserIdByContext();

        List<Phone> phonesByUserId = phoneRepository.findByUserId(userId);

        Optional<Phone> storedPhoneOptional = phonesByUserId.stream()
                .filter(phone -> phone.getId().equals(phoneId))
                .findFirst();

        checkIsUserPhone(storedPhoneOptional.isPresent(), phoneId, userId);

        Phone phone = storedPhoneOptional.get();
        phone.setPhone(phoneToUpdate);

        phoneRepository.save(phone);
    }

    @Override
    public void addPhone(String phone) {
        Long userId = getUserIdByContext();

        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Phone> optionalPhone = phoneRepository.findByPhone(phone);

        checkIsPhonePresent(optionalPhone);

        User user = userOptional.get();
        Phone entity = new Phone();
        entity.setUser(user);
        entity.setPhone(phone);

        phoneRepository.save(entity);
    }

    private void checkIsPhonePresent(Optional<Phone> optionalPhone) {
        if (optionalPhone.isPresent()) {
            String msg = String.format("Phone '%s' уже зарегистрирован в системе", optionalPhone.get().getPhone());
            log.error(msg);
            throw new UserPhoneException(msg);
        }
    }

    private void checkIsUserPhone(boolean isStoredPhone, Long phoneId, Long userId) {
        if (!isStoredPhone) {
            log.error(String.format("Phone c id '%s' не принадлежит пользователю с id '%s'", phoneId, userId));
            throw new UserPhoneException("Передан некорректный phone id.");
        }
    }

    private void checkIsLastPhone(List<Phone> phonesByUserId, Long userId) {
        if (phonesByUserId.size() == 1) {
            log.error(String.format("Невозможно удалить единственный phone для пользователя с id '%s'", userId));
            throw new UserPhoneException("Невозможно удалить единственный phone для пользователя");
        }
    }


    private Long getUserIdByContext() {
        return (Long) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
