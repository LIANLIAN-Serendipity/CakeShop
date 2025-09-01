package cn.smxy.zhouxuelian1.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.CakeListAdapter;
import cn.smxy.zhouxuelian1.adapter.UserOrderListAdapter;
import cn.smxy.zhouxuelian1.entity.Cake;
import cn.smxy.zhouxuelian1.entity.CakeOrderInfo;
import cn.smxy.zhouxuelian1.entity.CakeOrderListResponse;
import cn.smxy.zhouxuelian1.utils.SPUtil;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;


public class CakeUserOrderFragment extends Fragment {
    private  View rootView;
    private RecyclerView orderRecyclerView;
    private static final String TAG="UserOrderFragment";
    private List<Cake> cakeListData;
    private CakeListAdapter adapter;

    public CakeUserOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_user_order, container, false);
        initView();
        getDataFromNet();
        return rootView;
    }

    private void getDataFromNet() {
        String url = UrlConstants.USER_ORDER_LIST_URL;
        Map<String,String> headersMap = new HashMap<>();
        String token = SPUtil.getString(getActivity(),"token");
        headersMap.put("token",token);
        String cakeuserId = SPUtil.getString(getActivity(),"cakeuserId");
        OkHttpUtils.get().url(url).headers(headersMap).addParams("cakeuserId",cakeuserId).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d(TAG,"网络请求失败，失败原因: "+e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d(TAG,"网络请求成功，成功原因: "+ response);
                CakeOrderListResponse orderListResponse = new Gson().fromJson(response,CakeOrderListResponse.class);
                if(orderListResponse != null && orderListResponse.getCode() == 2000){
                    List<CakeOrderInfo> orderListData = orderListResponse.getDataobject();
                    //adapter适应期，getContext()获得上下文
                    UserOrderListAdapter adapter = new UserOrderListAdapter(getContext(),orderListData);
                    orderRecyclerView.setAdapter(adapter);
                }
            }
        });
    }

    private void initView() {
        orderRecyclerView = rootView.findViewById(R.id.user_order_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        orderRecyclerView.setLayoutManager(linearLayoutManager);
    }
}