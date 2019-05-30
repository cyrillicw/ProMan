package com.onudapps.proman.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.onudapps.proman.data.pojo.GroupWithUpdate;
import com.onudapps.proman.ui.fragments.BoardChartFragment;
import com.onudapps.proman.ui.fragments.BoardGroupFragment;
import com.onudapps.proman.ui.fragments.BoardPropertiesFragment;
import com.onudapps.proman.ui.fragments.EmptyBoardFragment;

import java.util.List;

public class BoardPagerAdapter extends FragmentStatePagerAdapter {
    private List<GroupWithUpdate> groupDBEntities;
    private int boardId;
    //private List<Fragment> fragments;
    public BoardPagerAdapter(FragmentManager fm, List<GroupWithUpdate> groupDBEntities, int boardId) {
        super(fm);
        this.groupDBEntities = groupDBEntities;
        this.boardId = boardId;
        //prepareFragments();
    }

//    private void prepareFragments() {
//        fragments = new ArrayList<>();
//        for (int i = 0; i < groupDBEntities.size(); i++) {
//            BoardGroupFragment fragment = BoardGroupFragment.newInstance(groupDBEntities.get(i)
//                    .getGroupDBEntity().getGroupId(), groupDBEntities.get(i).getGroupDBEntity().getBoardId());
//            fragments.add(fragment);
//        }
//        if (groupDBEntities.size() > 0) {
//            fragments.add(BoardChartFragment.newInstance(groupDBEntities.get(0).getGroupDBEntity().getBoardId()));
//        }
//    }

    public int getStatisticModePosition() {
        return groupDBEntities.size();
    }

    public int getPropertiesModePosition() {
        return groupDBEntities.size() + 1;
    }

    public void updateData(List<GroupWithUpdate> groupDBEntities) {
        this.groupDBEntities = groupDBEntities;
        //prepareFragments();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (position == getCount() - 1) {
            return BoardPropertiesFragment.newInstance(boardId);
        }
        if (groupDBEntities.size() == 0) {
            return new EmptyBoardFragment();
        }
        if (position < groupDBEntities.size()) {
            return BoardGroupFragment.newInstance(groupDBEntities.get(position)
                    .getGroupDBEntity().getGroupId(), boardId);
        }
        return BoardChartFragment.newInstance(boardId);
    }

    @Override
    public int getCount() {
        return groupDBEntities.size() + 2;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    //    @Override
//    public long getItemId(int position) {
//        return fragments.get(position).getId();
//    }
}