package com.example.a41;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a41.database.Event;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    List<Event> eventList;
    OnEventClickListener listener;

    private String formatDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(
                "dd/MM/yyyy HH:mm",
                Locale.getDefault()
        );
        return sdf.format(new Date(time));
    }

    public EventAdapter(List<Event> eventList, OnEventClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    public interface OnEventClickListener {
        void onEdit(Event event);
        void onDelete(Event event);
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.textTitle.setText(event.title);
        holder.textDate.setText(formatDate(event.dateTime));

        holder.itemView.setOnClickListener(v -> {
            listener.onEdit(event);
        });

        holder.itemView.setOnLongClickListener(v -> {
            listener.onDelete(event);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDate;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }
}