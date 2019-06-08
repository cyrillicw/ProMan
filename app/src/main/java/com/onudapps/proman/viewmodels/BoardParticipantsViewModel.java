package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;

import java.util.List;

public class BoardParticipantsViewModel extends ViewModel{
    private int boardId;
    private LiveData<List<ParticipantDBEntity>> participantsData;

    private BoardParticipantsViewModel(int boardId) {
        this.boardId = boardId;
    }

    public LiveData<List<ParticipantDBEntity>> getParticipantsData() {
        if (participantsData == null) {
            participantsData = Repository.REPOSITORY.getBoardParticipants(boardId);
        }
        return participantsData;
    }

    public void addBoardParticipant(String address) {
        Repository.REPOSITORY.addBoardParticipant(boardId, address);
    }

    public void removeParticipant(String address) {
        Repository.REPOSITORY.removeBoardParticipant(boardId, address);
    }

    public static class BoardParticipantsModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int boardId;

        public BoardParticipantsModelFactory(int boardId) {
            super();
            this.boardId = boardId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == BoardParticipantsViewModel.class) {
                return (T) new BoardParticipantsViewModel(boardId);
            }
            return null;
        }
    }
}
