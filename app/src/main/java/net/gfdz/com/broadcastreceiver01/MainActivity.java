package net.gfdz.com.broadcastreceiver01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 *                        动态注册广播监听网络变化
 * 1.我们在 MainActivity 中定义了一个内部类 NetworkChangeReceiver，这个类
 是继承自 BroadcastReceiver 的， 并重写了父类的 onReceive()方法。 这样每当网络状态发生变
 化时，onReceive()方法就会得到执行，这里只是简单地使用 Toast 提示了一段文本信息
 *
 *2. 首先我们创建了一个 IntentFilter 的实例，并给它添加了一个
 值为 android.net.conn.CONNECTIVITY_CHANGE 的 action，为什么要添加这个值呢？因为
 当网络状态发生变化时，系统发出的正是一条值为 android.net.conn.CONNECTIVITY_
 CHANGE 的广播，也就是说我们的广播接收器想要监听什么广播，就在这里添加相应的
 action 就行了。 接下来创建了一个 NetworkChangeReceiver 的实例， 然后调用 registerReceiver()
 方法进行注册，将 NetworkChangeReceiver 的实例和 IntentFilter 的实例都传了进去，这样
 NetworkChangeReceiver就会收到所有值为 android.net.conn.CONNECTIVITY_CHANGE的广
 播，也就实现了监听网络变化的功能。
 最后要记得，动态注册的广播接收器一定都要取消注册才行，这里我们是在 onDestroy()
 方法中通过调用 unregisterReceiver()方法来实现的

 3.在AndroidManifest.xml 文件中注册：
 <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 */
public class MainActivity extends AppCompatActivity {
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "network is available",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "network is unavailable",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
