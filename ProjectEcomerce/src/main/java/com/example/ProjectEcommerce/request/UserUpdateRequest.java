package com.example.ProjectEcommerce.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
}
