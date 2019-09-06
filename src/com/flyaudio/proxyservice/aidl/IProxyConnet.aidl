package com.flyaudio.proxyservice.aidl;
interface IProxyConnet {
	void openFunction(int value);
	void sendFlyKey(int value);
	/**
	 * 模拟按键 信息发送 用于android原生系統
	 * 
	 * @param value
	 *            The integer keycode for the event.
	 */
	void sendKey(int value);
	
	/**  **********   UI参数结构 ************* **/
	/**
	 * 返回跳页 
	 * @param null
	 */
	void sendJumpPage();
	/**
	 * 下拉OSD消失
	 * @param null
	 */
	void sendOsdDisappear();
	/**
	 * 凯立德播放的声音状态
	 * 
	 * @param key
	 *            The integer keycode .FlyConstant.NAVI_VOL_CLOSE :关闭导航声音，
	 *            FlyConstant.NAVI_VOL_PALY :播放导航声音
	 */
	void sendNaviVoice(int key);
	/**
	 * 请求播放声音/停止声音
	 * 
	 * @param key
	 *            The integer keycode .FlyConstant.REQUESTVOICE_STOP :停止声音，
	 *            FlyConstant.REQUESTVOICE_PALY: 播放声音
	 */
	void sendRequestVoice(int key);
	/**
	 * Galaxy电源动作
	 * 
	 * @param key
	 *            The integer keycode .FlyConstant.DORMANCY: 正常休眠，
	 *            FlyConstant.RESTART :重启 , FlyConstant.RESTORESET: 恢复出厂设置
	 *            ,FlyConstant.UPGRADE :升级
	 */
	void sendGalaxyPowerAction(int key);
	/**
	 * 请求所有条形码信息
	 * @param null
	 */
	void sendBarcodeInfo();
	/**
	 * SIM电话状态
	 * 
	 * @param key
	 *            The integer keycode.FlyConstant.IDLE: idle,
	 *            FlyConstant.RINGING: ringing, FlyConstant.OFFHOOK:
	 *            offhook
	 */
	void sendSIMState(int key);
	/**
	 * 调试信息第几个按钮的信息
	 * 
	 * @param value
	 *            int for the value.
	 */

	void sendDebugInfo(int value);
	
	/**
	 * 接受调试面板类型
	 * 
	 * @param value
	 *            int for the value.
	 */
	void sendPaneType(int value);
	/**
	 * 往导航模块发送数据
	 * 
	 * @param String
	 *            String for the String.
	 */
	void sendDataToNavi(String data);
	/**
	 * 屏幕亮度控制协议
	 * 
	 * @param key
	 *            The integer keycode.FlyConstant.SCREEN_BLACK
	 *            关屏,FlyConstant.SCREEN_DIM:暗淡,
	 *            FlyConstant.SCREEN_MEDIUM 适中,FlyConstant.SCREEN_LIGHT
	 *            明亮 ,SCREEN_HIGH_LIGHT 高亮
	 */
	void sendScreenBrightness(int key);
	
	/**
	 * Android媒体播放状态
	 * 
	 * @param key
	 *            The integer keycode.FlyConstant.MEDIA_STOP
	 *            停止,FlyConstant.MEDIA_PALY:播放,FlyConstant.MEDIA_PAUSE
	 *            暂停,
	 */
	void sendMediaPlayStatus(int key);
	/**
	 * 快速开关机0s
	 * 
	 * @param key
	 *            The integer keycode.FlyConstant.POWER_ON
	 *            开,FlyConstant.POWER_OFF:关
	 */
	void sendQuickSwitchPower(int key);
	/**
	 * 快速开关机30S
	 * 
	 * @param key
	 *            The integer keycode.FlyConstant.ACC_ON
	 *            开,FlyConstant.ACC_OFF:关
	 */
	void sendQuickSwitchAcc(int key);
/**
	 * 快速开关机60S 预留
	 * 
	 * @param key
	 *            The integer keycode.FlyConstant.RESERVED_POWER_ON
	 *            开,FlyConstant.RESERVED_POWER_OFF:关
	 */
	void sendQuickSwitch(int key);
	/**
	 * 快速开关机新的Ping Loop UI To Module
	 * @param value
	 				int for the value.
	 */
	void sendQuickSwitchModule(int value);
	
