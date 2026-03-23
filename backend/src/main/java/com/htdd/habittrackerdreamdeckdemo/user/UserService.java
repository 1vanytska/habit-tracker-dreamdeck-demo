package com.htdd.habittrackerdreamdeckdemo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User updateUserProfile(User currentUser, UserProfileUpdateRequest request) {
        if (request.getUsername() != null && !currentUser.getUsername().equals(request.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new RuntimeException("Цей username вже зайнятий");
            }
            currentUser.setUsername(request.getUsername());
        }

        if (request.getDescription() != null) {
            currentUser.setDescription(request.getDescription());
        }

        if (request.getProfilePicture() != null) {
            currentUser.setProfilePicture(request.getProfilePicture());
        }

        return userRepository.save(currentUser);
    }
}