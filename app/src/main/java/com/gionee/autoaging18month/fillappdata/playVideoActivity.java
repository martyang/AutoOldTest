package com.gionee.autoaging18month.fillappdata;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.gionee.autoaging18month.R;
import com.gionee.autoaging18month.Utils.Log;
import com.gionee.autoaging18month.fillappdata.Util.Configration;
import com.gionee.autoaging18month.fillappdata.Util.Preference;

import java.io.File;

import static com.gionee.autoaging18month.fillappdata.Util.Configration.isRunVideo;
import static com.gionee.autoaging18month.fillappdata.Util.Configration.isTest;
import static com.gionee.autoaging18month.fillappdata.Util.Configration.path;


public class playVideoActivity extends AppCompatActivity {
    private VideoView mPlayVideo;


    private File[] filelist;
    int i=0,t=0,res;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        Log.i("文件路径为:" + path);
        playVideo();
    }

    private void initView() {
        mPlayVideo = (VideoView) findViewById(R.id.playVideo);

    }


    private void playVideo() {
        if (isTest) {
            File mfile = new File(path);
            if (mfile.exists()) {
                Log.i("路径不存在进行新建");
                mfile.mkdir();
            }
            filelist=mfile.listFiles();
            res = Preference.getInt(this, "video", 0);
            Log.i("取到的视频循环次数为：" + res);
            mPlayVideo.setVideoPath(filelist[0].getPath());
            mPlayVideo.start();
            t=0;
            i = 0;
            Log.i("---------run---------------");
            mPlayVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    if(!isTest){
//                        stopPlay();
//                    }
                    mPlayVideo.setVideoPath(filelist[i].getPath());
                    mPlayVideo.start();
                    Log.i("-------finish--------" + i);
                    i = i + 1;
                    if (i >= filelist.length) {
                        i = 0;
                        t++;
                        if (t >= res) {
                            stopPlay();
                        }
                    }
                    Log.i("i:" + i);
                }
            });
        }
    }

    private void stopPlay() {
        Log.i("------stop-------");
        t = 0;
        mPlayVideo.stopPlayback();
        isRunVideo = false;
        Configration.isTest = false;
        finish();
        Log.i("------stop~-------");
    }
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定要退出播放?");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopPlay();
               startActivity(new Intent(playVideoActivity.this,AddDataActivity.class));
            }
        });
        dialog.setNegativeButton("取消", null);
        dialog.show();
    }
}
