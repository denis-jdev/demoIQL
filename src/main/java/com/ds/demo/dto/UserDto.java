package com.ds.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private LocalDate dateOfBirth;
    private List<EmailDto> emails;
    private List<PhoneDto> phones;
}
