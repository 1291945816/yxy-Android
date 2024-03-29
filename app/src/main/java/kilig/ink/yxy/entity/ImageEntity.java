package kilig.ink.yxy.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import kilig.ink.yxy.utils.OkhttpUtils;

public class ImageEntity implements Serializable
{
    private String imgID;
    private String authorID;
    private String displayImgUrl;
    private String displayImgName;
    private int    starNum;
    private int    downloadNum;
    private String authorName;
    @SerializedName("authorAvatar")
    private String authorProfileImgUrl;
    @SerializedName("stared")
    private boolean isStared;
    private String thumbnailUrl;

    public ImageEntity() {}

    public ImageEntity(String imgID, String authorID, String displayImgUrl,
                       String displayImgName, int starNum, int downloadNum,
                       String authorName, String authorProfileImgUrl, boolean isStared,
                       String thumbnailUrl)
    {
        this.imgID = imgID;
        this.authorID = authorID;
        this.displayImgUrl = displayImgUrl;
        this.displayImgName = displayImgName;
        this.starNum = starNum;
        this.downloadNum = downloadNum;
        this.authorName = authorName;
        this.authorProfileImgUrl = authorProfileImgUrl;
        this.isStared = isStared;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getThumbnailUrl()
    {
        return thumbnailUrl;
    }

    public void setDownloadNum(int downloadNum)
    {
        this.downloadNum = downloadNum;
    }

    public String getAuthorID()
    {
        return authorID;
    }

    public int getDownloadNum()
    {
        return downloadNum;
    }

    public String getImgID()
    {
        return imgID;
    }

    public String getDisplayImgUrl()
    {
        return displayImgUrl;
    }

    public String getDisplayImgName()
    {
        if (displayImgName.length() > 9)
            return displayImgName.substring(0, 9) + "···";
        return displayImgName;
    }

    public int getStarNum()
    {
        return starNum;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public String getAuthorProfileImgUrl()
    {
        return authorProfileImgUrl;
    }

    public boolean isStared()
    {
        return isStared;
    }

    public void setStared(boolean stared)
    {
        isStared = stared;
    }

    public void setStarNum(int starNum)
    {
        this.starNum = starNum;
    }
}
