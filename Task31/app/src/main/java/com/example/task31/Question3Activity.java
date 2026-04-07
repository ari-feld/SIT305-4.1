package com.example.task31;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Question3Activity extends AppCompatActivity {

    Button submitButton;
    RadioGroup radioGroup;
    boolean answered = false;

    private void lockAnswers(RadioGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            group.getChildAt(i).setEnabled(false); // Make Radio options read only
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_question3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        submitButton = findViewById(R.id.submitButton);
        radioGroup = findViewById(R.id.radioGroup);

        submitButton.setOnClickListener(v1 -> {
            if (answered) return;

            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) return;

            answered = true;

            RadioButton option1 = findViewById(R.id.option1);
            RadioButton option2 = findViewById(R.id.option2);
            RadioButton option3 = findViewById(R.id.option3);
            RadioButton option4 = findViewById(R.id.option4);

            RadioButton selected = findViewById(selectedId);

            int score = getIntent().getIntExtra("SCORE", 0);

            RadioButton correctAnswer = option2;

            if (selectedId != correctAnswer.getId()) {
                selected.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            } else {
                score++;
            }

            correctAnswer.setTextColor(getResources().getColor(android.R.color.holo_green_light));

            lockAnswers(radioGroup);

            final int finalScore = score;

            selected.postDelayed(() -> {
                Intent intent = new Intent(Question3Activity.this, Question4Activity.class);
                intent.putExtra("SCORE", finalScore);
                startActivity(intent);
            }, 1500);
        });
    }
}