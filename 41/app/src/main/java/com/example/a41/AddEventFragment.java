package com.example.a41;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a41.database.AppDatabase;
import com.example.a41.database.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEventFragment extends Fragment {

    EditText inputTitle, inputCategory, inputLocation;
    Button saveButton, selectDateTime;
    TextView dateDisplay;

    long selectedDateTime = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_event, container, false);

        inputTitle = view.findViewById(R.id.inputTitle);
        inputCategory = view.findViewById(R.id.inputCategory);
        inputLocation = view.findViewById(R.id.inputLocation);
        selectDateTime = view.findViewById(R.id.selectDateTime);
        dateDisplay = view.findViewById(R.id.dateDisplay);
        saveButton = view.findViewById(R.id.saveButton);

        // ---------------- DATE PICKER ----------------
        selectDateTime.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, dayOfMonth) -> {

                        TimePickerDialog timePicker = new TimePickerDialog(
                                getContext(),
                                (view2, hourOfDay, minute) -> {

                                    Calendar selected = Calendar.getInstance();
                                    selected.set(year, month, dayOfMonth, hourOfDay, minute);

                                    selectedDateTime = selected.getTimeInMillis();

                                    SimpleDateFormat sdf = new SimpleDateFormat(
                                            "dd/MM/yyyy HH:mm",
                                            Locale.getDefault()
                                    );

                                    dateDisplay.setText(sdf.format(new Date(selectedDateTime)));

                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                true
                        );

                        timePicker.show();

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePicker.show();
        });

        // ---------------- SAVE EVENT ----------------
        saveButton.setOnClickListener(v -> {

            String title = inputTitle.getText().toString().trim();
            String category = inputCategory.getText().toString().trim();
            String location = inputLocation.getText().toString().trim();

            // ✅ FIXED VALIDATION
            if (title.isEmpty() || selectedDateTime == 0) {
                Toast.makeText(getContext(),
                        "Title and Date required",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedDateTime < System.currentTimeMillis()) {
                Toast.makeText(getContext(),
                        "Can not create a past event",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Event event = new Event(title, category, location, selectedDateTime);

            AppDatabase db = AppDatabase.getInstance(getContext());

            new Thread(() -> {
                db.eventDao().insert(event);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(),
                            "Event Saved!",
                            Toast.LENGTH_SHORT).show();

                    // CLEAR FORM (FIXED)
                    inputTitle.setText("");
                    inputCategory.setText("");
                    inputLocation.setText("");
                    dateDisplay.setText("No date selected");
                    selectedDateTime = 0;
                });

            }).start();
        });

        return view;
    }
}