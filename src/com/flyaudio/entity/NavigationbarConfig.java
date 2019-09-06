package com.flyaudio.entity;


import java.util.List;

public class NavigationbarConfig {


    /**
     * navigationbar : {"buttonsize":"6","defaultbutton":"3","heigh":"178","idname":"flyaudio_navigationbar_bottom","layoutname":"flyauido_systemui_navigationbar_layout","background":"flyauido_systemui_navigationbar_bg","tablelist":{"tableheigh":"104","background":"flyauido_systemui_navigationbar_tablelayout","clickedbackground":"flyauido_systemui_navigationbar_table_clicked","idname":"flyaudio_navigationbar_table"}}
     * jumppages : [{"buttonpackage":"语音","buttonclass":"","broadcast":"","tablelist":[{"name":"","packageOther":"","classOther":"","textColor":"","background":"","then":"flyaudio.intent.action.CONTROL_VOICE","mintent":[{"key":"ENABLE_VOICE","value":"open_voice"}]}]},{"buttonpackage":"社交","buttonclass":"http://www.google.com1","broadcast":"","tablelist":[{"name":"flyaudio_Recommend","packageOther":"cn.flyaudio.flyforum","classOther":"cn.flyaudio.flyforum.RecommendActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_Chat","packageOther":"cn.flyaudio.flyforum","classOther":"cn.flyaudio.flyforum.ChatActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_AtTheMoment","packageOther":"cn.flyaudio.flyforum","classOther":"cn.flyaudio.flyforum.MomentActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_Activity","packageOther":"cn.flyaudio.flyforum","classOther":"cn.flyaudio.flyforum.CampaignActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_Information","packageOther":"cn.flyaudio.flyforum","classOther":"cn.flyaudio.flyforum.InformationActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]}]},{"buttonpackage":"多媒体","buttonclass":"","broadcast":"","tablelist":[{"name":"flyaudio_Local","packageOther":"com.flyaudio.flyguardian","classOther":"com.android.launcher.AutoMedia","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"2816","mintent":[{"key":"","value":""}]},{"name":"flyaudio_CloudLive","packageOther":"com.huajiao.sdk.demo","classOther":"com.huajiao.sdk.MainActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_CloudMusic","packageOther":"cn.kuwo.kwmusiccar","classOther":"cn.kuwo.kwmusiccar.MainActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_CloudVideo","packageOther":"com.youku.cloud.demo","classOther":"com.youku.cloud.demo.ListViewTest","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_NetworkRadio","packageOther":"com.app.test.android","classOther":"com.ximalaya.ting.android.opensdk.test.flyaudiofm.ui.activity.FmMainActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]}]},{"buttonpackage":"导航","buttonclass":"http://www.google.com3","broadcast":"","tablelist":[{"name":"flyaudio_Electric","packageOther":"AUTONAVI_STANDARD_BROADCAST_RECV","classOther":"AUTONAVI_STANDARD_BROADCAST_RECV","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"KEY_TYPE","value":"10036"},{"key":"KEYWORDS","value":"充电"},{"key":"SOURCE_APP","value":"Third App"}]},{"name":"flyaudio_Cate","packageOther":"AUTONAVI_STANDARD_BROADCAST_RECV","classOther":"AUTONAVI_STANDARD_BROADCAST_RECV","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"KEY_TYPE","value":"10036"},{"key":"KEYWORDS","value":"美食"},{"key":"SOURCE_APP","value":"Third App"}]},{"name":"flyaudio_Park","packageOther":"AUTONAVI_STANDARD_BROADCAST_RECV","classOther":"AUTONAVI_STANDARD_BROADCAST_RECV","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"KEY_TYPE","value":"10036"},{"key":"KEYWORDS","value":"停车场"},{"key":"SOURCE_APP","value":"Third App"}]},{"name":"flyaudio_4S","packageOther":"AUTONAVI_STANDARD_BROADCAST_RECV","classOther":"AUTONAVI_STANDARD_BROADCAST_RECV","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"KEY_TYPE","value":"10036"},{"key":"KEYWORDS","value":"4s"},{"key":"SOURCE_APP","value":"Third App"}]},{"name":"flyaudio_Map","packageOther":"AUTONAVI_STANDARD_BROADCAST_RECV","classOther":"AUTONAVI_STANDARD_BROADCAST_RECV","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"KEY_TYPE","value":"10008"}]}]},{"buttonpackage":"车辆","buttonclass":"http://www.google.com4","broadcast":"","tablelist":[{"name":"flyaudio_CarInfor","packageOther":"com.flyaudio.flyguardian","classOther":"com.android.launcher.DrivingInfo","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"8193","mintent":[{"key":"","value":""}]},{"name":"flyaudio_Phone","packageOther":"com.flyaudio.flyguardian","classOther":"com.android.launcher.Bluetooth","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"2304","mintent":[{"key":"","value":""}]},{"name":"flyaudio_AirConditioner","packageOther":"com.flyaudio.flyguardian","classOther":"com.android.launcher.Air","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"4096","mintent":[{"key":"","value":""}]},{"name":"flyaudio_CarSettings","packageOther":"com.flyaudio.flyguardian","classOther":"com.android.launcher.Setup","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"513","mintent":[{"key":"","value":""}]},{"name":"flyaudio_Smart","packageOther":"com.flyaudio.intelligentaccessory","classOther":"com.flyaudio.intelligentaccessory.AccessoryActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]}]},{"buttonpackage":"我的","buttonclass":"http://www.google.com5","broadcast":"","tablelist":[{"name":"flyaudio_Maintain","packageOther":"cn.flyaudio.vehiclemaintenance","classOther":"cn.flyaudio.vehiclemaintenance.activity.NetWorkVersionActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_Rescue","packageOther":"com.flyaudio.flymine","classOther":"com.flyaudio.flymine.activity.RescueActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"FragmentPage","value":"ResCueFragment"}]},{"name":"flyaudio_UserFeedback","packageOther":"cn.flyaudio.flyremotediagnose","classOther":"cn.flyaudio.flyremotediagnose.ui.UserInfoActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]},{"name":"flyaudio_PersonalCenter","packageOther":"com.flyaudio.flymine","classOther":"com.flyaudio.flymine.activity.AccountActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"FragmentPage","value":"AccountFragment"}]},{"name":"flyaudio_AllApp","packageOther":"cn.flyaudio.moreapplication","classOther":"cn.flyaudio.moreapplication.ui.MainActivity","textColor":"flyaudio_systemui_tablelist_textcolor","background":"","then":"-1","mintent":[{"key":"","value":""}]}]}]
     * resource : [{"icon":"flyauido_systemui_navigationbar_button_ai","selecticon":"flyauido_systemui_navigationbar_button_ai_c","name":"flyauido_systemui_navigationbar_button_ai","textColor":"","selectColor":""},{"icon":"flyauido_systemui_navigationbar_button_socialcontact","selecticon":"flyauido_systemui_navigationbar_button_socialcontact_c","name":"flyauido_systemui_navigationbar_button_socialcontact","textColor":"flyaudio_systemui_button_textcolor","selectColor":"flyaudio_systemui_button_textcolor_c"},{"icon":"flyauido_systemui_navigationbar_button_multimedia","selecticon":"flyauido_systemui_navigationbar_button_multimedia_c","name":"flyauido_systemui_navigationbar_button_multimedia","textColor":"flyaudio_systemui_button_textcolor","selectColor":"flyaudio_systemui_button_textcolor_c"},{"icon":"flyauido_systemui_navigationbar_button_nav","selecticon":"flyauido_systemui_navigationbar_button_nav_c","name":"flyauido_systemui_navigationbar_button_nav","textColor":"flyaudio_systemui_button_textcolor","selectColor":"flyaudio_systemui_button_textcolor_c"},{"icon":"flyauido_systemui_navigationbar_button_carinfor","selecticon":"flyauido_systemui_navigationbar_button_carinfor_c","name":"flyauido_systemui_navigationbar_button_carinfor","textColor":"flyaudio_systemui_button_textcolor","selectColor":"flyaudio_systemui_button_textcolor_c"},{"icon":"flyauido_systemui_navigationbar_button_me","selecticon":"flyauido_systemui_navigationbar_button_me_c","name":"flyauido_systemui_navigationbar_button_me","textColor":"flyaudio_systemui_button_textcolor","selectColor":"flyaudio_systemui_button_textcolor_c"}]
     */

