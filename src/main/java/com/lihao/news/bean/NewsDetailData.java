package com.lihao.news.bean;

import java.util.List;

/**
 * Created by hbm on 2017/4/18.
 */

public class NewsDetailData {

    /**
     * data : {"countcommenturl":"/client/content/countComment/","more":"/static/api/news/10007/list_2.json","news":[],"title":"北京","topic":[],"topnews":[{"comment":true,"commentlist":"/static/api/news/10007/53/147253/comment_1.json","commenturl":"/client/user/newComment/147253","id":147253,"pubdate":"2015-10-19 07:18","title":"市教委：中高考英语试卷结构不变","topimage":"/static/images/2015/10/19/36/1053274969EORV.jpg","type":"news","url":"/static/html/2015/10/19/714C6E504A6F1B7869277C42.html"},{},{},{"id":147274,"title":"年度最佳旅行地 让你砰然心动","topimage":"/static/images/2015/10/19/70/467283565C7N.jpg","url":"/static/html/2015/10/19/704D6C524D681D7E6D217D44.html","pubdate":"2015-10-19 11:42","comment":true,"commenturl":"/client/user/newComment/147274","type":"news","commentlist":"/static/api/news/10012/74/147274/comment_1.json"}]}
     * retcode : 200
     */

    private DataBean data;
    private int retcode;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public static class DataBean {
        /**
         * countcommenturl : /client/content/countComment/
         * more : /static/api/news/10007/list_2.json
         * news : []
         * title : 北京
         * topic : []
         * topnews : [{"comment":true,"commentlist":"/static/api/news/10007/53/147253/comment_1.json","commenturl":"/client/user/newComment/147253","id":147253,"pubdate":"2015-10-19 07:18","title":"市教委：中高考英语试卷结构不变","topimage":"/static/images/2015/10/19/36/1053274969EORV.jpg","type":"news","url":"/static/html/2015/10/19/714C6E504A6F1B7869277C42.html"},{},{},{"id":147274,"title":"年度最佳旅行地 让你砰然心动","topimage":"/static/images/2015/10/19/70/467283565C7N.jpg","url":"/static/html/2015/10/19/704D6C524D681D7E6D217D44.html","pubdate":"2015-10-19 11:42","comment":true,"commenturl":"/client/user/newComment/147274","type":"news","commentlist":"/static/api/news/10012/74/147274/comment_1.json"}]
         */

        private String countcommenturl;
        private String more;
        private String title;
        private List<NewsBean> news;
        private List<?> topic;
        private List<TopnewsBean> topnews;

        public String getCountcommenturl() {
            return countcommenturl;
        }

        public void setCountcommenturl(String countcommenturl) {
            this.countcommenturl = countcommenturl;
        }

        public String getMore() {
            return more;
        }

        public void setMore(String more) {
            this.more = more;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<NewsBean> getNews() {
            return news;
        }

        public void setNews(List<NewsBean> news) {
            this.news = news;
        }

        public List<?> getTopic() {
            return topic;
        }

        public void setTopic(List<?> topic) {
            this.topic = topic;
        }

        public List<TopnewsBean> getTopnews() {
            return topnews;
        }

        public void setTopnews(List<TopnewsBean> topnews) {
            this.topnews = topnews;
        }

        public static class TopnewsBean {
            /**
             * comment : true
             * commentlist : /static/api/news/10007/53/147253/comment_1.json
             * commenturl : /client/user/newComment/147253
             * id : 147253
             * pubdate : 2015-10-19 07:18
             * title : 市教委：中高考英语试卷结构不变
             * topimage : /static/images/2015/10/19/36/1053274969EORV.jpg
             * type : news
             * url : /static/html/2015/10/19/714C6E504A6F1B7869277C42.html
             */

            private boolean comment;
            private String commentlist;
            private String commenturl;
            private int id;
            private String pubdate;
            private String title;
            private String topimage;
            private String type;
            private String url;

            public boolean isComment() {
                return comment;
            }

            public void setComment(boolean comment) {
                this.comment = comment;
            }

            public String getCommentlist() {
                return commentlist;
            }

            public void setCommentlist(String commentlist) {
                this.commentlist = commentlist;
            }

            public String getCommenturl() {
                return commenturl;
            }

            public void setCommenturl(String commenturl) {
                this.commenturl = commenturl;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getPubdate() {
                return pubdate;
            }

            public void setPubdate(String pubdate) {
                this.pubdate = pubdate;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getTopimage() {
                return topimage;
            }

            public void setTopimage(String topimage) {
                this.topimage = topimage;
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
        }
        public static class NewsBean{
            @Override
            public boolean equals(Object obj) {
                NewsBean b = (NewsBean) obj;
                return this.getId()==b.getId();
            }

            /**
             * id : 147314
             * title : 徒步登高欣赏京城第一片红叶
             * url : /static/html/2015/10/20/714C6E50486D197B6F257C45.html
             * listimage : /static/images/2015/10/20/3/1420927604KB07.jpg
             * pubdate : 2015-10-20 09:37
             * comment : true
             * commenturl : /client/user/newComment/147314
             * type : news
             * commentlist : /static/api/news/10012/14/147314/comment_1.json
             */

            private int id;
            private String title;
            private String url;
            private String listimage;
            private String pubdate;
            private boolean comment;
            private String commenturl;
            private String type;
            private String commentlist;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getListimage() {
                return listimage;
            }

            public void setListimage(String listimage) {
                this.listimage = listimage;
            }

            public String getPubdate() {
                return pubdate;
            }

            public void setPubdate(String pubdate) {
                this.pubdate = pubdate;
            }

            public boolean isComment() {
                return comment;
            }

            public void setComment(boolean comment) {
                this.comment = comment;
            }

            public String getCommenturl() {
                return commenturl;
            }

            public void setCommenturl(String commenturl) {
                this.commenturl = commenturl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getCommentlist() {
                return commentlist;
            }

            public void setCommentlist(String commentlist) {
                this.commentlist = commentlist;
            }
        }
    }
}
