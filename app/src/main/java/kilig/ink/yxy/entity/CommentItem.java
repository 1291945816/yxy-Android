package kilig.ink.yxy.entity;


public class CommentItem {
    private String photoImageUrl;
    private String yxyNickName;
    private String comment;
    private String comment_time;

    public String getCommentTime() {
        return comment_time;
    }

    public void setCommentTime(String commentTime) {
        comment_time = commentTime;
    }

    public String getPhotoImageUrl() {
        return photoImageUrl;
    }

    public void setPhotoImageUrl(String photoImageUrl) {
        this.photoImageUrl = photoImageUrl;
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