    private NavigationbarBean navigationbar;
    private List<JumppagesBean> jumppages;
    private List<ResourceBean> resource;

    public NavigationbarBean getNavigationbar() {
        return navigationbar;
    }

    public void setNavigationbar(NavigationbarBean navigationbar) {
        this.navigationbar = navigationbar;
    }

    public List<JumppagesBean> getJumppages() {
        return jumppages;
    }

    public void setJumppages(List<JumppagesBean> jumppages) {
        this.jumppages = jumppages;
    }

    public List<ResourceBean> getResource() {
        return resource;
    }

    public void setResource(List<ResourceBean> resource) {
        this.resource = resource;
    }

    public static class NavigationbarBean {
        /**
         * buttonsize : 6
         * defaultbutton : 3
         * heigh : 178
         * idname : flyaudio_navigationbar_bottom
         * layoutname : flyauido_systemui_navigationbar_layout
         * background : flyauido_systemui_navigationbar_bg
         * tablelist : {"tableheigh":"104","background":"flyauido_systemui_navigationbar_tablelayout","clickedbackground":"flyauido_systemui_navigationbar_table_clicked","idname":"flyaudio_navigationbar_table"}
         */

        private int buttonsize;
        private int defaultbutton;
        private int heigh;
        private String idname;
        private String layoutname;
        private String background;
        private TablelistBean tablelist;

        public int getButtonsize() {
            return buttonsize;
        }

