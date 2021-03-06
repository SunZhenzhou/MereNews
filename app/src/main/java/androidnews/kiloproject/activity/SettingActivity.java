package androidnews.kiloproject.activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import androidnews.kiloproject.R;
import androidnews.kiloproject.entity.data.BlockItem;
import androidnews.kiloproject.entity.data.CacheNews;
import androidnews.kiloproject.entity.data.ExportBean;
import androidnews.kiloproject.system.AppConfig;
import androidnews.kiloproject.system.base.BaseActivity;
import androidnews.kiloproject.util.AlipayUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static androidnews.kiloproject.system.AppConfig.BUGLY_KEY;
import static androidnews.kiloproject.system.AppConfig.CHECK_UPADTE_ADDRESS;
import static androidnews.kiloproject.system.AppConfig.CONFIG_AUTO_LOADMORE;
import static androidnews.kiloproject.system.AppConfig.CONFIG_AUTO_REFRESH;
import static androidnews.kiloproject.system.AppConfig.CONFIG_BACK_EXIT;
import static androidnews.kiloproject.system.AppConfig.CONFIG_HEADER_COLOR;
import static androidnews.kiloproject.system.AppConfig.CONFIG_HIGH_RAM;
import static androidnews.kiloproject.system.AppConfig.CONFIG_LIST_TYPE;
import static androidnews.kiloproject.system.AppConfig.CONFIG_LANGUAGE;
import static androidnews.kiloproject.system.AppConfig.CONFIG_NIGHT_MODE;
import static androidnews.kiloproject.system.AppConfig.CONFIG_DISABLE_NOTICE;
import static androidnews.kiloproject.system.AppConfig.CONFIG_RANDOM_HEADER;
import static androidnews.kiloproject.system.AppConfig.CONFIG_STATUS_BAR;
import static androidnews.kiloproject.system.AppConfig.CONFIG_SWIPE_BACK;
import static androidnews.kiloproject.system.AppConfig.CONFIG_TEXT_SIZE;
import static androidnews.kiloproject.system.AppConfig.CONFIG_TYPE_ARRAY;
import static androidnews.kiloproject.system.AppConfig.DOWNLOAD_ADDRESS;
import static androidnews.kiloproject.system.AppConfig.LIST_TYPE_SINGLE;
import static com.blankj.utilcode.util.AppUtils.relaunchApp;

public class SettingActivity extends BaseActivity {

    Toolbar toolbar;
    TextView tvLanguage;
    TextView tvLanguageDetail;
    TextView tvClearCache;
    TextView tvClearCacheDetail;
    TextView tvTextSize;
    TextView tvTextSizeDetail;
    TextView tvAutoRefresh;
    TextView tvAutoRefreshDetail;
    Switch swAutoRefresh;
    TextView tvAutoLoadmore;
    TextView tvAutoLoadmoreDetail;
    Switch swAutoLoadmore;
    TextView tvBackExit;
    TextView tvBackExitDetail;
    Switch swBackExit;
    TextView tvSwipeBack;
    TextView tvSwipeBackDetail;
    Switch swSwipeBack;
    TextView tvHighRam;
    TextView tvHighRamDetail;
    Switch swHighRam;
    TextView tvDisNotice;
    TextView tvDisNoticeDetail;
    Switch swDisNotice;
    TextView tvStatusBar;
    TextView tvStatusBarDetail;
    Switch swStatusBar;
    TextView tvInput;
    TextView tvInputDetail;
    TextView tvExport;
    TextView tvExportDetail;
    TextView tvCheckUpdate;
    TextView tvCheckUpdateDetail;
    TextView tvRandomHeader;
    TextView tvRandomHeaderDetail;
    TextView tvListType;
    TextView tvListTypeDetail;

    private int currentLanguage;
    private int currentRandomHeader;
    SPUtils spUtils;

    String[] languageItems;
    String[] headerItems;
    String[] listItems;
    String[] sizeItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvLanguage = (TextView) findViewById(R.id.tv_language);
        tvLanguageDetail = (TextView) findViewById(R.id.tv_language_detail);

