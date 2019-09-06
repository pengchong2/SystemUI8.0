# 1. 项目名称

J6样机SystemUI

# 2.分支说明

只有master分支

# 3.功能描述

在原生的systemui基础上，底部导航栏显示空调的温度，具有返回，主页，全部应用功能，下拉菜单可以设置wifi,4G，蓝牙，热点，声音，亮度功能

# 4.编译方式

ssh pengchong@172.168.1.20

密码：pengchong

cd  /J6-Android-8.1.0/mydroid

. build/envsetup.sh

lunch 38

cd frameworks/base/packages/SystemUI1  （这里需要注意为工程的名字不要为SystemUI,其他的都可以）

mm

# 5.类说明

SystemUIApplication.java 继承Application,应用初始化

FlyaudioSystemUI.java 监听导航栏底部的操作事件返回，主页，全部应用

IProxyConnet.aidl 定义调用系统的接口

StatusBar.java 加载状态栏和底部导航栏

PanelView.java 监听下拉事件来显示和收起下拉菜单

QuickStatusBarHeader.java 监听亮度设置和声音设置

PowerButton.java 所有快捷按钮wifi,蓝牙等的基础类

PowerWidget.java 下拉菜单快捷按钮自定义布局类，继承FrameLayout

# 6.0项目进度
完成




