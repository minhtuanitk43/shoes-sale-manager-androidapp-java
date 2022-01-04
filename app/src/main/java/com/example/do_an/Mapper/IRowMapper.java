package com.example.do_an.Mapper;

import android.database.Cursor;

public interface IRowMapper<T>{
    T mapRow(Cursor cursor);
}
