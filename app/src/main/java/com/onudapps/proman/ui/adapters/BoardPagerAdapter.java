package com.onudapps.proman.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.onudapps.proman.data.db.entities.GroupDBEntity;
import com.onudapps.proman.ui.fragments.BoardGroupFragment;

import java.util.List;

public class BoardPagerAdapter extends FragmentPagerAdapter {
    private List<GroupDBEntity> groupDBEntities;
    public BoardPagerAdapter(FragmentManager fm, List<GroupDBEntity> groupDBEntities) {
        super(fm);
        this.groupDBEntities = groupDBEntities;
    }

    public void updateData(List<GroupDBEntity> groupDBEntities) {
        this.groupDBEntities = groupDBEntities;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return new BoardGroupFragment(groupDBEntities.get(position).getGroupId());
    }

    @Override
    public int getCount() {
        return groupDBEntities.size();
    }
}
