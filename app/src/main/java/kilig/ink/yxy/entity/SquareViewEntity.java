package kilig.ink.yxy.entity;

public class SquareViewEntity
{
    private String imgID;
    private String displayImgUrl;
    private String displayImgName;
    private int    starNum;
    private String authorName;
    private String authorProfileImgUrl;

    public SquareViewEntity() {}

    public SquareViewEntity(String imgID, String imgUrl, String imgName, int starNum, String authorName, String authorImgUrl)
    {
        this.imgID = imgID;
        displayImgUrl = imgUrl;
        this.displayImgName = imgName;
        this.starNum = starNum;
        this.authorName = authorName;
        this.authorProfileImgUrl = authorImgUrl;
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
}
