package com.ds.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@ApiModel(value = "Email", description = "Объект содержащий параметры электронной почты")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto {

    @ApiModelProperty(value = "Id обновляемого/удаляемого email. Не требует заполнения при добавлении нового email",
            example = "1")
    private Long id;

    @ApiModelProperty(value = "email пользователя", example = "first@email.com", required = true)
    @Pattern(regexp = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
            message = "Некорректный email")
    private String email;
}
