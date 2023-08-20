package io.horizontalsystems.komercokit.sample.core

import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.FullTransaction
import io.horizontalsystems.komercokit.models.GasPrice
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal

interface IAdapter {

    val name: String
    val coin: String

    val lastBlockHeight: Long?
    val syncState: KomercoKit.SyncState
    val transactionsSyncState: KomercoKit.SyncState
    val balance: BigDecimal

    val receiveAddress: Address

    val lastBlockHeightFlowable: Flowable<Unit>
    val syncStateFlowable: Flowable<Unit>
    val transactionsSyncStateFlowable: Flowable<Unit>
    val balanceFlowable: Flowable<Unit>
    val transactionsFlowable: Flowable<Unit>

    fun start()
    fun stop()
    fun refresh()
    fun send(address: Address, amount: BigDecimal, gasPrice: GasPrice, gasLimit: Long): Single<FullTransaction>
    fun transactions(fromHash: ByteArray? = null, limit: Int? = null): Single<List<TransactionRecord>>

    fun estimatedGasLimit(toAddress: Address, value: BigDecimal, gasPrice: GasPrice): Single<Long>
}
