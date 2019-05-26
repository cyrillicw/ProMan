package com.onudapps.proman.viewmodels;

import androidx.lifecycle.ViewModel;

public class CreateGroupViewModel extends ViewModel {

    private String expectedGroupTitle;

    public CreateGroupViewModel() {
        expectedGroupTitle = "";
    }

    public String getExpectedGroupTitle() {
        return expectedGroupTitle;
    }

    public void setExpectedGroupTitle(String expectedGroupTitle) {
        this.expectedGroupTitle = expectedGroupTitle;
    }
}
