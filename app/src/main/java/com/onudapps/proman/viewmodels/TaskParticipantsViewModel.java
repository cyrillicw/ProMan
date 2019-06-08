package com.onudapps.proman.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.onudapps.proman.data.Repository;
import com.onudapps.proman.data.db.entities.ParticipantDBEntity;

import java.util.List;

public class TaskParticipantsViewModel extends ViewModel{
    private int taskId;
    private LiveData<List<ParticipantDBEntity>> participantsData;

    private TaskParticipantsViewModel(int taskId) {
        this.taskId = taskId;
    }

    public LiveData<List<ParticipantDBEntity>> getParticipantsData() {
        if (participantsData == null) {
            participantsData = Repository.REPOSITORY.getTaskParticipants(taskId);
        }
        return participantsData;
    }

    public void addTaskParticipant(String address) {
        Repository.REPOSITORY.addTaskParticipant(taskId, address);
    }

    public void removeParticipant(String address) {
        Repository.REPOSITORY.removeTaskParticipant(taskId, address);
    }

    public static class TaskParticipantsModelFactory extends ViewModelProvider.NewInstanceFactory {

        private final int taskId;

        public TaskParticipantsModelFactory(int taskId) {
            super();
            this.taskId = taskId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == TaskParticipantsViewModel.class) {
                return (T) new TaskParticipantsViewModel(taskId);
            }
            return null;
        }
    }
}
