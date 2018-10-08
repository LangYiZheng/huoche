package bean;

/**
 * Created by dell003 on 2018/5/9.
 */

public class UserBean{
    /**
     * code : 200
     * message : 请求成功
     * data : {"id":"3","password":"202cb962ac59075b964b07152d234b70","true_name":"sdaf","type":"2","number":"V123","status":"1","work":"0"}
     */

    private int code;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * password : 202cb962ac59075b964b07152d234b70
         * true_name : sdaf
         * type : 2
         * number : V123
         * status : 1
         * work : 0
         */

        private String id;
        private String password;
        private String true_name;
        private String type;
        private String number;
        private String status;
        private String work;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTrue_name() {
            return true_name;
        }

        public void setTrue_name(String true_name) {
            this.true_name = true_name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getWork() {
            return work;
        }

        public void setWork(String work) {
            this.work = work;
        }

        //3：上水，2：贵宾，1：茶座，6：行包值班员（操作行包）7：班长，5：客运值班员
        public String getPost(String number){
            char header ='\u0000';
            String position="";
            if(null != number && !"".equals(number)){
                header= number.charAt(0);
            }
            switch (header){
                case 'A':
                    position = "站台安全员";
                    break ;
                case 'C':
                    position = "茶座";
                    break ;
                case 'D':
                    position = "大班长";
                    break ;
                case 'G':
                    position = "贵宾";
                    break ;
                case 'H':
                    position = "候车室";
                    break ;
                case 'J':
                    position = "计划室";
                    break ;
                case 'P':
                    position = "票房";
                    break ;
                case 'S':
                    position = "上水组";
                    break ;
                case 'X':
                    position = "行包房";
                    break ;
                case 'Z':
                    position = "值班干部";
                    break ;
            }
            return position;
        }
    }
}
