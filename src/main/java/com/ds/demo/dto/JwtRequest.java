package com.ds.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@ApiModel(value = "Auth", description = "Объект содержащий параметры, необходимые для авторизации в системе")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {

    @ApiModelProperty(value = "email пользователя", example = "first@email.com", required = true)
    @NotBlank
    private String email;

    @ApiModelProperty(value = "пароль пользователя", example = "qwerty123", required = true)
    @NotBlank
    private String password;
}
