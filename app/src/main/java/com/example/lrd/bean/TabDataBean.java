package com.example.lrd.bean;

import java.util.List;

/**
 * Created By LRD
 * on 2018/7/2
 */
public class TabDataBean {

    private String Message;
    private String AppName;
    private String BaseUrl;
    private String SplashImgUrl;
    private String LoginBgImg;
    private List<TabsBean> Tabs;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String AppName) {
        this.AppName = AppName;
    }

    public String getBaseUrl() {
        return BaseUrl;
    }

    public void setBaseUrl(String BaseUrl) {
        this.BaseUrl = BaseUrl;
    }

    public String getSplashImgUrl() {
        return SplashImgUrl;
    }

    public void setSplashImgUrl(String SplashImgUrl) {
        this.SplashImgUrl = SplashImgUrl;
    }

    public String getLoginBgImg() {
        return LoginBgImg;
    }

    public void setLoginBgImg(String LoginBgImg) {
        this.LoginBgImg = LoginBgImg;
    }

    public List<TabsBean> getTabs() {
        return Tabs;
    }

    public void setTabs(List<TabsBean> Tabs) {
        this.Tabs = Tabs;
    }

    public static class TabsBean {
        /**
         * Id : 首页
         * Title : 首页
         * LayoutId : TrcHome
         * ImageUrl : /clients/useruploads/iap/nav/Home.png
         * FeedUrl : /clients/layouts/TrcHome.json?wsid=
         * LayoutTitle : /clients/useruploads/iap/home/logo.png
         */

        private String Id;
        private String Title;
        private String LayoutId;
        private String ImageUrl;
        private String FeedUrl;
        private String LayoutTitle;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getLayoutId() {
            return LayoutId;
        }

        public void setLayoutId(String LayoutId) {
            this.LayoutId = LayoutId;
        }

        public String getImageUrl() {
            return ImageUrl;
        }

        public void setImageUrl(String ImageUrl) {
            this.ImageUrl = ImageUrl;
        }

        public String getFeedUrl() {
            return FeedUrl;
        }

        public void setFeedUrl(String FeedUrl) {
            this.FeedUrl = FeedUrl;
        }

        public String getLayoutTitle() {
            return LayoutTitle;
        }

        public void setLayoutTitle(String LayoutTitle) {
            this.LayoutTitle = LayoutTitle;
        }
    }
}
