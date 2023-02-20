package com.ds.demo.controller;

import com.ds.demo.dto.EmailDto;
import com.ds.demo.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Операции с электронной почтой пользователя")
@RestController
@RequestMapping("api/v1/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса")})
    @ApiOperation(value = "Изменение электронной почты")
    @PutMapping
    public void updateEmail(@Valid @RequestBody EmailDto dto) {
        emailService.updateEmail(dto.getId(), dto.getEmail());
    }

    @ApiOperation(value = "Удаление электронной почты")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса")})
    @DeleteMapping
    public void deleteEmail(@RequestBody EmailDto dto) {
        emailService.deleteEmail(dto.getId());
    }

    @ApiOperation(value = "Добавление электронной почты")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса")})
    @PostMapping
    public void addEmail(@Valid @RequestBody EmailDto dto) {
        emailService.addEmail(dto.getEmail());
    }
}
