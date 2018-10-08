package bean;

/**
 * 项目名称：trunk
 * 类描述：
 * 创建人：dell003
 * 创建时间：2018/7/9 19:44.
 * 修改人：dell003
 * 修改时间：2018/7/9 19:44
 * 修改备注：
 */

public class ReleaseBean {

    /**
     * code : 200
     * message : 请求成功
     * data : null
     */

    private int code;
    private String message;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
