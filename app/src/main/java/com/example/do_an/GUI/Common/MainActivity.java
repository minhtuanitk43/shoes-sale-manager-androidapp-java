package com.example.do_an.GUI.Common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.do_an.Database.Database;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.Service.ProductService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<ProductModel> list;
    private static Menu menu;
    private static BadgeDrawable badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //check context - declare sessionUtils
        new SessionUtil(getBaseContext());
        if (Database.context == null) {
            Database.context = this;
        }
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        menu = bottomNav.getMenu();
        badge = bottomNav.getOrCreateBadge(R.id.nav_cart);
        // An icon only badge will be displayed unless a number is set:
        int userId = SessionUtil.getUserId();
        badge.clearNumber();
        badge.setVisible(false);
        if (userId > 0) {
            int numberCart = CartService.getInstance().findALlByUserId(userId).size();
            if (numberCart > 0) {
                badge.setVisible(true);
                badge.setNumber(numberCart);
            }
        }
        //Check permission
        checkPermission(menu);

        //check backFrom and select Fragment
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String fragmentKey = bundle.getString("fragmentKey");
            if (fragmentKey.equalsIgnoreCase(SystemConstant.FRAGMENT_KEY_MANAGER)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ManagerFragment()).commit();
                bottomNav.setSelectedItemId(R.id.nav_manage);
            } else if (fragmentKey.equalsIgnoreCase(SystemConstant.FRAGMENT_KEY_CART)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CartFragment()).commit();
                bottomNav.setSelectedItemId(R.id.nav_cart);
            }
        } else {
            if (savedInstanceState == null) {
                list = ProductService.getInstance().findALl();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment(list)).commit();
            }
        }
        bottomNav.setOnNavigationItemSelectedListener(navListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int i = 0;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            list = ProductService.getInstance().findALlActive(SystemConstant.STATUS_ACTIVE);
                            selectedFragment = new HomeFragment(list);
                            i = 1;
                            break;
                        case R.id.nav_cart:
                            selectedFragment = new CartFragment();
                            i = 1;
                            break;
                        case R.id.nav_favourite: {
                            String userNameLogin = SessionUtil.getUserNameUserLogin();
                            if (userNameLogin.isEmpty()) {
                                if (list != null) {
                                    list.clear();
                                }
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setIcon(getResources().getDrawable(R.drawable.ic_baseline_login_24));
                                builder.setTitle("Bạn chưa đăng nhập");
                                builder.setPositiveButton("Đăng nhập ngay", (dialog, which) -> {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                });
                                builder.setNegativeButton("Lúc khác", (dialog, which) -> {
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            } else {
                                list = ProductService.getInstance().findFavouriteByUserId(SessionUtil.getUserId());
                            }
                            selectedFragment = new HomeFragment(list);
                            i = 1;
                            break;
                        }
                        case R.id.nav_user:
                            selectedFragment = new UserFragment();
                            i = 1;
                            break;
                        case R.id.nav_manage:
                            selectedFragment = new ManagerFragment();
                            i = 1;
                            break;
                    }
                    if (i == 1) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();
                    }
                    return true;
                }
            };

    void checkPermission(Menu menu) {
        if (SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_ADMIN)
                || SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_STAFF)) {
            if (!menu.getItem(4).isVisible()) {
                menu.getItem(4).setVisible(true);
            }
        } else if (menu.getItem(4).isVisible()) {
            if (menu.getItem(4).isVisible()) {
                menu.getItem(4).setVisible(false);
            }
        }

    }

    public static void setNotificationCart(int number) {
        if (number > 0) {
            badge.setVisible(true);
            badge.setNumber(number);
        } else {
            badge.clearNumber();
            badge.setVisible(false);
        }
    }

    public static void hideManagerFragment() {
        if (menu.getItem(4).isVisible()) {
            menu.getItem(4).setVisible(false);
        }
    }
}