package com.example.do_an.DAO;

import com.example.do_an.Mapper.CategoryMapper;
import com.example.do_an.Models.CategoryModel;
import com.example.do_an.GetContentValues.CategoryContentValues;

import java.util.List;

public class CategoryDAO extends AbstractDAO implements IDAO<CategoryModel> {
    CategoryContentValues categoryContentValues = new CategoryContentValues();
    @Override
    public List<CategoryModel> findAll() {
        String sql ="SELECT * FROM Category";
        return query(sql, new CategoryMapper());
    }

    @Override
    public CategoryModel findOne(int id) {
        String sql = "SELECT * FROM Category WHERE id=?";
        List<CategoryModel> list = query(sql, new CategoryMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public long insert(CategoryModel model) {
        return insert(CategoryModel.TABLE_NAME, categoryContentValues.getContentValues(model));
    }

    @Override
    public int update(CategoryModel model) {
        return update(CategoryModel.TABLE_NAME,categoryContentValues.getContentValues(model), model.getId());
    }

    @Override
    public int delete(int id) {
        return delete(CategoryModel.TABLE_NAME, "id=?",id);
    }
}
