package kilig.ink.yxy.entity;


public class CommentItem {

    private String yxyUserAvatar;
    private String yxyNickName;
    private String comment;
    private String comment_time;
    public String getYxyUserAvatar() {
        return yxyUserAvatar;
    }

    public void setYxyUserAvatar(String yxyUserAvatar) {
        this.yxyUserAvatar = yxyUserAvatar;
    }

    public String getCommentTime() {
        return comment_time;
    }

    public void setCommentTime(String commentTime) {
        comment_time = commentTime;
    }


    public String getNickName() {
        return yxyNickName;
    }

    public void setNickName(String nickName) {
        yxyNickName = nickName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        comment = comment;
    }
}
