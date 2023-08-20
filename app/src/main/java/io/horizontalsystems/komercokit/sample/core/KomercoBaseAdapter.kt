package io.horizontalsystems.komercokit.sample.core

import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.core.toHexString
import io.horizontalsystems.komercokit.decorations.TransactionDecoration
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.FullTransaction
import io.horizontalsystems.komercokit.models.GasPrice
import io.horizontalsystems.oneinchkit.decorations.OneInchUnknownDecoration
import io.reactivex.Flowable
import io.reactivex.Single
import java.math.BigDecimal

open class KomercoBaseAdapter(private val komercoKit: KomercoKit) : IAdapter {

    private val decimal = 18

    override val name: String
        get() = "Ether"

    override val coin: String
        get() = "KMC"

    override val lastBlockHeight: Long?
        get() = komercoKit.lastBlockHeight

    override val syncState: KomercoKit.SyncState
        get() = komercoKit.syncState

    override val transactionsSyncState: KomercoKit.SyncState
        get() = komercoKit.transactionsSyncState

    override val balance: BigDecimal
        get() = komercoKit.accountState?.balance?.toBigDecimal()?.movePointLeft(decimal)
            ?: BigDecimal.ZERO

    override val receiveAddress: Address
        get() = komercoKit.receiveAddress

    override val lastBlockHeightFlowable: Flowable<Unit>
        get() = komercoKit.lastBlockHeightFlowable.map { }

    override val syncStateFlowable: Flowable<Unit>
        get() = komercoKit.syncStateFlowable.map { }

    override val transactionsSyncStateFlowable: Flowable<Unit>
        get() = komercoKit.transactionsSyncStateFlowable.map { }

    override val balanceFlowable: Flowable<Unit>
        get() = komercoKit.accountStateFlowable.map { }

    override val transactionsFlowable: Flowable<Unit>
        get() = komercoKit.allTransactionsFlowable.map { }


    override fun start() {
        komercoKit.start()
    }

    override fun stop() {
        komercoKit.stop()
    }

    override fun refresh() {
        komercoKit.refresh()
    }

    override fun estimatedGasLimit(
        toAddress: Address,
        value: BigDecimal,
        gasPrice: GasPrice
    ): Single<Long> {
        return komercoKit.estimateGas(
            toAddress,
            value.movePointRight(decimal).toBigInteger(),
            gasPrice
        )
    }

    override fun send(
        address: Address,
        amount: BigDecimal,
        gasPrice: GasPrice,
        gasLimit: Long
    ): Single<FullTransaction> {
        throw Exception("Subclass must override")
    }

    override fun transactions(fromHash: ByteArray?, limit: Int?): Single<List<TransactionRecord>> {
        return komercoKit.getFullTransactionsAsync(listOf(), fromHash, limit)
            .map { transactions ->
                transactions.map { transactionRecord(it) }
            }
    }

    private fun transactionRecord(fullTransaction: FullTransaction): TransactionRecord {
        val transaction = fullTransaction.transaction
        val mineAddress = komercoKit.receiveAddress

        var amount: BigDecimal = 0.toBigDecimal()

        transaction.value?.toBigDecimal()?.let {
            amount = it.movePointLeft(decimal)
        }

        return TransactionRecord(
            transactionHash = transaction.hash.toHexString(),
            timestamp = transaction.timestamp,
            isError = fullTransaction.transaction.isFailed,
            from = transaction.from,
            to = transaction.to,
            amount = amount,
            blockHeight = transaction.blockNumber,
            transactionIndex = transaction.transactionIndex ?: 0,
            decoration = fullTransaction.decoration.describe()
        )
    }
}

fun TransactionDecoration.describe(): String =
    when (this) {
        is OneInchUnknownDecoration -> {
            val _out = this.tokenAmountOut?.let { "${it.value} (${it.token.toString()})" } ?: "n/a"
            val _in = this.tokenAmountIn?.let { "${it.value} (${it.token.toString()})" } ?: "n/a"

            "OneInchUnknownDecoration($_out <-> $_in)"
        }

        else -> this.toString()
    }

