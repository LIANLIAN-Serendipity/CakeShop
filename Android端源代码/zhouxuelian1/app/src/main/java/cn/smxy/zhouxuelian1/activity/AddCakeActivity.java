package cn.smxy.zhouxuelian1.activity;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.entity.CakeType;
import cn.smxy.zhouxuelian1.entity.CakeTypeListResponse;
import cn.smxy.zhouxuelian1.entity.UpLoadImageResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class AddCakeActivity extends AppCompatActivity {
    private static final String TAG = "AddCakeActivity";
    private ImageView img;
    private ActivityResultLauncher<String> resultLauncher;
    private Button add;
    private EditText cakeName, introduce, price;
    private TextView caketypeId;
    private Toolbar back;
    private List<CakeType> caketypeList;
    private String cakePicture;
    private CakeType caketype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cake);
        initView();
        getCcFoodTypeList();
        setOnListener();
    }

    private void initView() {
        img = findViewById(R.id.add_cake_cakeImg);
        add = findViewById(R.id.add_Cake_Add);
        cakeName = findViewById(R.id.add_cake_cakeName);
        introduce = findViewById(R.id.add_Introduce);
        price = findViewById(R.id.add_Price);
        caketypeId = findViewById(R.id.add_TypeId);
        back = findViewById(R.id.back);
    }

    private void getCcFoodTypeList() {
        String url = UrlConstants.CAKE_TYPE_LIST_URL;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                CakeTypeListResponse cakeTypeListResponse = new Gson().fromJson(response, CakeTypeListResponse.class);
                if (cakeTypeListResponse != null && cakeTypeListResponse.getCode() == 2000) {
                    caketypeList = cakeTypeListResponse.getDataobject();
                }
            }
        });
    }

    private void setOnListener() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Log.d(TAG, "Uri:" + result);
                        if (result != null) {
                            img.setImageURI(result);
                        }
                    }
                });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultLauncher.launch("image/*");
            }
        });

        caketypeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProductType();
            }
        });

        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCakeActivity.this, ManagementActivity.class);
                intent.putExtra("intent_fragmentId", "4");
                startActivity(intent);
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 添加输入验证
                if (cakeName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddCakeActivity.this, "请输入蛋糕名称", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (price.getText().toString().trim().isEmpty()) {
                    Toast.makeText(AddCakeActivity.this, "请输入价格", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (caketype == null) {
                    Toast.makeText(AddCakeActivity.this, "请选择蛋糕类型", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadImage();
            }
        });
    }

    private void chooseProductType() {
        if (caketypeList == null || caketypeList.isEmpty()) {
            Toast.makeText(this, "没有可用的蛋糕类型", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] strings = new String[caketypeList.size()];
        for (int i = 0; i < caketypeList.size(); i++) {
            strings[i] = caketypeList.get(i).getCaketypeName();
        }

        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setItems(strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                caketypeId.setText(strings[which]);
                caketype = caketypeList.get(which);
            }
        });
        dlg.show();
    }

    private void uploadImage() {
        Drawable drawable = img.getDrawable();
        if (drawable == null) {
            Toast.makeText(this, "请选择图片", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = UrlConstants.UPLOAD_IMAGE_URL;

        OkHttpUtils.post()
                .addFile("file", "image.png", toImageFile("product.png"))
                .url(url).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "EEEEEEEEE" + e);
                        Toast.makeText(AddCakeActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "response:" + response);
                        UpLoadImageResponse upLoadImageResponse = new Gson().fromJson(response, UpLoadImageResponse.class);
                        if (upLoadImageResponse != null && upLoadImageResponse.getCode() == 2000) {
                            cakePicture = upLoadImageResponse.getDataobject();
                            addProduct();
                        } else {
                            Toast.makeText(AddCakeActivity.this, "图片上传失败: " + upLoadImageResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private File toImageFile(String fileName) {
        Drawable drawable = img.getDrawable();
        if (drawable == null) {
            Log.e(TAG, "没有选择图片");
            return null;
        }

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        File file = new File(getCacheDir(), fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "图片转换失败: " + e.getMessage());
            return null;
        }
    }

    private void addProduct() {
        String url = UrlConstants.ADD_CAKE_URL;

        Map<String, String> map = new HashMap<>();
        map.put("cakeName", cakeName.getText().toString());
        map.put("introduce", introduce.getText().toString());
        map.put("price", price.getText().toString());
        map.put("cakePicture", cakePicture);
        map.put("caketypeId", String.valueOf(caketype.getCaketypeId()));

        // 注意：不设置cakeId，由数据库自动生成

        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因：" + e.getMessage());
                Toast.makeText(AddCakeActivity.this, "添加失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "添加蛋糕成功，响应：" + response);
                Toast.makeText(AddCakeActivity.this, "添加蛋糕成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddCakeActivity.this, ManagementActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}