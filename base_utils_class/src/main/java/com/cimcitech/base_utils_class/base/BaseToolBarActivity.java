package com.cimcitech.base_utils_class.base;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cimcitech.base_utils_class.R;
import com.cimcitech.base_utils_class.dialog.CommonHintDialog;
import com.cimcitech.base_utils_class.progress_dialog.PromptDialog;
import com.cimcitech.base_utils_class.utils.DensityUtils;
import com.cimcitech.base_utils_class.utils.LogUtils;
import com.cimcitech.base_utils_class.utils.OpenCameraUtils;
import com.cimcitech.base_utils_class.utils.ToastUtil;
import com.cimcitech.base_utils_class.view.BaseViewIF;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import butterknife.ButterKnife;


public abstract class BaseToolBarActivity extends AppCompatActivity implements BaseViewIF {

    public static final int REQUEST_CAMERA_CODE = 100;
    protected static final String TAG = "BaseToolBarActivity";
    protected Activity mActivity;
    protected Bundle savedInstanceState;
    protected Toolbar sToolbar;
    private TextView tvTitle;
    private TextView tvH;
    private FrameLayout viewContent;
    private OnClickListener onClickListenerTopLeft;
    private OnClickListener onClickListenerTopRight;
    private int menuResId;
    private String menuStr;
    MenuItem menuItem;

    CommonHintDialog rCommonHintDialog;

    PromptDialog promptDialog;
    public View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.savedInstanceState = savedInstanceState;
        //标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base_toolbar);
        sToolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        tvH = findViewById(R.id.tv_h);
        viewContent = findViewById(R.id.viewContent);
        //将继承 TopBarBaseActivity 的布局解析到 FrameLayout 里面
        view = LayoutInflater.from(this).inflate(getLayoutId(), viewContent);
        //初始化设置 Toolbar
        setSupportActionBar(sToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        mActivity = this;
        setHideToolbar(false);
//        StatusBarColorUtil.setStatusBarLightMode(this, ContextCompat.getColor(this, R.color.white));

        initBase();
        initView();
        initListener();
        initData();

    }


    protected void setTopTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    protected void setHideToolbar(boolean hide) {
        if (hide) {
            sToolbar.setVisibility(View.GONE);
            tvH.setVisibility(View.GONE);
        } else {
            sToolbar.setVisibility(View.VISIBLE);
            tvH.setVisibility(View.VISIBLE);
        }
    }


    protected void setTopLeftButton(int iconResId, OnClickListener onClickListener) {
        sToolbar.setNavigationIcon(iconResId);
        this.onClickListenerTopLeft = onClickListener;
    }

    protected void setTopRightButton(int menuResId, OnClickListener onClickListener) {
        this.onClickListenerTopRight = onClickListener;
        this.menuResId = menuResId;
    }

    protected void setTopRightButton(String menuStr, OnClickListener onClickListener) {
        this.onClickListenerTopRight = onClickListener;
        this.menuStr = menuStr;

    }

