package com.example.do_an.GUI.Common;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.do_an.GUI.Users.ChangePasswordActivity;
import com.example.do_an.GUI.Users.ChangeUserInfoActivity;
import com.example.do_an.GUI.Users.UserOrdersActivity;
import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.UserService;
import com.example.do_an.Utils.SessionUtil;

public class UserFragment extends Fragment {
    View v;
    TextView tvLogOut, tvFullName, tvUsername, tvPhone, tvLogin, tvRegister,
            tvAddress, tvChangePassword, tvNotLogin, tvListOrder, tvChangeInfo;
    LinearLayout lnlLoginOrRegister, lnlUserInfo;
    UserModel userModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        v = view;
        initialize();
        int userId = SessionUtil.getUserId();
        if (userId > 0) {
            userModel = UserService.getInstance().findOne(userId);
            setStatusLogin(1);
            tvLogOut.setOnClickListener(view1 -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(view1.getContext());
                builder.setIcon(getResources().getDrawable(R.drawable.ic_baseline_logout_24));
                builder.setTitle("Bạn có chắc muốn đăng xuất !");
                builder.setPositiveButton("Đăng xuất", (dialog, which) -> {
                    SessionUtil.unSetUserLogin();
                    MainActivity.hideManagerFragment();
                    MainActivity.setNotificationCart(0);
                    setStatusLogin(0);
                });
                builder.setNegativeButton("Không", (dialog, which) -> {
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });
            tvListOrder.setOnClickListener(view12 -> {
                Intent intent = new Intent(view12.getContext(), UserOrdersActivity.class);
                intent.putExtra("fromUser", "fromUser");
                startActivity(intent);
            });
            tvChangeInfo.setOnClickListener(view13 -> {
                Intent intent = new Intent(view13.getContext(), ChangeUserInfoActivity.class);
                startActivity(intent);
            });
            tvChangePassword.setOnClickListener(view14 -> {
                Intent intent = new Intent(view14.getContext(), ChangePasswordActivity.class);
                startActivity(intent);
            });
        } else {
            setStatusLogin(0);
        }

    }

    private void setStatusLogin(int i) {
        if (i == 1) {
            if (lnlUserInfo.getVisibility() == LinearLayout.GONE) {
                lnlUserInfo.setVisibility(LinearLayout.VISIBLE);
            }
            lnlLoginOrRegister.setVisibility(LinearLayout.GONE);
            tvNotLogin.setVisibility(TextView.GONE);
            tvFullName.setText(userModel.getFullName());
            tvUsername.setText(userModel.getUserName());
            tvPhone.setText(userModel.getPhone());
            tvAddress.setText(userModel.getAddress());
        } else {
            lnlUserInfo.setVisibility(LinearLayout.GONE);
            tvFullName.setText("");
            if (lnlLoginOrRegister.getVisibility() == LinearLayout.GONE) {
                lnlLoginOrRegister.setVisibility(LinearLayout.VISIBLE);
            }
            if (tvNotLogin.getVisibility() == TextView.GONE) {
                tvNotLogin.setVisibility(TextView.VISIBLE);
            }
            tvLogin.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                startActivity(intent);
            });
            tvRegister.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), RegisterActivity.class);
                startActivity(intent);
            });
        }
    }

    private void initialize() {
        lnlLoginOrRegister = v.findViewById(R.id.lnlLoginOrRegister);
        lnlUserInfo = v.findViewById(R.id.lnlInfoUser);
        tvLogOut = v.findViewById(R.id.tvLogOut);
        tvLogin = v.findViewById(R.id.tvLogin);
        tvFullName = v.findViewById(R.id.tvUserFullName);
        tvUsername = v.findViewById(R.id.tvUserUsername);
        tvPhone = v.findViewById(R.id.tvPhone);
        tvAddress = v.findViewById(R.id.tvAddress);
        tvChangePassword = v.findViewById(R.id.tvChangePassword);
        tvNotLogin = v.findViewById(R.id.tvNotLogin);
        tvRegister = v.findViewById(R.id.tvRegister);
        tvListOrder = v.findViewById(R.id.tvListOrder);
        tvChangeInfo = v.findViewById(R.id.tvChangeInfo);
    }
}