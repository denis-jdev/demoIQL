package com.ds.demo.controller;

import com.ds.demo.dto.JwtRequest;
import com.ds.demo.dto.JwtResponse;
import com.ds.demo.dto.SearchUserDto;
import com.ds.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Операции с пользователями")
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса")})
    @ApiOperation(value = "Авторизация в системе")
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest request) {
        String token = userService.verifyEmailPassword(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса")})
    @ApiOperation(value = "Поиск пользователей в системе")
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestBody SearchUserDto dto,
            @PageableDefault(value = 2, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(
                userService.userSearch(dto, pageable)
        );
    }

}
