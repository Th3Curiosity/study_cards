package com.th3curiosity.studycards.mapper;

import com.th3curiosity.studycards.dto.user.UserResponse;
import com.th3curiosity.studycards.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponseDto(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }


}
