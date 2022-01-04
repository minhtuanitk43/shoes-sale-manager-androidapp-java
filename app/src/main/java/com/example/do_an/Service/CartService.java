package com.example.do_an.Service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.do_an.DAO.CartDAO;
import com.example.do_an.Models.CartModel;

import java.util.List;

public class CartService implements IService<CartModel> {
    private CartDAO cartDao = new CartDAO();
    private static CartService instance = null;

    private CartService() {
    }

    public static CartService getInstance() {
        if (instance == null) {
            synchronized (CartService.class) {
                instance = new CartService();
            }
        }
        return instance;
    }

    @Override
    public List<CartModel> findALl() {
        return null;
    }

    @Override
    public CartModel findOne(int id) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public long insert(CartModel model) {
        CartModel oldCart = cartDao.findByProductIdAndUserIdAndSize(
                model.getProduct().getId(), model.getUser().getId(), model.getProductSize());
        if(oldCart != null){
            oldCart.setQuantity(oldCart.getQuantity()+model.getQuantity());
            return cartDao.update(oldCart);
        }else{
            return cartDao.insert(model);
        }
    }

    @Override
    public int update(CartModel model) {
        return cartDao.update(model);
    }

    @Override
    public int delete(int id) {
        return cartDao.delete(id);
    }

    public void deleteAllByProductId(Integer productId) {
        cartDao.deleteAllByProductId(productId);
    }

    public List<CartModel> findALlByUserId(int userId) {
        return cartDao.findAllByUserId(userId);
    }

    public void deleteAllByUserId(int userId) {
        cartDao.deleteAllByUserId(userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CartModel findByProductIdAndUserIdAndSize(CartModel model) {
        return cartDao.findByProductIdAndUserIdAndSize(
                model.getProduct().getId(), model.getUser().getId(), model.getProductSize());
    }
}
