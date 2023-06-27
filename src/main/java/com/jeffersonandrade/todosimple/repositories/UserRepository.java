package com.jeffersonandrade.todosimple.repositories;

import com.jeffersonandrade.todosimple.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
