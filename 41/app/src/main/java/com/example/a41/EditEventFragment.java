package com.example.a41;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a41.database.AppDatabase;
import com.example.a41.database.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditEventFragment extends Fragment {

    EditText editTitle, editCategory, editLocation, editDateTime;
    Button updateButton, deleteButton;

    Event event;
    long selectedDateTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        editTitle = view.findViewById(R.id.editTitle);
        editCategory = view.findViewById(R.id.editCategory);
        editLocation = view.findViewById(R.id.editLocation);
        editDateTime = view.findViewById(R.id.editDateTime);
        updateButton = view.findViewById(R.id.updateButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        AppDatabase db = AppDatabase.getInstance(getContext());

        // Get data from arguments
        if (getArguments() != null) {
            event = (Event) getArguments().getSerializable("event");

            editTitle.setText(event.title);
            editCategory.setText(event.category);
            editLocation.setText(event.location);
            selectedDateTime = event.dateTime;
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "dd/MM/yyyy HH:mm",
                    Locale.getDefault()
            );

            editDateTime.setText(sdf.format(new Date(event.dateTime)));
        }
        editDateTime.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selectedDateTime);

            DatePickerDialog datePicker = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, dayOfMonth) -> {

                        TimePickerDialog timePicker = new TimePickerDialog(
                                getContext(),
                                (view2, hourOfDay, minute) -> {

                                    Calendar selected = Calendar.getInstance();
                                    selected.set(year, month, dayOfMonth, hourOfDay, minute);

                                    selectedDateTime = selected.getTimeInMillis();

                                    SimpleDateFormat sdf2 = new SimpleDateFormat(
                                            "dd/MM/yyyy HH:mm",
                                            Locale.getDefault()
                                    );

                                    editDateTime.setText(
                                            sdf2.format(new Date(selectedDateTime))
                                    );
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

        updateButton.setOnClickListener(v -> {

            event.title = editTitle.getText().toString();
            event.category = editCategory.getText().toString();
            event.location = editLocation.getText().toString();
            event.dateTime = selectedDateTime;

            new Thread(() -> {
                db.eventDao().update(event);

                requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                });

            }).start();
        });

        deleteButton.setOnClickListener(v -> {

            new Thread(() -> {
                db.eventDao().delete(event);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(),
                            "Deleted",
                            Toast.LENGTH_SHORT).show();

                    requireActivity()
                            .getSupportFragmentManager()
                            .popBackStack();
                });

            }).start();
        });

        return view;
    }
}