	/**  **********   Key模块通信  ************* **/
	/**
	 * 模拟按键 信息发送 KEY_MEDIA
	 */
	void sendKeyMedia();
	/**
	 * 模拟按键 信息发送 KEY_SEEK ;
	 * 
	 * @param key
	 *            The integer keycode for the KEY_SEEK
	 *            event.FlyConstant.SEEK_INC Seek+ ,
	 *            FlyConstant.SEEK_DEC Seek-
	 */
	void sendKeySeek(int key);
	/**
	 * 模拟按键 信息发送 KEY_NAVI
	 */
	void sendKeyNavi();
	/**
	 * 模拟按键 信息发送 KEY_DEST
	 */
	void sendKeyDest();
	
	/**
	 * 模拟按键 信息发送 KEY_VOL ;
	 * 
	 * @param key
	 *            The integer keycode for the KEY_VOL
	 *            event.FlyConstant.VOL_MUTE 静音 , FlyConstant.VOL_DEC
	 *            减小，FlyConstant.VOL_INC 增加
	 */
	void sendKeyVol(int key);
	/**
	 * 智能风扇远程控制 ;
	 * 
	 * @param key
	 *            The integer keycode .FlyConstant.FAN_OPEN 打开 ,
	 *            FlyConstant.FAN_CLOSE 关闭，FlyConstant.FAN_AUTO 自动 FlyConstant.FAN_PERFORMANCE 高性能
	 */
	void sendKeyFan(int key);
	/**
	 * LCD高中低亮度 ;
	 * 
	 * @param key
	 *            The integer keycode .FlyConstant.LCD_HIGH 高 ,
	 *            FlyConstant.LCD_MID 中，FlyConstant.LCD_LOW 低， FlyConstant.LCD_CLOSE 关闭
	 */
	void sendKeyLCD(int key);
	
	
	/**  **********   MP3模块通信*  ************* **/
	
	/**
	 * MP3播放状态
	 * 
	 * @param key
	 *            The integer keycode .FlyConstant.MP3_STOP 停止，
	 *            FlyConstant.MP3_PLAY 播放，FlyConstant.MP3_PAUSE 暫停
	 */
	void sendMp3Status(int key);
	/**
	 * 播放时间(秒)
	 * 
	 * @param value
	 *            int for the value. 将转换成一个长度为4的字节数组.byte(2) Totaltime(s),
	 *            byte(2) current _playtime(s)
	 */
	void sendMp3Time(int value);
	/**
	 * 歌曲名字
	 * 
	 * @param name
	 *            String for the value.
	 */
	void sendMp3Name(String name);
		/**
	 * FlyMedia的track情况 总track数
	 * 
	 * @param value
	 *           int for the value.
	 */
	void sendMp3TotalTrack(int track);
	/**
	 * FlyMedia的track情况 当前第几个track
	 * 
	 * @param value
	 *            int for the value.
	 */
	void sendMp3CurrentTrack(int  currentTrack);
	
	/** **********    GPS模块通信   **********   **/
	/**
	 * 转弯方向（有路径） 0：直行 1:右前方 2:向右 3:右后方 4:调头 5:向左 6:左前方
	 * 
	 * @param value
	 *            int for the value.
	 */
	void sendTurnDirection(int value);
		/**
	 * 距离诱导点的距离，单位m（有路径）
	 * 
	 * @param value
	 *            int for the value.
	 */
	void sendDistance(int value);
	/**
	 * 当前距离目的地的距离，单位m
	 * 
	 * @param value
	 *            int for the value.Array size is four.
	 */
	void sendDestinationDistance(int value);
	/**
	 * 出发地与目的地之间的总距离，单位m
	 * 
	 * @param value
	 *            int for the value.Array size is four.
	 */
	void sendTotalDistance(int value);
	/**
	 * 距离目的地的剩余时间,单位秒
	 * 
	 * @param value
	 *            int for the value.Array size is four.
	 */
	void sendRemainderTime(int value);
	/**
	 * 出发地与目的地之间的总时间秒
	 * 
	 * @param value
	 *            int for the value.Array size is four.
	 */
	void sendTotalTime(int value);
	/**
	 * unicode当前道路名字
	 * 
	 * @param value
	 *            String for the value.Array size is N.
	 */
	void sendCurrentRoadNameUni(String value);
	/**
	 * 
	 * unicode下一道路名字
	 * 
	 * @param value
	 *            string for the value.Array size is N.
	 */
	void sendNextRoadNameUni(String value);
	/**
	 * 
	 * 当前道路类型 0:高速道路 1:快速道路 2:一般道路
	 * 
	 * @param value
	 *            int for the value.Array size is four.
	 */
	void sendCurrentRoadType(int value);
	/**
	 * 
	 * 当前车速，km/h
	 * 
	 * @param value
	 *            byte[] for the value.Array size is four.
	 */
	void sendCurrentSpeed(int value);
	
