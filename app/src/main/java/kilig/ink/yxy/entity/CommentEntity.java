package kilig.ink.yxy.entity;

import com.google.gson.annotations.SerializedName;

public class CommentEntity
{
    @SerializedName("yxyUserName")
    private String userID;
    @SerializedName("yxyNickName")
    private String userName;
    @SerializedName("comment")
    private String content;
    @SerializedName("comment_time")
    private String createTime;
    @SerializedName("yxyUserAvatar")
    private String profile;

    public CommentEntity(){}

    public CommentEntity(String userID, String userName, String content, String createTime, String profile)
    {
        this.userID = userID;
        this.userName = userName;
        this.content = content;
        this.createTime = createTime;
        this.profile = profile;
    }

    public String getUserID()
    {
        return userID;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getContent()
    {
        return content;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public String getProfile()
    {
        return profile;
    }
}
