package com.example.achuan.bombtest.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.app.Constants;
import com.example.achuan.bombtest.ui.NotificationActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
            //LogUtil.d("lyc-changeworld", "客户端收到推送内容："+msg);
            /*推送消息的格式为：
        *
        * {"alert" : "推送消息测试。。。。", }
        *
    　　　*/
            //对推送过来的消息进行简单的解析处理
            String message = null;
            JSONTokener jsonTokener=new JSONTokener(msg);
            try {
                JSONObject jsonObject= (JSONObject) jsonTokener.nextValue();
                message=jsonObject.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*****创建意图,并将其暂时存在栈中,当用户点击通通知弹窗时才触发启动这个意图*****/
            Intent resultIntent = new Intent(context, NotificationActivity.class);
            resultIntent.putExtra("msg",message);
            //创建堆栈生成器
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            //将返回栈添加到堆栈生成器
            stackBuilder.addParentStack(NotificationActivity.class);
            //添加可从通知中启动 Activity 的 Intent
            stackBuilder.addNextIntent(resultIntent);
            //获得此返回栈的 PendingIntent
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            /********************实例化-通知栏构造器****************/
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("重要通知")
                    .setContentText(message);
            /************将Notification和上面的跳转意图联系起来***********/
            mBuilder.setContentIntent(resultPendingIntent);
            Notification notification = mBuilder.build();//通过构造器拿到具体属性设置实例
            /*//1-设置通知的铃声
            Uri soundUri=Uri.fromFile(new File("xxx/xxx/xxx"));//音频文件的路径
            mBuilder.setSound(soundUri);*/
            /*//2-设置手机振动
            long[] vibrates ={0,1000,1000,1000};//偶数表示手机静止的时长，奇数表示手机振动的时长
            notification.vibrate=vibrates;
            //3-设置手机提示灯
            notification.ledARGB= Color.RED;//1-LED灯的颜色
            notification.ledOnMS=1000;//2-亮起的时间
            notification.ledOffMS=1000;//3-熄灭的时间
            notification.flags=Notification.FLAG_SHOW_LIGHTS;*/
            //使用通知的默认效果
            notification.defaults= Notification.DEFAULT_ALL;
            //获取系统的通知服务管理对象
            NotificationManager manager= (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            manager.notify(Constants.NotificationID,notification);//创建通知,指定id号为1,方便后面取消通知

        }
    }
}
