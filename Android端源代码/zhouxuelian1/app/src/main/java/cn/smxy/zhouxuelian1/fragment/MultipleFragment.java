package cn.smxy.zhouxuelian1.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.hhmedic.android.sdk.okhttputils.okhttp.OkHttpUtils;
import com.hhmedic.android.sdk.okhttputils.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.adapter.ViewPagerAdapter;
import cn.smxy.zhouxuelian1.entity.CakeType;
import cn.smxy.zhouxuelian1.entity.CakeTypeListResponse;
import cn.smxy.zhouxuelian1.utils.UrlConstants;
import okhttp3.Call;



public class MultipleFragment extends Fragment {
    private static final String TAG = "MultipleFragment";
    private View rootView;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private List<Fragment> fragmentList;


    public MultipleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_multiple, container, false);
        initView();
        getCakeTypeList();
        return rootView;
    }

    private void initView() {
        tabLayout = rootView.findViewById(R.id.tablayout);
        viewPager = rootView.findViewById(R.id.viewpager2);
    }

    private void getCakeTypeList() {
        String url = UrlConstants.CAKE_TYPE_LIST_URL;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "网络请求失败：" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "网络请求成功，相应结果：" + response);
                        CakeTypeListResponse cakeTypeListResponse = new Gson().fromJson(response, CakeTypeListResponse.class);
                        if (cakeTypeListResponse != null && cakeTypeListResponse.getCode() ==2000) {
                            List<CakeType> caketypeList = cakeTypeListResponse.getDataobject();

                            fragmentList = new ArrayList<>();
                            for (int i = 0; i < caketypeList.size(); i++) {
                                CakeListByTypeFragment cakeListByTypeFragment = new CakeListByTypeFragment();
                                cakeListByTypeFragment.setTypeId(caketypeList.get(i).getCaketypeId());
                                fragmentList.add(cakeListByTypeFragment);
                            }
                           ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(),fragmentList);
                            viewPager.setAdapter(adapter);

                            new TabLayoutMediator (tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
                                @Override
                                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                                    tab.setText(caketypeList.get(position).getCaketypeName());
                                }
                            }).attach();
                        }
                    }
        });
    }
}