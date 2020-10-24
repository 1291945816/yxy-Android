package kilig.ink.yxy.utils;

public enum FragmentEnum {
    ABLUM("个人相册"),MY("我的"),PUBLISH("广场");
    private String name;
    FragmentEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