	/**
	 * 
	 * 当前道路限制车速，km/h
	 * 
	 * @param value
	 *            byte[] for the value.Array size is four.
	 */
	void sendLimitSpeed(int value);
	/**
	 * 当前GPS角度
	 * 
	 * @param value
	 *            byte[] for the value.Array size is four.
	 */
	void sendGpsAngle(int value);
	/**
	 * 环岛出口序号
	 * 
	 * @param value
	 *            byte[] for the value.Array size is four.
	 */
	void sendRoundaboutExitNumbers(int value);
	/**
	 * 环岛出口数
	 * 
	 * @param value
	 *            String for the value.Array size is four.
	 */
	void sendRoundaboutExitCount(int value);
		/**
	 * 当前行政区域名称
	 * 
	 * @param value
	 *            String for the value.Array size is N.
	 */
	void sendCurrentZoneName(String value);
	/**
	 * 
	 * 发送整个转向信息数值
	 * 
	 * @param value
	 *            String for the value.Array size is N.
	 */
	void sendSteeringInfoValue(String value);
	/**
	 * 行驶方位（十六方位） 0：北 1：东北偏北 2：东北 3：东北偏东 4：东 5：东南偏东 6：东南 7：东南偏南 8：南 9：西南偏南 10：西南
	 * 11：西南偏西 12：西 13：西北偏西 14：西北 15：西北偏北
	 * 
	 * 
	 * @param value
	 *            int for the value.Array size is four.
	 */
	void sendDrivingPosition(int value);
	/**
	 * 
	 * Utf-8当前道路名
	 * 
	 * @param value
	 *            String for the value.Array size is N.
	 */
	void sendCurrentRoadNameUtf(String value);
	
	/**
	 * Utf-8下一个道路名
	 * 
	 * @param value
	 *            String for the value.Array size is N.
	 */
	void sendNextRoadNameUtf(String value);
	
	/**      **********   service模块通信（ControlID为0xFF00FE，UI操作为必须为0xFE）  **********   **/ 
	/**
	 * 3G页面模拟打开调试信息方法,密码(高位在前)
	 * 
	 * @param password
	 *            int for the password.Array size is four.
	 */
	void sendOpenDebugPassword(int password);
	
	
	/**      **********    externalCtrl模块通信   **********   **/
	/** (1) 语音信息（ControlID为0xFC0001，UI操作为必须为0xFE）**/
	void sendVolumeSize(int value);//音量具体大小控制
	/**
	 * 音量最大
	 */
	void sendVolumMax();
	/**
	 * 音量最小
	 */
	void sendVolumMin();
	/**
	 * 设置音量为指定大小
     * @param value 音量大小值
	 */
	void sendControlVolum(int value);
	/**
	 * 语音通道切换
	 */
	void sendVolumeChange(int value);
	/**
	 * 关闭收音机或打开收音机
	 * 
	 * @param value
	 *            The integer keycode.FlyConstant.RADIO_CLOSE
	 *            关闭，FlyConstant.RADIO_OPEN 打开
	 * 
	 */
	void sendControlRadio(int value);
	/**
	 * 收听特定电台
	 * 
	 * @param name
	 *            The String for name.
	 */
	void sendListenRadio(String name);
	
