package com.jeffersonandrade.todosimple.controllers;

import com.jeffersonandrade.todosimple.models.User;
import com.jeffersonandrade.todosimple.models.dto.UserCreateDTO;
import com.jeffersonandrade.todosimple.models.dto.UserUpdateDTO;
import com.jeffersonandrade.todosimple.services.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User>findById(@PathVariable Long id){
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<Void>create(@Valid @RequestBody UserCreateDTO dto){
        User user = userService.fromDTO(dto);
        userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();

    }

    @PutMapping("/{id}")

    public ResponseEntity<Void>update(@Valid @RequestBody UserUpdateDTO dto, @PathVariable Long id){
        User user = userService.fromDTO(dto);
        user.setId(id);
        userService.update(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
