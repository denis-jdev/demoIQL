package com.ds.demo.service;

public interface PhoneService {

    void deletePhone(Long phoneId);

    void updatePhone(Long phoneId, String phone);

    void addPhone(String phone);

}
