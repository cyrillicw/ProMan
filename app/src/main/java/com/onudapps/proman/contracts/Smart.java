package com.onudapps.proman.contracts;

import io.reactivex.Flowable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Int32;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
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
    private static final String BINARY = "0x6080604052600080546001608060020a031916905534801561002057600080fd5b50612303806100306000396000f3fe6080604052600436106101065763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416630d60436d811461010b5780631274290214610139578063381468e81461016a578063385e17db1461019757806339d2ff17146101b957806354272d94146101e95780635bc4ec8b1461020b5780636320b3f8146102205780636a5baba1146102405780637dc03f741461027057806380caf07d1461028557806384dc3886146102a5578063884eaab5146102d3578063ac85f7f7146102e8578063cb9d8c5c14610318578063ce8823a514610338578063de0040be14610358578063f8f3378e14610378578063fc0a5e91146103a5575b600080fd5b34801561011757600080fd5b506101206103c5565b604051610130949392919061205f565b60405180910390f35b34801561014557600080fd5b50610159610154366004611e4a565b6107de565b60405161013095949392919061213d565b34801561017657600080fd5b5061018a610185366004611e4a565b610938565b604051610130919061218d565b3480156101a357600080fd5b506101b76101b2366004611e4a565b610a8a565b005b3480156101c557600080fd5b506101d96101d4366004611e4a565b610ad9565b60405161013094939291906121dd565b3480156101f557600080fd5b506101fe610c8d565b60405161013091906120ca565b34801561021757600080fd5b506101fe610c9e565b34801561022c57600080fd5b506101b761023b366004611e10565b610ca7565b34801561024c57600080fd5b5061026061025b366004611e4a565b610e23565b60405161013094939291906120f8565b34801561027c57600080fd5b506101fe610eeb565b34801561029157600080fd5b506101b76102a0366004611e70565b610f00565b3480156102b157600080fd5b506102c56102c0366004611e4a565b611026565b6040516101309291906120d8565b3480156102df57600080fd5b506101fe6110d2565b3480156102f457600080fd5b50610308610303366004611e4a565b6110eb565b604051610130949392919061219e565b34801561032457600080fd5b506101b7610333366004611eb8565b6112f9565b34801561034457600080fd5b506101b7610353366004611e70565b6115df565b34801561036457600080fd5b506101b7610373366004611e10565b6116ee565b34801561038457600080fd5b50610398610393366004611e4a565b61183f565b604051610130919061204e565b3480156103b157600080fd5b506101b76103c0366004611e4a565b6118d1565b60608060608060606004600033600160a060020a0316600160a060020a0316815260200190815260200160002060010180549050604051908082528060200260200182016040528015610422578160200160208202803883390190505b5033600090815260046020908152604091829020600101548251818152818302810190920190925291925060609190801561047157816020015b606081526020019060019003908161045c5790505b503360009081526004602090815260409182902060010154825181815281830281019092019092529192506060919080156104b6578160200160208202803883390190505b503360009081526004602090815260409182902060010154825181815281830281019092019092529192506060919080156104fb578160200160208202803883390190505b50905060005b336000908152600460205260409020600101548110156107cf57336000908152600460205260408120600190810180549192918490811061053e57fe5b90600052602060002090600891828204019190066004029054906101000a900460030b60030b60030b815260200190815260200160002060000160009054906101000a900460030b858281518110151561059457fe5b600392830b90920b602092830290910182015233600090815260049091526040812060019081018054919291849081106105ca57fe5b90600052602060002090600891828204019190066004029054906101000a900460030b60030b60030b81526020019081526020016000206001018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106985780601f1061066d57610100808354040283529160200191610698565b820191906000526020600020905b81548152906001019060200180831161067b57829003601f168201915b505050505084828151811015156106ab57fe5b602090810290910181019190915233600090815260049091526040812060019081018054919291849081106106dc57fe5b6000918252602080832060088304015460046007938416026101000a9004600390810b810b900b845283019390935260409091019020600201548451910b9084908390811061072757fe5b600792830b90920b6020928302909101820152336000908152600490915260408120600190810180549192918490811061075d57fe5b90600052602060002090600891828204019190066004029054906101000a900460030b60030b60030b815260200190815260200160002060020160089054906101000a900460070b82828151811015156107b357fe5b600792830b90920b602092830290910190910152600101610501565b50929791965094509092509050565b6003602081815260009283526040928390208054600180830180548751600261010094831615949094026000190190911692909204601f81018690048602830186019097528682529190940b949193929091908301828280156108825780601f1061085757610100808354040283529160200191610882565b820191906000526020600020905b81548152906001019060200180831161086557829003601f168201915b50505060028085018054604080516020601f60001961010060018716150201909416959095049283018590048502810185019091528181529596959450909250908301828280156109145780601f106108e957610100808354040283529160200191610914565b820191906000526020600020905b8154815290600101906020018083116108f757829003601f168201915b50505060039093015491925050600781810b91680100000000000000009004900b85565b6060610942611a29565b600383810b810b60009081526002602081815260409283902083516060810185528154860b860b90950b85526001808201805486516101009382161593909302600019011694909404601f810184900484028201840190955284815290938583019391928301828280156109f75780601f106109cc576101008083540402835291602001916109f7565b820191906000526020600020905b8154815290600101906020018083116109da57829003601f168201915b5050505050815260200160028201805480602002602001604051908101604052809291908181526020018280548015610a7557602002820191906000526020600020906000905b82829054906101000a900460030b60030b81526020019060040190602082600301049283019260010382029150808411610a3e5790505b50505091909252505050602001519392505050565b610a943382610ca7565b610a9e33826116ee565b7ff8fc2d154446e315941a028488baf3204c5ef227c4ac71004de6446b7630d0926001604051610ace91906120b6565b60405180910390a150565b606080600080610ae7611a4e565b600386810b810b60009081526020828152604091829020825160a0810184528154850b850b90940b8452600180820180548551600261010094831615949094026000190190911692909204601f8101859004850283018501909552848252919385840193919291830182828015610b9f5780601f10610b7457610100808354040283529160200191610b9f565b820191906000526020600020905b815481529060010190602001808311610b8257829003601f168201915b5050509183525050600282810180546040805160206001841615610100026000190190931694909404601f81018390048302850183019091528084529381019390830182828015610c315780601f10610c0657610100808354040283529160200191610c31565b820191906000526020600020905b815481529060010190602001808311610c1457829003601f168201915b505050918352505060039190910154600781810b810b810b60208085019190915268010000000000000000909204810b810b900b6040928301528201519082015160608301516080909301519199909850919650945092505050565b600054640100000000900460030b81565b60005460030b81565b600160a060020a03821660009081526004602052604081209080805b6001840154811015610d24578460030b8460010182815481101515610ce457fe5b90600052602060002090600891828204019190066004029054906101000a900460030b60030b1415610d1c5780915060019250610d24565b600101610cc3565b508115610e1c57600181015b6001840154811015610dc65760018401805482908110610d4c57fe5b90600052602060002090600891828204019190066004029054906101000a900460030b8460010160018303815481101515610d8357fe5b90600052602060002090600891828204019190066004026101000a81548163ffffffff021916908360030b63ffffffff1602179055508080600101915050610d30565b506001830180546000198101908110610ddb57fe5b90600052602060002090600891828204019190066004026101000a81549063ffffffff021916905582600101805480919060019003610e1a9190611a7d565b505b5050505050565b600160208181526000928352604092839020805481840180548651600261010097831615979097026000190190911695909504601f810185900485028601850190965285855260039190910b94919392909190830182828015610ec75780601f10610e9c57610100808354040283529160200191610ec7565b820191906000526020600020905b815481529060010190602001808311610eaa57829003601f168201915b50505060029093015491925050600781810b91680100000000000000009004900b84565b60005468010000000000000000900460030b81565b610f08611a4e565b506040805160a081018252600080546c0100000000000000000000000090819004600390810b810b8085526020808601889052865180820188528581528688015260001960608701819052608087015288830b830b855260028082528786200180546001810182559086529085206008820401805460079092166004026101000a63ffffffff8181021990931692909316929092021790559154935192937f6be49f4d1712d0fb22da3e4c1396a3e80a71bca29e577dbef8001fb4e5f15be793610fd9939290910490910b906120ca565b60405180910390a15050600080546fffffffff00000000000000000000000019811663ffffffff6c0100000000000000000000000092839004600390810b600101900b1690910217905550565b6002602081815260009283526040928390208054600180830180548751601f6000199483161561010002949094019091169690960491820185900485028601850190965280855260039190910b949193928301828280156110c85780601f1061109d576101008083540402835291602001916110c8565b820191906000526020600020905b8154815290600101906020018083116110ab57829003601f168201915b5050505050905082565b6000546c01000000000000000000000000900460030b81565b606060008060606110fa611ab6565b600386810b810b600090815260016020818152604092839020835160c0810185528154860b860b90950b855280830180548551600261010096831615969096026000190190911694909404601f8101849004840285018401909552848452909385830193928301828280156111b05780601f10611185576101008083540402835291602001916111b0565b820191906000526020600020905b81548152906001019060200180831161119357829003601f168201915b505050505081526020016002820160009054906101000a900460070b60070b60070b81526020016002820160089054906101000a900460070b60070b60070b81526020016003820180548060200260200160405190810160405280929190818152602001828054801561124c57602002820191906000526020600020905b8154600160a060020a0316815260019091019060200180831161122e575b50505050508152602001600482018054806020026020016040519081016040528092919081815260200182805480156112ca57602002820191906000526020600020906000905b82829054906101000a900460030b60030b815260200190600401906020826003010492830192600103820291508084116112935790505b5050509190925250505060208101516040820151606083015160a0909301519199909850919650945092505050565b6040805160c081018252600080546401000000009004600390810b810b808452602080850187815260001986880181905260608701528651858152808301885260808701528651858152808301885260a087015291830b84526001808252959093208451815463ffffffff191663ffffffff9190940b169290921782555180519394919361138f93928501929190910190611aea565b506040820151600282018054606085015167ffffffffffffffff19909116600793840b67ffffffffffffffff908116919091176fffffffffffffffff00000000000000001916680100000000000000009290940b160291909117905560808201518051611406916003840191602090910190611b68565b5060a08201518051611422916004840191602090910190611bd6565b50506000805464010000000090819004600390810b810b810b835260016020818152604080862084018054808501825590875282872001805473ffffffffffffffffffffffffffffffffffffffff191633908117909155865495909504840b840b90930b855282852093855260059093018352818420805460ff19169091179055600490915290205460ff161515905061150d5760408051808201825260018082528251600080825260208083018652808501928352338252600481529490208351815460ff19169015151781559051805193949193611509938501929190910190611bd6565b5050505b336000908152600460208181526040808420845460019182018054928301815586529285206008820401805464010000000094859004600390810b810b63ffffffff90811660079095169097026101000a93840296909302191694909417909355925492517faedc68e9a9eb366b79839e0b46c3f2d1a3b9eec9c785ec3bd3fcb50f2322aa6d936115a49392900490910b906120ca565b60405180910390a1506000805467ffffffff0000000019811663ffffffff64010000000092839004600390810b600101900b16909102179055565b6115e7611a29565b5060408051606081018252600080546801000000000000000090819004600390810b810b8085526020808601889052865185815280820188528688015288830b830b8552600180825287862060049081018054928301815587529186206008820401805463ffffffff94851660079093169093026101000a91820293909102199091169190911790559154935192937facdbba31b030f49229d93f7468a1df7733eeb8f4b8f707dbafa0b7a53dfe2125936116a9939290910490910b906120ca565b60405180910390a15050600080546bffffffff000000000000000019811663ffffffff6801000000000000000092839004600390810b600101900b1690910217905550565b600381810b900b60009081526001602052604081209080805b600384015481101561175d5785600160a060020a0316846003018281548110151561172e57fe5b600091825260209091200154600160a060020a03161415611755578091506001925061175d565b600101611707565b508115610e1c57600181015b60038401548110156117ef576003840180548290811061178557fe5b600091825260209091200154600385018054600160a060020a039092169160001984019081106117b157fe5b6000918252602090912001805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055600101611769565b50600383018054600019810190811061180457fe5b6000918252602090912001805473ffffffffffffffffffffffffffffffffffffffff1916905560038301805490610e1a906000198301611c84565b600381810b900b6000908152600160209081526040918290206004018054835181840281018401909452808452606093928301828280156118c557602002820191906000526020600020906000905b82829054906101000a900460030b60030b8152602001906004019060208260030104928301926001038202915080841161188e5790505b50505050509050919050565b600381810b900b6000908152600160209081526040808320338452600581019092529091205460ff16156119ec5760005b60038201548110156119455761193d826003018281548110151561192257fe5b600091825260209091200154600160a060020a031684610ca7565b600101611902565b50600382810b900b60009081526001602081905260408220805463ffffffff19168155919061197690830182611ca8565b6002820180546fffffffffffffffffffffffffffffffff1916905561199f600383016000611cef565b6119ad600483016000611d0d565b50507f2ba7d88b70a50545812b72874f1c186473c9d50d28e9e907f3ed2a7bc5baed4560016040516119df91906120b6565b60405180910390a1611a25565b7f2ba7d88b70a50545812b72874f1c186473c9d50d28e9e907f3ed2a7bc5baed456000604051611a1c91906120b6565b60405180910390a15b5050565b606060405190810160405280600060030b815260200160608152602001606081525090565b6040805160a0810182526000808252606060208301819052928201839052918101829052608081019190915290565b815481835581811115611ab1576007016008900481600701600890048360005260206000209182019101611ab19190611d2e565b505050565b6040805160c0810182526000808252606060208301819052928201819052828201526080810182905260a081019190915290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10611b2b57805160ff1916838001178555611b58565b82800160010185558215611b58579182015b82811115611b58578251825591602001919060010190611b3d565b50611b64929150611d2e565b5090565b828054828255906000526020600020908101928215611bca579160200282015b82811115611bca578251825473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a03909116178255602090920191600190910190611b88565b50611b64929150611d4b565b82805482825590600052602060002090600701600890048101928215611c785791602002820160005b83821115611c4657835183826101000a81548163ffffffff021916908360030b63ffffffff1602179055509260200192600401602081600301049283019260010302611bff565b8015611c765782816101000a81549063ffffffff0219169055600401602081600301049283019260010302611c46565b505b50611b64929150611d7c565b815481835581811115611ab157600083815260209020611ab1918101908301611d2e565b50805460018160011615610100020316600290046000825580601f10611cce5750611cec565b601f016020900490600052602060002090810190611cec9190611d2e565b50565b5080546000825590600052602060002090810190611cec9190611d2e565b508054600082556007016008900490600052602060002090810190611cec91905b611d4891905b80821115611b645760008155600101611d34565b90565b611d4891905b80821115611b6457805473ffffffffffffffffffffffffffffffffffffffff19168155600101611d51565b611d4891905b80821115611b6457805463ffffffff19168155600101611d82565b6000611da98235612278565b9392505050565b6000611da98235612260565b6000601f82018313611dcd57600080fd5b8135611de0611ddb82612229565b612202565b91508082526020830160208301858383011115611dfc57600080fd5b611e07838284612283565b50505092915050565b60008060408385031215611e2357600080fd5b6000611e2f8585611d9d565b9250506020611e4085828601611db0565b9150509250929050565b600060208284031215611e5c57600080fd5b6000611e688484611db0565b949350505050565b60008060408385031215611e8357600080fd5b6000611e8f8585611db0565b925050602083013567ffffffffffffffff811115611eac57600080fd5b611e4085828601611dbc565b600060208284031215611eca57600080fd5b813567ffffffffffffffff811115611ee157600080fd5b611e6884828501611dbc565b6000611ef882612257565b808452602084019350611f0a83612251565b60005b82811015611f3a57611f20868351612007565b611f2982612251565b602096909601959150600101611f0d565b5093949350505050565b6000611f4f82612257565b808452602084019350611f6183612251565b60005b82811015611f3a57611f77868351612010565b611f8082612251565b602096909601959150600101611f64565b6000611f9c82612257565b80845260208401935083602082028501611fb585612251565b60005b84811015611fec578383038852611fd0838351612019565b9250611fdb82612251565b602098909801979150600101611fb8565b50909695505050505050565b6120018161225b565b82525050565b61200181612260565b61200181612266565b600061202482612257565b80845261203881602086016020860161228f565b612041816122bf565b9093016020019392505050565b60208082528101611da98184611eed565b608080825281016120708187611eed565b905081810360208301526120848186611f91565b905081810360408301526120988185611f44565b905081810360608301526120ac8184611f44565b9695505050505050565b602081016120c48284611ff8565b92915050565b602081016120c48284612007565b604081016120e68285612007565b8181036020830152611e688184612019565b608081016121068287612007565b81810360208301526121188186612019565b90506121276040830185612010565b6121346060830184612010565b95945050505050565b60a0810161214b8288612007565b818103602083015261215d8187612019565b905081810360408301526121718186612019565b90506121806060830185612010565b6120ac6080830184612010565b60208082528101611da98184612019565b608080825281016121af8187612019565b90506121be6020830186612010565b6121cb6040830185612010565b81810360608301526120ac8184611eed565b608080825281016121ee8187612019565b905081810360208301526121188186612019565b60405181810167ffffffffffffffff8111828210171561222157600080fd5b604052919050565b600067ffffffffffffffff82111561224057600080fd5b506020601f91909101601f19160190565b60200190565b5190565b151590565b60030b90565b60070b90565b600160a060020a031690565b60006120c48261226c565b82818337506000910152565b60005b838110156122aa578181015183820152602001612292565b838111156122b9576000848401525b50505050565b601f01601f19169056fea265627a7a72305820847ed46fc5ce36d583a61e9c12ce2fac1782b6eb6cfa3e345dccfed5d4da9b576c6578706572696d656e74616cf50037";

    public static final String FUNC_TASKS = "tasks";

    public static final String FUNC_BOARDSCREATED = "boardsCreated";

    public static final String FUNC_BOARDSCOUNT = "boardsCount";

    public static final String FUNC_BOARDS = "boards";

    public static final String FUNC_GROUPSCREATED = "groupsCreated";

    public static final String FUNC_GROUPS = "groups";

    public static final String FUNC_TASKSCREATED = "tasksCreated";

    public static final String FUNC_ADDBOARD = "addBoard";

    public static final String FUNC_ADDGROUP = "addGroup";

    public static final String FUNC_ADDTASK = "addTask";

    public static final String FUNC_GETBOARDCARDS = "getBoardCards";

    public static final String FUNC_GETBOARD = "getBoard";

    public static final String FUNC_GETGROUPSINDICES = "getGroupsIndices";

    public static final String FUNC_GETGROUP = "getGroup";

    public static final String FUNC_GETTASK = "getTask";

    public static final String FUNC_REMOVEBOARDFROMUSER = "removeBoardFromUser";

    public static final String FUNC_REMOVEUSERFROMBOARD = "removeUserFromBoard";

    public static final String FUNC_LEAVEBOARD = "leaveBoard";

    public static final String FUNC_ERASEBOARD = "eraseBoard";

    public static final Event BOARDADDED_EVENT = new Event("boardAdded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}));
    ;

    public static final Event BOARDLEFT_EVENT = new Event("boardLeft",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
    ;

    public static final Event BOARDREMOVED_EVENT = new Event("boardRemoved",
            Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
    ;

    public static final Event GROUPADDED_EVENT = new Event("groupAdded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}));
    ;

    public static final Event TASKADDED_EVENT = new Event("taskAdded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}));
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

    public RemoteCall<Tuple5<BigInteger, String, String, BigInteger, BigInteger>> tasks(BigInteger param0) {
        final Function function = new Function(FUNC_TASKS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}));
        return new RemoteCall<Tuple5<BigInteger, String, String, BigInteger, BigInteger>>(
                new Callable<Tuple5<BigInteger, String, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, String, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, String, String, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (String) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> boardsCreated() {
        final Function function = new Function(FUNC_BOARDSCREATED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> boardsCount() {
        final Function function = new Function(FUNC_BOARDSCOUNT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple4<BigInteger, String, BigInteger, BigInteger>> boards(BigInteger param0) {
        final Function function = new Function(FUNC_BOARDS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}));
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

    public RemoteCall<BigInteger> groupsCreated() {
        final Function function = new Function(FUNC_GROUPSCREATED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple2<BigInteger, String>> groups(BigInteger param0) {
        final Function function = new Function(FUNC_GROUPS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple2<BigInteger, String>>(
                new Callable<Tuple2<BigInteger, String>>() {
                    @Override
                    public Tuple2<BigInteger, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<BigInteger, String>(
                                (BigInteger) results.get(0).getValue(),
                                (String) results.get(1).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> tasksCreated() {
        final Function function = new Function(FUNC_TASKSCREATED,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int32>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public List<BoardLeftEventResponse> getBoardLeftEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BOARDLEFT_EVENT, transactionReceipt);
        ArrayList<BoardLeftEventResponse> responses = new ArrayList<BoardLeftEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BoardLeftEventResponse typedResponse = new BoardLeftEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.left = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BoardLeftEventResponse> boardLeftEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, BoardLeftEventResponse>() {
            @Override
            public BoardLeftEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BOARDLEFT_EVENT, log);
                BoardLeftEventResponse typedResponse = new BoardLeftEventResponse();
                typedResponse.log = log;
                typedResponse.left = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BoardLeftEventResponse> boardLeftEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOARDLEFT_EVENT));
        return boardLeftEventFlowable(filter);
    }

    public List<BoardRemovedEventResponse> getBoardRemovedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BOARDREMOVED_EVENT, transactionReceipt);
        ArrayList<BoardRemovedEventResponse> responses = new ArrayList<BoardRemovedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BoardRemovedEventResponse typedResponse = new BoardRemovedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.removed = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BoardRemovedEventResponse> boardRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, BoardRemovedEventResponse>() {
            @Override
            public BoardRemovedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BOARDREMOVED_EVENT, log);
                BoardRemovedEventResponse typedResponse = new BoardRemovedEventResponse();
                typedResponse.log = log;
                typedResponse.removed = (Boolean) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BoardRemovedEventResponse> boardRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOARDREMOVED_EVENT));
        return boardRemovedEventFlowable(filter);
    }

    public List<GroupAddedEventResponse> getGroupAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(GROUPADDED_EVENT, transactionReceipt);
        ArrayList<GroupAddedEventResponse> responses = new ArrayList<GroupAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GroupAddedEventResponse typedResponse = new GroupAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GroupAddedEventResponse> groupAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, GroupAddedEventResponse>() {
            @Override
            public GroupAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(GROUPADDED_EVENT, log);
                GroupAddedEventResponse typedResponse = new GroupAddedEventResponse();
                typedResponse.log = log;
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GroupAddedEventResponse> groupAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GROUPADDED_EVENT));
        return groupAddedEventFlowable(filter);
    }

    public List<TaskAddedEventResponse> getTaskAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TASKADDED_EVENT, transactionReceipt);
        ArrayList<TaskAddedEventResponse> responses = new ArrayList<TaskAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TaskAddedEventResponse typedResponse = new TaskAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TaskAddedEventResponse> taskAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TaskAddedEventResponse>() {
            @Override
            public TaskAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TASKADDED_EVENT, log);
                TaskAddedEventResponse typedResponse = new TaskAddedEventResponse();
                typedResponse.log = log;
                typedResponse.id = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TaskAddedEventResponse> taskAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TASKADDED_EVENT));
        return taskAddedEventFlowable(filter);
    }

    public RemoteCall<TransactionReceipt> addBoard(String title) {
        final Function function = new Function(
                FUNC_ADDBOARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(title)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addGroup(BigInteger boardId, String title) {
        final Function function = new Function(
                FUNC_ADDGROUP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(boardId),
                        new org.web3j.abi.datatypes.Utf8String(title)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> addTask(BigInteger groupId, String title) {
        final Function function = new Function(
                FUNC_ADDTASK,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(groupId),
                        new org.web3j.abi.datatypes.Utf8String(title)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Tuple4<List<BigInteger>, List<String>, List<BigInteger>, List<BigInteger>>> getBoardCards() {
        final Function function = new Function(FUNC_GETBOARDCARDS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Int32>>() {}, new TypeReference<DynamicArray<Utf8String>>() {}, new TypeReference<DynamicArray<Int64>>() {}, new TypeReference<DynamicArray<Int64>>() {}));
        return new RemoteCall<Tuple4<List<BigInteger>, List<String>, List<BigInteger>, List<BigInteger>>>(
                new Callable<Tuple4<List<BigInteger>, List<String>, List<BigInteger>, List<BigInteger>>>() {
                    @Override
                    public Tuple4<List<BigInteger>, List<String>, List<BigInteger>, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<List<BigInteger>, List<String>, List<BigInteger>, List<BigInteger>>(
                                convertToNative((List<Int32>) results.get(0).getValue()),
                                convertToNative((List<Utf8String>) results.get(1).getValue()),
                                convertToNative((List<Int64>) results.get(2).getValue()),
                                convertToNative((List<Int64>) results.get(3).getValue()));
                    }
                });
    }

    public RemoteCall<Tuple4<String, BigInteger, BigInteger, List<BigInteger>>> getBoard(BigInteger id) {
        final Function function = new Function(FUNC_GETBOARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(id)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}, new TypeReference<DynamicArray<Int32>>() {}));
        return new RemoteCall<Tuple4<String, BigInteger, BigInteger, List<BigInteger>>>(
                new Callable<Tuple4<String, BigInteger, BigInteger, List<BigInteger>>>() {
                    @Override
                    public Tuple4<String, BigInteger, BigInteger, List<BigInteger>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, BigInteger, BigInteger, List<BigInteger>>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                convertToNative((List<Int32>) results.get(3).getValue()));
                    }
                });
    }

    public RemoteCall<List> getGroupsIndices(BigInteger boardId) {
        final Function function = new Function(FUNC_GETGROUPSINDICES,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(boardId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Int32>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<String> getGroup(BigInteger groupId) {
        final Function function = new Function(FUNC_GETGROUP,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(groupId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<Tuple4<String, String, BigInteger, BigInteger>> getTask(BigInteger taskId) {
        final Function function = new Function(FUNC_GETTASK,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(taskId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Int64>() {}, new TypeReference<Int64>() {}));
        return new RemoteCall<Tuple4<String, String, BigInteger, BigInteger>>(
                new Callable<Tuple4<String, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<String, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> removeBoardFromUser(String userId, BigInteger boardId) {
        final Function function = new Function(
                FUNC_REMOVEBOARDFROMUSER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userId),
                        new org.web3j.abi.datatypes.generated.Int32(boardId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> removeUserFromBoard(String userId, BigInteger boardId) {
        final Function function = new Function(
                FUNC_REMOVEUSERFROMBOARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(userId),
                        new org.web3j.abi.datatypes.generated.Int32(boardId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> leaveBoard(BigInteger id) {
        final Function function = new Function(
                FUNC_LEAVEBOARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(id)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> eraseBoard(BigInteger id) {
        final Function function = new Function(
                FUNC_ERASEBOARD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Int32(id)),
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

    public static class BoardLeftEventResponse {
        public Log log;

        public Boolean left;
    }

    public static class BoardRemovedEventResponse {
        public Log log;

        public Boolean removed;
    }

    public static class GroupAddedEventResponse {
        public Log log;

        public BigInteger id;
    }

    public static class TaskAddedEventResponse {
        public Log log;

        public BigInteger id;
    }
}
