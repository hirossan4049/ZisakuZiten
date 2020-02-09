package com.example.zisakuziten;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;
import java.util.Locale;

import io.realm.Realm;

public class TtsActivity extends Fragment implements TextToSpeech.OnInitListener{
    Spinner speedspinner;
    Spinner titleLangspinner;
    Spinner contentLangspinner;

    TextView grouptitle;
    TextView zitentitle;

    boolean ttsStatus;

    TextToSpeech tts;
    //true play false pause;
    Boolean playpause;
    FloatingActionButton playpauseBtn;

    Realm realm;
    Group group;
    int groupIndex;
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

        tts = new TextToSpeech(getContext(),this);
        ttsStatus = false;
        realm = Realm.getDefaultInstance();

        group = realm.where(Group.class).equalTo("updateTime",getArguments().getString("updateTime")).findFirst();
        grouptitle.setText(group.groupName);
        groupIndex = 0;
        playpause = false;

        titleLocale = Locale.JAPANESE;
        contentLocale = Locale.JAPANESE;

        playpauseBtn = (FloatingActionButton) view.findViewById(R.id.playpauseBtm);
        playpauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playpause == true){
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null);
                    playpauseBtn.setImageDrawable(drawable);
                    playpause = false;
                }else if(playpause == false){
                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null);
                    playpauseBtn.setImageDrawable(drawable);
                    playpause = true;
                }
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
                    Log.i("TextToSpeech","On Done");
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
        playGroup();
        playGroup();
        playGroup();
        playGroup();
    }

    public void playGroup(){
        if(groupIndex >= group.ziten_updT_List.size()){
            groupIndex = 0;
        }
        Log.d("groupIndex", String.valueOf(groupIndex));
        Ziten ziten = group.ziten_updT_List.get(groupIndex);
        Log.d("while","while ↓");
//        while(true){
//            if(ttsStatus){
////                Log.d("while","isspeaking");
//
//            }else {
//                zitentitle.setText(ziten.title);
//                Log.d("while","is not speaking");
//                speakText(ziten.title,titleLocale);
////                speakText(ziten.content,contentLocale);
//                break;
//            }
//        }
//        groupIndex ++;

        Log.d("while","is not speaking");
        speakText(ziten.title,titleLocale);
        ttswait();
        speakText(ziten.content,contentLocale);
        groupIndex ++;
        ttswait();

    }

    public void speakText(String text,Locale locale){
        tts.setLanguage(locale);
        zitentitle.setText(text);

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

    public synchronized int ttswait() {
        while (ttsStatus) {
            try {
                wait(1000);          // プロデューサからの notify()呼び出しを待つ
            } catch (InterruptedException e) {
            }
        }
        notify();
        return 0;
    }

    //speak speed
    private void setSpeechRate(float rate){
        if(null != tts){
            tts.setSpeechRate(rate);
        }
    }
}
