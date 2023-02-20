package com.ds.demo.service;

import com.ds.demo.dto.SearchUserDto;
import com.ds.demo.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    String verifyEmailPassword(String email, String password);

    Page<UserDto> userSearch(SearchUserDto dto, Pageable pageable);

}
