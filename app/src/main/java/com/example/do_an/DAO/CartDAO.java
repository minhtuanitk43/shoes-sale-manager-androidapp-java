package com.example.do_an.DAO;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.do_an.GetContentValues.CartContentValues;
import com.example.do_an.Mapper.CartMapper;
import com.example.do_an.Models.CartModel;

import java.util.List;

public class CartDAO extends AbstractDAO implements IDAO<CartModel> {
    private CartContentValues contentValues = new CartContentValues();

    @Override
    public List<CartModel> findAll() {
        return null;
    }

    @Override
    public CartModel findOne(int id) {
        return null;
    }

    @Override
    public long insert(CartModel model) {
        return insert(CartModel.TABLE_NAME, contentValues.getContentValues(model));
    }

    @Override
    public int update(CartModel model) {
        return update(CartModel.TABLE_NAME, contentValues.getContentValues(model), model.getId());
    }

    @Override
    public int delete(int id) {
        return delete(CartModel.TABLE_NAME, "id=?", id);
    }

    public int deleteAllByProductId(Integer productId) {
        return delete(CartModel.TABLE_NAME, "productid=?", productId);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public CartModel findByProductIdAndUserIdAndSize(int productId, int userId, String size) {
        String sql = "SELECT * FROM Cart WHERE productid = ? AND userid = ? AND productSize = ?";
        return query(sql, new CartMapper(), productId, userId, size).stream().findFirst().orElse(null);
    }

    public List<CartModel> findAllByUserId(int userId) {
        String sql = "SELECT * FROM Cart WHERE userid = ?";
        return query(sql, new CartMapper(), userId);
    }

    public void deleteAllByUserId(int userId) {
        delete(CartModel.TABLE_NAME,"userid = ?", userId);
    }
}
