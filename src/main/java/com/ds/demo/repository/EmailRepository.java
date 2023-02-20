package com.ds.demo.repository;

import com.ds.demo.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

    List<Email> findByUserId(Long userId);

    Optional<Email> findByEmail(String email);

}
