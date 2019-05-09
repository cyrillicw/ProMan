package com.onudapps.proman.contracts;

import com.onudapps.proman.pojo.Board;
import com.onudapps.proman.pojo.BoardCard;
import org.web3j.protocol.core.RemoteCall;

import java.util.List;

public interface ProManSmartContractDeclaration {
    RemoteCall<List<BoardCard>> getBoards(String privateKey);
    RemoteCall<Board> getBoard(String boardId, String privateKey);
}
