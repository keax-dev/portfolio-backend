package com.keax.infrastructure.controllers.DTO;

import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse <T>{

    private boolean status;
    private String alert;
    private List<String> messages;
    private T data;

    public ApiResponse(boolean status, String alert, T data) {
        this.status = status;
        this.alert = alert;
        this.data = data;
        this.messages = new ArrayList<>();
    }

}