        public void setButtonsize(int buttonsize) {
            this.buttonsize = buttonsize;
        }

        public int getDefaultbutton() {
            return defaultbutton;
        }

        public void setDefaultbutton(int defaultbutton) {
            this.defaultbutton = defaultbutton;
        }

        public int getHeigh() {
            return heigh;
        }

        public void setHeigh(int heigh) {
            this.heigh = heigh;
        }

        public String getIdname() {
            return idname;
        }

        public void setIdname(String idname) {
            this.idname = idname;
        }

        public String getLayoutname() {
            return layoutname;
        }

        public void setLayoutname(String layoutname) {
            this.layoutname = layoutname;
        }

        public String getBackground() {
            return background;
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public TablelistBean getTablelist() {
            return tablelist;
        }

        public void setTablelist(TablelistBean tablelist) {
            this.tablelist = tablelist;
        }

        public static class TablelistBean {
            /**
             * tableheigh : 104
             * background : flyauido_systemui_navigationbar_tablelayout
             * clickedbackground : flyauido_systemui_navigationbar_table_clicked
             * idname : flyaudio_navigationbar_table
             */

            private int tableheigh;
            private String background;
            private String clickedbackground;
            private String idname;

            public int getTableheigh() {
                return tableheigh;
            }

            public void setTableheigh(int tableheigh) {
                this.tableheigh = tableheigh;
            }

            public String getBackground() {
                return background;
            }

            public void setBackground(String background) {
                this.background = background;
            }

            public String getClickedbackground() {
                return clickedbackground;
            }

            public void setClickedbackground(String clickedbackground) {
                this.clickedbackground = clickedbackground;
            }

            public String getIdname() {
                return idname;
            }

            public void setIdname(String idname) {
                this.idname = idname;
            }
        }
    }

    public static class JumppagesBean {
        /**
         * buttonpackage : 语音
         * buttonclass :
         * broadcast :
         * tablelist : [{"name":"","packageOther":"","classOther":"","textColor":"","background":"","then":"flyaudio.intent.action.CONTROL_VOICE","mintent":[{"key":"ENABLE_VOICE","value":"open_voice"}]}]
         */

        private String buttonpackage;
        private String buttonclass;
        private String broadcast;
        private List<TablelistBeanX> tablelist;

        public String getButtonpackage() {
            return buttonpackage;
        }

        public void setButtonpackage(String buttonpackage) {
            this.buttonpackage = buttonpackage;
        }

        public String getButtonclass() {
            return buttonclass;
        }

        public void setButtonclass(String buttonclass) {
            this.buttonclass = buttonclass;
        }

        public String getBroadcast() {
            return broadcast;
        }

        public void setBroadcast(String broadcast) {
            this.broadcast = broadcast;
        }

        public List<TablelistBeanX> getTablelist() {
            return tablelist;
        }

        public void setTablelist(List<TablelistBeanX> tablelist) {
            this.tablelist = tablelist;
        }

        public static class TablelistBeanX {
            /**
             * name :
             * packageOther :
             * classOther :
             * textColor :
             * background :
             * then : flyaudio.intent.action.CONTROL_VOICE
             * mintent : [{"key":"ENABLE_VOICE","value":"open_voice"}]
             */

            private String name;
            private String packageOther;
            private String classOther;
            private String textColor;
            private String background;
            private String then;
            private List<MintentBean> mintent;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPackageOther() {
                return packageOther;
            }

            public void setPackageOther(String packageOther) {
                this.packageOther = packageOther;
            }

            public String getClassOther() {
                return classOther;
            }

            public void setClassOther(String classOther) {
                this.classOther = classOther;
            }

            public String getTextColor() {
                return textColor;
            }

            public void setTextColor(String textColor) {
                this.textColor = textColor;
            }

            public String getBackground() {
                return background;
            }

            public void setBackground(String background) {
                this.background = background;
            }

            public String getThen() {
                return then;
            }

            public void setThen(String then) {
                this.then = then;
            }

            public List<MintentBean> getMintent() {
                return mintent;
            }

            public void setMintent(List<MintentBean> mintent) {
                this.mintent = mintent;
            }

            public static class MintentBean {
                /**
                 * key : ENABLE_VOICE
                 * value : open_voice
                 */

                private String key;
                private String value;

                public String getKey() {
                    return key;
                }

                public void setKey(String key) {
                    this.key = key;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }

    public static class ResourceBean {
        /**
         * icon : flyauido_systemui_navigationbar_button_ai
         * selecticon : flyauido_systemui_navigationbar_button_ai_c
         * name : flyauido_systemui_navigationbar_button_ai
         * textColor :
         * selectColor :
         */

        private String icon;
        private String selecticon;
        private String name;
        private String textColor;
        private String selectColor;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getSelecticon() {
            return selecticon;
        }

        public void setSelecticon(String selecticon) {
            this.selecticon = selecticon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getSelectColor() {
            return selectColor;
        }

        public void setSelectColor(String selectColor) {
            this.selectColor = selectColor;
        }
    }
}
