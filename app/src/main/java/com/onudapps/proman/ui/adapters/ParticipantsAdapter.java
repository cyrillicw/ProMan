package com.onudapps.proman.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.onudapps.proman.R;

import java.util.List;

public class ParticipantsAdapter extends RecyclerView.Adapter<ParticipantsAdapter.ParticipantViewHolder> {
    private List<String> participants;
    public ParticipantsAdapter(List<String> participants) {
        this.participants = participants;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        holder.bindData(participants.get(position));
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_item, parent, false);
        return new ParticipantViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return participants.size();
    }

    class ParticipantViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ParticipantViewHolder (View view){
            super(view);
            name = view.findViewById(R.id.participant);
        }

        void bindData(String participant) {
            name.setText(participant);
        }
    }
}
