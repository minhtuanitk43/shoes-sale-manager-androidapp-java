package com.example.do_an.DAO;

import android.content.ContentValues;

import com.example.do_an.Mapper.ProductMapper;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.GetContentValues.ProductContentValues;

import java.util.List;

public class ProductDAO extends AbstractDAO implements IDAO<ProductModel> {
    private ProductContentValues productContentValues = new ProductContentValues();

    @Override
    public List<ProductModel> findAll() {
        String sql = "SELECT * FROM Product";
        return query(sql, new ProductMapper());
    }

    @Override
    public ProductModel findOne(int id) {
        String sql = "SELECT * FROM Product WHERE id=?";
        List<ProductModel> list = query(sql, new ProductMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public long insert(ProductModel model) {
        ContentValues contentValues = productContentValues.getContentValues(model);
        return insert(ProductModel.TABLE_NAME, contentValues);
    }

    @Override
    public int update(ProductModel model) {
        ContentValues contentValues = productContentValues.getContentValues(model);
        return update(ProductModel.TABLE_NAME, contentValues, model.getId());
    }

    @Override
    public int delete(int id) {
        return delete(ProductModel.TABLE_NAME, "id=?", id);
    }

    public boolean checkCategoryId(Integer id) {
        String sql = "SELECT * FROM Product WHERE categoryid=? LIMIT ?";
        List<ProductModel> list = query(sql, new ProductMapper(), id, 1);
        return !list.isEmpty();
    }

    public List<ProductModel> findAllActive(int status) {
        String sql = "SELECT * FROM Product WHERE status=?";
        return query(sql, new ProductMapper(), status);
    }

    public List<ProductModel> findFavouriteByUserId(int userId) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT Product.id, name, thumbnail, price, stock, purchases, status, sizes, ");
        sql.append("categoryid, shortdescription, description, product.createddate, product.createdby, ");
        sql.append("product.modifieddate, product.modifiedby FROM Product ");
        sql.append("INNER JOIN Favourite ON Product.id = Favourite.productid WHERE userid = ?");
        return query(sql.toString(), new ProductMapper(), userId);
    }

    public List<ProductModel> findAllByCategoryId(int categoryId) {
        String sql = "SELECT * FROM Product WHERE categoryid = ?";
        return query(sql, new ProductMapper(), categoryId);
    }

    public List<ProductModel> findALlSort(String columnName, String sort) {
        String sql = "SELECT * FROM Product ORDER BY ? ?";
        return query(sql, new ProductMapper(), columnName, sort);
    }

    public List<ProductModel> findALlByKeySearch(String keySearch) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT Product.id, Product.name, thumbnail, price, stock, purchases, status, sizes, ");
        sql.append("categoryid, shortdescription, description, product.createddate, product.createdby, ");
        sql.append("product.modifieddate, product.modifiedby FROM Product ");
        sql.append("INNER JOIN Category ON Product.categoryid = Category.id ");
        sql.append("WHERE Category.keysearch COLLATE UTF8CI LIKE '%' || ? || '%'");
        return query(sql.toString(), new ProductMapper(), keySearch);
    }
    public List<ProductModel>findAllByKey(String key){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM Product ");
        sql.append("WHERE name COLLATE UTF8CI LIKE '%' || ? || '%' OR shortdescription COLLATE UTF8CI LIKE '%' || ? || '%' ");
        sql.append("OR description COLLATE UTF8CI LIKE '%' || ? || '%'");
        return query(sql.toString(), new ProductMapper(), key);
    }
}
