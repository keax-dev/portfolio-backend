package com.keax.shared.infrastructure.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDTO<T>{

    private boolean status;
    private String alert;
    private List<String> messages;
    private T data;

    public ApiResponseDTO(boolean status, String alert, T data) {
        this.status = status;
        this.alert = alert;
        this.data = data;
        this.messages = new ArrayList<>();
    }

}
