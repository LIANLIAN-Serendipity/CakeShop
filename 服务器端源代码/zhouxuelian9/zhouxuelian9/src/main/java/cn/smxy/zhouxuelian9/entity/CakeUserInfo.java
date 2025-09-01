package cn.smxy.zhouxuelian9.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CakeUserInfo {
    private Integer cakeuserId;  // 保持为null，由数据库自动生成
    private String cakeusername;
    private String password;
    private String newpassword;
    private String sex;
    private String address;
    private String phone;
    private String cakeuserImage;
    private String role;
}