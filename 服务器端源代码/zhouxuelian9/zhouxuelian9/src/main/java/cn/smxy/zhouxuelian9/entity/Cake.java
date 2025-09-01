package cn.smxy.zhouxuelian9.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cake {
    private int cakeId;
    private String cakeName;
    private String introduce;
    private float price;
    private String cakePicture;
    private int caketypeId;  // 修正：与数据库字段名一致
    private int num;
}