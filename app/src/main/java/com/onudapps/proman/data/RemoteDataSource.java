package com.onudapps.proman.data;

import android.util.Log;
import com.onudapps.proman.BuildConfig;
import com.onudapps.proman.contracts.ProManSmartContractDeclaration;
import com.onudapps.proman.contracts.Smart;
import com.onudapps.proman.data.pojo.Task;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.UUID;

public class RemoteDataSource {
    private static final String LOG_TAG = "RemoteDataSource";
    private ProManSmartContractDeclaration smartContract;
    public RemoteDataSource() {
        Web3j web3j = Web3j.build(new HttpService(BuildConfig.hostAPI));
        Credentials credentials = Credentials.create("28f3d307e639526a072b94cfa7f484ac84991118fbe7ac59cceb3abf53a58b67");
        smartContract = Smart.load(BuildConfig.contractAddress, web3j, credentials, new DefaultGasProvider());
    }

    public Task getTask(UUID id) {
        try {
            return smartContract.getTask(id.toString()).send();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return null;
        }
    }
}
