package com.example.do_an.Service;

import com.example.do_an.DAO.FavouriteDAO;
import com.example.do_an.Models.FavouriteModel;

import java.util.List;

public class FavouriteService implements IService<FavouriteModel> {
    private FavouriteDAO favouriteDAO = new FavouriteDAO();
    private static FavouriteService instance = null;
    private  FavouriteService(){}
    public static FavouriteService getInstance(){
        if(instance==null){
            synchronized (FavouriteService.class){
                instance = new FavouriteService();
            }
        }
        return instance;
    }
    public List<FavouriteModel> findAllByUserId(int userId){
        return favouriteDAO.findAllByUserId(userId);
    }
    @Override
    public List<FavouriteModel> findALl() {
        return null;
    }

    @Override
    public FavouriteModel findOne(int id) {
        return null;
    }

    @Override
    public long insert(FavouriteModel model) {
        return favouriteDAO.insert(model);
    }

    @Override
    public int update(FavouriteModel model) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return favouriteDAO.delete(id);
    }

    public FavouriteModel checkFavouriteByUserIdAndProductId(int userId, int productId) {
        return favouriteDAO.checkFavouriteByUserIdAndProductId(userId, productId);
    }
}
