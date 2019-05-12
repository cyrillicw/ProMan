package com.onudapps.proman.contracts;

import com.onudapps.proman.data.entities.Board;
import com.onudapps.proman.data.entities.BoardCard;
import org.web3j.protocol.core.RemoteCall;

import java.util.List;

public interface ProManSmartContractDeclaration {
    RemoteCall<List<BoardCard>> getBoards(String privateKey);
    RemoteCall<Board> getBoard(String boardId, String privateKey);
}
