package com.betafore.evoting.response;

import lombok.*;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class ApiResponse {

    private String status;

    public ApiResponse() {
        this.status = "success";
    }

}
