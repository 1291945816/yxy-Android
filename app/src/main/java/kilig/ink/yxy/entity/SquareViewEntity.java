package kilig.ink.yxy.entity;

public class SquareViewEntity
{
    private String imgID;
    private String displayImgUrl;
    private String displayImgName;
    private int    starNum;
    private String authorName;
    private String authorProfileImgUrl;
    private boolean isStared;

    public SquareViewEntity() {}

    public SquareViewEntity(String imgID, String displayImgUrl, String displayImgName, int starNum, String authorName, String authorProfileImgUrl, boolean isStared)
    {
        this.imgID = imgID;
        this.displayImgUrl = displayImgUrl;
        this.displayImgName = displayImgName;
        this.starNum = starNum;
        this.authorName = authorName;
        this.authorProfileImgUrl = authorProfileImgUrl;
        this.isStared = isStared;
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
