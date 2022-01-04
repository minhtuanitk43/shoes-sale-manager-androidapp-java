package com.example.do_an.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.do_an.Database.Database;
import com.example.do_an.Mapper.IRowMapper;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO{
    private static final String DATABASE_NAME = "ShoesShopDB.sqlite";
    private final SQLiteDatabase database = Database.initDatabase(DATABASE_NAME);

    public <T> List<T> query(String sql, IRowMapper<T> IRowMapper, Object... parameters) {
        List<T> result = new ArrayList<>();
        Cursor cursor = database.rawQuery(sql, getSelection(parameters));
        while (cursor.moveToNext()){
            result.add(IRowMapper.mapRow(cursor));
        }
        return result;
    }
    public int update(String tableName, ContentValues contentValues, int id) {
        return database.update(tableName, contentValues, "id=?", new String[]{String.valueOf(id)});
    }

    public Long insert(String tableName, ContentValues contentValues) {
        return database.insert(tableName, null, contentValues);
    }
    public int delete(String tableName, String where, Object... parameters){
        return database.delete(tableName, where, getSelection(parameters));
    }
    private String[] getSelection(Object[] parameters) {
        if(parameters == null){
            return null;
        }
        String[] strings = new String[parameters.length];
        for (int i=0; i<parameters.length;i++){
            strings[i] = parameters[i].toString();
        }
        return strings;
    }
}
