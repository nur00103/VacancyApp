package com.example.vacancyapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String category;
    private String address;
    private Double salary;
    private Date startDate;
    private Date endDate;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Skill> skills;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ColumnDefault("1")
    private Integer status;


}
