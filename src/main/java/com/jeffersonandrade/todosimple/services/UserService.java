package com.jeffersonandrade.todosimple.services;

import com.jeffersonandrade.todosimple.models.User;
import com.jeffersonandrade.todosimple.models.dto.UserCreateDTO;
import com.jeffersonandrade.todosimple.models.dto.UserUpdateDTO;
import com.jeffersonandrade.todosimple.models.enums.ProfileEnums;
import com.jeffersonandrade.todosimple.repositories.UserRepository;
import com.jeffersonandrade.todosimple.security.UserSpringSecurity;
import com.jeffersonandrade.todosimple.services.exceptions.AuthorizationException;
import com.jeffersonandrade.todosimple.services.exceptions.DataBindingViolationException;
import com.jeffersonandrade.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


    public User findById(Long id){
        UserSpringSecurity userSpringSecurity = authentication();
        if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnums.ADMIN) && !id.equals(userSpringSecurity.getId())){
            throw new AuthorizationException("Acesso negado!");
        }
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuario não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }
    @Transactional
    public User create(User user){
        user.setId(null);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setProfiles(Stream.of(ProfileEnums.USER.getCode()).collect(Collectors.toSet()));
        return userRepository.save(user);
    }
    @Transactional
    public User update(User user){
        User newUser = findById(user.getId());
        newUser.setPassword(user.getPassword());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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

    public static UserSpringSecurity authentication(){
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }catch (Exception e){
            return null;
        }
    }

    public User fromDTO(@Valid UserCreateDTO dto){
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
    public User fromDTO(@Valid UserUpdateDTO dto){
        User user = new User();
        user.setId(dto.getId());
        user.setPassword(dto.getPassword());
        return user;
    }
}
