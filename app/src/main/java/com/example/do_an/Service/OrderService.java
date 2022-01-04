package com.example.do_an.Service;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.do_an.DAO.OrderDAO;
import com.example.do_an.Models.OrderModel;
import com.example.do_an.Utils.GetDate;
import com.example.do_an.Utils.SessionUtil;

import java.util.List;

public class OrderService implements IService<OrderModel> {
    private OrderDAO orderDAO = new OrderDAO();
    private static OrderService instance = null;
    private OrderService(){}
    public static OrderService getInstance(){
        if(instance == null){
            synchronized (OrderService.class){
                instance = new OrderService();
            }
        }
        return instance;
    }
    @Override
    public List<OrderModel> findALl() {
        return orderDAO.findAll();
    }

    @Override
    public OrderModel findOne(int id) {
        return orderDAO.findOne(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public long insert(OrderModel model) {
        model.setCreatedDate(GetDate.getDate());
        model.setCreatedBy(SessionUtil.getUserNameUserLogin());
        return orderDAO.insert(model);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int update(OrderModel model) {
        model.setModifiedDate(GetDate.getDate());
        model.setModifiedBy(SessionUtil.getUserNameUserLogin());
        return orderDAO.update(model);
    }

    @Override
    public int delete(int id) {
        return orderDAO.delete(id);
    }

    public List<OrderModel> findAllByUserId(int userId) {
        return orderDAO.findAllByUserId(userId);
    }
}
