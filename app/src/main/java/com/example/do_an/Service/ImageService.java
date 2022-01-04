package com.example.do_an.Service;

import com.example.do_an.DAO.ImageDAO;
import com.example.do_an.Models.ImageModel;

import java.util.List;

public class ImageService implements IService<ImageModel>{
    private ImageDAO imageDAO = new ImageDAO();
    private static ImageService instance = null;
    private ImageService(){}
    public static ImageService getInstance(){
        if(instance == null){
            synchronized (ImageService.class){
                instance = new ImageService();
            }
        }
        return instance;
    }
    @Override
    public List<ImageModel> findALl() {
        return imageDAO.findAll();
    }

    @Override
    public ImageModel findOne(int id) {
        return null;
    }

    @Override
    public long insert(ImageModel model) {
        return imageDAO.insert(model);
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
        imageDAO.deleteByProductId(productId);
    }
}
