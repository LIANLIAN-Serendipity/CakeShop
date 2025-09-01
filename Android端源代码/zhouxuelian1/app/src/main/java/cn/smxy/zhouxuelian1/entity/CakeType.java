package cn.smxy.zhouxuelian1.entity;

public class CakeType {
    private int caketypeId;
    private String caketypeName;

    public CakeType() {
    }

    public CakeType(int caketypeId, String caketypeName) {
        this.caketypeId = caketypeId;
        this.caketypeName = caketypeName;
    }

    public int getCaketypeId() {
        return caketypeId;
    }

    public void setCaketypeId(int caketypeId) {
        this.caketypeId = caketypeId;
    }

    public String getCaketypeName() {
        return caketypeName;
    }

    public void setCaketypeName(String caketypeName) {
        this.caketypeName = caketypeName;
    }

    @Override
    public String toString() {
        return "CakeType{" +
                "caketypeId=" + caketypeId +
                ", caketypeName='" + caketypeName + '\'' +
                '}';
    }
}
