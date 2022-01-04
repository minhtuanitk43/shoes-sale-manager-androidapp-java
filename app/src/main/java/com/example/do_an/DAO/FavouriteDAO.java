package com.example.do_an.DAO;

import com.example.do_an.Mapper.FavouriteMapper;
import com.example.do_an.Models.FavouriteModel;
import com.example.do_an.GetContentValues.FavouriteContentValues;

import java.util.List;

public class FavouriteDAO extends AbstractDAO implements IDAO<FavouriteModel> {
    FavouriteContentValues contentValues = new FavouriteContentValues();
    @Override
    public List<FavouriteModel> findAll() {
        return null;
    }

    @Override
    public FavouriteModel findOne(int id) {
        return null;
    }

    @Override
    public long insert(FavouriteModel model) {
        return insert(FavouriteModel.TABLE_NAME, contentValues.getContentValues(model));
    }

    @Override
    public int update(FavouriteModel model) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return delete(FavouriteModel.TABLE_NAME, "id=?", id);
    }

    public List<FavouriteModel> findAllByUserId(int userId) {
        String sql = "SELECT * FROM Favourite WHERE userid=?";
        return query(sql, new FavouriteMapper(), userId);
    }

    public FavouriteModel checkFavouriteByUserIdAndProductId(int userId, int productId) {
        String sql = "SELECT * FROM Favourite WHERE userid=? AND productid=?";
        List<FavouriteModel> list = query(sql, new FavouriteMapper(), userId, productId);
        return list.isEmpty() ? null : list.get(0);
    }
}
