package com.example.do_an.Service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.do_an.DAO.UserDAO;
import com.example.do_an.Models.UserModel;
import com.example.do_an.Utils.GetPasswordInMD5;
import com.example.do_an.Utils.GetDate;
import com.example.do_an.Utils.SessionUtil;

import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserService implements IService<UserModel> {
    private static UserService instance = null;
    private UserDAO userDAO = new UserDAO();

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                instance = new UserService();
            }
        }
        return (UserService) instance;
    }

    public boolean checkUsername(String userName) {
        UserModel userModel = userDAO.getByUsername(userName);
        return userModel != null;
    }

    public UserModel findOneByUserNameAndPasswordAndStatus(String username, String password, int satus) throws NoSuchAlgorithmException {
        return userDAO.findOneByUserNameAndPasswordAndStatus(
                username, GetPasswordInMD5.getPasswordInMD5(password), satus);
    }

    @Override
    public List<UserModel> findALl() {
        return userDAO.findAll();
    }

    @Override
    public UserModel findOne(int id) {
        return userDAO.findOne(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public long insert(UserModel model) {
        model.setCreatedDate(GetDate.getDate());
        try {
            model.setPassword(GetPasswordInMD5.getPasswordInMD5(model.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (model.getCreatedBy() == null) {
            model.setCreatedBy(SessionUtil.getUserNameUserLogin());
        }
        return userDAO.insert(model);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int update(UserModel model) {
        model.setModifiedDate(GetDate.getDate());
        model.setModifiedBy(SessionUtil.getUserNameUserLogin());
        if (!model.getPassword().isEmpty()) {
            try {
                model.setPassword(GetPasswordInMD5.getPasswordInMD5(model.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            model.setPassword(userDAO.findOne(model.getId()).getPassword());
        }
        return userDAO.update(model);
    }

    @Override
    public int delete(int id) {
        return userDAO.delete(id);
    }
}
