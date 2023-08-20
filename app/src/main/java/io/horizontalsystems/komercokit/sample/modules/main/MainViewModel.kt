package io.horizontalsystems.komercokit.sample.modules.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.horizontalsystems.erc20kit.core.Erc20Kit
import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.core.KomercoKit.SyncState
import io.horizontalsystems.komercokit.core.eip1559.Eip1559GasPriceProvider
import io.horizontalsystems.komercokit.core.signer.Signer
import io.horizontalsystems.komercokit.core.toHexString
import io.horizontalsystems.komercokit.models.*
import io.horizontalsystems.komercokit.sample.App
import io.horizontalsystems.komercokit.sample.Configuration
import io.horizontalsystems.komercokit.sample.SingleLiveEvent
import io.horizontalsystems.komercokit.sample.core.Erc20Adapter
import io.horizontalsystems.komercokit.sample.core.KomercoAdapter
import io.horizontalsystems.komercokit.sample.core.TransactionRecord
import io.horizontalsystems.hdwalletkit.Mnemonic
import io.horizontalsystems.oneinchkit.OneInchKit
import io.horizontalsystems.uniswapkit.UniswapKit
import io.horizontalsystems.uniswapkit.UniswapV3Kit
import io.horizontalsystems.uniswapkit.models.SwapData
import io.horizontalsystems.uniswapkit.models.Token
import io.horizontalsystems.uniswapkit.models.TradeData
import io.horizontalsystems.uniswapkit.models.TradeOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.util.logging.Logger

class MainViewModel : ViewModel() {
    private val logger = Logger.getLogger("MainViewModel")

    private val disposables = CompositeDisposable()

    lateinit var komercoKit: KomercoKit
    lateinit var komercoAdapter: KomercoAdapter
    lateinit var signer: Signer

    lateinit var erc20Adapter: Erc20Adapter

    val transactions = MutableLiveData<List<TransactionRecord>>()
    val balance = MutableLiveData<BigDecimal>()
    val lastBlockHeight = MutableLiveData<Long>()
    val syncState = MutableLiveData<SyncState>()
    val transactionsSyncState = MutableLiveData<SyncState>()
    val erc20SyncState = MutableLiveData<SyncState>()
    val erc20TransactionsSyncState = MutableLiveData<SyncState>()

    val erc20TokenBalance = MutableLiveData<BigDecimal>()
    val sendStatus = SingleLiveEvent<Throwable?>()
    val estimatedGas = SingleLiveEvent<String>()
    val showTxTypeLiveData = MutableLiveData<ShowTxType>()

    private var showTxType = ShowTxType.Eth

    private val recommendedPriorityFee: Long? = null
    private var gasPrice: GasPrice = GasPrice.Legacy(20_000_000_000)

    private var ethTxs = listOf<TransactionRecord>()
    private var erc20Txs = listOf<TransactionRecord>()

    private lateinit var uniswapKit: UniswapKit
    private val tradeOptions = TradeOptions(allowedSlippagePercent = BigDecimal("0.5"))
    var swapData = MutableLiveData<SwapData?>()
    var tradeData = MutableLiveData<TradeData?>()
    val swapStatus = SingleLiveEvent<Throwable?>()

    val fromToken: Erc20Token = Configuration.erc20Tokens[0]
    val toToken: Erc20Token = Configuration.erc20Tokens[1]
    lateinit var gasPriceHelper: GasPriceHelper

