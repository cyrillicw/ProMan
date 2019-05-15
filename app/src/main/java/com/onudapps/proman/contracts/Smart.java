package com.onudapps.proman.contracts;

import com.onudapps.proman.data.pojo.Board;
import com.onudapps.proman.data.pojo.BoardCard;
import com.onudapps.proman.data.pojo.Task;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class Smart extends Contract implements ProManSmartContractDeclaration{
    private static final String BINARY = "60806040526000805534801561001457600080fd5b5060408051808201909152600e81527f436865636b206f75742064617070000000000000000000000000000000000000602082015261005b90640100000000610060810204565b610167565b60008054600190810180835560408051606081018252828152602080820187815282840187905293865284815291909420845181559151805192936100ad939085019291909101906100cc565b50604091909101516002909101805460ff191691151591909117905550565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061010d57805160ff191683800117855561013a565b8280016001018555821561013a579182015b8281111561013a57825182559160200191906001019061011f565b5061014692915061014a565b5090565b61016491905b808211156101465760008155600101610150565b90565b6103d3806101766000396000f3fe6080604052600436106100565763ffffffff7c0100000000000000000000000000000000000000000000000000000000600035041663111002aa811461005b5780638d97767214610110578063b6cb58a5146101c4575b600080fd5b34801561006757600080fd5b5061010e6004803603602081101561007e57600080fd5b81019060208101813564010000000081111561009957600080fd5b8201836020820111156100ab57600080fd5b803590602001918460018302840111640100000000831117156100cd57600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506101eb945050505050565b005b34801561011c57600080fd5b5061013a6004803603602081101561013357600080fd5b5035610257565b604051808481526020018060200183151515158152602001828103825284818151815260200191508051906020019080838360005b8381101561018757818101518382015260200161016f565b50505050905090810190601f1680156101b45780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b3480156101d057600080fd5b506101d9610306565b60408051918252519081900360200190f35b60008054600190810180835560408051606081018252828152602080820187815282840187905293865284815291909420845181559151805192936102389390850192919091019061030c565b50604091909101516002909101805460ff191691151591909117905550565b600160208181526000928352604092839020805481840180548651600296821615610100026000190190911695909504601f810185900485028601850190965285855290949193929091908301828280156102f35780601f106102c8576101008083540402835291602001916102f3565b820191906000526020600020905b8154815290600101906020018083116102d657829003601f168201915b5050506002909301549192505060ff1683565b60005481565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061034d57805160ff191683800117855561037a565b8280016001018555821561037a579182015b8281111561037a57825182559160200191906001019061035f565b5061038692915061038a565b5090565b6103a491905b808211156103865760008155600101610390565b9056fea165627a7a72305820ad671513b85d355442fd2b0a15992efb30a94e6bc21291496cff6c59d11341e20029";

    public static final String FUNC_TASKS = "tasks";

    public static final String FUNC_TASKCOUNT = "taskCount";

    public static final String FUNC_CREATETASK = "createTask";

    @Deprecated
    protected Smart(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Smart(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Smart(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Smart(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<Tuple3<BigInteger, String, Boolean>> tasks(BigInteger param0) {
        final Function function = new Function(FUNC_TASKS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple3<BigInteger, String, Boolean>>(
                new Callable<Tuple3<BigInteger, String, Boolean>>() {
                    @Override
                    public Tuple3<BigInteger, String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, String, Boolean>(
                                (BigInteger) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (Boolean) results.get(2).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> taskCount() {
        final Function function = new Function(FUNC_TASKCOUNT,
                Arrays.asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> createTask(String _content) {
        final Function function = new Function(
                FUNC_CREATETASK,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_content)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Smart load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Smart(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Smart load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Smart(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Smart load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Smart(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Smart load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Smart(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Smart> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Smart.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Smart> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Smart.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Smart> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Smart.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Smart> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Smart.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    @Override
    public RemoteCall<List<BoardCard>> getBoards(String privateKey) {
        return null;
    }

    @Override
    public RemoteCall<Board> getBoard(String boardId, String privateKey) {
        return null;
    }

    @Override
    public RemoteCall<Task> getTask(String id) {
        return null;
    }
}
