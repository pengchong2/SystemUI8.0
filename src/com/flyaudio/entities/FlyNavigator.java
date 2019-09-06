package com.flyaudio.entities;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.flyaudio.proxyservice.aidl.IProxyConnet;


public class FlyNavigator {
    private static FlyNavigator mFlyNavigator = null;
    public IProxyConnet mIProxyConnet;
    Context context;
    boolean flag = false;

    private FlyNavigator() {
    }

    private FlyNavigator(Context context) {

        this.context = context;
        Intent intent = new Intent("com.flyaudio.nativeservice.IProxyService").setPackage("com.flyaudio.proxy.service");
        context.bindService(intent, conn, Service.BIND_AUTO_CREATE);
    }

    public static synchronized FlyNavigator getInstance(Context context) {
        if (mFlyNavigator == null) {
            mFlyNavigator = new FlyNavigator(context);
        }
        return mFlyNavigator;
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        synchronized public void onServiceConnected(ComponentName name,
                IBinder service) {
            mIProxyConnet = IProxyConnet.Stub.asInterface(service);
            flag = true;
            Log.d("FlyaudioSystemUI","onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
			Log.d("FlyaudioSystemUI","onServiceDisconnected");
            mIProxyConnet = null;
            flag = false;
        }
    };

    public void unbindService()
    {
        if (flag)
            context.unbindService(conn);
        flag = false;
    }

//    public class AdapterCenter {
//
//        public AdapterCenter() {
//        }
	

        public void sendControlBT(int value) {
            try {
                if (mIProxyConnet != null)
                mIProxyConnet.sendControlBT(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


	public void setControlScreen(int value) {
            try {

                if (mIProxyConnet != null){
                	mIProxyConnet.setControlScreen(value);
		}
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void sendControlVolum(int value) {
            try {
                if (mIProxyConnet != null)
                mIProxyConnet.sendControlVolum(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }



        public void openFunction(int value) {
            try {
                if (mIProxyConnet != null)
                mIProxyConnet.openFunction(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        
        public void sendFlyKey(int value) {
            try {
                if (mIProxyConnet != null)
                mIProxyConnet.sendFlyKey(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        
        public void sendKey(int value) {
            try {
                if (mIProxyConnet != null)
                mIProxyConnet.sendKey(value);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
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
    	public void sendPanelKeyControl(int key0,int key1){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendPanelKeyControl(key0,key1);
                 }
             } catch (RemoteException e) {
                 e.printStackTrace();
             }
    	}
    	
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
    	 *            FlyConstant.CAR_INFO:车辆信息。
    	 * 
    	 * 
    	 * 
    	 * @param key1
    	 *            The integer keycode.FlyConstant.OFF:关闭；FlyConstant.ON:打开。
    	 */
    	public void sendFlyAppManger(int key0,int key1){
   		 try {
                if (mIProxyConnet != null){
               	 mIProxyConnet.sendFlyAppManger(key0,key1);
                }
            } catch (RemoteException e) {
                e.printStackTrace();

            }
    	}
    //音量具体大小控制	
    public void sendVolumeSize(byte value){
    	 try {
             if (mIProxyConnet != null){
            	 mIProxyConnet.sendVolumeSize(value);
             }
         } catch (RemoteException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    }
    	
    	/**
    	 * 音量最大
    	 */
    	public void sendVolumMax( ){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendVolumMax();
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
     	}
    	
    	/**
    	 * 音量最小
    	 */
    	public void sendVolumMin( ){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendVolumMin();
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
     	}
    	
    	
    	/**
    	 * 关闭收音机或打开收音机
    	 * 
    	 * @param value
    	 *            The integer keycode.FlyConstant.RADIO_CLOSE
    	 *            关闭，FlyConstant.RADIO_OPEN 打开
    	 * 
    	 */
    	public void sendControlRadio(int value){
     		 try {
                  if (mIProxyConnet != null){
                 	 mIProxyConnet.sendControlRadio(value);
                  }
              } catch (RemoteException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
      	}
    	/**
    	 * 调到指定频道(FM)+频率(有小数点*100)
    	 * 
    	 * @param frequency
    	 *            The byte[] for FM
    	 */
    	public void sendFM(float frequency){
      		 try {
                   if (mIProxyConnet != null){
                  	 mIProxyConnet.sendFM(frequency);
                   }
               } catch (RemoteException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
       	}
    	
    	/**
    	 * 调到指定频道(AM)+频率
    	 * 
    	 * @param frequency
    	 *            The byte[] for AM
    	 */
    	public void sendAM(float frequency){
      		 try {
                   if (mIProxyConnet != null){
                  	 mIProxyConnet.sendAM(frequency);
                   }
               } catch (RemoteException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
       	}
    	
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
    	public void sendDvdPlayControl(int key){
      		 try {
                   if (mIProxyConnet != null){
                  	 mIProxyConnet.sendDvdPlayControl(key);
                   }
               } catch (RemoteException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
       	}
    	
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
    	public void sendCarBodyControl(int key0,int key1){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendCarBodyControl(key0,key1);
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
    	}
    	/**
    	 * 空调控制
    	 * 
    	 * @param key
    	 *            The integer
    	 *            keycode.FlyConstant.OFF:关闭空调;FlyConstant.ON:打开空调,开启空调.
    	 */
    	public void sendAirCinditionControl(int key){
   		 try {
                if (mIProxyConnet != null){
               	 mIProxyConnet.sendAirCinditionControl(key);
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
   	}
    	
    	/**
    	 * @param key
    	 *            The integer
    	 *            keycode.FlyConstant.INC_TEMP:调高温度;FlyConstant.DEC_TEMP:调低温度
    	 */
    	public void sendTemControl(int key){
      		 try {
                   if (mIProxyConnet != null){
                  	 mIProxyConnet.sendTemControl(key);
                   }
               } catch (RemoteException e) {
                   // TODO Auto-generated catch block
                   e.printStackTrace();
               }
      	}
    	
    	/**
    	 * 调节空调模式
    	 * 
    	 * @param key
    	 *            The integer keycode.lyConstant.CRYOGEN:制冷;FlyConstant.HEATING:
    	 *            制热;FlyConstant.DEHUMIDIFICATION: 除湿; FlyConstant.DEFROST: 除霜.
    	 */
    	public void sendAirCinditionMode(int key){
     		 try {
                  if (mIProxyConnet != null){
                 	 mIProxyConnet.sendAirCinditionMode(key);
                  }
              } catch (RemoteException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
     	}
    	/**
    	 * 调节空调风向
    	 * 
    	 * @param key
    	 *            The integer
    	 *            keycode.FlyConstant.UP_BLOW:上吹;FlyConstant.DOWN_BLOW:
    	 *            下吹;FlyConstant.SWEPT: 扫风.
    	 */
    	public void sendWindDirection(int key){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendWindDirection(key);
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
    	}
    	/**
    	 * 调节空调风力
    	 * 
    	 * @param key
    	 *            The integer
    	 *            keycode.FlyConstant.MAX_WIND:至最大;FlyConstant.MIN_WIND:
    	 *            至最小;FlyConstant.HIGH_WIND: 高风;FlyConstant.LOW_WIND:
    	 *            低风;FlyConstant.PLUS_WIND: 加;FlyConstant.MINUS_WIND: 减.
    	 */
    	public void sendWindPower(int key){
   		 try {
                if (mIProxyConnet != null){
               	 mIProxyConnet.sendWindPower(key);
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
   	}
    	
    	/**
    	 * 调节空调减多少度
    	 * 
    	 */
    	public void sendTemDecDegerees(byte degree){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendTemDecDegerees(degree);
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
    	}
    	
    	/**
    	 * 调节空调加多少度
    	 * 
    	 */
    	public void sendTemIncDegerees(byte degree){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendTemIncDegerees(degree);
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
    	}
    	
    	/**
    	 * 调节空调到多少度
    	 * 
    	 */
    	public void sendTemToDegrees(byte degree){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendTemToDegrees(degree);
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
    	}
    	
    	/**
    	 * 切换通道
    	 * 
    	 */
    	public void sendVolumeChange(byte arg0){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendVolumeChange(arg0);
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
    	}
    	
    	/**
    	 * 蓝牙通话
    	 * 
    	 */
    	public void sendCallBTPhone(String number){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendCallBTPhone(number);
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
    	}
    	
    	/**
    	 * 请求蓝牙电话本
    	 * 
    	 */
    	public void sendRequestBTPhoneBook(){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.sendRequestBTPhoneBook();
                 }
             } catch (RemoteException e) {
                 // TODO Auto-generated catch block
                 e.printStackTrace();
             }
    	}
    	
    	/**
    	 * 设置降噪模块模式
    	 *   @param mode
    	 * FlyConstant.CHANGE2DN为降噪模式
    	 * FlyConstant.CHANGE2WK为唤醒模式
    	 */
    	public void setMicMode(int mode){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.setMicMode(mode);
                 }
             } catch (RemoteException e){
                 e.printStackTrace();
             }
    	}
    	
        /**
         * 控制风扇是否打开高性能模式
         * 
         * @param mode
         *            0 关闭 ,1 打开
         * 
         */
    	public void setControlFan(int mode){
    		 try {
                 if (mIProxyConnet != null){
                	 mIProxyConnet.setControlFan(mode);
                 }
             } catch (RemoteException e){
                 e.printStackTrace();
             }
    	}
    	
    	
      	public void setZControl(int id,byte mbyte ,byte[] buf){
   		 try {
                if (mIProxyConnet != null){
               	 mIProxyConnet.ZControl(id, mbyte, buf);
                }
            } catch (RemoteException e){
                e.printStackTrace();
            }
   	}
    	
    	
   // }
}
