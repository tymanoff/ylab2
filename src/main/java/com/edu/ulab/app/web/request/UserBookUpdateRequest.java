package com.edu.ulab.app.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserBookUpdateRequest {

    @JsonProperty("userRequest")
    private UserUpdateRequest userUpdateRequest;

    @JsonProperty("bookRequests")
    private List<BookUpdateRequest> bookUpdateRequests;
}
