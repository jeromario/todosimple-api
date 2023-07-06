package com.jeffersonandrade.todosimple.services;

import com.jeffersonandrade.todosimple.models.User;
import com.jeffersonandrade.todosimple.repositories.UserRepository;
import com.jeffersonandrade.todosimple.services.exceptions.DataBindingViolationException;
import com.jeffersonandrade.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User findById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuario não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }
    @Transactional
    public User create(User user){
        user.setId(null);
        user = userRepository.save(user);
        return user;
    }
    @Transactional
    public User update(User user){
        User newUser = findById(user.getId());
        newUser.setPassword(user.getPassword());
        return userRepository.save(newUser);

    }

    public void delete(Long id){
        findById(id);
        try {
            userRepository.deleteById(id);
        }catch (Exception e){
            throw new DataBindingViolationException("Não é possivel excluir, pois há entidades relacionadas!");
        }
    }
}
