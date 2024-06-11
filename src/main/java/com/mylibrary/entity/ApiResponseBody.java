package com.mylibrary.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Api Success Response Body", hidden = true)
public class ApiResponseBody<dataFormat> {
    @JsonInclude(Include.ALWAYS)
    private int code;

    @JsonInclude(Include.ALWAYS)
    private boolean status;

    @JsonInclude(Include.NON_NULL)
    private String message;

    private dataFormat data;


}