    fun init() {
        val words = Configuration.defaultsWords.split(" ")
        val seed = Mnemonic().toSeed(words)
        signer = Signer.getInstance(seed, Configuration.chain)
        komercoKit = createKit()
        komercoAdapter = KomercoAdapter(komercoKit, signer)
        erc20Adapter = Erc20Adapter(App.instance, fromToken ?: toToken
        ?: Configuration.erc20Tokens.first(), komercoKit, signer)
        uniswapKit = UniswapKit.getInstance(komercoKit)

        Erc20Kit.addTransactionSyncer(komercoKit)
        Erc20Kit.addDecorators(komercoKit)
        UniswapKit.addDecorators(komercoKit)
        UniswapV3Kit.addDecorators(komercoKit)
        OneInchKit.addDecorators(komercoKit)

        updateBalance()
        updateErc20Balance()
        updateState()
        updateTransactionsSyncState()
        updateErc20State()
        updateErc20TransactionsSyncState()
        updateLastBlockHeight()

        filterTransactions(true)

        //
        // Komerco
        //

        komercoAdapter.lastBlockHeightFlowable.subscribe {
            updateLastBlockHeight()
            updateEthTransactions()
        }.let {
            disposables.add(it)
        }

        komercoAdapter.transactionsFlowable.subscribe {
            updateEthTransactions()
        }.let {
            disposables.add(it)
        }

        komercoAdapter.balanceFlowable.subscribe {
            updateBalance()
        }.let {
            disposables.add(it)
        }

        komercoAdapter.syncStateFlowable.subscribe {
            updateState()
        }.let {
            disposables.add(it)
        }

        komercoAdapter.transactionsSyncStateFlowable.subscribe {
            updateTransactionsSyncState()
        }.let {
            disposables.add(it)
        }


        //
        // ERC20
        //

        erc20Adapter.transactionsFlowable.subscribe {
            updateErc20Transactions()
        }.let {
            disposables.add(it)
        }

        erc20Adapter.balanceFlowable.subscribe {
            updateErc20Balance()
        }.let {
            disposables.add(it)
        }

        erc20Adapter.syncStateFlowable.subscribe {
            updateErc20State()
        }.let {
            disposables.add(it)
        }

        erc20Adapter.transactionsSyncStateFlowable.subscribe {
            updateErc20TransactionsSyncState()
        }.let {
            disposables.add(it)
        }

        komercoAdapter.start()
        erc20Adapter.start()

        gasPriceHelper = GasPriceHelper(Eip1559GasPriceProvider(komercoKit))
        gasPriceHelper.gasPriceFlowable()
            .subscribe({
                gasPrice = it
                Log.e("AAA", "set gasPrice: $gasPrice")
            }, {
                Log.e(
                    "AAA",
                    "error: ${it.localizedMessage ?: it.message ?: it.javaClass.simpleName}"
                )
            }).let { disposables.add(it) }
    }

    private fun createKit(): KomercoKit {
        val rpcSource: RpcSource?
        val transactionSource: TransactionSource?

        when (Configuration.chain) {
            Chain.BinanceSmartChain -> {
                transactionSource = TransactionSource.bscscan(Configuration.bscScanKey)
                rpcSource = RpcSource.binanceSmartChainHttp()
            }
            Chain.Komerco -> {
                transactionSource = TransactionSource.komercoEtherscan(Configuration.etherscanKey)
                rpcSource = if (Configuration.webSocket)
                    RpcSource.komercoInfuraWebSocket(Configuration.infuraProjectId, Configuration.infuraSecret)
                else
                    RpcSource.komercoInfuraHttp(Configuration.infuraProjectId, Configuration.infuraSecret)
            }
            Chain.ArbitrumOne -> {
                transactionSource = TransactionSource.arbiscan(Configuration.arbiscanApiKey)
                rpcSource = RpcSource.arbitrumOneRpcHttp()
            }
            Chain.KomercoGoerli -> {
                transactionSource = TransactionSource.goerliEtherscan(Configuration.etherscanKey)
                rpcSource = if (Configuration.webSocket)
                    RpcSource.goerliInfuraWebSocket(Configuration.infuraProjectId, Configuration.infuraSecret)
                else
                    RpcSource.goerliInfuraHttp(Configuration.infuraProjectId, Configuration.infuraSecret)
            }
            else -> {
                rpcSource = null
                transactionSource = null
            }
        }

        checkNotNull(rpcSource) {
            throw Exception("Could not get rpcSource!")
        }

        checkNotNull(transactionSource) {
            throw Exception("Could not get transactionSource!")
        }

        return if (Configuration.watchAddress != null) {
            KomercoKit.getInstance(
                App.instance, Address(Configuration.watchAddress),
                Configuration.chain, rpcSource, transactionSource,
                Configuration.walletId
            )
        } else {
            val words = Configuration.defaultsWords.split(" ")
            KomercoKit.getInstance(
                App.instance, words, "",
                Configuration.chain, rpcSource, transactionSource,
                Configuration.walletId
            )
        }
    }