	/**
	 * 按广播类型搜台
	 * 
	 * @param type
	 *            The String for type.
	 */
	void sendSearchRadio(String type);
	/**
	 * 调到指定频道(FM)+频率(有小数点*100)
	 * 
	 * @param frequency
	 *            The float for FM
	 */
	void sendFM(float frequency);
	/**
	 * 调到指定频道(AM)+频率
	 * 
	 * @param frequency
	 *            The float for AM
	 */
	void sendAM(float frequency);
	/**
	 * SCAN
	 * 
	 * @param value
	 *            The integer valuecode.
	 *            FlyConstant.RADIO_SCAN:SCAN;RADIO_SCAN_PLUS
	 *            :SCAN+;RADIO_SCAN_MINUS:SCAN-;
	 *            RADIO_SCAN_STOP:STOP;RADIO_SCAN_REPEAT:SCAN
	 *            REPEAT;RADIO_SCAN_PLUS_REPEAT:SCAN+ REPEAT;
	 *            RADIO_SCAN_MINUS_REPEAT:SCAN- REPAT
	 */
	void sendScan(int value);
		/**
	 * 碟机播放控制
	 * 
	 * @param key
	 *            The integer
	 *            keycode.FlyConstant.DVD_STOP:停止;DVD_PAUSE:暂停;DVD_PLAY:播放;
	 *            DVD_REWIND
	 *            :快退;DVD_FORWARD:快进;DVD_ORDER_CYCLE:顺序循环;DVD_SHUFFLE:随机播放;
	 *            DVD_SINGLE_CYCLE:单曲循环;DVD_SCAN_CONTROL:Dvd scan扫描控制;
	 * 
	 */
	void sendDvdPlayControl(int key);
		/**
	 * 车身控制
	 * 
	 * @param key0
	 *            The integer keycode.
	 *            FlyConstant.DORMER:天窗;TRUNK:后备箱;NEAR_LIGHTS
	 *            :近光灯;FAR_LIGHTS:远光灯
	 *            ;FOG_LIGHTS:雾灯;FRONT_FOG_LIGHTS:前雾灯;BEHIND_FOG_LIGHTS
	 *            :后雾灯;GALLERY_LIGHTS:示廓灯;WARNING_LIGHTS:警示灯;CAR_WINDOWS:车窗.
	 * @param key1
	 *            The integer keycode.FlyConstant.OFF:关；FlyConstant.ON：开
	 */
	void sendCarBodyControl(int key0,int key1);
	/**
	 * 模拟面板KEY功能控制
	 * 
	 * @param key0
	 *            The integer
	 *            keycode.FlyConstant.PANEL_PWR_VO:PWR-VO音量控制旋钮;PANEL_SEEK
	 *            :Seek+、Seek-;PANEL_NAVI:地图导航;PANEL_TUNE_AUDIO:TUNE-AUDIO旋钮;
	 *            PANEL_DVD:出碟按钮
	 * @param key1
	 *            The integer
	 *            keycode.在key1=FlyConstant.PANEL_PWR_VO状态下，FlyConstant
	 *            .PANEL_VOL_MUTE：静音、关闭音量、关闭声音；
	 *            FlyConstant.PANEL_VOL_OPEN:打开音量、打开声音
	 *            ；FlyConstant.PANEL_VOL_INC:音量加、增加音量、调大音量;
	 *            FlyConstant.PANEL_VOL_DEC
	 *            :音量减、减小音量、调小音量。其他状态下，FlyConstant.PANEL_NEXT：Seek+(下一首、下一曲、下一个
	 *            等等)， NAVI(打开导航、跳到地图页)，Tune+，进碟；
	 *            FlyConstant.PANEL_PREVIOUS：Seek-(上一首、上一曲、上一个
	 *            等等)，DEST()，Tune-，出碟。
	 */
	void sendPanelKeyControl(int key0,int key1);
		/**
	 * 飞歌应用管理
	 * 
	 * @param key0
	 *            The integer
	 *            keycode.FlyConstant.NAVI:导航;FlyConstant.DVD:碟机;FlyConstant
	 *            .RADIO:收音机;FlyConstant.MEDIA:媒体;FlyConstant.BLUETOOTH:蓝牙;
	 *            FlyConstant
	 *            .SYNC:SYNC;FlyConstant.DRIVING_RECORD:行车记录仪;FlyConstant.TV:电视;
	 *            FlyConstant
	 *            .TIRE:胎压;FlyConstant.BLUETOOTH_MUSIC:蓝牙音乐;FlyConstant
	 *            .SYSTEM_SET
	 *            :系统设置;FlyConstant.ENTERTAINMENT:后座娱乐;FlyConstant.AUXI_INPUT
	 *            :辅助输入;FlyConstant.IPOD:IPOD;FlyConstant.AIR_CINDITION:空调;
	 *            FlyConstant.CAR_INFO:车辆信息;FlyConstant.SETTINGS:系统设置页。
	 * 
	 * 
	 * 
	 * @param key1
	 *            The integer keycode.FlyConstant.OFF:关闭；FlyConstant.ON:打开。
	 */
	void sendFlyAppManger(int key0,int key1);
	
