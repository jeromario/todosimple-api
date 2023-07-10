package com.jeffersonandrade.todosimple.repositories;

import com.jeffersonandrade.todosimple.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional(readOnly = true)
    User findByUsername(String username);
}
