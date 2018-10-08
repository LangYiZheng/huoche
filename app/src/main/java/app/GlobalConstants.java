package app;

/**
 * Created by Administrator on 2018/5/6/006.
 */

public class GlobalConstants {
    public static final String SERVER_IP = "192.168.199.202";// 测试服务器主域名
//    public static final String SERVER_IP = "192.168.0.250";// 火车站服务器主域名
    public static final String UPDATE = "http://"+ SERVER_IP +"/Jiekou/returndata/To_Update";
    public static final String SERVER_URI = "http://"+SERVER_IP;
    public static final String UPDATE_APK= SERVER_URI+"/release/app-release.apk";

    //登录接口 192.168.199.122/jiekou/Returndata/login?number=编号&password=密码
    public static final String LOGIN_URL = SERVER_URI+"/jiekou/Returndata/login";

    //退出接口    192.168.199.122/jiekou/Returndata/outlogin?id=用户id&number=用户编号
    public static final String LOGOUT_URL = SERVER_URI+"/jiekou/Returndata/outlogin";

    //历史查询接口 192.168.199.122/jiekou/Returndata/history?point=1527033960&late=1527043920
    public static final String HISTORY_QUERY_URL = SERVER_URI+"/jiekou/Returndata/history";

    //站台值班员提交接口 192.168.199.122/jiekou/Returndata/train_pushu?id=车次id&status=发车状态
    public static final String DUTY_PUSH_URL = SERVER_URI+"/jiekou/Returndata";

    public static final String ADD_OPTION_URL = SERVER_URI+"/jiekou/Returndata";

    //提交作业人员编号
    public static final String PUSH = SERVER_URI+"/jiekou/returndata/andiord_init";
    //推送
    public static final String CPUSH = SERVER_URI+"/jiekou/returndata/andiord_send";
    //安全员放客 http://127.0.0.1/jiekou/returndata/passenger_init?id=1
    public static final String RELEASE = SERVER_URI+"/jiekou/returndata/passenger_init";
    //候车室放客http://127.0.0.1/jiekou/returndata/andiord_show?id=2
    public static final String RELEASE_WAITE = SERVER_URI+"/jiekou/returndata/andiord_show";
    //vip放客 http://127.0.0.1/jiekou/returndata/vip_init?id=1
    public static final String RELEASE_VIP = SERVER_URI+"/jiekou/returndata/vip_init";
    //茶座放客 http://127.0.0.1/jiekou/returndata/teahouse_init?id=1
    public static final String RELEASE_TEAHOUSE = SERVER_URI+"/jiekou/returndata/teahouse_init";
    //查询单列车信息
    public static final String SINGLE_TRAIN = SERVER_URI+"/jiekou/returndata/andiord_show";

}
