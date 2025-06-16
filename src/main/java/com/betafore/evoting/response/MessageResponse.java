package com.betafore.evoting.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MessageResponse extends ApiResponse {

    private String message;

    public MessageResponse(String status, String message) {
        super(status);
        this.message = message;
    }

}
