package com.jeffersonandrade.todosimple.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    public interface CreateUser{}
    public interface UpdateUser{}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotBlank(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", length = 60,nullable = false)
    @NotBlank(groups = {CreateUser.class,UpdateUser.class})
    @Size(groups = {CreateUser.class,UpdateUser.class}, min = 8,max = 60)
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Task> tasks = new ArrayList<>();

    
}
