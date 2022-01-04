package com.example.do_an.DAO;

import com.example.do_an.Mapper.UserMapper;
import com.example.do_an.Models.UserModel;
import com.example.do_an.GetContentValues.UserContentValues;

import java.util.List;

import dagger.Module;

@Module
public class UserDAO extends AbstractDAO implements IDAO<UserModel> {
    private UserContentValues userContentValues = new UserContentValues();

    public UserModel findOneByUserNameAndPasswordAndStatus(String userName, String password, int statusActive) {
        String sql = "SELECT * FROM Users WHERE username=? AND password=? AND status=?";
        List<UserModel> users = query(sql, new UserMapper(), userName, password, statusActive);
        return users.isEmpty() ? null : users.get(0);
    }


    @Override
    public List<UserModel> findAll() {
        String sql = "SELECT * FROM Users";
        return query(sql, new UserMapper());
    }

    @Override
    public UserModel findOne(int id) {
        String sql = "SELECT * FROM Users WHERE id=?";
        List<UserModel> users = query(sql, new UserMapper(), id);
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public long insert(UserModel model) {
        return insert(UserModel.TABLE_NAME, userContentValues.getContentValues(model));
    }

    @Override
    public int update(UserModel model) {
        return update(UserModel.TABLE_NAME, userContentValues.getContentValues(model), model.getId());
    }

    @Override
    public int delete(int id) {
        return delete(UserModel.TABLE_NAME, "id=?", id);
    }

    public UserModel getByUsername(String userName) {
        String sql = "SELECT * FROM Users WHERE username=?";
        List<UserModel> list = query(sql,new UserMapper(), userName);
        return list.isEmpty() ? null : list.get(0);
    }
}
