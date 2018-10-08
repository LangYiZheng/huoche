package bean;

import java.util.List;

/**
 * Created by dell004 on 2018/5/29.
 */

public class UpdateVersion {
    /**
     * code : 200
     * message : 请求成功
     * data : [{"outputType":{"type":"APK"},"apkInfo":{"type":"MAIN","splits":[],"versionCode":2},"path":"app-release.apk","properties":{"packageId":"com.example.dell004.myapplication","split":"","minSdkVersion":"17"}}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * outputType : {"type":"APK"}
         * apkInfo : {"type":"MAIN","splits":[],"versionCode":2}
         * path : app-release.apk
         * properties : {"packageId":"com.example.dell004.myapplication","split":"","minSdkVersion":"17"}
         */

        private OutputTypeBean outputType;
        private ApkInfoBean apkInfo;
        private String path;
        private PropertiesBean properties;

        public OutputTypeBean getOutputType() {
            return outputType;
        }

        public void setOutputType(OutputTypeBean outputType) {
            this.outputType = outputType;
        }

        public ApkInfoBean getApkInfo() {
            return apkInfo;
        }

        public void setApkInfo(ApkInfoBean apkInfo) {
            this.apkInfo = apkInfo;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public PropertiesBean getProperties() {
            return properties;
        }

        public void setProperties(PropertiesBean properties) {
            this.properties = properties;
        }

        public static class OutputTypeBean {
            /**
             * type : APK
             */

            private String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class ApkInfoBean {
            /**
             * type : MAIN
             * splits : []
             * versionCode : 2
             */

            private String type;
            private int versionCode;
            private List<?> splits;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public int getVersionCode() {
                return versionCode;
            }

            public void setVersionCode(int versionCode) {
                this.versionCode = versionCode;
            }

            public List<?> getSplits() {
                return splits;
            }

            public void setSplits(List<?> splits) {
                this.splits = splits;
            }
        }

        public static class PropertiesBean {
            /**
             * packageId : com.example.dell004.myapplication
             * split :
             * minSdkVersion : 17
             */

            private String packageId;
            private String split;
            private String minSdkVersion;

            public String getPackageId() {
                return packageId;
            }

            public void setPackageId(String packageId) {
                this.packageId = packageId;
            }

            public String getSplit() {
                return split;
            }

            public void setSplit(String split) {
                this.split = split;
            }

            public String getMinSdkVersion() {
                return minSdkVersion;
            }

            public void setMinSdkVersion(String minSdkVersion) {
                this.minSdkVersion = minSdkVersion;
            }
        }
    }
}
