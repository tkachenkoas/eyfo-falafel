package com.atstudio.eyfofalafel.backend.repository;

import com.atstudio.eyfofalafel.backend.domain.security.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserName(String userName);
}
