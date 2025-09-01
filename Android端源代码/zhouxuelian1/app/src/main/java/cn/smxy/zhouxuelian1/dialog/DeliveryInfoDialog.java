package cn.smxy.zhouxuelian1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import cn.smxy.zhouxuelian1.R;
import cn.smxy.zhouxuelian1.dao.CakeCartDao;
import cn.smxy.zhouxuelian1.fragment.CartFragment;

public class DeliveryInfoDialog extends DialogFragment {
    private EditText etName, etPhone, etAddress, etRemark;
    private CartFragment cartFragment;
    private CakeCartDao cakeCartDao;

    public void setCartFragment(CartFragment cartFragment) {
        this.cartFragment = cartFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_delivery_info, null);
        etName = view.findViewById(R.id.et_delivery_name);
        etPhone = view.findViewById(R.id.et_delivery_phone);
        etAddress = view.findViewById(R.id.et_delivery_address);
        etRemark = view.findViewById(R.id.et_delivery_remark);

        // 初始化 CakeCartDao
        cakeCartDao = new CakeCartDao(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("填写收货信息")
                .setView(view)
                .setPositiveButton("确认付款", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = etName.getText().toString().trim();
                        String phone = etPhone.getText().toString().trim();
                        String address = etAddress.getText().toString().trim();
                        String remark = etRemark.getText().toString().trim();

                        if (cartFragment != null) {
                            cartFragment.onDeliveryInfoConfirmed(name, phone, address, remark);
                        }

                        // 清空购物车
                        if (cakeCartDao != null) {
                            cakeCartDao.clearCart();
                            cakeCartDao.close();
                        }

                        // 刷新购物车页面
                        if (cartFragment != null) {
                            cartFragment.refreshCart();
                        }
                    }
                })
                .setNegativeButton("取消", null);

        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cakeCartDao != null) {
            cakeCartDao.close();
        }
    }
}