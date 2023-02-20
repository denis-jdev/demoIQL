package com.ds.demo.service;

import com.ds.demo.exception.UserEmailException;

public interface EmailService {

    void deleteEmail(Long emailId) throws UserEmailException;

    void updateEmail(Long emailId, String email) throws UserEmailException;

    void addEmail(String email) throws UserEmailException;

}
