package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    public static final String a_score = "a_score";
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private TextView Question,Score,QuestionCount,CountDown;
    private RadioGroup rbgroup;
    private RadioButton r1,r2,r3,r4;
    private Button btn;
    private ColorStateList DefaultRb;
    private ColorStateList DefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    private List<Question> questionList;
    private int questionCounter,questionCountTotal;
    private Question curques;
    private int score;
    private boolean answ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Question = findViewById(R.id.questions);
        CountDown = findViewById(R.id.countdown);
        Score = findViewById(R.id.viewscore);
        QuestionCount = findViewById(R.id.questioncount);
        rbgroup = findViewById(R.id.radiogrp);
       // curques = questionList.get(questionCounter);
        r1 = findViewById(R.id.radiobtn1);
        r2 = findViewById(R.id.radiobtn2);
        r3 = findViewById(R.id.radiobtn3);
        r4 = findViewById(R.id.radiobtn4);
        btn = findViewById(R.id.btnconfirm);
        DefaultRb = r1.getTextColors();
        DefaultCd = CountDown.getTextColors();

        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestion();
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answ){
                    if(r1.isChecked()||r2.isChecked()||r3.isChecked()||r4.isChecked()){
                        checkAnswer();
                    }
                    else{
                        Toast.makeText(QuizActivity.this,"Select One option",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    showNextQuestion();
                }
            }
        });
    }
    private void showNextQuestion(){
        r1.setTextColor(DefaultRb);
        r2.setTextColor(DefaultRb);
        r3.setTextColor(DefaultRb);
        r4.setTextColor(DefaultRb);
        rbgroup.clearCheck();

        if(questionCounter <questionCountTotal){
            curques = questionList.get(questionCounter);
            Question.setText(curques.getQuestion());
            r1.setText(curques.getOption1());
            r2.setText(curques.getOption2());
            r3.setText(curques.getOption3());
            r4.setText(curques.getOption4());

            questionCounter++;
            QuestionCount.setText("Question : "+questionCounter+"/"+questionCountTotal);
            answ = false;
            btn.setText("Confirm");
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        }
        else{
            Intent intent = new Intent();
            intent.putExtra(a_score,score);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis=0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }
    private void updateCountDownText(){
        int minutes = (int) (timeLeftInMillis/1000)/60;
        int seconds = (int) (timeLeftInMillis/1000)%60;
        String timeformatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
        CountDown.setText(timeformatted);
        if(timeLeftInMillis<10000){
            CountDown.setTextColor(Color.RED);
        }
        else{
            CountDown.setTextColor(DefaultCd);
        }
    }
    private void checkAnswer(){
        answ=true;
        countDownTimer.cancel();
        RadioButton rbsel = findViewById(rbgroup.getCheckedRadioButtonId());
        int ansnm = rbgroup.indexOfChild(rbsel)+1;
        String s = Integer.toString(curques.getAnswer());
        Toast.makeText(QuizActivity.this,s,Toast.LENGTH_SHORT).show();
        if((int)ansnm==(int)(curques.getAnswer())){
            score++;
            Score.setText("Score : "+score);
        }
        showSolution();
    }
    private void showSolution(){
        r1.setTextColor(Color.RED);
        r2.setTextColor(Color.RED);
        r3.setTextColor(Color.RED);
        r4.setTextColor(Color.RED);
      //  Toast.makeText(QuizActivity.this,curques.getAnswer(),Toast.LENGTH_SHORT).show();
        if(curques.getAnswer()==1){
            r1.setTextColor(Color.GREEN);
            Question.setText("Answer 1 is correct");
        }
        else if(curques.getAnswer()==2){
            r2.setTextColor(Color.GREEN);
            Question.setText("Answer 2 is correct");
        }
        else if(curques.getAnswer()==3){
            r3.setTextColor(Color.GREEN);
            Question.setText("Answer 3 is correct");
        }
        else{
            r4.setTextColor(Color.GREEN);
            Question.setText("Answer 4 is correct");
        }

        if(questionCounter<questionCountTotal){
            btn.setText("Next");
        }
        else{
            btn.setText("Finish");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
}
