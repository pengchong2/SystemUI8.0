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

cd frameworks/base/packages/SystemUI1

mm
