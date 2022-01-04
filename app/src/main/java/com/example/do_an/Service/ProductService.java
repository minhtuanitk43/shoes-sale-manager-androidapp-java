package com.example.do_an.Service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.do_an.DAO.ProductDAO;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.Utils.GetDate;
import com.example.do_an.Utils.SessionUtil;

import java.util.List;

public class ProductService implements IService<ProductModel> {
    private ProductDAO productDAO = new ProductDAO();
    private static ProductService instance = null;

    private ProductService() {
    }

    public static ProductService getInstance() {
        if (instance == null) {
            synchronized (ProductService.class) {
                instance = new ProductService();
            }
        }
        return instance;
    }

    @Override
    public List<ProductModel> findALl() {
        return productDAO.findAll();
    }

    @Override
    public ProductModel findOne(int id) {
        return productDAO.findOne(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public long insert(ProductModel model) {
        model.setCreatedDate(GetDate.getDate());
        model.setCreatedBy(SessionUtil.getUserNameUserLogin());
        return productDAO.insert(model);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int update(ProductModel model) {
        model.setModifiedDate(GetDate.getDate());
        model.setModifiedBy(SessionUtil.getUserNameUserLogin());
        return productDAO.update(model);
    }

    @Override
    public int delete(int id) {
        return productDAO.delete(id);
    }

    public boolean checkCategoryId(Integer id) {
        return productDAO.checkCategoryId(id);
    }

    public List<ProductModel> findALlActive(int status) {
        return productDAO.findAllActive(status);
    }

    public List<ProductModel> findFavouriteByUserId(int userId) {
        return productDAO.findFavouriteByUserId(userId);
    }

    public List<ProductModel> findALlByCategoryId(int categoryId) {
        return productDAO.findAllByCategoryId(categoryId);
    }

    public List<ProductModel> findALlSort(String columnName, String sort) {
        return productDAO.findALlSort(columnName, sort);
    }

    public List<ProductModel> findALlByKeySearch(String keySearch) {
        return productDAO.findALlByKeySearch(keySearch);
    }

    public List<ProductModel> findAllByKey(String key) {
        return productDAO.findAllByKey(key);
    }

    public void updatePurchase(int id, int quantity) {
        ProductModel model = this.findOne(id);
        model.setStock(model.getStock() - quantity);
        model.setPurchases(model.getPurchases() + quantity);
        productDAO.update(model);
    }
}
