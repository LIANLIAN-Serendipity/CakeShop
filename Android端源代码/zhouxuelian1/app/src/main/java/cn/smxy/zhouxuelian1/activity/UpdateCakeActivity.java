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

import com.bumptech.glide.Glide;
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
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeListResponse;
import cn.smxy.zhouxuelian1.entity.CakeType;
import cn.smxy.zhouxuelian1.entity.CakeTypeListResponse;
import cn.smxy.zhouxuelian1.entity.UpLoadImageResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class UpdateCakeActivity extends AppCompatActivity {

    private static final String TAG = "UpdateCakeActivity";
    String cakeId;
    private ImageView img;
    private ActivityResultLauncher<String> resultLauncher;
    private Button add;
    private EditText cakeName, introduce, price,num;
    private TextView caketypeId;

    private Toolbar back;
    private List<CakeType> caketypeList;
    private List<Cake> cakeListData;
    private String cakePicture;
    private CakeType caketype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cake);
        Intent intent = getIntent();
        cakeId = intent.getStringExtra("cakeId");
        getCcCakeTypeList();
        findone();
        initView();
        setOnListener();
    }

    private void initView() {
        img = findViewById(R.id.update_cake_image);
        add = findViewById(R.id.update_cake_button);
        cakeName = findViewById(R.id.update_cake_name);
        introduce = findViewById(R.id.update_cake_introduce);
        price = findViewById(R.id.update_cake_price);
        caketypeId = findViewById(R.id.update_cake_type);
        back = findViewById(R.id.back);
    }

    private void getCcCakeTypeList() {
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
                    Log.d(TAG, "onResponse: " + caketypeList);
                }
            }
        });
    }

    private void setOnListener() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Log.d(TAG, "Uri: " + result);
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
                chooseCakeType();
            }
        });

        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateCakeActivity.this, ManagementActivity.class);
                startActivity(intent);
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void chooseCakeType() {
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
        String url = UrlConstants.UPLOAD_IMAGE_URL;

        OkHttpUtils.post()
                .addFile("file", "image.png", toImageFile("cake.png"))
                .url(url).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "EEEEEEEEE" + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "response: " + response);
                        UpLoadImageResponse upLoadImageResponse = new Gson().fromJson(response, UpLoadImageResponse.class);
                        cakePicture = upLoadImageResponse.getDataobject();
                        updateCake();
                    }
                });
    }

    private File toImageFile(String fileName) {
        Drawable drawable = img.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        File file = new File(getCacheDir(), fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void updateCake() {
        String url = UrlConstants.CAKE_UPDATE_URL;

        Map<String, String> map = new HashMap<>();
        map.put("cakeId", cakeId);
        map.put("cakeName", cakeName.getText().toString());
        map.put("introduce", introduce.getText().toString());
        map.put("price", price.getText().toString());
        map.put("cakePicture", cakePicture);
        map.put("caketypeId", String.valueOf(caketype.getCaketypeId()));

        OkHttpUtils.post().url(url).params(map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因：" + e.getMessage());
                Toast.makeText(UpdateCakeActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "更新蛋糕信息成功，响应：" + response);
                Toast.makeText(UpdateCakeActivity.this, "更新产品信息成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateCakeActivity.this, ManagementActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void findone() {
        String url = UrlConstants.CAKE_FINDONE_URL + cakeId;
        Log.d(TAG, "2222222222222222" + url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因：" + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "findone成功：" + response);
                CakeListResponse cakeListResponse = new Gson().fromJson(response, CakeListResponse.class);
                if (cakeListResponse != null && cakeListResponse.getCode() == 2000) {
                    cakeListData = cakeListResponse.getDataobject();
                    Cake cake = cakeListData.get(0);
                    Glide.with(UpdateCakeActivity.this).load(cake.getCakePicture()).into(img);
                    cakeName.setText(cake.getCakeName());
                    introduce.setText(cake.getIntroduce());
                    price.setText(String.valueOf(cake.getPrice()));

                    String[] strings = new String[caketypeList.size()];
                    for (int i = 0; i < caketypeList.size(); i++) {
                        strings[i] = caketypeList.get(i).getCaketypeName();
                    }

                    if (cake.getCaketypeId() == 1) {
                        caketypeId.setText(strings[0]);
                        caketype = caketypeList.get(0);
                    } else if (cake.getCaketypeId() == 2) {
                        caketypeId.setText(strings[1]);
                        caketype = caketypeList.get(1);
                    }else {
                        caketypeId.setText(strings[2]);
                        caketype = caketypeList.get(2);
                    }
                }
            }
        });
    }
}