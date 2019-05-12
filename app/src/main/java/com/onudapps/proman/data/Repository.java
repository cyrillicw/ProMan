package com.onudapps.proman.data;

import android.content.Context;

public enum  Repository {
    REPOSITORY;
    //private LocalDataSource localDataSource;
    private RemoteDataSource remoteDataSource;

    public static void initialize(Context context) {
        //REPOSITORY.localDataSource = new LocalDataSource(context);
        REPOSITORY.remoteDataSource = new RemoteDataSource();
    }
}
