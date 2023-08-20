package io.horizontalsystems.komercokit.transactionsyncers

import io.horizontalsystems.komercokit.core.ITransactionProvider
import io.horizontalsystems.komercokit.core.ITransactionSyncer
import io.horizontalsystems.komercokit.core.storage.TransactionSyncerStateStorage
import io.horizontalsystems.komercokit.models.ProviderTransaction
import io.horizontalsystems.komercokit.models.Transaction
import io.horizontalsystems.komercokit.models.TransactionSyncerState
import io.reactivex.Single

class KomercoTransactionSyncer(
        private val transactionProvider: ITransactionProvider,
        private val storage: TransactionSyncerStateStorage
) : ITransactionSyncer {

    companion object {
        const val SyncerId = "komerco-transaction-syncer"
    }

    override fun getTransactionsSingle(): Single<Pair<List<Transaction>, Boolean>> {
        val lastTransactionBlockNumber = storage.get(SyncerId)?.lastBlockNumber ?: 0
        val initial = lastTransactionBlockNumber == 0L

        return transactionProvider.getTransactions(lastTransactionBlockNumber + 1)
                .doOnSuccess { providerTransactions -> handle(providerTransactions) }
                .map { providerTransactions ->
                    val array = providerTransactions.map { transaction ->
                        val isFailed = when {
                            transaction.txReceiptStatus != null -> {
                                transaction.txReceiptStatus != 1
                            }
                            transaction.isError != null -> {
                                transaction.isError != 0
                            }
                            transaction.gasUsed != null -> {
                                transaction.gasUsed == transaction.gasLimit
                            }
                            else -> {
                                false
                            }
                        }

                        Transaction(
                                hash = transaction.hash,
                                timestamp = transaction.timestamp,
                                isFailed = isFailed,
                                blockNumber = transaction.blockNumber,
                                transactionIndex = transaction.transactionIndex,
                                from = transaction.from,
                                to = transaction.to,
                                value = transaction.value,
                                input = transaction.input,
                                nonce = transaction.nonce,
                                gasPrice = transaction.gasPrice,
                                gasUsed = transaction.gasUsed
                        )
                    }

                    Pair(array, initial)
                }
                .onErrorReturnItem(Pair(listOf(), initial))
    }

    private fun handle(transactions: List<ProviderTransaction>) {
        val maxBlockNumber = transactions.maxOfOrNull { it.blockNumber } ?: return
        val syncerState = TransactionSyncerState(SyncerId, maxBlockNumber)

        storage.save(syncerState)
    }

}
