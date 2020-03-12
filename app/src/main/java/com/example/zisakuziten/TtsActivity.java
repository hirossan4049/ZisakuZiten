package com.example.zisakuziten;

import android.app.Notification;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Locale;

import io.realm.Realm;

public class TtsActivity extends Fragment implements TextToSpeech.OnInitListener{
    Spinner speedspinner;
    Spinner titleLangspinner;
    Spinner contentLangspinner;

    TextView grouptitle;
    TextView zitentitle;
    TextView nowIndex;
    ProgressBar progressBar;

    boolean ttsStatus;
    boolean playTitle;

    TextToSpeech tts;
    //true play false pause;
    Boolean playpause;
    FloatingActionButton playpauseBtn;

    Realm realm;
    Group group;

    int groupIndex;
    int groupsize;
    Locale titleLocale;
    Locale contentLocale;

    @Override
    public View onCreateView(final  LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.activity_tts,container,false);
        //=^~^=
        speedspinner = view.findViewById(R.id.speedspinner);
        titleLangspinner = view.findViewById(R.id.titlespinner);
        contentLangspinner = view.findViewById(R.id.contentspinner);

        grouptitle = view.findViewById(R.id.grouptitle);
        zitentitle = view.findViewById(R.id.zitentitle);
        nowIndex = view.findViewById(R.id.nowIndex);
        progressBar = view.findViewById(R.id.progressBar);

        tts = new TextToSpeech(getContext(),this);
        ttsStatus = false;
        playTitle = false;
        realm = Realm.getDefaultInstance();

        Group cashe_group = realm.where(Group.class).equalTo("updateTime",getArguments().getString("updateTime")).findFirst();
        group = realm.copyFromRealm(cashe_group);

        grouptitle.setText(group.groupName);
        groupIndex = 0;
        groupsize = group.ziten_updT_List.size();
        playpause = false;

        progressBar.setMax(groupsize);

        titleLocale = Locale.JAPANESE;
        contentLocale = Locale.JAPANESE;

        notific();


        playpauseBtn = (FloatingActionButton) view.findViewById(R.id.playpauseBtm);
        playpauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playpause == true){
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null);
                    playpauseBtn.setImageDrawable(drawable);
                    playpause = false;
                    playGroup();

                }else if(playpause == false){
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null);
                    playpauseBtn.setImageDrawable(drawable);
                    playpause = true;
                    tts.stop();
                }
            }
        });

        //next and back
        view.findViewById(R.id.nextbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.stop();
//                groupIndex ++;
                playGroup();
            }
        });
        view.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.stop();
                groupIndex --;
                if (groupIndex <= 0){
                    groupIndex = groupsize;
                }
                playGroup();
            }
        });


        speedspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                Log.d("onItemSelected",position+"");
                switch (position){
                    case 0:
                        //1.0
                        setSpeechRate(1.0f);
                        break;
                    case 1:
                        //0.5
                        setSpeechRate(0.5f);
                        break;
                    case 2:
                        //2.0
                        setSpeechRate(2.0f);
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //titleSpinnnenrenrnernernerne
        titleLangspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        titleLocale = Locale.JAPANESE;
                        break;
                    case 1:
                        titleLocale = Locale.ENGLISH;
                        break;
                    case 2:
                        titleLocale = Locale.CHINESE;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //contentSpinnner
        contentLangspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position){
                    case 0:
                        contentLocale = Locale.JAPANESE;
                        break;
                    case 1:
                        contentLocale = Locale.ENGLISH;
                        break;
                    case 2:
                        contentLocale = Locale.CHINESE;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }

    @Override
    public void onInit(int status) {
        if(TextToSpeech.SUCCESS == status){
            Locale locale = Locale.getDefault();
            if(tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE){
                tts.setLanguage(locale);

            }else{
                Log.e("","Error SetLocale");
            }
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    ttsStatus = true;
                    Log.i("TextToSpeech","On Start");
                }

                @Override
                public void onDone(String utteranceId) {
                    ttsStatus = false;
                    playGroup();
                    Log.i("TextToSpeech","On どーん、どーん");
                }

                @Override
                public void onError(String utteranceId) {
                    Log.i("TextToSpeech","On Error");
                }
            });


        }else{
            Log.e("","Error Init");
        }
        playGroup();
    }


    public void playGroup(){
        Log.d("groupIndex", String.valueOf(groupIndex));
        if(groupIndex >= groupsize){
            groupIndex = 0;
        }
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        // メインスレッド以外でUI変更したらだめらしい（）
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                nowIndex.setText(groupIndex + 1 + "/" + groupsize);
                progressBar.setProgress(groupIndex + 1);
            }
        });

        Log.d("groupIndex", String.valueOf(groupIndex));
        Ziten ziten = group.ziten_updT_List.get(groupIndex);

        if (playTitle){
            speakText(ziten.content,titleLocale);
            groupIndex ++;
            playTitle = false;
        }else {
            speakText(ziten.title, contentLocale);
            playTitle = true;
        }
//        speakText(String.valueOf(groupIndex),titleLocale);
//        ttswait();
//        speakText(ziten.content,contentLocale);
//        ttswait();

    }

    public void speakText(final String text, Locale locale){
        tts.setLanguage(locale);
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        // メインスレッド以外でUI変更したらだめらしい（）
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                zitentitle.setText(text);
            }
        });

        Log.d("speakText",text);
        if(Build.VERSION.SDK_INT >= 21){
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,"messageID");
        }
        else{
            HashMap<String,String> map = new HashMap<String, String>();
            map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,"messageID");
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,map);
        }
    }

//    public synchronized int ttswait() {
//        while (ttsStatus) {
//            try {
//                wait(100);          // プロデューサからの notify()呼び出しを待つ
//            } catch (InterruptedException e) {
//            }
//        }
//        notify();
//        return 0;
//    }

    //speak speed
    private void setSpeechRate(float rate){
        if(null != tts){
            tts.setSpeechRate(rate);
        }
    }


    public void notific(){
        // Get the layouts to use in the custom notification
        RemoteViews notificationLayout = new RemoteViews(getContext().getPackageName(), R.layout.notification_small);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getContext().getPackageName(), R.layout.notification_small);

        notificationLayout.setTextViewText(R.id.notification_title,"たいとる");
        notificationLayoutExpanded.setTextViewText(R.id.notification_title,"たいとる");

        // Apply the layouts to the notification
        Notification customNotification = new NotificationCompat.Builder(getContext(), "1")
                .setSmallIcon(R.drawable.ic_play)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutExpanded)
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.notify(1, customNotification);
    }
}
