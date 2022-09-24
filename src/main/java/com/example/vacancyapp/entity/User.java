package com.example.vacancyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String mail;
    private String password;
    private String photo;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Role> roles;

    @ColumnDefault("1")
    private Integer status;

}
