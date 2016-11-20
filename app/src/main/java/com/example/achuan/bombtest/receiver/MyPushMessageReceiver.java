package com.example.achuan.bombtest.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.util.LogUtil;

import cn.bmob.push.PushConstants;
import cn.bmob.v3.helper.NotificationCompat;

/**
 * Created by achuan on 16-11-20.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String msg=intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            LogUtil.d("lyc-changeworld", "客户端收到推送内容："+msg);

            //实例化-通知栏构造器
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("My notification")
                            .setContentText(msg);
            Notification notification = mBuilder.build();//通过构造器拿到具体属性设置实例
            /*//1-设置通知的铃声
               Uri soundUri=Uri.fromFile(new File("xxx/xxx/xxx"));//音频文件的路径
               mBuilder.setSound(soundUri);*/
               /*//2-设置手机振动
               long[] vibrates ={0,1000,1000,1000};//偶数表示手机静止的时长，奇数表示手机振动的时长
               notification.vibrate=vibrates;*/
               /*//3-设置手机提示灯
               notification.ledARGB=Color.GREEN;//1-LED灯的颜色
               notification.ledOnMS=1000;//2-亮起的时间
               notification.ledOffMS=1000;//3-熄灭的时间
               notification.flags=Notification.FLAG_SHOW_LIGHTS;*/
            notification.defaults= Notification.DEFAULT_ALL;//使用通知的默认效果
            NotificationManager manager= (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE
            );
            manager.notify(1,notification);//创建通知,指定id号为1,方便后面取消通知

        }
    }
}
