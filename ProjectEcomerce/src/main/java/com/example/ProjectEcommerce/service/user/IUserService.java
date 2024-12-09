package com.example.ProjectEcommerce.service.user;

import com.example.ProjectEcommerce.dto.UserDto;
import com.example.ProjectEcommerce.model.User;
import com.example.ProjectEcommerce.request.CreateUserRequest;
import com.example.ProjectEcommerce.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);
}
