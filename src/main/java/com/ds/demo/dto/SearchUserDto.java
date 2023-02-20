package com.ds.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserDto {
    private String name;
    private LocalDate dateOfBirth;
    private String email;
    private String phone;
}
