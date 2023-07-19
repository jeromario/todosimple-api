package com.jeffersonandrade.todosimple.controllers;

import com.jeffersonandrade.todosimple.models.Task;
import com.jeffersonandrade.todosimple.services.TaskService;
import com.jeffersonandrade.todosimple.services.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;


    @GetMapping("/{id}")
    public ResponseEntity<Task>findById(@PathVariable Long id){
        Task task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>>findAllByUser(){

        List<Task> taskList = taskService.findAllByUser();
        return ResponseEntity.ok(taskList);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void>create(@Valid @RequestBody Task task){
        taskService.create(task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void>update(@Valid @RequestBody Task task,@PathVariable Long id){
        task.setId(id);
        taskService.update(task);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }



}
