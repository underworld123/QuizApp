package com.example.quizapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int Request_code =1;
    public static final String Shared_pre="sharedpre";
    public static final String key_highscore="highscorekey";
    private TextView highscore;
    private int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highscore=findViewById(R.id.highscore);
        loadHighScore();
        Button btn=findViewById(R.id.btnstart);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QuizActivity.class);
                startActivityForResult(intent,Request_code);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Request_code){
            if(resultCode==RESULT_OK){
                int escore = data.getIntExtra(QuizActivity.a_score,0);
                if(escore>score){
                    score=escore;
                    highscore.setText("Highscore : "+score);

                    SharedPreferences prefs = getSharedPreferences(Shared_pre,MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(key_highscore,score);
                    editor.apply();
                }
            }
        }
    }

    private void loadHighScore(){
        SharedPreferences prefs = getSharedPreferences(Shared_pre,MODE_PRIVATE);
        score = prefs.getInt(key_highscore,0);
        highscore.setText("Highscore : "+score);
    }
}
