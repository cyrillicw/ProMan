package com.onudapps.proman.contracts;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
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
public class Smart extends Contract {
    private static final String BINARY = "0x60806040526000805534801561001457600080fd5b5061041b806100246000396000f3fe6080604052600436106100565763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416635bc4ec8b811461005b5780639a7083f914610082578063a32d427714610139575b600080fd5b34801561006757600080fd5b506100706101f3565b60408051918252519081900360200190f35b34801561008e57600080fd5b506100ac600480360360208110156100a557600080fd5b50356101f9565b6040518085815260200180602001848152602001838152602001828103825285818151815260200191508051906020019080838360005b838110156100fb5781810151838201526020016100e3565b50505050905090810190601f1680156101285780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b34801561014557600080fd5b506101f16004803603606081101561015c57600080fd5b81019060208101813564010000000081111561017757600080fd5b82018360208201111561018957600080fd5b803590602001918460018302840111640100000000831117156101ab57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050823593505050602001356102ab565b005b60005481565b600160208181526000928352604092839020805481840180548651600296821615610100026000190190911695909504601f810185900485028601850190965285855290949193929091908301828280156102955780601f1061026a57610100808354040283529160200191610295565b820191906000526020600020905b81548152906001019060200180831161027857829003601f168201915b5050505050908060020154908060030154905084565b60408051608081018252600080548083526020808401888152848601889052606085018790529183526001808252949092208351815590518051939491936102fb93928501929190910190610354565b5060408281015160028301556060909201516003909101556000805460010190819055815190815290517f94c9d5a4cff8187062c4711b5c943fe6525337a780885d7a4128a72c3ed975aa9181900360200190a1505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061039557805160ff19168380011785556103c2565b828001600101855582156103c2579182015b828111156103c25782518255916020019190600101906103a7565b506103ce9291506103d2565b5090565b6103ec91905b808211156103ce57600081556001016103d8565b9056fea165627a7a72305820d462ebc0980dfb80bf49812ae140aaca02b2493e7385791ec7446f10ad233e950029";

    public static final String FUNC_BOARDSCOUNT = "boardsCount";

    public static final String FUNC_BOARDS = "boards";

    public static final String FUNC_ADDBOARD = "addBoard";

    public static final Event BOARDADDED_EVENT = new Event("boardAdded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

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

    public RemoteCall<BigInteger> boardsCount() {
        final Function function = new Function(FUNC_BOARDSCOUNT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple4<BigInteger, String, BigInteger, BigInteger>> boards(BigInteger param0) {
        final Function function = new Function(FUNC_BOARDS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteCall<Tuple4<BigInteger, String, BigInteger, BigInteger>>(
                new Callable<Tuple4<BigInteger, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<BigInteger, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<BigInteger, String, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public List<BoardAddedEventResponse> getBoardAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BOARDADDED_EVENT, transactionReceipt);
        ArrayList<BoardAddedEventResponse> responses = new ArrayList<BoardAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BoardAddedEventResponse typedResponse = new BoardAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BoardAddedEventResponse> boardAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, BoardAddedEventResponse>() {
            @Override
            public BoardAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BOARDADDED_EVENT, log);
                BoardAddedEventResponse typedResponse = new BoardAddedEventResponse();
                typedResponse.log = log;
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BoardAddedEventResponse> boardAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOARDADDED_EVENT));
        return boardAddedEventFlowable(filter);
    }

    public RemoteCall<TransactionReceipt> addBoard(String title, BigInteger start, BigInteger finish) {
        final Function function = new Function(
                FUNC_ADDBOARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(title),
                        new org.web3j.abi.datatypes.generated.Uint256(start),
                        new org.web3j.abi.datatypes.generated.Uint256(finish)),
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

    @Deprecated
    public static RemoteCall<Smart> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Smart.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Smart> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Smart.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Smart> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Smart.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class BoardAddedEventResponse {
        public Log log;

        public BigInteger id;
    }
}
