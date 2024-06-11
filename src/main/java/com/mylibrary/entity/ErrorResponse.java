package com.mylibrary.entity;

import java.util.Map;

import javax.validation.constraints.AssertFalse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Sample API Error Response
 * {
 * "code": 400,
 * "status": false,
 * "error": "Invalid parameters.",
 * "error_messages" : [
 * ]
 * }
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Api Error Response Body", hidden = true)
public class ErrorResponse {
    @JsonInclude(Include.ALWAYS)
    private int code;

    @JsonInclude(Include.ALWAYS)
    @AssertFalse
    private boolean status;

    @JsonInclude(Include.ALWAYS)
    private String error;

    @JsonInclude(Include.NON_NULL)
    private Map<String, String> error_messages;
}
