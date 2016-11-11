package com.example.achuan.bombtest.ui.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.app.App;
import com.example.achuan.bombtest.app.Constants;
import com.example.achuan.bombtest.base.BaseActivity;
import com.example.achuan.bombtest.presenter.MainPresenter;
import com.example.achuan.bombtest.presenter.contract.MainContract;
import com.example.achuan.bombtest.ui.assistant.fragment.AssistantMainFragment;
import com.example.achuan.bombtest.ui.main.fragment.SettingFragment;
import com.example.achuan.bombtest.util.SharedPreferenceUtil;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends BaseActivity<MainPresenter>
        implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.view_search)
    MaterialSearchView mViewSearch;

    RelativeLayout mNavViewHeaderView;//左侧的头部布局
    TextView mTV_nickName;//用户名
    TextView mTV_userInfo;//用户信息

    ActionBarDrawerToggle mDrawerToggle;//左侧部分打开按钮
    //fragment的引用变量
    SettingFragment mSettingFragment;//设置部分界面
    AssistantMainFragment mAssistantMainFragment;

    //记录左侧navigation的item点击
    MenuItem mLastMenuItem;//历史
    //搜索item的引用变量
    MenuItem mSearchMenuItem;

    //定义变量记录需要隐藏和显示的fragment的编号
    private int hideFragment = Constants.TYPE_SIGNINASSISTANT;
    private int showFragment = Constants.TYPE_SIGNINASSISTANT;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        //初始化设置标题栏
        setToolBar(mToolbar, "签到助手");
        //初始化创建fragment实例对象
        mSettingFragment = new SettingFragment();
        mAssistantMainFragment=new AssistantMainFragment();

        /***将需要显示的fragment全部装载到当前activity中***/
        //越靠前添加的fragment越在上面显示
        loadMultipleRootFragment(R.id.fl_main_content, 0,
                mAssistantMainFragment,
                mSettingFragment);
        //初始化第一次显示的item为设置界面
        mLastMenuItem = mNavView.getMenu().findItem(R.id.signInAssistant);
        mLastMenuItem.setChecked(true);

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(
                this,            /* host Activity */
                mDrawerLayout,  /* DrawerLayout object */
                mToolbar,
                R.string.navigation_drawer_open,/* "open drawer" description for accessibility */
                R.string.navigation_drawer_close);/* "close drawer" description for accessibility */
        mDrawerLayout.addDrawerListener(mDrawerToggle);//添加点击监听事件
        //将DrawerToggle中的drawer图标,设置为ActionBar中的Home-Button的Icon
        mDrawerToggle.syncState();
        //为左侧的navigation设置item点击监听
        mNavView.setNavigationItemSelectedListener(this);
        /*****获取NavigationView中的控件元素******/
        //先获取navigationView中的nav_header_main布局,0代表取第一个子元素
        mNavViewHeaderView = (RelativeLayout) mNavView.getHeaderView(0);
        //加载nav_header_main中的子元素控件
        mTV_nickName = (TextView) mNavViewHeaderView.findViewById(R.id.tv_nickName);
        mTV_userInfo = (TextView) mNavViewHeaderView.findViewById(R.id.tv_userInfo);

        //对navView头部的布局进行点击事件监听,登录和非登录时触发效果不一样
        mNavViewHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.getInstance().getIsLogin()) {
                    //跳转到修改个人资料的界面
                    Toast.makeText(MainActivity.this,
                            "修改个人资料",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //跳转到登录界面
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    //该方法启动活动后,会在活动销毁的时候返回一个结果给上一个活动
                    startActivity(intent);
                }
            }
        });

        /*为search_view添加查询监听事件*//*
        mViewSearch.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(showFragment==Constants.TYPE_SIGNINASSISTANT){
                    //执行查询操作,并刷新列表
                    Toast.makeText(MainActivity.this, "开始查询", Toast.LENGTH_SHORT).show();
                    mAssistantFragment.doSearch(query);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //刚开始启动应用时savedInstanceState == null
        if (savedInstanceState == null) {
        } else {//reCreate活动调用执行
            showFragment = SharedPreferenceUtil.getCurrentItem();
            hideFragment = Constants.TYPE_SIGNINASSISTANT;
            showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
            hideFragment = showFragment;//显示过的fragment变成了历史了
            mNavView.getMenu().findItem(R.id.signInAssistant).
                    setChecked(false);
        }
        /*//针对5.0以下的系统使用fitsSystemWindows属性时进行的兼容性判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //将侧边栏顶部延伸至status bar
                mDrawerLayout.setFitsSystemWindows(true);
                //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                mDrawerLayout.setClipToPadding(false);
            }
        }*/
    }

    //在活动准备好和用户进行交互时调用
    @Override
    protected void onResume() {
        super.onResume();
        //交互的同时更新显示的内容
        if (App.getInstance().getIsLogin()) {
            //显示用户名称
            mTV_nickName.setText(App.getBmobUser().getUsername());
            mTV_userInfo.setText(R.string.clickSetting);
        } else {
            mTV_nickName.setText(R.string.clickLogin);
            mTV_userInfo.setText(R.string.loginInfo);
        }
    }

    /***
     * navigation的item点击事件监听方法实现
     ***/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.bluetooth_connected:
                showFragment = Constants.TYPE_BLUETOOTH;
                //mSearchMenuItem.setVisible(false);
                break;
            case R.id.preference_setting:
                showFragment = Constants.TYPE_SETTING;
                //mSearchMenuItem.setVisible(false);
                break;
            case R.id.signInAssistant:
                showFragment = Constants.TYPE_SIGNINASSISTANT;
                //mSearchMenuItem.setVisible(true);
                break;
            default:
                break;
        }
        /***点击item后进行显示切换处理,并记录在本地中***/
        if (mLastMenuItem != null) {
            mLastMenuItem.setChecked(false);//取消历史选择
        }
        item.setChecked(true);//设置当前选择
        //改变标题栏的内容
        mToolbar.setTitle(item.getTitle());
        //记录当前显示的item
        SharedPreferenceUtil.setCurrentItem(showFragment);
        //实现fragment的切换显示
        showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
        //选择过的item变成了历史
        mLastMenuItem = item;
        //关闭左侧drawer菜单栏
        mDrawerLayout.closeDrawer(GravityCompat.START);
        //当前fragment显示完就成为历史了
        hideFragment = showFragment;
        return true;
    }

    /*创建右上角的菜单选项时执行该方法*//*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载菜单item文件资源到menu实例中
        getMenuInflater().inflate(R.menu.main_menu, menu);
        //获取menu集合中的search选项的实例
        MenuItem item = menu.findItem(R.id.action_search);
        //默认为显示search的图标,部分fragment才显示搜索菜单项
        item.setVisible(false);
        //将item菜单图标和search控件关联到一起,即该菜单将具备搜索功能
        mViewSearch.setMenuItem(item);
        //将search引用变量指向该该菜单item,用来控制是否隐藏搜索菜单项
        mSearchMenuItem=item;
        return true;
    }*/

    //重写back按钮的点击事件
    @Override
    public void onBackPressedSupport() {
        /*
        //如果drawer处于打开状态,back键将使其关闭
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
        showExitDialog();
        }*/
        //如果搜索控件处于打开状态,back键将使其关闭
        if(mViewSearch.isSearchOpen()){
            mViewSearch.closeSearch();
        }else {
            showExitDialog();
        }
    }

    //弹出对话框,确认是否退出App
    private void showExitDialog() {
        //弹出一个对话框
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);//先创建一个构造实例
        dialog.setTitle("提示");//设置标题
        dialog.setMessage("确定退出签到助手吗");//设置内容部分
        dialog.setCancelable(true);//设置是否可以通过Back键取消：false为不可以取消,true为可以取消
        //设置右边按钮的信息
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override//点击触发事件
            public void onClick(DialogInterface dialogInterface, int i) {
                //BluetoothUtil.getBluetooth().disable();//退出应用后关闭蓝牙
                //点击确定后退出app
                //将所有的活动依次出栈,然后回收所有的资源
                App.getInstance().exitApp();
            }
        });
        //设置左边按钮的信息
        dialog.setNegativeButton("取消", null);
        dialog.show();//将对话框显示出来
    }

    //根据item编号获取fragment对象的方法
    private SupportFragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_SETTING:
                return mSettingFragment;
            case Constants.TYPE_SIGNINASSISTANT:
                return mAssistantMainFragment;
            default:
                break;
        }
        return mAssistantMainFragment;
    }

    @Override
    public void showError(String msg) {

    }
}