    protected void setTopRightButton(String menuStr, int menuResId, OnClickListener onClickListener) {
        this.menuResId = menuResId;
        this.menuStr = menuStr;
        this.onClickListenerTopRight = onClickListener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuResId != 0 || !TextUtils.isEmpty(menuStr)) {

            getMenuInflater().inflate(R.menu.menu_activity_base_top_bar, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuResId != 0) {
            menu.findItem(R.id.menu_1).setIcon(menuResId);
        }
        if (!TextUtils.isEmpty(menuStr)) {
            menuItem = menu.findItem(R.id.menu_1).setTitle(menuStr);
//            menuItem.setVisible(visible);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    boolean visible;

    protected void setMenuItemVisible(boolean visible) {
        this.visible = visible;
        if (menuItem != null) {
            menuItem.setVisible(visible);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onClickListenerTopLeft.onClick();
        } else if (item.getItemId() == R.id.menu_1) {
            onClickListenerTopRight.onClick();
        }
        // true 告诉系统我们自己处理了点击事件
        return true;
    }

    protected interface OnClickListener {
        /**
         * toolBar 监听接口重写
         */
        void onClick();
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID
     *
     * @return
     */
    protected abstract int getLayoutId();


    protected abstract void initBase();

    protected abstract void initView();

    protected abstract void initListener();

    protected abstract void initData();

    public void startActivity(Class target, boolean isFinish) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
        if (isFinish) {
            finish();
            //overridePendingTransition( R.anim.activity_left_in,R.anim.activity_right_out);
        } else {
            //overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        }

    }

    /**
     * 启动activity 并设置动画
     *
     * @param target
     * @param isFinish
     * @param orientation -1:左 1:右
     */
    public void startActivity(Class target, boolean isFinish, int orientation) {
        Intent intent = new Intent(this, target);
        startActivity(intent);
        if (isFinish) {
            finish();
            //overridePendingTransition( R.anim.activity_left_in,R.anim.activity_right_out);
        } else {
            //overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        }
        if (orientation == -1) {
            overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
        } else {
            overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        }
    }


    public void startActivity(Class target, boolean isFinish, boolean isTopping) {
        Intent intent = new Intent(this, target);
        if (isTopping) {

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            LogUtils.e(TAG, "startActivity: 所有活动出栈");
        }
        startActivity(intent);
        if (isFinish) {
            finish();
            //overridePendingTransition( R.anim.activity_left_in,R.anim.activity_right_out);
        } else {
            // overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
        }

    }

    public Context getContext() {
        return this;
    }


    @Override
    public void showToast(final String msg) {
        //hideAwaitDialog();
        //LogUtils.e(TAG, msg);
        try {
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }


    }


    @Override
    public void finish() {
        super.finish();
//        关闭dialog
        dismissImmediatelyAwaitDialog();
        dismissSelectDialog();
        //overridePendingTransition( R.anim.activity_left_in,R.anim.activity_right_out);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    /**
     * 返回键
     */
    public final void setLeftBack() {
        setTopLeftButton(R.mipmap.back, this::finish);

    }

    public void showSelectDialog(String content, View.OnClickListener onOKListener) {

        CommonHintDialog.Builder builder = new CommonHintDialog.Builder(this).setContent(content);
        builder.setNegativeButton(getResources().getString(R.string.confirm), onOKListener);


        builder.setPositiveButton(getResources().getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rCommonHintDialog != null) {
                    rCommonHintDialog.dismiss();
                }
            }
        });
        rCommonHintDialog = builder.create();

        rCommonHintDialog.show();
        Window win = rCommonHintDialog.getWindow();
        win.getDecorView().setPadding(DensityUtils.dip2px(this, 20), 0, DensityUtils.dip2px(this, 20), 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

    }


    public void dismissSelectDialog() {
        if (rCommonHintDialog != null) {
            rCommonHintDialog.dismiss();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionsResult: " + requestCode);
        switch (requestCode) {
            case REQUEST_CAMERA_CODE:
                boolean isGo = true;
                if (permissions.length > 0) {
                    for (int result : grantResults) {
                        if (result == PackageManager.PERMISSION_DENIED) {
                            isGo = false;
                        }
                    }
                }
                if (!isGo) {
                    ToastUtil.showToast(this, "权限不足");
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                } else {
                    OpenCameraUtils.getInstance().openAlbum();
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onSuccess(String value) {
        showSuccessDialog();
    }

    @Override
    public void onFailure(String code, String error) {
        showErrorDialog();
    }


    @Override
    public void setAdapter() {

    }


    @Override
    public void showAwaitDialog(int id) {
        if (isFinishing()) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(mActivity);
        }
        promptDialog.dismissImmediately();

        promptDialog.showLoading(getResources().getString(id), false);

    }

    @Override
    public void showAwaitDialog() {
        if (isFinishing()) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(mActivity);
        }
        promptDialog.dismissImmediately();
        promptDialog.showLoading(getResources().getString(R.string.dialog_loading), false);

    }


    @Override
    public void dismissAwaitDialog() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);

                    if (mActivity != null) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (promptDialog != null) {
                                    promptDialog.dismissImmediately();
                                }
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }

    @Override
    public void dismissImmediatelyAwaitDialog() {
        if (promptDialog != null) {
            promptDialog.dismissImmediately();
        }

    }

    @Override
    public void showSuccessDialog(){
        if (isFinishing()) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(mActivity);
        }
        promptDialog.dismissImmediately();
        promptDialog.showSuccess(getResources().getString(R.string.dialog_loading_finish), true);
    }

    @Override
    public void showWarningDialog() {
        if (isFinishing()) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(mActivity);
        }
        promptDialog.dismissImmediately();
        promptDialog.showWarn(getResources().getString(R.string.dialog_warning), true);
    }

    @Override
    public void showErrorDialog() {
        if (isFinishing()) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(mActivity);
        }
        promptDialog.dismissImmediately();
        promptDialog.showError(getResources().getString(R.string.dialog_error), true);
    }


    @Override
    public void showPromptDialog() {
        if (isFinishing()) {
            return;
        }
        if (promptDialog == null) {
            promptDialog = new PromptDialog(mActivity);
        }
        promptDialog.dismissImmediately();
        promptDialog.showInfo(getResources().getString(R.string.dialog_prompt), true);
    }
}
