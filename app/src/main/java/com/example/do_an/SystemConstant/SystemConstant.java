package com.example.do_an.SystemConstant;

import java.util.ArrayList;
import java.util.List;

public class SystemConstant {
    public static final String PRODUCT_SOLD_OUT = "HẾT HÀNG";
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_INACTIVE = 0;

    public static final int STATUS_ORDER_PENDING = 1;
    public static final int STATUS_ORDER_DELIVERING = 2;
    public static final int STATUS_ORDER_DELIVERED = 3;
    public static final int STATUS_ORDER_CANCEL = 0;
    public static final String STATUS_ORDER_PENDING_STR = "Đang xử lý";
    public static final String STATUS_ORDER_DELIVERING_STR = "Đang giao";
    public static final String STATUS_ORDER_DELIVERED_STR = "Đã giao";
    public static final String STATUS_ORDER_CANCEL_STR = "Đã hủy";


    public static final String PERMISSION_ADMIN = "ADMIN";
    public static final String PERMISSION_STAFF = "STAFF";
    public static final String PERMISSION_USER = "USER";

    public static final String FRAGMENT_KEY_MANAGER = "manager";
    public static final String FRAGMENT_KEY_USER = "user";
    public static final String FRAGMENT_KEY_CART = "cart";

    public static final String SORT_ALL = "Tất cả";
    public static final String SORT_IN_STOCK = "Còn hàng";
    public static final String SORT_OUT_STOCK = "Hết hàng";
    public static final String SORT_HIDE = "Trạng thái ẩn";
    public static final String SORT_VISIBLE = "Trạng thái hiện";

    public static final String SORT_ADMIN = "Quyền Admin";
    public static final String SORT_STAFF = "Quyền Staff";
    public static final String SORT_USER = "Quyền User";
    public static final String SORT_ACTIVE = "Đang hoạt động";
    public static final String SORT_INACTIVE = "Không hoạt động";

    public static final List<String> PERMISSIONS = new ArrayList<>();
    public static final List<String> SORT_PRODUCT = new ArrayList<>();
    public static final List<String> SORT_USERS = new ArrayList<>();

    static {
        PERMISSIONS.add(PERMISSION_ADMIN);
        PERMISSIONS.add(PERMISSION_STAFF);
        PERMISSIONS.add(PERMISSION_USER);

        SORT_PRODUCT.add(SORT_ALL);
        SORT_PRODUCT.add(SORT_IN_STOCK);
        SORT_PRODUCT.add(SORT_OUT_STOCK);
        SORT_PRODUCT.add(SORT_HIDE);
        SORT_PRODUCT.add(SORT_VISIBLE);

        SORT_USERS.add(SORT_ALL);
        SORT_USERS.add(SORT_ADMIN);
        SORT_USERS.add(SORT_STAFF);
        SORT_USERS.add(SORT_USER);
        SORT_USERS.add(SORT_INACTIVE);
        SORT_USERS.add(SORT_ACTIVE);

    }
}
