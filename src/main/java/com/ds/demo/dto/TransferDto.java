package com.ds.demo.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel(value = "Transfer", description = "Объект, содержащий параметры для перевода средств" +
        " от текущего пользователя к целевому")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    @NotNull
    @ApiModelProperty(value = "Id получателя", example = "1", required = true)
    private Long recipientId;

    @ApiModelProperty(value = "Сумма перевода в копейках", example = "101", required = true)
    @NotNull
    private BigDecimal value;
}
