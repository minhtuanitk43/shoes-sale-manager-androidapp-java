package com.example.do_an.GUI.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_an.Models.CategoryModel;
import com.example.do_an.Models.ImageModel;
import com.example.do_an.Models.ProductModel;
import com.example.do_an.GUI.Users.UserProductDetailActivity;
import com.example.do_an.R;
import com.example.do_an.Service.CategoryService;
import com.example.do_an.Service.ImageService;
import com.example.do_an.Service.ProductService;
import com.example.do_an.Utils.GetByteArrayOrBitmap;
import com.example.do_an.SystemConstant.SystemConstant;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InsertOrUpdateProductActivity extends AppCompatActivity {
    private List<CategoryModel> listCategory;
    private List<String> categoryNames = new ArrayList<>();
    private List<String> listStatus = new ArrayList<>();
    private Button btnInsertOrUpdate, btnRefresh, btnResetImage;
    private TextView tvTitleInsertOrUpdateProduct;
    private ImageView imgBack;
    private EditText edtName, edtShortDes, edtDescription, edtPrice, edtStock, edtSizes;
    private String productName, productShortDes, productDes, productSizes;
    private double productPrice;
    private int productStock;
    private Spinner spinnerCategory, spinnerStatus;
    private ImageView image1, image2, image3, image4, image5, image6;
    int checkImage = 0;
    private Integer id;
    int SELECT_PHOTO = 1;
    private CategoryModel categoryModel;
    Uri uri;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ProductModel productModelUpdate;
    private List<byte[]> images;
    private List<ImageView> imageViews;
    private int status;
    private ArrayAdapter<String> adapterCategory, adapterStatus;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_or_update_product);

        //initialize elements
        initializeData();

        //set dataSource for spinner category
        listCategory = CategoryService.getInstance().findALl();
        for (CategoryModel s : listCategory) {
            categoryNames.add(s.getName());
        }
        adapterCategory = new ArrayAdapter<>(InsertOrUpdateProductActivity.this,
                android.R.layout.simple_spinner_item, categoryNames);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategory);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categoryModel = listCategory.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //set dataSource for spinnerStatus
        listStatus.add("Hiện");
        listStatus.add("Ẩn");
        adapterStatus = new ArrayAdapter<>(InsertOrUpdateProductActivity.this,
                android.R.layout.simple_spinner_item, listStatus);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (listStatus.get(i).equals("Hiện")) {
                    status = SystemConstant.STATUS_ACTIVE;
                } else status = SystemConstant.STATUS_INACTIVE;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //check Insert or Update, get data
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (getIntent().hasExtra("id")) {
                id = bundle.getInt("id");
                //Set event imgBack to List Product
                imgBack.setOnClickListener(view -> {
                    Intent intent = new Intent(InsertOrUpdateProductActivity.this, AdminProductActivity.class);
                    startActivity(intent);
                });
            } else if (getIntent().hasExtra("productDetailId2")) {
                id = bundle.getInt("productDetailId2");
                imgBack.setOnClickListener(view -> {
                    Intent intent = new Intent(InsertOrUpdateProductActivity.this, UserProductDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                });

            } else {
                id = bundle.getInt("productDetailId");
                imgBack.setOnClickListener(view -> {
                    Intent intent = new Intent(InsertOrUpdateProductActivity.this, AdminProductDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                });
            }
            tvTitleInsertOrUpdateProduct.setText("Cập nhật sản phẩm");
            btnInsertOrUpdate.setText("Cập nhật");
            getData();
        } else {
            imgBack.setOnClickListener(view -> {
                Intent intent = new Intent(InsertOrUpdateProductActivity.this, AdminProductActivity.class);
                startActivity(intent);
            });
        }

        //set event choose Image
        image1.setOnClickListener(this::onClick);
        image2.setOnClickListener(this::onClick);
        image3.setOnClickListener(this::onClick);
        image4.setOnClickListener(this::onClick);
        image5.setOnClickListener(this::onClick);
        image6.setOnClickListener(this::onClick);

        //set event btn InsertOrUpdate
        btnInsertOrUpdate.setOnClickListener(view -> {
            int check = 1;
            try {
                Pattern patternName = Pattern.compile("[^@#$%*&!/~`]*$");
                productName = edtName.getText().toString().trim();
                if (productName.isEmpty()) {
                    edtName.setError("Chưa nhập tên sản phẩm");
                    check = 0;
                }
                if (!patternName.matcher(productName).matches()) {
                    check = 0;
                    edtName.setError("Tên chỉ bao gồm chữ cái, số và các ký tự: '_', '.', '-'.");
                }
                Pattern patternPrice = Pattern.compile("^([0-9][0-9].*)$");
                if (edtPrice.getText().toString().trim().length() <= 0) {
                    edtPrice.setError("Chưa nhập giá bán !");
                    check = 0;
                }
                if (!patternPrice.matcher(edtPrice.getText().toString()).matches()) {
                    edtPrice.setError("Giá bán không hợp lệ !");
                    check = 0;
                }
                Pattern patternStock = Pattern.compile("^[0-9]\\d*$");
                if (edtStock.getText().toString().trim().length() <= 0) {
                    edtStock.setError("Chưa nhập stock !");
                    check = 0;
                }
                if (!patternStock.matcher(edtStock.getText().toString()).matches()) {
                    edtStock.setError("Stock không hợp lệ !");
                    check = 0;
                }
                Pattern patternSizes = Pattern.compile("^[0-9,.\\s]*$");
                productSizes = edtSizes.getText().toString().trim();
                if (productSizes.length() <= 0) {
                    edtSizes.setError("Chưa nhập size !");
                    check = 0;
                }
                if (!patternSizes.matcher(productSizes).matches()) {
                    edtSizes.setError("Size là các số phân cách bằng dấu phẩy ','");
                    check = 0;
                }
                if (check == 0) {
                    throw new Exception("Thông tin không hợp lệ !");
                }
                productPrice = Double.parseDouble(edtPrice.getText().toString());
                productStock = Integer.parseInt(edtStock.getText().toString().trim());
                images = GetByteArrayOrBitmap.getListByteArray(imageViews);
                if (images.size() == 0) {
                    throw new Exception("Sản phẩm phải có ít nhất 1 hình ành !");
                }
                productShortDes = edtShortDes.getText().toString();
                productDes = edtDescription.getText().toString();
                save();
                View layout = LayoutInflater.from(getBaseContext()).inflate(R.layout.toast, (ViewGroup) view.findViewById(R.id.toast_layout_root));
                TextView text = (TextView) layout.findViewById(R.id.text);
                ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                imageView.setImageResource(R.drawable.ic_baseline_check_24);
                if (id == null) {
                    text.setText("Thêm thành công");
                } else {
                    text.setText("Cập nhật thành công");
                }
                Toast toast = new Toast(getBaseContext());
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            } catch (Exception e) {
                android.app.AlertDialog.Builder dlgAlert =
                        new android.app.AlertDialog.Builder(InsertOrUpdateProductActivity.this);
                dlgAlert.setMessage(e.getMessage());
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }

        });
        btnResetImage.setOnClickListener(view -> resetImage());
        btnRefresh.setOnClickListener(view -> {
            edtName.setText("");
            edtStock.setText("");
            edtDescription.setText("");
            edtShortDes.setText("");
            edtPrice.setText("");
            edtSizes.setText("");
            resetImage();
            if (id != null) {
                getData();
            }
        });
    }

    private void resetImage() {
        for (ImageView imageView : imageViews) {
            if (imageView.getDrawable() != null) {
                imageView.setImageDrawable(null);
            }
        }
    }

    private void getData() {
        productModelUpdate = ProductService.getInstance().findOne(id);
        if (productModelUpdate != null) {
            edtName.setText(productModelUpdate.getName());
            edtDescription.setText(productModelUpdate.getDescription());
            edtStock.setText(String.valueOf(productModelUpdate.getStock()));
            edtPrice.setText(String.valueOf((long) productModelUpdate.getPrice()));
            edtShortDes.setText(productModelUpdate.getShortDescription());
            edtSizes.setText(productModelUpdate.getSizes());
            Bitmap bitmap = BitmapFactory.decodeByteArray(productModelUpdate.getThumbnail(), 0, productModelUpdate.getThumbnail().length);
            image1.setImageBitmap(bitmap);
            List<Bitmap> bitmapList = new ArrayList<>();
            for (ImageModel imageModel : productModelUpdate.getImages()) {
                Bitmap bitmap1 = BitmapFactory.decodeByteArray(imageModel.getImage(), 0, imageModel.getImage().length);
                bitmapList.add(bitmap1);
            }
            ImageView[] listImageView = {image2, image3, image4, image5, image6};
            for (int i = 0; i < bitmapList.size(); i++) {
                listImageView[i].setImageBitmap(bitmapList.get(i));
            }
            spinnerCategory.setSelection(adapterCategory.getPosition(productModelUpdate.getCategory().getName()));
            spinnerStatus.setSelection(adapterStatus.getPosition(productModelUpdate.getStatus() == 1 ? "Hiện" : "Ẩn"));
        }
    }

    @SuppressLint("CutPasteId")
    private void initializeData() {
        imgBack = findViewById(R.id.imgBack);
        spinnerCategory = findViewById(R.id.spinner_category);
        spinnerStatus = findViewById(R.id.spinner_status);
        tvTitleInsertOrUpdateProduct = findViewById(R.id.tv_title_insertProduct);
        btnInsertOrUpdate = findViewById(R.id.btn_insertOrUpdate);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnResetImage = findViewById(R.id.btn_resetImage);
        images = new ArrayList<>();

        edtName = findViewById(R.id.edt_productName);
        edtPrice = findViewById(R.id.edt_productStock);
        edtPrice = findViewById(R.id.edt_productPrice);
        edtShortDes = findViewById(R.id.edt_productShortDes);
        edtDescription = findViewById(R.id.edt_productDescription);
        edtStock = findViewById(R.id.edt_productStock);
        edtSizes = findViewById(R.id.edt_productSizes);

        image1 = findViewById(R.id.imgImage1);
        image2 = findViewById(R.id.imgImage2);
        image3 = findViewById(R.id.imgImage3);
        image4 = findViewById(R.id.imgImage4);
        image5 = findViewById(R.id.imgImage5);
        image6 = findViewById(R.id.imgImage6);

        imageViews = new ArrayList<>();
        imageViews.add(image1);
        imageViews.add(image2);
        imageViews.add(image3);
        imageViews.add(image4);
        imageViews.add(image5);
        imageViews.add(image6);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void save() {
        ProductModel productModel = new ProductModel();

        productModel.setName(productName);
        productModel.setThumbnail(images.get(0));
        productModel.setPrice(productPrice);
        productModel.setShortDescription(productShortDes);
        productModel.setDescription(productDes);
        productModel.setStock(productStock);
        productModel.setPurchases(0);
        productModel.setCategory(categoryModel);
        productModel.setSizes(productSizes);
        productModel.setStatus(status);
        int productId;
        if (id == null) {
            productId = (int) ProductService.getInstance().insert(productModel);

        } else {
            productId = id;
            productModel.setId(productId);
            productModel.setCreatedDate(productModelUpdate.getCreatedDate());
            productModel.setCreatedBy(productModelUpdate.getCreatedBy());
            ProductService.getInstance().update(productModel);
            ImageService.getInstance().deleteByProductId(productId);
        }
        for (int i = 1; i < images.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImage(images.get(i));
            imageModel.setProductId(productId);
            ImageService.getInstance().insert(imageModel);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("NonConstantResourceId")
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgImage1:
                checkImage = 1;
                selectImage();
                break;
            case R.id.imgImage2:
                checkImage = 2;
                selectImage();
                break;
            case R.id.imgImage3:
                checkImage = 3;
                selectImage();
                break;
            case R.id.imgImage4:
                checkImage = 4;
                selectImage();
                break;
            case R.id.imgImage5:
                checkImage = 5;
                selectImage();
                break;
            case R.id.imgImage6:
                checkImage = 6;
                selectImage();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertOrUpdateProductActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            } else if (options[item].equals("Choose from Gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap img_Bitmap;
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            assert data != null;
            img_Bitmap = (Bitmap) data.getExtras().get("data");
            switch (checkImage) {
                case 1:
                    image1.setImageBitmap(img_Bitmap);
                    break;
                case 2:
                    image2.setImageBitmap(img_Bitmap);
                    break;
                case 3:
                    image3.setImageBitmap(img_Bitmap);
                    break;
                case 4:
                    image4.setImageBitmap(img_Bitmap);
                    break;
                case 5:
                    image5.setImageBitmap(img_Bitmap);
                    break;
                case 6:
                    image6.setImageBitmap(img_Bitmap);
                    break;
            }
        } else {
            if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null) {
                uri = data.getData();
                try {
                    img_Bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    switch (checkImage) {
                        case 1:
                            image1.setImageBitmap(img_Bitmap);
                            break;
                        case 2:
                            image2.setImageBitmap(img_Bitmap);
                            break;
                        case 3:
                            image3.setImageBitmap(img_Bitmap);
                            break;
                        case 4:
                            image4.setImageBitmap(img_Bitmap);
                            break;
                        case 5:
                            image5.setImageBitmap(img_Bitmap);
                            break;
                        case 6:
                            image6.setImageBitmap(img_Bitmap);
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}