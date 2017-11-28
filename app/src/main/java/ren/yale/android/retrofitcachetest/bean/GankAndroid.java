package ren.yale.android.retrofitcachetest.bean;

import java.util.List;

/**
 * Created by Yale on 2017/7/5.
 */

public class GankAndroid {

    /**
     * error : false
     * results : [{"_id":"595ad074421aa90ca3bb6a90","createdAt":"2017-07-04T07:17:08.609Z","desc":"Android 有两套相机 Api，使用起来很麻烦，好在 Foto 开源了他们在 Android 上的 Camera 封装 Api，力荐！","images":["http://img.gank.io/0a15bae7-c513-4feb-bbe2-1273b8b809ce"],"publishedAt":"2017-07-04T11:50:36.484Z","source":"chrome","type":"Android","url":"https://github.com/Fotoapparat/Fotoapparat","used":true,"who":"代码家"},{"_id":"595ad096421aa90cb4724b5b","createdAt":"2017-07-04T07:17:42.635Z","desc":"MD 风格的日历组件，很精致哦。","images":["http://img.gank.io/75a6251f-ffaf-41dc-8dbc-fa58802b0d8e"],"publishedAt":"2017-07-04T11:50:36.484Z","source":"chrome","type":"Android","url":"https://github.com/Applandeo/Material-Calendar-View","used":true,"who":"代码家"},{"_id":"595ad0d4421aa90cb4724b5c","createdAt":"2017-07-04T07:18:44.154Z","desc":"非常 Fancy 的选项过滤器。","images":["http://img.gank.io/f9e1e0ef-88fc-4e02-8620-2cf1700966c5"],"publishedAt":"2017-07-04T11:50:36.484Z","source":"chrome","type":"Android","url":"https://github.com/Krupen/FabulousFilter","used":true,"who":"代码家"},{"_id":"595aec75421aa90c9203d31c","createdAt":"2017-07-04T09:16:37.902Z","desc":"Android单元测试框架Robolectric3.0(二)：数据篇","publishedAt":"2017-07-04T11:50:36.484Z","source":"web","type":"Android","url":"http://url.cn/4BHx7ZG","used":true,"who":"陈宇明"},{"_id":"595b0bed421aa90ca3bb6a98","createdAt":"2017-07-04T11:30:53.793Z","desc":"前端每周清单第 20 期：React 组件解耦之道；基于Headless Chrome的自动化测试；Angular 2/4是否为时已晚？","publishedAt":"2017-07-04T11:50:36.484Z","source":"chrome","type":"Android","url":"https://zhuanlan.zhihu.com/p/27684971","used":true,"who":"王下邀月熊"},{"_id":"595b0f5a421aa90cb4724b60","createdAt":"2017-07-04T11:45:30.184Z","desc":"Android App Performance Optimization","publishedAt":"2017-07-04T11:50:36.484Z","source":"web","type":"Android","url":"https://blog.mindorks.com/android-app-performance-optimization-cdccb422e38e","used":true,"who":"AMIT SHEKHAR"},{"_id":"593f2091421aa92c769a8c6a","createdAt":"2017-06-13T07:15:29.423Z","desc":"Android之自定义View：侧滑删除","publishedAt":"2017-06-15T13:55:57.947Z","source":"web","type":"Android","url":"https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&mid=2247484934&idx=1&sn=f2a40261efe8ebee45804e9df93c1cce&chksm=96cda74ba1ba2e5dbbac15a9e57b5329176d1fe43478e5c63f7bc502a6ca50e4dfa6c0a9041e#rd","used":true,"who":"陈宇明"},{"_id":"594109e5421aa92c769a8c84","createdAt":"2017-06-14T18:03:17.393Z","desc":"RecyclerView：利用打造悬浮效果","images":["http://img.gank.io/775b8ae5-4c21-4553-a77e-a0842248e1af"],"publishedAt":"2017-06-15T13:55:57.947Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/b335b620af39","used":true,"who":null},{"_id":"5941e2ac421aa92c7be61c14","createdAt":"2017-06-15T09:28:12.702Z","desc":"《From Java To Kotlin》从Java到Kotlin·译 （双语对比）","publishedAt":"2017-06-15T13:55:57.947Z","source":"web","type":"Android","url":"http://url.cn/4AS5wCG","used":true,"who":"陈宇明"},{"_id":"5941f5f3421aa92c7be61c16","createdAt":"2017-06-15T10:50:27.317Z","desc":"仿Nice首页图片列表9图样式，并实现拖拽效果","images":["http://img.gank.io/4f54c011-e293-436a-ada1-dc03669ffb10"],"publishedAt":"2017-06-15T13:55:57.947Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/0ea96b952170","used":true,"who":"兔子吃过窝边草"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 595ad074421aa90ca3bb6a90
         * createdAt : 2017-07-04T07:17:08.609Z
         * desc : Android 有两套相机 Api，使用起来很麻烦，好在 Foto 开源了他们在 Android 上的 Camera 封装 Api，力荐！
         * images : ["http://img.gank.io/0a15bae7-c513-4feb-bbe2-1273b8b809ce"]
         * publishedAt : 2017-07-04T11:50:36.484Z
         * source : chrome
         * type : Android
         * url : https://github.com/Fotoapparat/Fotoapparat
         * used : true
         * who : 代码家
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private List<String> images;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}
