package com.example.do_an.GUI.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.do_an.GUI.Admin.AdminCategoryActivity;
import com.example.do_an.GUI.Admin.AdminOrderActivity;
import com.example.do_an.GUI.Admin.AdminProductActivity;
import com.example.do_an.R;
import com.example.do_an.GUI.Admin.AdminUserActivity;
import com.example.do_an.Utils.SessionUtil;
import com.example.do_an.SystemConstant.SystemConstant;

public class ManagerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manager, container, false);
    }

    @Override
    public void onViewCreated(@Nullable View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnManagerIncome = view.findViewById(R.id.btn_managerIncome);
        Button btnManagerOrder = view.findViewById(R.id.btn_managerOrder);
        Button btnManagerUser = view.findViewById(R.id.btn_managerUser);
        Button btnManagerProduct = view.findViewById(R.id.btn_managerProduct);
        Button btnManagerCategory = view.findViewById(R.id.btn_managerCategory);
        btnManagerUser = view.findViewById(R.id.btn_managerUser);

        btnManagerProduct.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AdminProductActivity.class);
            startActivity(intent);
        });
        btnManagerCategory.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), AdminCategoryActivity.class);
            startActivity(intent);
        });
        if(SessionUtil.getPermissionUserLogin().equals(SystemConstant.PERMISSION_ADMIN)) {
            btnManagerUser.setOnClickListener(view13 -> {
                Intent intent = new Intent(getContext(), AdminUserActivity.class);
                startActivity(intent);
            });
        }else{
            btnManagerUser.setEnabled(false);
        }
        btnManagerOrder.setOnClickListener(view14 -> {
            Intent intent = new Intent(view14.getContext(), AdminOrderActivity.class);
            startActivity(intent);
        });
    }
}
