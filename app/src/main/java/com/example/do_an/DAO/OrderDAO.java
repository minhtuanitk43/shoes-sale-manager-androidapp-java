package com.example.do_an.DAO;

import com.example.do_an.GetContentValues.OrderContentValues;
import com.example.do_an.Mapper.OrderMapper;
import com.example.do_an.Models.OrderModel;

import java.util.List;

public class OrderDAO extends AbstractDAO implements IDAO<OrderModel> {
    private OrderContentValues contentValues = new OrderContentValues();
    @Override
    public List<OrderModel> findAll() {
        String sql = "SELECT * FROM Orders";
        return query(sql, new OrderMapper());
    }

    @Override
    public OrderModel findOne(int id) {
        String sql = "SELECT * FROM Orders WHERE id = ?";
        return query(sql, new OrderMapper(), id).get(0);
    }

    @Override
    public long insert(OrderModel model) {
        return insert(OrderModel.TABLE_NAME,contentValues.getContentValues(model));
    }

    @Override
    public int update(OrderModel model) {
        return update(OrderModel.TABLE_NAME, contentValues.getContentValues(model), model.getId());
    }

    @Override
    public int delete(int id) {
        return delete(OrderModel.TABLE_NAME,"id = ?", id);
    }

    public List<OrderModel> findAllByUserId(int userId) {
        String sql = "SELECT * FROM Orders WHERE userid = ?";
        return query(sql, new OrderMapper(), userId);
    }
}
