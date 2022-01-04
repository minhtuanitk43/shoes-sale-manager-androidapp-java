package com.example.do_an.DAO;

import com.example.do_an.Mapper.ImageMapper;
import com.example.do_an.Models.ImageModel;
import com.example.do_an.GetContentValues.ImageContenValues;

import java.util.List;

public class ImageDAO extends AbstractDAO implements IDAO<ImageModel> {
    private ImageContenValues imageContenValues = new ImageContenValues();
    public List<ImageModel> findAllByProductId (int id){
        String sql = "SELECT * FROM Image WHERE productid=?";
        return query(sql, new ImageMapper(), id);
    }
    @Override
    public List<ImageModel> findAll() {
        String sql = "SELECT * FROM Image";
        return query(sql, new ImageMapper());
    }

    @Override
    public ImageModel findOne(int id) {
        return null;
    }

    @Override
    public long insert(ImageModel model) {
        return insert(ImageModel.TABLE_NAME, imageContenValues.getContentValues(model));
    }

    @Override
    public int update(ImageModel model) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }

    public void deleteByProductId(Integer productId) {
        delete(ImageModel.TABLE_NAME, "productid=?", productId);
    }
}
