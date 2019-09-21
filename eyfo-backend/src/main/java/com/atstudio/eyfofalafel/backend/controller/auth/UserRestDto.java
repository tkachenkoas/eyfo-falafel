package com.atstudio.eyfofalafel.backend.controller.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class UserRestDto {

    @ApiModelProperty(notes = "Only for server response; will be ignored in request")
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    @ApiModelProperty(notes = "Only for create user request; will be empty in server response")
    private String password;
}