    private fun updateLastBlockHeight() {
        lastBlockHeight.postValue(komercoKit.lastBlockHeight)
    }

    private fun updateState() {
        syncState.postValue(komercoAdapter.syncState)
    }

    private fun updateTransactionsSyncState() {
        transactionsSyncState.postValue(komercoAdapter.transactionsSyncState)
    }

    private fun updateErc20State() {
        erc20SyncState.postValue(erc20Adapter.syncState)
    }

    private fun updateErc20TransactionsSyncState() {
        erc20TransactionsSyncState.postValue(erc20Adapter.transactionsSyncState)
    }

    private fun updateBalance() {
        balance.postValue(komercoAdapter.balance)
    }

    private fun updateErc20Balance() {
        erc20TokenBalance.postValue(erc20Adapter.balance)
    }

    private fun updateEthTransactions() {
        komercoAdapter.transactions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list: List<TransactionRecord> ->
                ethTxs = list
                updateTransactionList()
            }.let {
                disposables.add(it)
            }
    }

    private fun updateErc20Transactions() {
        erc20Adapter.transactions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list: List<TransactionRecord> ->
                erc20Txs = list
                updateTransactionList()
            }.let {
                disposables.add(it)
            }
    }

    private fun updateTransactionList() {
        val list = when (showTxType) {
            ShowTxType.Eth -> ethTxs
            ShowTxType.Erc20 -> erc20Txs
        }
        transactions.value = list
    }


    //
    // Komerco
    //

    fun refresh() {
        komercoAdapter.refresh()
        erc20Adapter.refresh()
    }

    fun clear() {
        KomercoKit.clear(App.instance, Configuration.chain, Configuration.walletId)
        Erc20Kit.clear(App.instance, Configuration.chain, Configuration.walletId)
        init()
    }

    fun receiveAddress(): String {
        return komercoKit.receiveAddress.hex
    }

    fun estimateGas(toAddress: String?, value: BigDecimal, isErc20: Boolean) {
        estimatedGas.postValue(null)

        if (toAddress == null) return

        val estimateSingle = if (isErc20)
            erc20Adapter.estimatedGasLimit(Address(toAddress), value, gasPrice)
        else
            komercoAdapter.estimatedGasLimit(Address(toAddress), value, gasPrice)

        estimateSingle
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //success
                estimatedGas.value = it.toString()
            }, {
                logger.warning("Gas estimate: ${it.message}")
                estimatedGas.value = it.message
            })
            .let { disposables.add(it) }
    }

    fun send(toAddress: String, amount: BigDecimal) {
        val gasLimit = estimatedGas.value?.toLongOrNull() ?: kotlin.run {
            sendStatus.value = Exception("No gas limit!!")
            return
        }

        komercoAdapter.send(Address(toAddress), amount, gasPrice, gasLimit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ fullTransaction ->
                //success
                logger.info("Successfully sent, hash: ${fullTransaction.transaction.hash.toHexString()}")

                sendStatus.value = null
            }, {
                logger.warning("Ether send failed: ${it.message}")
                sendStatus.value = it
            }).let { disposables.add(it) }

    }

    //
    // ERC20
    //

    fun sendERC20(toAddress: String, amount: BigDecimal) {
        val gasLimit = estimatedGas.value?.toLongOrNull() ?: kotlin.run {
            sendStatus.value = Exception("No gas limit!!")
            return
        }

        erc20Adapter.send(Address(toAddress), amount, gasPrice, gasLimit)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ fullTransaction ->
                logger.info("Successfully sent, hash: ${fullTransaction.transaction.hash.toHexString()}")
                //success
                sendStatus.value = null
            }, {
                logger.warning("Erc20 send failed: ${it.message}")
                sendStatus.value = it
            }).let { disposables.add(it) }
    }

    fun filterTransactions(ethTx: Boolean) {
        showTxType = if (ethTx) {
            updateEthTransactions()
            ShowTxType.Eth
        } else {
            updateErc20Transactions()
            ShowTxType.Erc20
        }
        showTxTypeLiveData.postValue(showTxType)
    }

    //
    // SWAP
    //


    fun syncSwapData() {
        val tokenIn = uniswapToken(fromToken)
        val tokenOut = uniswapToken(toToken)

        uniswapKit.swapData(tokenIn, tokenOut)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                swapData.value = it
            }, {
                logger.warning("swapData ERROR = ${it.message}")
            }).let {
                disposables.add(it)
            }
    }

    fun syncAllowance() {
        erc20Adapter.allowance(uniswapKit.routerAddress)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                logger.info("allowance: ${it.toPlainString()}")
            }, {
                logger.warning("swapData ERROR = ${it.message}")
            }).let {
                disposables.add(it)
            }
    }

    fun approve(decimalAmount: BigDecimal) {
        val spenderAddress = uniswapKit.routerAddress

        val token = fromToken ?: return
        val amount = decimalAmount.movePointRight(token.decimals).toBigInteger()

        val transactionData = erc20Adapter.approveTransactionData(spenderAddress, amount)

        komercoKit.estimateGas(transactionData, gasPrice)
            .flatMap { gasLimit ->
                logger.info("gas limit: $gasLimit")
                komercoKit.rawTransaction(transactionData, gasPrice, gasLimit)
            }
            .flatMap { rawTransaction ->
                val signature = signer.signature(rawTransaction)
                komercoKit.send(rawTransaction, signature)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ fullTransaction ->
                logger.info("approve: ${fullTransaction.transaction.hash}")
            }, {
                logger.warning("approve ERROR = ${it.message}")
            }).let {
                disposables.add(it)
            }
    }

    private fun uniswapToken(token: Erc20Token?): Token {
        if (token == null)
            return uniswapKit.etherToken()

        return uniswapKit.token(token.contractAddress, token.decimals)
    }


    fun onChangeAmountIn(amountIn: BigDecimal) {
        swapData.value?.let {
            tradeData.value = try {
                uniswapKit.bestTradeExactIn(it, amountIn, tradeOptions)
            } catch (error: Throwable) {
                logger.info("bestTradeExactIn error: ${error.javaClass.simpleName} (${error.localizedMessage})")
                null
            }
        }
    }

    fun onChangeAmountOut(amountOut: BigDecimal) {
        swapData.value?.let {
            tradeData.value = try {
                uniswapKit.bestTradeExactOut(it, amountOut, tradeOptions)
            } catch (error: Throwable) {
                logger.info("bestTradeExactOut error: ${error.javaClass.simpleName} (${error.localizedMessage})")
                null
            }
        }
    }


    fun swap() {
        tradeData.value?.let { tradeData ->

            val transactionData = uniswapKit.transactionData(tradeData)
            komercoKit.estimateGas(transactionData, gasPrice)
                .flatMap { gasLimit ->
                    logger.info("gas limit: $gasLimit")

                    val transactionData = uniswapKit.transactionData(tradeData)
                    komercoKit.rawTransaction(transactionData, gasPrice, gasLimit)
                }
                .flatMap { rawTransaction ->
                    val signature = signer.signature(rawTransaction)
                    komercoKit.send(rawTransaction, signature)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ fullTransaction ->
                    swapStatus.value = null
                    logger.info("swap SUCCESS, txHash=${fullTransaction.transaction.hash.toHexString()}")
                }, {
                    swapStatus.value = it
                    logger.info("swap ERROR, error=${it.message}")
                    it.printStackTrace()
                }).let { disposables.add(it) }
        }
    }

}

enum class ShowTxType {
    Eth, Erc20
}

data class Erc20Token(
    val name: String,
    val code: String,
    val contractAddress: Address,
    val decimals: Int)
