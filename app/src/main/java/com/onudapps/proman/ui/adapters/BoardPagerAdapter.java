package com.onudapps.proman.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.onudapps.proman.data.pojo.GroupWithUpdate;
import com.onudapps.proman.ui.fragments.BoardChartFragment;
import com.onudapps.proman.ui.fragments.BoardGroupFragment;

import java.util.List;

public class BoardPagerAdapter extends FragmentStatePagerAdapter {
    private List<GroupWithUpdate> groupDBEntities;
    //private List<Fragment> fragments;
    public BoardPagerAdapter(FragmentManager fm, List<GroupWithUpdate> groupDBEntities) {
        super(fm);
        this.groupDBEntities = groupDBEntities;
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

    public void updateData(List<GroupWithUpdate> groupDBEntities) {
        this.groupDBEntities = groupDBEntities;
        //prepareFragments();
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (position < groupDBEntities.size()) {
            return BoardGroupFragment.newInstance(groupDBEntities.get(position)
                    .getGroupDBEntity().getGroupId(), groupDBEntities.get(position).getGroupDBEntity().getBoardId());
        }
        return BoardChartFragment.newInstance(groupDBEntities.get(0).getGroupDBEntity().getBoardId());
    }

    @Override
    public int getCount() {
        if (groupDBEntities.size() > 0) {
            return groupDBEntities.size() + 1;
        }
        return 0;
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