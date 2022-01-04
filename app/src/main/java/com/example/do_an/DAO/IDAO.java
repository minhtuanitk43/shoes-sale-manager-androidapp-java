package com.example.do_an.DAO;

import java.util.List;

public interface IDAO <T>{
    List<T> findAll();
    T findOne(int id);
    long insert(T model);
    int update(T model);
    int delete(int id);
}
