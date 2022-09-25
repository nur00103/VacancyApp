package com.example.vacancyapp.dao;

import com.example.vacancyapp.entity.Vacancy;
import lombok.RequiredArgsConstructor;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class VacancyDao {
    private final EntityManager entityManager;


    public List<Vacancy> list(String name, String category, String address, Integer salary, String startDate, String endDate, Integer page, Integer size) {
        Session session = entityManager.unwrap(Session.class);
        String sql = "select v from Vacancy v where 1=1 ";
        String where = "";
        Map<String, Object> parameters = new HashMap<>();
        if(!ObjectUtils.isEmpty(name)){
            where += "and lower(v.name) like lower(:name) ";
            parameters.put("name", name);
        }
        if(!ObjectUtils.isEmpty(category)){
            where += "and lower(v.category) like lower(:category) ";
            parameters.put("category", category);
        }if(!ObjectUtils.isEmpty(address)){
            where += "and lower(v.address)=lower(:address) ";
            parameters.put("address", address);
        }if(!ObjectUtils.isEmpty(salary)){
            where += "and v.salary=:salary ";
            parameters.put("salary", salary);
        }if(!ObjectUtils.isEmpty(startDate)){
            where += "and v.startDate>=':startDate' ";
            parameters.put("startDate", startDate);
        }if(!ObjectUtils.isEmpty(endDate)){
            where += "and v.endDate>=':endDate' ";
            parameters.put("endDate", endDate);
        }

        Query query = session.createQuery(sql);
        if(!parameters.isEmpty()){
            parameters.entrySet().forEach(entry->{
                query.setParameter(entry.getKey(), entry.getValue());
            });
        }

        int firstResult = page * size;
        query.setFirstResult(firstResult);
        query.setMaxResults(size);
        List<Vacancy> vacancyList = query.getResultList();

        return vacancyList;
    }
}
