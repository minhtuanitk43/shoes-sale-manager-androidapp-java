package com.example.do_an.Service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.do_an.DAO.CategoryDAO;
import com.example.do_an.Models.CategoryModel;
import com.example.do_an.Utils.GetDate;
import com.example.do_an.Utils.SessionUtil;

import java.util.List;

public class CategoryService implements IService<CategoryModel> {
    private CategoryDAO categoryDAO = new CategoryDAO();
    private static CategoryService instance = null;
    private CategoryService(){}
    public static CategoryService getInstance(){
        if(instance == null){
            synchronized (CategoryService.class){
                instance = new CategoryService();
            }
        }
        return instance;
    }
    @Override
    public List<CategoryModel> findALl() {
        return categoryDAO.findAll();
    }

    @Override
    public CategoryModel findOne(int id) {
        return categoryDAO.findOne(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public long insert(CategoryModel model) {
        model.setCreatedDate(GetDate.getDate());
        model.setCreatedBy(SessionUtil.getUserNameUserLogin());
        return categoryDAO.insert(model);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int update(CategoryModel model) {
        model.setModifiedDate(GetDate.getDate());
        model.setModifiedBy(SessionUtil.getUserNameUserLogin());
        return categoryDAO.update(model);
    }

    @Override
    public int delete(int id) {
        return categoryDAO.delete(id);
    }
}
