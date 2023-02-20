package com.ds.demo.controller;

import com.ds.demo.dto.TransferDto;
import com.ds.demo.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "Операции со счетом пользователя")
@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @ApiOperation(value = "Перевод средств с баланса текущего пользователя на баланс указанного пользователя")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Некорректные параметры запроса на перевод")})
    @PostMapping("/transfer")
    public void transfer(@Valid @RequestBody TransferDto dto) {
        accountService.transfer(dto);
    }
}