	//空调控制
		/**
	 * 空调控制
	 * 
	 * @param key
	 *            The integer
	 *            keycode.FlyConstant.OFF:关闭空调;FlyConstant.ON:打开空调,开启空调.
	 */
	void sendAirCinditionControl(int key);
	/**
	 * 温度调至/到+度数
	 * 
	 * @param degree
	 *            the int
	 */
	void sendTemToDegrees(int degree);
	/**
	 * 温度调高+度数
	 * 
	 * @param degree
	 *            the int
	 */
	void sendTemIncDegerees(int degree);
	/**
	 * 温度调低+度数
	 * 
	 * @param degree
	 *            the int
	 */
	void sendTemDecDegerees(int degree);
	/**
	 * @param key
	 *            The integer
	 *            keycode.FlyConstant.INC_TEMP:调高温度;FlyConstant.DEC_TEMP:调低温度
	 */
	void sendTemControl(int key);
		/**
	 * 调节空调模式
	 * 
	 * @param key
	 *            The integer keycode.FlyConstant.CRYOGEN:制冷;FlyConstant.HEATING:
	 *            制热;FlyConstant.DEHUMIDIFICATION: 除湿; FlyConstant.DEFROST: 除霜.
	 */
	void sendAirCinditionMode(int key);
	/**
	 * 调节空调风向
	 * 
	 * @param key
	 *            The integer
	 *            keycode.FlyConstant.UP_BLOW:上吹;FlyConstant.DOWN_BLOW:
	 *            下吹;FlyConstant.SWEPT: 扫风.
	 */
	void sendWindDirection(int key);
	/**
	 * 调节空调风力
	 * 
	 * @param key
	 *            The integer
	 *            keycode.FlyConstant.MAX_WIND:至最大;FlyConstant.MIN_WIND:
	 *            至最小;FlyConstant.HIGH_WIND: 高风;FlyConstant.LOW_WIND:
	 *            低风;FlyConstant.PLUS_WIND: 加;FlyConstant.MINUS_WIND: 减.
	 */
	 
	void sendWindPower(int key);
	 	/**
	 * 控制蓝牙打开或者关闭
     * @param value  蓝牙打开或者关闭
	 */
	void sendControlBT(int value);
	/**
	 * 拨打蓝牙电话电话号码
	 * 
	 * @param values
	 *            the String. 电话号码
	 */
	void sendCallBTPhone(String phone);
	 /**
	 * 请求发送蓝牙电话本
	 */
	void sendRequestBTPhoneBook();
	/**
	 * 设置麦克风 降噪模块模式
	 * 
	 * @param mode
	 *            FlyConstant.NOISE_REDUCTION 降噪;FlyConstant.ECHO_CANCEL 通话回声消除功能;
	 *            FlyConstant.WAKE 唤醒；FlyConstant.DIRECT_RECORD  所有功能关闭直接录音。
	 */
	void setMicMode(int mode);
	
		/**
	 *  控制打开或者关闭屏幕
	 * 
	 * @param mode
	 *           0：关闭屏幕  1：打开屏幕
	 */
	void setControlScreen(int mode);
	
	/**
	 *  控制凯立德地图呼叫一键通
	 * 
	 * @param number 一键通号码
	 *           
	 */
	void setControlKLDCall(String number);
	
		/**
	 *  控制风扇是否打开高性能模式
	 * 
	 * @param mode  0 关闭 ,1 打开
	 *           
	 */
	void setControlFan(int mode);
	
	
    /**
	 *  通用透明通道
	 * 
	 * @param int id , byte UI操作, bmp  协议
	 *           
	 */
	void ZControl( int id, byte type, inout byte[] bmp); 
}