        tvClearCache = (TextView) findViewById(R.id.tv_clear_cache);
        tvClearCacheDetail = (TextView) findViewById(R.id.tv_clear_cache_detail);

        tvTextSize = (TextView) findViewById(R.id.tv_text_size);
        tvTextSizeDetail = (TextView) findViewById(R.id.tv_text_size_detail)
        ;
        tvAutoRefresh = (TextView) findViewById(R.id.tv_auto_refresh);
        tvAutoRefreshDetail = (TextView) findViewById(R.id.tv_auto_refresh_detail);
        swAutoRefresh = (Switch) findViewById(R.id.sw_auto_refresh);

        tvAutoLoadmore = (TextView) findViewById(R.id.tv_auto_loadmore);
        tvAutoLoadmoreDetail = (TextView) findViewById(R.id.tv_auto_loadmore_detail);
        swAutoLoadmore = (Switch) findViewById(R.id.sw_auto_loadmore);

        tvBackExit = (TextView) findViewById(R.id.tv_back_exit);
        tvBackExitDetail = (TextView) findViewById(R.id.tv_back_exit_detail);
        swBackExit = (Switch) findViewById(R.id.sw_back_exit);

        tvSwipeBack = (TextView) findViewById(R.id.tv_swipe_back);
        tvSwipeBackDetail = (TextView) findViewById(R.id.tv_swipe_back_detail);
        swSwipeBack = (Switch) findViewById(R.id.sw_swipe_back);

        tvHighRam = (TextView) findViewById(R.id.tv_high_ram);
        tvHighRamDetail = (TextView) findViewById(R.id.tv_high_ram_detail);
        swHighRam = (Switch) findViewById(R.id.sw_high_ram);

        tvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        tvStatusBarDetail = (TextView) findViewById(R.id.tv_status_bar_detail);
        swStatusBar = (Switch) findViewById(R.id.sw_status_bar);

        tvDisNotice = (TextView) findViewById(R.id.tv_dis_notice);
        tvDisNoticeDetail = (TextView) findViewById(R.id.tv_dis_notice_detail);
        swDisNotice = (Switch) findViewById(R.id.sw_dis_notice);

        tvInput = (TextView) findViewById(R.id.tv_input);
        tvInputDetail = (TextView) findViewById(R.id.tv_input_detail);

        tvExport = (TextView) findViewById(R.id.tv_export);
        tvExportDetail = (TextView) findViewById(R.id.tv_export_detail);

        tvCheckUpdate = (TextView) findViewById(R.id.tv_check_update);
        tvCheckUpdateDetail = (TextView) findViewById(R.id.tv_check_update_detail);

        tvRandomHeader = (TextView) findViewById(R.id.tv_random_header);
        tvRandomHeaderDetail = (TextView) findViewById(R.id.tv_random_header_detail);

        tvListType = (TextView) findViewById(R.id.tv_list_type);
        tvListTypeDetail = (TextView) findViewById(R.id.tv_list_type_detail);

