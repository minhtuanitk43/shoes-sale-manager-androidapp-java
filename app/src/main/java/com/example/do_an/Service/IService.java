package com.example.do_an.Service;

import java.util.List;

public interface IService<T> {
    List<T> findALl();
    T findOne(int id);
    long insert(T model);
    int update(T model);
    int delete(int id);
}
