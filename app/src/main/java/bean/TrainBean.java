package bean;

/**
 * 项目名称：trunk
 * 类描述：
 * 创建人：dell003
 * 创建时间：2018/7/10 11:24.
 * 修改人：dell003
 * 修改时间：2018/7/10 11:24
 * 修改备注：
 */

public class TrainBean {

    /**
     * code : 200
     * message : 请求成功
     * data : {"id":"2","number":"K337","point":"1530153960","late":"1530154320","status":"0","departure":"郑州","terminus":"昆明","arrive":"1","market_id":"1","ticket":null,"car_id":"16","passenger_time":null,"passenger_status":"2","region":"3候3区","train_status":"1","passenger_number":"","vip_status":"2","vip_number":null,"vip_time":null,"bag_status":"2","bag_number":null,"bag_time":null,"water_status":"2","water_number":null,"water_time":null,"teahouse_status":"2","teahouse_number":null,"teahouse_time":null,"car_status":"0","car_time":null,"car_number":"H3","market_name":"1","leader_number":null,"car_name":"3候","water_type":"0","teahouse_type":"0","vip_type":"0","bag_type":"0"}
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
         * id : 2
         * number : K337
         * point : 1530153960
         * late : 1530154320
         * status : 0
         * departure : 郑州
         * terminus : 昆明
         * arrive : 1
         * market_id : 1
         * ticket : null
         * car_id : 16
         * passenger_time : null
         * passenger_status : 2
         * region : 3候3区
         * train_status : 1
         * passenger_number :
         * vip_status : 2
         * vip_number : null
         * vip_time : null
         * bag_status : 2
         * bag_number : null
         * bag_time : null
         * water_status : 2
         * water_number : null
         * water_time : null
         * teahouse_status : 2
         * teahouse_number : null
         * teahouse_time : null
         * car_status : 0
         * car_time : null
         * car_number : H3
         * market_name : 1
         * leader_number : null
         * car_name : 3候
         * water_type : 0
         * teahouse_type : 0
         * vip_type : 0
         * bag_type : 0
         */

        private String id;
        private String number;
        private String point;
        private String late;
        private String status;
        private String departure;
        private String terminus;
        private String arrive;
        private String market_id;
        private String ticket;
        private String car_id;
        private String passenger_time;
        private String passenger_status;
        private String region;
        private String train_status;
        private String passenger_number;
        private String vip_status;
        private String vip_number;
        private String vip_time;
        private String bag_status;
        private String bag_number;
        private String bag_time;
        private String water_status;
        private String water_number;
        private String water_time;
        private String teahouse_status;
        private String teahouse_number;
        private String teahouse_time;
        private String car_status;
        private String car_time;
        private String car_number;
        private String market_name;
        private String leader_number;
        private String car_name;
        private String water_type;
        private String teahouse_type;
        private String vip_type;
        private String bag_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getLate() {
            return late;
        }

        public void setLate(String late) {
            this.late = late;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getTerminus() {
            return terminus;
        }

        public void setTerminus(String terminus) {
            this.terminus = terminus;
        }

        public String getArrive() {
            return arrive;
        }

        public void setArrive(String arrive) {
            this.arrive = arrive;
        }

        public String getMarket_id() {
            return market_id;
        }

        public void setMarket_id(String market_id) {
            this.market_id = market_id;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public String getCar_id() {
            return car_id;
        }

        public void setCar_id(String car_id) {
            this.car_id = car_id;
        }

        public String getPassenger_time() {
            return passenger_time;
        }

        public void setPassenger_time(String passenger_time) {
            this.passenger_time = passenger_time;
        }

        public String getPassenger_status() {
            return passenger_status;
        }

        public void setPassenger_status(String passenger_status) {
            this.passenger_status = passenger_status;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getTrain_status() {
            return train_status;
        }

        public void setTrain_status(String train_status) {
            this.train_status = train_status;
        }

        public String getPassenger_number() {
            return passenger_number;
        }

        public void setPassenger_number(String passenger_number) {
            this.passenger_number = passenger_number;
        }

        public String getVip_status() {
            return vip_status;
        }

        public void setVip_status(String vip_status) {
            this.vip_status = vip_status;
        }

        public String getVip_number() {
            return vip_number;
        }

        public void setVip_number(String vip_number) {
            this.vip_number = vip_number;
        }

        public String getVip_time() {
            return vip_time;
        }

        public void setVip_time(String vip_time) {
            this.vip_time = vip_time;
        }

        public String getBag_status() {
            return bag_status;
        }

        public void setBag_status(String bag_status) {
            this.bag_status = bag_status;
        }

        public String getBag_number() {
            return bag_number;
        }

        public void setBag_number(String bag_number) {
            this.bag_number = bag_number;
        }

        public String getBag_time() {
            return bag_time;
        }

        public void setBag_time(String bag_time) {
            this.bag_time = bag_time;
        }

        public String getWater_status() {
            return water_status;
        }

        public void setWater_status(String water_status) {
            this.water_status = water_status;
        }

        public String getWater_number() {
            return water_number;
        }

        public void setWater_number(String water_number) {
            this.water_number = water_number;
        }

        public String getWater_time() {
            return water_time;
        }

        public void setWater_time(String water_time) {
            this.water_time = water_time;
        }

        public String getTeahouse_status() {
            return teahouse_status;
        }

        public void setTeahouse_status(String teahouse_status) {
            this.teahouse_status = teahouse_status;
        }

        public String getTeahouse_number() {
            return teahouse_number;
        }

        public void setTeahouse_number(String teahouse_number) {
            this.teahouse_number = teahouse_number;
        }

        public String getTeahouse_time() {
            return teahouse_time;
        }

        public void setTeahouse_time(String teahouse_time) {
            this.teahouse_time = teahouse_time;
        }

        public String getCar_status() {
            return car_status;
        }

        public void setCar_status(String car_status) {
            this.car_status = car_status;
        }

        public String getCar_time() {
            return car_time;
        }

        public void setCar_time(String car_time) {
            this.car_time = car_time;
        }

        public String getCar_number() {
            return car_number;
        }

        public void setCar_number(String car_number) {
            this.car_number = car_number;
        }

        public String getMarket_name() {
            return market_name;
        }

        public void setMarket_name(String market_name) {
            this.market_name = market_name;
        }

        public String getLeader_number() {
            return leader_number;
        }

        public void setLeader_number(String leader_number) {
            this.leader_number = leader_number;
        }

        public String getCar_name() {
            return car_name;
        }

        public void setCar_name(String car_name) {
            this.car_name = car_name;
        }

        public String getWater_type() {
            return water_type;
        }

        public void setWater_type(String water_type) {
            this.water_type = water_type;
        }

        public String getTeahouse_type() {
            return teahouse_type;
        }

        public void setTeahouse_type(String teahouse_type) {
            this.teahouse_type = teahouse_type;
        }

        public String getVip_type() {
            return vip_type;
        }

        public void setVip_type(String vip_type) {
            this.vip_type = vip_type;
        }

        public String getBag_type() {
            return bag_type;
        }

        public void setBag_type(String bag_type) {
            this.bag_type = bag_type;
        }
    }
}
