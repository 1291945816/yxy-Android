package kilig.ink.yxy.entity;

import android.widget.ImageView;

import java.io.Serializable;

public class AblumItem implements Serializable {
    private String ablumName;
    private int ablumId;
    private int nums;
    private String ablumCreateTime;

    public String getAblumName() {
        return ablumName;
    }

    public void setAblumName(String ablumName) {
        this.ablumName = ablumName;
    }

    public int getAblumId() {
        return ablumId;
    }

    public void setAblumId(int ablumId) {
        this.ablumId = ablumId;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getAblumCreateTime() {
        return ablumCreateTime;
    }

    public void setAblumCreateTime(String ablumCreateTime) {
        this.ablumCreateTime = ablumCreateTime;
    }
}
