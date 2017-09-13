package com.myapp.activity;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.base.BaseAppCompatActivity;
import com.myapp.R;

/**
 * Created by admin on 2017/9/13.
 */

public class MediaActivity extends BaseAppCompatActivity implements View.OnClickListener,
        SoundPool.OnLoadCompleteListener {
    TextView tv;
    SoundPool sndPool;
    int sndid;
    int[] StreamID = new int[10];
    int mNum = 0;
    int mNumPause = 0;
    int[] rid = new int[]{R.raw.beep, R.raw.add, R.raw.acc, R.raw.abb, R.raw.add};

    private static final int SOUND_LOAD_OK = 1;
    private final Handler mHandler = new MyHandler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_media, R.layout.title_toolbar);
        setTitle("媒体播放");

        tv = (TextView) findViewById(R.id.textvbb100);
        tv.setText("1234567");
        Button b1 = (Button) findViewById(R.id.play100);
        b1.setOnClickListener(this);

        b1 = (Button) findViewById(R.id.more100);
        b1.setOnClickListener(this);

        b1 = (Button) findViewById(R.id.pause100);
        b1.setOnClickListener(this);
        sndPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 0);
        sndPool.setOnLoadCompleteListener(this);

    }

    @Override
    public void onDestroy() {
        sndPool.release();
        super.onDestroy();
    }

    private class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SOUND_LOAD_OK:
                    StreamID[mNum] = sndPool.play(msg.arg1, (float) 0.8, (float) 0.8, 16, 10, (float) 1.0);
                    mNum++;
                    if (mNum >= 4)
                        mNum = 0;
                    break;
            }
        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Message msg = mHandler.obtainMessage(SOUND_LOAD_OK);
        msg.arg1 = sampleId;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.play100:
                tv.setText("play");
                if (sndPool != null)
                    sndid = sndPool.load(this, rid[mNum], 1);
                break;
            case R.id.more100:
                tv.setText("more");
                sndPool.load(this, rid[mNum], 1);
                break;
            case R.id.pause100:
                tv.setText("pause");
                if (mNumPause < mNum) {
                    sndPool.pause(StreamID[mNumPause]);
                    mNumPause++;
                }
                break;
        }
    }
}
