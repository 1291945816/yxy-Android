package kilig.ink.yxy.entity;

public class YxyUser {
    private String yxyUserName; //用户名
    private String yxyNickName; //昵称
    private String yxyPassword; //密码
    private String yxyUserAvatar;//头像路径
    private String yxyUserIntro;//用户介绍

    public String getYxyUserName() {
        return yxyUserName;
    }

    public void setYxyUserName(String yxyUserName) {
        this.yxyUserName = yxyUserName;
    }

    public String getYxyNickName() {
        return yxyNickName;
    }

    public void setYxyNickName(String yxyNickName) {
        this.yxyNickName = yxyNickName;
    }

    public String getYxyPassword() {
        return yxyPassword;
    }

    public void setYxyPassword(String yxyPassword) {
        this.yxyPassword = yxyPassword;
    }

    public String getYxyUserAvatar() {
        return yxyUserAvatar;
    }

    public void setYxyUserAvatar(String yxyUserAvatar) {
        this.yxyUserAvatar = yxyUserAvatar;
    }

    public String getYxyUserIntro() {
        return yxyUserIntro;
    }

    public void setYxyUserIntro(String yxyUserIntro) {
        this.yxyUserIntro = yxyUserIntro;
    }

    @Override
    public String toString() {
        return "YxyUser{" +
                "yxyUserName='" + yxyUserName + '\'' +
                ", yxyNickName='" + yxyNickName + '\'' +
                ", yxyPassword='" + yxyPassword + '\'' +
                ", yxyUserAvatar='" + yxyUserAvatar + '\'' +
                ", yxyUserIntro='" + yxyUserIntro + '\'' +
                '}';
    }
}
