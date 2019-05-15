package com.onudapps.proman.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.onudapps.proman.ui.fragments.BoardGroupFragment;
import com.onudapps.proman.data.pojo.Board;
import com.onudapps.proman.data.pojo.BoardGroup;

import java.util.List;

public class BoardPagerAdapter extends FragmentPagerAdapter {
    private Board board;
    public BoardPagerAdapter(FragmentManager fm, Board board) {
        super(fm);
        this.board = board;
    }

    public void updateData(Board board) {
        this.board = board;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return new BoardGroupFragment(board.getBoardGroups().get(position));
    }

    @Override
    public int getCount() {
        List<BoardGroup> boardGroups = board.getBoardGroups();
        return boardGroups == null ? 0 : boardGroups.size();
    }
}
