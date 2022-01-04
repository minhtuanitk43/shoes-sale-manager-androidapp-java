package com.example.do_an.Adapter.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.do_an.GUI.Admin.InsertOrUpdateUserActivity;
import com.example.do_an.Models.UserModel;
import com.example.do_an.R;
import com.example.do_an.Service.CartService;
import com.example.do_an.Service.UserService;
import com.example.do_an.SystemConstant.SystemConstant;
import com.example.do_an.Utils.SessionUtil;

import java.util.List;

public class AdminUserAdapter extends BaseAdapter {
    private Context context;
    private List<UserModel> list;

    public AdminUserAdapter(Context context, List<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForColorStateLists"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View row = inflater.inflate(R.layout.list_row_admin_user, null);
        final UserModel userModel = list.get(i);
        TextView userName = row.findViewById(R.id.tvUserName);
        TextView fullName = row.findViewById(R.id.tvUserFullName);
        TextView permission = row.findViewById(R.id.tvUserPermission);
        TextView status = row.findViewById(R.id.tvUserStatus);
        ImageView imgUser = row.findViewById(R.id.imgUser);

        if(userModel.getStatus() == SystemConstant.STATUS_ACTIVE) {
            status.setText("Hoạt động");
            status.setTextColor(Color.parseColor("#007700"));
            if (userModel.getPermission().equals(SystemConstant.PERMISSION_ADMIN)) {
                imgUser.setBackgroundTintList(context.getResources().getColorStateList(R.color.violet));
            }else if(userModel.getPermission().equals(SystemConstant.PERMISSION_STAFF)){
                imgUser.setBackgroundTintList(context.getResources().getColorStateList(R.color.blue));
            }
        }else {
            status.setText("Không hoạt động");
            status.setTextColor(Color.parseColor("#FF3333"));
            imgUser.setBackgroundTintList(context.getResources().getColorStateList(R.color.gray));
        }
        userName.setText(userModel.getUserName());
        fullName.setText(userModel.getFullName());
        permission.setText(userModel.getPermission());


        Button btnUpdate = row.findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(view1 -> {
            Intent intent = new Intent(row.getContext(), InsertOrUpdateUserActivity.class);
            intent.putExtra("id", userModel.getId());
            context.startActivity(intent);
        });

        Button btnDelete = row.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setTitle("Xác nhận xóa");
            builder.setMessage("Bạn có chắc chắn muốn xóa user này ?");
            builder.setPositiveButton("Có", (dialog, which) -> {
                if (SessionUtil.getUserNameUserLogin().equals(userModel.getUserName())) {
                    AlertDialog.Builder dlgAlert =
                            new AlertDialog.Builder(context);
                    dlgAlert.setMessage("Không thể xóa tài khoản hiện hành !");
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                } else {
                    UserService.getInstance().delete(userModel.getId());
                    CartService.getInstance().deleteAllByUserId(userModel.getId());
                    list.clear();
                    list = UserService.getInstance().findALl();
                    notifyDataSetChanged();
                }

            });
            builder.setNegativeButton("Không", (dialog, which) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
        return row;
    }
}
