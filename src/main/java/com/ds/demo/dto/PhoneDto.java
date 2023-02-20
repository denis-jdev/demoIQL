package com.ds.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@ApiModel(value = "Phone", description = "Объект содержащий параметры телефона пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {
    @ApiModelProperty(value = "Id обновляемого/удаляемого телефона. Не требует заполнения при добавлении нового email",
            example = "1")
    private Long id;

    @ApiModelProperty(value = "телефон пользователя", example = "79002003010", required = true)
    @Pattern(regexp = "(7)\\d{10}", message = "Некорректный формат номера телефона. Должен быть вида 7xxxxxxxxxx")
    private String phone;
}
