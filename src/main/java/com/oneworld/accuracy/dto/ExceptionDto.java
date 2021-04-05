package com.oneworld.accuracy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDto {
    private String errorDescription;
    private String error;
    protected boolean valid;
    protected String responseCode;
}
