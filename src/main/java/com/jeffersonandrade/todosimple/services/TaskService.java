package com.jeffersonandrade.todosimple.services;

import com.jeffersonandrade.todosimple.models.Task;
import com.jeffersonandrade.todosimple.models.User;
import com.jeffersonandrade.todosimple.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id){
        Optional<Task> task = taskRepository.findById(id);
        return task.orElseThrow(()-> new RuntimeException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));
    }
    @Transactional
    public Task create(Task task){
        User user = userService.findById(task.getUser().getId());
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
            throw new RuntimeException("Não é possivel excluir, pois há entidades relacionadas!");
        }
    }
}
