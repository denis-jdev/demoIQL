package com.ds.demo.controller;

import com.ds.demo.dto.PhoneDto;
import com.ds.demo.service.PhoneService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Операции с телефоном пользователя")
@RestController
@RequestMapping("api/v1/phone")
public class PhoneController {

    @Autowired
    private PhoneService phoneService;

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса")})
    @ApiOperation(value = "Удаление телефона")
    @DeleteMapping
    public void deletePhone(@RequestBody PhoneDto dto) {
        phoneService.deletePhone(dto.getId());
    }

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса")})
    @ApiOperation(value = "Изменение телефона")
    @PutMapping
    public void updatePhone(@Valid @RequestBody PhoneDto dto) {
        phoneService.updatePhone(dto.getId(), dto.getPhone());
    }

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса")})
    @ApiOperation(value = "Добавление телефона")
    @PostMapping
    public void addPhone(@Valid @RequestBody PhoneDto dto) {
        phoneService.addPhone(dto.getPhone());
    }
}
