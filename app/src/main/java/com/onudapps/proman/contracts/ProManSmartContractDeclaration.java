package com.onudapps.proman.contracts;

import com.onudapps.proman.data.pojo.Board;
import com.onudapps.proman.data.pojo.BoardCard;
import com.onudapps.proman.data.pojo.Task;
import org.web3j.protocol.core.RemoteCall;

import java.util.List;

public interface ProManSmartContractDeclaration {
    RemoteCall<List<BoardCard>> getBoards(String privateKey);
    RemoteCall<Board> getBoard(String boardId, String privateKey);
    RemoteCall<Task> getTask(String id);
}
