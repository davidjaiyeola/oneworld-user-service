package com.oneworld.accuracy.dto;

import lombok.Data;

@Data
public class ExceptionDto {
    private String errorDescription;
    private String error;
    protected boolean valid;
    protected String responseCode;
}
