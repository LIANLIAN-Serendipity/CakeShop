package cn.smxy.zhouxuelian1.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.CakeListAdapter;
import cn.smxy.zhouxuelian1.dao.CakeCartDao;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeListResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;

public class CakeListByTypeFragment extends Fragment {

    private static final String TAG = "ProductListByTypeFragment";
    private View rootView;
    private int typeId;
    private ListView listView;
    private CakeListAdapter cakeListAdapter;
    private CakeCartDao cakeCartDao;
    private List<Cake> cakeListData;

    public CakeListByTypeFragment() {
        // Required empty public constructor
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cake_list_by_type, container, false);
        cakeCartDao = new CakeCartDao(getContext()); // 初始化 CakeCartDao
        initView();
        getCakeListByType();
        return rootView;
    }

    private void initView() {
        listView = rootView.findViewById(R.id.listview);
        cakeListAdapter = new CakeListAdapter(getContext(), null);
        listView.setAdapter(cakeListAdapter);
    }

    private void getCakeListByType() {
        String url = UrlConstants.CAKE_LIST_BY_TYPE_URL;
        OkHttpUtils.post().url(url).addParams("caketypeId", String.valueOf(typeId)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG, "网络请求失败，失败原因: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG, "网络请求成功,相应数据为: " + response);
                CakeListResponse cakeListResponse = new Gson().fromJson(response, CakeListResponse.class);
                if (cakeListResponse != null && cakeListResponse.getCode() == 2000) {
                    cakeListData = cakeListResponse.getDataobject();
                    cakeListAdapter = new CakeListAdapter(getContext(), cakeListData);
                    listView.setAdapter(cakeListAdapter);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getCakeListByType(); // 重新加载数据
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cakeCartDao != null) {
            cakeCartDao.close();
        }
    }
}