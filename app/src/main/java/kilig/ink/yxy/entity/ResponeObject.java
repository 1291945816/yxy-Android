package kilig.ink.yxy.entity;

import java.io.Serializable;

/**
 * 响应对象
 * @param <T> T表示数据的返回类型 如token的时候，可能是String
 */
public class ResponeObject<T> implements Serializable {

    private static final long serialVersionUID = 1111013L;

    private String code ;//状态码
    private String message; //响应消息
    private T data; //响应的数据，视情况决定

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponeObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
