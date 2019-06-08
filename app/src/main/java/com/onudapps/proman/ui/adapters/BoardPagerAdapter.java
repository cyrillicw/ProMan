package com.onudapps.proman.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.onudapps.proman.data.pojo.GroupWithUpdate;
import com.onudapps.proman.ui.fragments.*;

import java.util.List;

public class BoardPagerAdapter extends FragmentStatePagerAdapter {
    public enum BoardPagerMode {
        GROUPS, STATISTICS, PROPERTIES, USER;
    }
    private List<GroupWithUpdate> groupDBEntities;
    private int boardId;
    public BoardPagerAdapter(FragmentManager fm, List<GroupWithUpdate> groupDBEntities, int boardId) {
        super(fm);
        this.groupDBEntities = groupDBEntities;
        this.boardId = boardId;
        //prepareFragments();
    }

    public int getStatisticModePosition() {
        return groupDBEntities.size();
    }

    public int getPropertiesModePosition() {
        return groupDBEntities.size() + 1;
    }

    public int getUserModePosition() {
        return groupDBEntities.size() + 2;
    }

    public void updateData(List<GroupWithUpdate> groupDBEntities) {
        this.groupDBEntities = groupDBEntities;
        notifyDataSetChanged();
    }

    public BoardPagerMode getMode(int position) {
        if (groupDBEntities.size() == 0) {
            if (position == 0) {
                return BoardPagerMode.GROUPS;
            }
            else {
                return BoardPagerMode.PROPERTIES;
            }
        }
        else {
            if (position < groupDBEntities.size()) {
                return BoardPagerMode.GROUPS;
            }
            if (position == groupDBEntities.size()) {
                return BoardPagerMode.STATISTICS;
            }
            if (position == groupDBEntities.size() + 1) {
                return BoardPagerMode.PROPERTIES;
            }
            else {
                return BoardPagerMode.USER;
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (groupDBEntities.size() == 0) {
            if (position == 0) {
                return new EmptyBoardFragment();
            }
            else {
                return BoardPropertiesFragment.newInstance(boardId);
            }
        }
        else {
            if (position < groupDBEntities.size()) {
                return BoardGroupFragment.newInstance(groupDBEntities.get(position)
                        .getGroupDBEntity().getGroupId(), boardId);
            }
            if (position == groupDBEntities.size()) {
                return BoardChartFragment.newInstance(boardId);
            }
            if (position == groupDBEntities.size() + 1) {
                return BoardPropertiesFragment.newInstance(boardId);
            }
            else {
                return CurrentUserTasksFragment.newInstance(boardId);
            }
        }
    }

    @Override
    public int getCount() {
        if (groupDBEntities.size() == 0) {
            return 2;
        }
        else {
            return groupDBEntities.size() + 3;
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}