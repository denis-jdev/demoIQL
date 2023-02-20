package com.ds.demo.repository;

import com.ds.demo.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {

    List<Phone> findByUserId(Long userId);

    Optional<Phone> findByPhone(String phone);
}
