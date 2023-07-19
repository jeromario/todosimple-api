package com.jeffersonandrade.todosimple.services;

import com.jeffersonandrade.todosimple.models.Task;
import com.jeffersonandrade.todosimple.models.User;
import com.jeffersonandrade.todosimple.models.enums.ProfileEnums;
import com.jeffersonandrade.todosimple.models.projection.TaskProjection;
import com.jeffersonandrade.todosimple.repositories.TaskRepository;
import com.jeffersonandrade.todosimple.security.UserSpringSecurity;
import com.jeffersonandrade.todosimple.services.exceptions.AuthorizationException;
import com.jeffersonandrade.todosimple.services.exceptions.DataBindingViolationException;
import com.jeffersonandrade.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Objects;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id){
        Task task = taskRepository.findById(id).orElseThrow(()-> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authentication();

        if (Objects.isNull(userSpringSecurity)
        || !userSpringSecurity.hasRole(ProfileEnums.ADMIN) && !userHasTask(userSpringSecurity,task)){
            throw new AuthorizationException("Acesso negado!");
        }

        return task;
    }

    public List<TaskProjection> findAllByUser(){
        UserSpringSecurity userSpringSecurity = UserService.authentication();
        if (Objects.isNull(userSpringSecurity)){
            throw new AuthorizationException("Acesso negado!");
        }
        return taskRepository.findByUser_Id(userSpringSecurity.getId());

    }
    @Transactional
    public Task create(Task task){
        UserSpringSecurity userSpringSecurity = UserService.authentication();
        if (Objects.isNull(userSpringSecurity)){
            throw new AuthorizationException("Acesso negado!");
        }

        User user = userService.findById(userSpringSecurity.getId());
        task.setId(null);
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Task task){
        Task newTask = findById(task.getId());
        newTask.setDescription(task.getDescription());
        return taskRepository.save(newTask);
    }

    public void delete(Long id){
        findById(id);
        try {
            taskRepository.deleteById(id);
        }catch (Exception e){
            throw new DataBindingViolationException("Não é possivel excluir, pois há entidades relacionadas!");
        }
    }

    public Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }
}
