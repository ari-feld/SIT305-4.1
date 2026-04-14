package com.example.a41;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a41.database.AppDatabase;
import com.example.a41.database.Event;

import java.util.List;

public class EventListFragment extends Fragment {

    RecyclerView recyclerView;
    AppDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(getContext());

        loadEvents();

        return view;
    }



    private void loadEvents() {

        new Thread(() -> {

            List<Event> events = db.eventDao().getAllEvents();

            requireActivity().runOnUiThread(() -> {

                EventAdapter adapter = new EventAdapter(
                        events,
                        new EventAdapter.OnEventClickListener() {

                            @Override
                            public void onEdit(Event event) {

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("event", event);

                                Navigation.findNavController(requireView())
                                        .navigate(R.id.editListFragment, bundle);
                            }

                            @Override
                            public void onDelete(Event event) {

                                new Thread(() -> {

                                    db.eventDao().delete(event);

                                    requireActivity().runOnUiThread(() -> {
                                        loadEvents();
                                    });

                                }).start();
                            }
                        }
                );

                recyclerView.setAdapter(adapter);

            });

        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadEvents();
    }
}