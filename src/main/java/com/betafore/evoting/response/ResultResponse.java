package com.betafore.evoting.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ResultResponse extends ApiResponse {

    private Object results;

    public ResultResponse(String status, Object results) {
        super(status);
        this.results = results;
    }

}
