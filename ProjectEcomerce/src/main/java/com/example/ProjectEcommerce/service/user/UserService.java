package com.example.ProjectEcommerce.service.user;

import com.example.ProjectEcommerce.exceptions.AlreadyExistsException;
import com.example.ProjectEcommerce.exceptions.ResourceNotFoundException;
import com.example.ProjectEcommerce.model.User;
import com.example.ProjectEcommerce.repository.UserRepository;
import com.example.ProjectEcommerce.request.CreateUserRequest;
import com.example.ProjectEcommerce.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user-> !userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(()-> new AlreadyExistsException("This "+request.getEmail()+" already existed!!"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(existingUser->{
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser);
        }).orElseThrow(()->new ResourceNotFoundException("User not found!!"));

    }

    @Override
    public void deleteUser(Long userId) {
         userRepository.findById(userId).ifPresentOrElse(userRepository::delete, ()->{
             throw new ResourceNotFoundException("User not found!!");
         });
    }
}