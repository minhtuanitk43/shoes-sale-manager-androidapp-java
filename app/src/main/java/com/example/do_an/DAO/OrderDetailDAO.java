package com.example.do_an.DAO;

import com.example.do_an.GetContentValues.OrderDetailContentValues;
import com.example.do_an.Mapper.OrderDetailMapper;
import com.example.do_an.Models.OrderDetailModel;

import java.util.List;

public class OrderDetailDAO extends AbstractDAO implements IDAO<OrderDetailModel> {
    private OrderDetailContentValues contentValues = new OrderDetailContentValues();
    @Override
    public List<OrderDetailModel> findAll() {
        return null;
    }

    @Override
    public OrderDetailModel findOne(int id) {
        return null;
    }

    @Override
    public long insert(OrderDetailModel model) {
        return insert(OrderDetailModel.TABLE_NAME, contentValues.getContentValues(model));
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
        String sql = "SELECT * FROM OrderDetail WHERE orderid = ?";
        return query(sql, new OrderDetailMapper(), orderId);
    }

    public int deleteAllByOrderId(int orderId) {
        return delete(OrderDetailModel.TABLE_NAME,"orderid = ?", orderId);
    }
}
