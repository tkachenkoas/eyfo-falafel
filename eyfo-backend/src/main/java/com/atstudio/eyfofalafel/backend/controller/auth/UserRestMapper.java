package com.atstudio.eyfofalafel.backend.controller.auth;

import com.atstudio.eyfofalafel.backend.controller.beanmapper.SimpleRestObjectMapper;
import com.atstudio.eyfofalafel.backend.entities.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRestMapper extends SimpleRestObjectMapper<User, UserRestDto> {

    private BCryptPasswordEncoder encoder;

    @Autowired
    public UserRestMapper(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected Class<UserRestDto> getRestDtoClass() {
        return UserRestDto.class;
    }

    @Override
    public User toEntity(UserRestDto restObject) {
        User result = super.toEntity(restObject);
        result.setPassword(encoder.encode(restObject.getPassword()));
        result.setId(null);
        return result;
    }

    @Override
    public UserRestDto toRest(User entity) {
        UserRestDto result = super.toRest(entity);
        result.setPassword(null);
        return result;
    }
}
