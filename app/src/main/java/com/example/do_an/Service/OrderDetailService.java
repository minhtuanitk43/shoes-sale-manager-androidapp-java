package com.example.do_an.Service;

import com.example.do_an.DAO.OrderDetailDAO;
import com.example.do_an.Models.OrderDetailModel;

import java.util.List;

public class OrderDetailService implements IService<OrderDetailModel> {
    private OrderDetailDAO orderDetailDAO = new OrderDetailDAO();
    private static OrderDetailService instance = null;

    private OrderDetailService() { }
    public static OrderDetailService getInstance(){
        if(instance==null){
            synchronized (OrderDetailService.class){
                instance = new OrderDetailService();
            }
        }
        return instance;
    }
    @Override
    public List<OrderDetailModel> findALl() {
        return null;
    }

    @Override
    public OrderDetailModel findOne(int id) {
        return null;
    }

    @Override
    public long insert(OrderDetailModel model) {
        return orderDetailDAO.insert(model);
    }

    @Override
    public int update(OrderDetailModel model) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }

    public List<OrderDetailModel> findAllByOrderId(int orderId) {
        return orderDetailDAO.findAllByOrderId(orderId);
    }

    public int deleteAllByOrderId(int orderId) {
        return orderDetailDAO.deleteAllByOrderId(orderId);
    }
}
