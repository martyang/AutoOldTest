package com.gionee.autoaging18month.slidetest;

import android.accessibilityservice.AccessibilityService;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.gionee.autoaging18month.Utils.Instrument;
import com.gionee.autoaging18month.Utils.Log;

import gionee.autotest.UI;
import gionee.autotest.UINotFoundException;
import gionee.autotest.UIO;


public class MyAccessibilityService extends AccessibilityService {
    String[] content = {"继续", "确认", "同意", "允许", "开始体验", "跳过", "关闭", "去听歌", "知道了"};
    String[] readID = {"com.chaozh.iReaderGionee:id/iv_female","com.chaozh.iReaderFree:id/user_gift_close" ,"com.chaozh.iReaderGionee:id/select_whiteskin"
            , "com.chaozh.iReaderGionee:id/iv_network", "com.chaozh.iReaderGionee:id/select_defaultskin"
            , "com.chaozh.iReaderGionee:id/bt_start","com.chaozh.iReaderFree:id/iv_item_view"};
    private float width;
    private float height;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        this.processEvent(event);
    }

    private void processEvent(AccessibilityEvent event) {
        if (event.getSource() == null || !Configurator.isTest) {
            return;
        }
        try {
            closeLimitDialog(event);
            switch (SlideService.currentPkgName) {
                case "com.ss.android.article.news":
                    closeArticleNewDialog(event);
                    break;
                case "com.tencent.news":
                    closeTencentNewDialog(event);
                    break;
                case "com.chaozh.iReaderGionee":
                    closeReaderDialog(event);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.i(e.toString());
        }
    }

    private void closeArticleNewDialog(AccessibilityEvent event) {
        UIO confirm = UI.findByText(event, -1, "确定");
        if (confirm.exist()) {
            try {
                confirm.click();
            } catch (UINotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeTencentNewDialog(AccessibilityEvent event) {
        UIO guide = UI.findByID(event, "com.tencent.news:id/guide_image");
        if (guide.exist()) {
            new Thread() {
                @Override
                public void run() {
                    Instrument.click(550 * width / 1080, 1600 * height / 1920);
                    SystemClock.sleep(1000);
                    Instrument.swipe(Instrument.Direction.LEFT, (int) width, (int) height, 10);
                }
            }.start();
        }
    }

    private void closeLimitDialog(AccessibilityEvent event) {
        UIO confirm = UI.findByText(event, -1, content);
        if (confirm.exist()) {
            try {
                confirm.click();
            } catch (Exception e) {
                Log.i(e.toString());
            }
        }
        UIO allow = UI.findByText(event, -1, "允许");
        if (allow.exist()) {
            try {
                allow.click();
            } catch (UINotFoundException e) {
                e.printStackTrace();
            }
        }
        UIO allow1 = UI.findByID(event, -1, "com.android.packageinstaller:id/permission_allow_button");
        if (allow1.exist()) {
            try {
                allow1.click();
            } catch (UINotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeReaderDialog(AccessibilityEvent event) {
        UIO female = UI.findByID(event, readID);
        if (female.exist()) {
            try {
                female.click();
            } catch (Exception e) {
                Log.i(e.toString());
            }
        }
        UIO goodIdea = UI.findByID(event, "com.chaozh.iReaderGionee:id/id_splash_view");
        if (goodIdea.exist()) {
            new Thread() {
                @Override
                public void run() {
                    Instrument.click((int) (550f * width / 1080f), (int) (1800f * height / 1920f));
                }
            }.start();
            new Thread() {
                @Override
                public void run() {
                    Instrument.click((int) (1000f * width / 1080f), (int) (150f * height / 1920f));
                }
            }.start();
            try {
                goodIdea.click();
            } catch (Exception e) {
                Log.i(e.toString());
            }
        }
        UIO gotoAlbum = UI.findByID(event, "com.chaozh.iReaderGionee:id/select_whiteskin");
        if (gotoAlbum.exist()) {
            new Thread() {
                @Override
                public void run() {
                    Instrument.click((int) (1050f * width / 1450f), (int) (1360f * height / 2560f));
                }
            }.start();
            try {
                gotoAlbum.click();
            } catch (Exception e) {
                Log.i(e.toString());
            }
        }
        UIO book = UI.findByID(event, "com.chaozh.iReaderGionee:id/iv_item_view");
        if (book.exist()) {
            try {
                book.click();
            } catch (Exception e) {
                Log.i(e.toString());
            }
        }
        if (UI.findByID(event, "com.chaozh.iReaderGionee:id/id_splash_view").exist()) {
            new Thread() {
                @Override
                public void run() {
                    Instrument.click((int) (970f * width / 1080f), (int) (151f * height / 1920f));
                }
            }.start();
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return true;

    }

    @Override
    public void onInterrupt() {
        Log.i("onInterrupt");
    }


}
