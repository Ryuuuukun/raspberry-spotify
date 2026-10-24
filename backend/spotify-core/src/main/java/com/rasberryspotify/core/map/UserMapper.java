package com.rasberryspotify.core.map;

import com.rasberryspotify.core.dto.user.UserResponse;
import com.rasberryspotify.core.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