        initToolbar(toolbar, true);
        getSupportActionBar().setTitle(R.string.setting);
        initStatusBar(R.color.main_background, true);
    }

    @Override
    protected void initSlowly() {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                spUtils = SPUtils.getInstance();

                headerItems = getResources().getStringArray(R.array.header_setting);
                listItems = getResources().getStringArray(R.array.list_type);
                languageItems = getResources().getStringArray(R.array.language);
                sizeItems = getResources().getStringArray(R.array.size);

                AppConfig.mTextSize = spUtils.getInt(CONFIG_TEXT_SIZE, 1);

                currentRandomHeader = spUtils.getInt(CONFIG_RANDOM_HEADER, 0);

                currentLanguage = spUtils.getInt(CONFIG_LANGUAGE, 0);

                AppConfig.isSwipeBack = spUtils.getBoolean(CONFIG_SWIPE_BACK);

                e.onNext(true);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        try {
                            tvRandomHeaderDetail.setText(headerItems[currentRandomHeader]);
                            tvListTypeDetail.setText(listItems[spUtils.getInt(CONFIG_LIST_TYPE, LIST_TYPE_SINGLE)]);
                            tvTextSizeDetail.setText(sizeItems[AppConfig.mTextSize]);
                            tvLanguageDetail.setText(languageItems[currentLanguage]);

                            swAutoRefresh.setChecked(AppConfig.isAutoRefresh);
                            swAutoRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    AppConfig.isAutoRefresh = isChecked;
                                    spUtils.put(CONFIG_AUTO_REFRESH, AppConfig.isAutoRefresh);
                                }
                            });

                            swAutoLoadmore.setChecked(AppConfig.isAutoLoadMore);
                            swAutoLoadmore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    AppConfig.isAutoLoadMore = isChecked;
                                    spUtils.put(CONFIG_AUTO_LOADMORE, AppConfig.isAutoLoadMore);
                                    setResult(RESULT_OK);
                                    SnackbarUtils.with(toolbar).setMessage(getString(R.string.start_after_restart_list)).show();
                                }
                            });

                            swBackExit.setChecked(AppConfig.isBackExit);
                            swBackExit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    AppConfig.isBackExit = isChecked;
                                    spUtils.put(CONFIG_BACK_EXIT, AppConfig.isBackExit);
                                }
                            });

                            swSwipeBack.setChecked(AppConfig.isSwipeBack);
                            swSwipeBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    AppConfig.isSwipeBack = isChecked;
                                    spUtils.put(CONFIG_SWIPE_BACK, AppConfig.isSwipeBack);
                                    SnackbarUtils.with(toolbar).setMessage(getString(R.string.start_after_exit)).show();
                                }
                            });

                            swHighRam.setChecked(AppConfig.isHighRam);
                            swHighRam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    AppConfig.isHighRam = isChecked;
                                    spUtils.put(CONFIG_HIGH_RAM, AppConfig.isHighRam);
                                    SnackbarUtils.with(toolbar).setMessage(getString(R.string.start_after_exit)).show();
                                }
                            });

                            swDisNotice.setChecked(AppConfig.isDisNotice);
                            swDisNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    AppConfig.isDisNotice = isChecked;
                                    spUtils.put(CONFIG_DISABLE_NOTICE, AppConfig.isDisNotice);
                                    SnackbarUtils.with(toolbar).setMessage(getString(R.string.successful)).show();
                                }
                            });

                            swStatusBar.setChecked(AppConfig.isStatusBar);
                            swStatusBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    AppConfig.isStatusBar = isChecked;
                                    spUtils.put(CONFIG_STATUS_BAR, AppConfig.isStatusBar);
                                    SnackbarUtils.with(toolbar).setMessage(getString(R.string.start_after_restart_app)).show();
                                }
                            });

                            tvCheckUpdateDetail.setText(getString(R.string.check_update_detail) + AppUtils.getAppVersionName());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_language:
            case R.id.tv_language_detail:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.language)
                        .setCancelable(true)
                        .setSingleChoiceItems(languageItems,
                                currentLanguage, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SnackbarUtils.with(toolbar).setMessage(getString(R.string.loading)).show();
                                        currentLanguage = which;
                                        spUtils.put(CONFIG_LANGUAGE, currentLanguage);
                                        tvLanguageDetail.setText(languageItems[currentLanguage]);
                                        restartWithAnime(R.id.root_view,R.id.content);
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create().show();
                break;

            case R.id.tv_text_size:
            case R.id.tv_text_size_detail:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.text_size)
                        .setCancelable(true)
                        .setSingleChoiceItems(getResources().getStringArray(R.array.size),
                                AppConfig.mTextSize, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AppConfig.mTextSize = which;
                                        spUtils.put(CONFIG_TEXT_SIZE, which);
                                        tvTextSizeDetail.setText(sizeItems[which]);
                                        SnackbarUtils.with(toolbar)
                                                .setMessage(getResources().getString(R.string.successful))
                                                .show();
                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create().show();
                break;

            case R.id.tv_auto_refresh:
            case R.id.tv_auto_refresh_detail:
                if (AppConfig.isAutoRefresh) {
                    swAutoRefresh.setChecked(false);
                    AppConfig.isAutoRefresh = false;
                } else {
                    swAutoRefresh.setChecked(true);
                    AppConfig.isAutoRefresh = true;
                }
                spUtils.put(CONFIG_AUTO_REFRESH, AppConfig.isAutoRefresh);
                break;

            case R.id.tv_auto_loadmore:
            case R.id.tv_auto_loadmore_detail:
                if (AppConfig.isAutoLoadMore) {
                    swAutoLoadmore.setChecked(false);
                    AppConfig.isAutoLoadMore = false;
                } else {
                    swAutoLoadmore.setChecked(true);
                    AppConfig.isAutoLoadMore = true;
                }
                spUtils.put(CONFIG_AUTO_LOADMORE, AppConfig.isAutoLoadMore);
                setResult(RESULT_OK);
                SnackbarUtils.with(toolbar).setMessage(getString(R.string.start_after_restart_list)).show();
                break;

            case R.id.tv_back_exit:
            case R.id.tv_back_exit_detail:
                if (AppConfig.isBackExit) {
                    swBackExit.setChecked(false);
                    AppConfig.isBackExit = false;
                } else {
                    swBackExit.setChecked(true);
                    AppConfig.isBackExit = true;
                }
                spUtils.put(CONFIG_BACK_EXIT, AppConfig.isBackExit);
                break;

            case R.id.tv_random_header:
            case R.id.tv_random_header_detail:
                new AlertDialog.Builder(mActivity)
                        .setTitle(R.string.random_header_server)
                        .setCancelable(true)
                        .setSingleChoiceItems(
                                headerItems, currentRandomHeader, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spUtils.put(CONFIG_RANDOM_HEADER, which);
                                        tvRandomHeaderDetail.setText(headerItems[which]);
                                        currentRandomHeader = which;
                                        if (which > 3)
                                            showColorPicker();
                                        else
                                            SnackbarUtils.with(toolbar)
                                                    .setMessage(getString(R.string.start_after_restart_app))
                                                    .show();
                                        dialog.dismiss();
                                    }
                                }
                        ).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;

            case R.id.tv_list_type:
            case R.id.tv_list_type_detail:
                new AlertDialog.Builder(mActivity)
                        .setTitle(R.string.list_type)
                        .setCancelable(true)
                        .setSingleChoiceItems(listItems, AppConfig.listType, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        spUtils.put(CONFIG_LIST_TYPE, which);
                                        tvListTypeDetail.setText(listItems[which]);
                                        AppConfig.listType = which;
                                        SnackbarUtils.with(toolbar)
                                                .setMessage(getString(R.string.start_after_restart_app))
                                                .show();
                                        dialog.dismiss();
                                    }
                                }
                        ).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;

            case R.id.tv_swipe_back:
            case R.id.tv_swipe_back_detail:
                if (AppConfig.isSwipeBack) {
                    swSwipeBack.setChecked(false);
                    AppConfig.isSwipeBack = false;
                } else {
                    swSwipeBack.setChecked(true);
                    AppConfig.isSwipeBack = true;
                }
                spUtils.put(CONFIG_SWIPE_BACK, AppConfig.isSwipeBack);
                SnackbarUtils.with(toolbar).setMessage(getString(R.string.start_after_exit)).show();
                break;

            case R.id.tv_high_ram:
            case R.id.tv_high_ram_detail:
                if (AppConfig.isHighRam) {
                    swHighRam.setChecked(false);
                    AppConfig.isHighRam = false;
                } else {
                    swHighRam.setChecked(true);
                    AppConfig.isHighRam = true;
                }
                spUtils.put(CONFIG_HIGH_RAM, AppConfig.isHighRam);
                SnackbarUtils.with(toolbar).setMessage(getString(R.string.start_after_exit)).show();
                break;

            case R.id.tv_dis_notice:
            case R.id.tv_dis_notice_detail:
                if (AppConfig.isDisNotice) {
                    swDisNotice.setChecked(false);
                    AppConfig.isDisNotice = false;
                } else {
                    swDisNotice.setChecked(true);
                    AppConfig.isDisNotice = true;
                }
                spUtils.put(CONFIG_DISABLE_NOTICE, AppConfig.isDisNotice);
                SnackbarUtils.with(toolbar).setMessage(getString(R.string.successful)).show();
                break;

            case R.id.tv_status_bar:
            case R.id.tv_status_bar_detail:
                if (AppConfig.isStatusBar) {
                    swStatusBar.setChecked(false);
                    AppConfig.isStatusBar = false;
                } else {
                    swStatusBar.setChecked(true);
                    AppConfig.isStatusBar = true;
                }
                spUtils.put(CONFIG_STATUS_BAR, AppConfig.isStatusBar);
                SnackbarUtils.with(toolbar).setMessage(getString(R.string.start_after_restart_app)).show();
                break;

            case R.id.tv_input:
            case R.id.tv_input_detail:
                final EditText editText = new EditText(mActivity);
                editText.setHint(R.string.input_backup_detail);
                editText.setTextColor(getResources().getColor(R.color.black));
                new MaterialStyledDialog.Builder(mActivity)
                        .setHeaderDrawable(R.drawable.ic_edit)
                        .setHeaderScaleType(ImageView.ScaleType.CENTER)
                        .setTitle(getResources().getString(R.string.input_backup))
                        .setDescription(getResources().getString(R.string.input_message))
                        .setHeaderColor(R.color.colorPrimary)
                        .setCustomView(editText)
                        .setPositiveText(android.R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final String str = editText.getText().toString().trim();
                                if (TextUtils.isEmpty(str)) {
                                    SnackbarUtils.with(toolbar).setMessage(getString(R.string.empty_content)).show();
                                } else {
                                    SnackbarUtils.with(toolbar).setMessage(getString(R.string.loading)).show();
                                    Observable.create(new ObservableOnSubscribe<Boolean>() {
                                        @Override
                                        public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                                            try {
                                                ExportBean exportBean = gson.fromJson(str, ExportBean.class);
                                                if (exportBean != null) {
                                                    spUtils.put(CONFIG_NIGHT_MODE, exportBean.isNightMode);
                                                    spUtils.put(CONFIG_HIGH_RAM, exportBean.isHighRam);
                                                    spUtils.put(CONFIG_SWIPE_BACK, exportBean.isSwipeBack);
                                                    spUtils.put(CONFIG_AUTO_REFRESH, exportBean.isAutoRefresh);
                                                    spUtils.put(CONFIG_AUTO_LOADMORE, exportBean.isAutoLoadMore);
                                                    spUtils.put(CONFIG_DISABLE_NOTICE, exportBean.isDisNotice);
                                                    spUtils.put(CONFIG_STATUS_BAR, exportBean.isStatusBar);
                                                    spUtils.put(CONFIG_BACK_EXIT, exportBean.isBackExit);
                                                    spUtils.put(CONFIG_LIST_TYPE, exportBean.listType);
                                                    spUtils.put(CONFIG_RANDOM_HEADER, exportBean.currentRandomHeader);
                                                    spUtils.put(CONFIG_LANGUAGE, exportBean.currentLanguage);
                                                    spUtils.put(CONFIG_TEXT_SIZE, exportBean.mTextSize);
                                                    spUtils.put(CONFIG_TYPE_ARRAY, exportBean.arrayStr);
                                                    LitePal.saveAll(exportBean.blockList);
                                                    e.onNext(true);
                                                } else
                                                    e.onNext(false);
                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                                e.onNext(false);
                                            }
                                            e.onComplete();
                                        }
                                    }).subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<Boolean>() {
                                                @Override
                                                public void accept(Boolean o) throws Exception {
                                                    try {
                                                        if (o) {
                                                            SnackbarUtils.with(toolbar).setMessage(getString(R.string.successful)).show();
                                                            restartWithAnime(R.id.root_view,R.id.content);
                                                        }else
                                                            SnackbarUtils.with(toolbar).setMessage(getString(R.string.fail)).show();
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            }
                        })
                        .setNegativeText(getResources().getString(android.R.string.cancel))
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            }
                        })
                        .show();
                break;

            case R.id.tv_export:
            case R.id.tv_export_detail:
                new MaterialStyledDialog.Builder(mActivity)
                        .setHeaderDrawable(R.drawable.ic_restore)
                        .setHeaderScaleType(ImageView.ScaleType.CENTER)
                        .setTitle(getResources().getString(R.string.export_backup))
                        .setDescription(getResources().getString(R.string.export_message))
                        .setHeaderColor(R.color.colorPrimary)
                        .setPositiveText(android.R.string.ok)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                SnackbarUtils.with(toolbar).setMessage(getString(R.string.loading)).show();
                                Observable.create(new ObservableOnSubscribe<String>() {
                                    @Override
                                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                                        List<BlockItem> blockList = new ArrayList<>();
                                        try {
                                            blockList = LitePal.findAll(BlockItem.class);
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                        try {
                                            ExportBean exportBean = new ExportBean();
                                            exportBean.arrayStr = spUtils.getString(CONFIG_TYPE_ARRAY);
                                            exportBean.isNightMode = AppConfig.isNightMode;
                                            exportBean.isHighRam = AppConfig.isHighRam;
                                            exportBean.isSwipeBack = AppConfig.isSwipeBack;
                                            exportBean.isAutoRefresh  = AppConfig.isAutoRefresh;
                                            exportBean.isDisNotice = AppConfig.isDisNotice;
                                            exportBean.isStatusBar = AppConfig.isStatusBar;
                                            exportBean.isAutoLoadMore  = AppConfig.isAutoLoadMore;
                                            exportBean.isBackExit  = AppConfig.isBackExit;
                                            exportBean.listType  = AppConfig.listType;
                                            exportBean.mTextSize  = AppConfig.mTextSize;
                                            exportBean.currentRandomHeader  = currentRandomHeader;
                                            exportBean.currentLanguage = currentLanguage;
                                            exportBean.blockList = blockList;

                                            String json = gson.toJson(exportBean);
                                            e.onNext(json);
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            e.onNext("");
                                        }
                                        e.onComplete();
                                    }
                                }).subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<String>() {
                                            @Override
                                            public void accept(String str) throws Exception {
                                                try {
                                                    if (!TextUtils.isEmpty(str)) {
                                                        ClipboardManager cm = (ClipboardManager) Utils.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
                                                        //noinspection ConstantConditions
                                                        cm.setPrimaryClip(ClipData.newPlainText("json", str));
                                                        SnackbarUtils.with(toolbar).setMessage(getString(R.string.successful)).show();
                                                    }else
                                                        SnackbarUtils.with(toolbar).setMessage(getString(R.string.fail)).show();
                                                } catch (Exception e1) {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeText(getResources().getString(android.R.string.cancel))
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                break;

            case R.id.tv_clear_cache:
            case R.id.tv_clear_cache_detail:
                new AlertDialog.Builder(mActivity)
                        .setTitle(R.string.clear_cache)
                        .setCancelable(true)
                        .setSingleChoiceItems(
                                getResources().getStringArray(R.array.delete_cache),0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        LitePal.deleteAll(CacheNews.class);
                                                        String[] address = getResources().getStringArray(R.array.address);
                                                        Glide.get(mActivity).clearDiskCache();
                                                        for (String typeStr : address) {
                                                            SPUtils.getInstance().put(typeStr + "_data", "");
                                                        }
                                                    }
                                                }).start();
                                                break;
                                            case 1:
                                                LitePal.deleteAll(CacheNews.class, "type = ?", ""+CacheNews.CACHE_HISTORY);
                                                break;
                                            case 2:
                                                LitePal.deleteAll(CacheNews.class, "type = ?", ""+CacheNews.CACHE_COLLECTION);
                                                break;
                                            case 3:
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        String[] address = getResources().getStringArray(R.array.address);
                                                        Glide.get(mActivity).clearDiskCache();
                                                        for (String typeStr : address) {
                                                            SPUtils.getInstance().put(typeStr + "_data", "");
                                                        }
                                                    }
                                                }).start();
                                                break;
                                        }
                                        SnackbarUtils.with(toolbar).setMessage(getString(R.string.successful)).show();
                                        dialog.dismiss();
                                    }
                                }
                        ).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;

            case R.id.tv_check_update:
            case R.id.tv_check_update_detail:
                SnackbarUtils.with(toolbar).setMessage(getResources()
                        .getString(R.string.loading)).show();
                EasyHttp.get(CHECK_UPADTE_ADDRESS)
                        .readTimeOut(30 * 1000)//局部定义读超时
                        .writeTimeOut(30 * 1000)
                        .connectTimeout(30 * 1000)
                        .timeStamp(true)
                        .execute(new SimpleCallBack<String>() {
                            @Override
                            public void onError(ApiException e) {
                                SnackbarUtils.with(toolbar)
                                        .setMessage(getString(R.string.load_fail) + e.getMessage())
                                        .showError();
                            }

                            @Override
                            public void onSuccess(String response) {
                                if (response != null) {
                                    int newVersionCode;
                                    try {
                                        newVersionCode = Integer.parseInt(response.trim());
                                        if (AppUtils.getAppVersionCode() < newVersionCode) {
                                            new MaterialStyledDialog.Builder(mActivity)
                                                    .setHeaderDrawable(R.drawable.ic_warning)
                                                    .setHeaderScaleType(ImageView.ScaleType.CENTER)
                                                    .setTitle(getResources().getString(R.string.update_title))
                                                    .setDescription(getResources().getString(R.string.update_message))
                                                    .setHeaderColor(R.color.colorPrimary)
                                                    .setPositiveText(android.R.string.ok)
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            Uri uri = Uri.parse(DOWNLOAD_ADDRESS);
                                                            startActivity(new Intent(Intent.ACTION_VIEW, uri));
                                                        }
                                                    })
                                                    .setNegativeText(getResources().getString(android.R.string.cancel))
                                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        } else {
                                            SnackbarUtils.with(toolbar)
                                                    .setMessage(getString(R.string.update_title_no))
                                                    .show();
                                        }
                                    } catch (Exception e) {
                                        SnackbarUtils.with(toolbar)
                                                .setMessage(getString(R.string.load_fail) + e.getMessage())
                                                .showError();
                                    }
                                } else {
                                    SnackbarUtils.with(toolbar)
                                            .setMessage(getString(R.string.load_fail))
                                            .showError();
                                }
                            }
                        });
                break;
            case R.id.tv_join_us:
            case R.id.tv_join_us_detail:
                Intent intent = new Intent();
                intent.setData(Uri.parse(BUGLY_KEY));
                // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    // 未安装手Q或安装的版本不支持
                    SnackbarUtils.with(toolbar)
                            .setMessage(getString(R.string.join_us_error))
                            .showError();
                }
            case R.id.tv_donate:
            case R.id.tv_donate_detail:
                AlipayUtil.startAlipayClient(mActivity, AlipayUtil.PAY_ID);
                break;
        }
    }

    private void showColorPicker() {
        int color = spUtils.getInt(CONFIG_HEADER_COLOR, 9999);
        if (color == 9999)
            color = Color.parseColor("#FFA000");
        ColorPickerDialogBuilder
                .with(mActivity)
                .setTitle(R.string.color_choose)
                .initialColor(color)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
//                        ToastUtils.showShort("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton(android.R.string.ok, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        spUtils.put(CONFIG_HEADER_COLOR, selectedColor);
                        SnackbarUtils.with(toolbar)
                                .setMessage(getString(R.string.start_after_restart_app))
                                .show();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}
