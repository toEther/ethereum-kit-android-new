package io.horizontalsystems.komercokit.core

import android.content.Context
import io.horizontalsystems.komercokit.api.storage.ApiDatabase
import io.horizontalsystems.komercokit.core.storage.Eip20Database
import io.horizontalsystems.komercokit.core.storage.TransactionDatabase
import io.horizontalsystems.komercokit.models.Chain
import io.horizontalsystems.komercokit.spv.core.storage.SpvDatabase

internal object KomercoDatabaseManager {

    fun getKomercoApiDatabase(context: Context, walletId: String, chain: Chain): ApiDatabase {
        return ApiDatabase.getInstance(context, getDbNameApi(walletId, chain))
    }

    fun getKomercoSpvDatabase(context: Context, walletId: String, chain: Chain): SpvDatabase {
        return SpvDatabase.getInstance(context, getDbNameSpv(walletId, chain))
    }

    fun getTransactionDatabase(context: Context, walletId: String, chain: Chain): TransactionDatabase {
        return TransactionDatabase.getInstance(context, getDbNameTransactions(walletId, chain))
    }

    fun getErc20Database(context: Context, walletId: String, chain: Chain): Eip20Database {
        return Eip20Database.getInstance(context, getDbNameErc20Events(walletId, chain))
    }

    fun clear(context: Context, chain: Chain, walletId: String) {
        synchronized(this) {
            context.deleteDatabase(getDbNameApi(walletId, chain))
            context.deleteDatabase(getDbNameSpv(walletId, chain))
            context.deleteDatabase(getDbNameTransactions(walletId, chain))
            context.deleteDatabase(getDbNameErc20Events(walletId, chain))
        }
    }

    private fun getDbNameApi(walletId: String, chain: Chain): String {
        return getDbName(chain, walletId, "api")
    }

    private fun getDbNameSpv(walletId: String, chain: Chain): String {
        return getDbName(chain, walletId, "spv")
    }

    private fun getDbNameTransactions(walletId: String, chain: Chain): String {
        return getDbName(chain, walletId, "txs")
    }

    private fun getDbNameErc20Events(walletId: String, chain: Chain): String {
        return getDbName(chain, walletId, "erc20_events")
    }

    private fun getDbName(chain: Chain, walletId: String, suffix: String): String {
        return "Komerco-${chain.id}-$walletId-$suffix"
    }
}
