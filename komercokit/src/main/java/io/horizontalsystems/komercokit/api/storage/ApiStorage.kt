package io.horizontalsystems.komercokit.api.storage

import io.horizontalsystems.komercokit.api.models.AccountState
import io.horizontalsystems.komercokit.api.models.LastBlockHeight
import io.horizontalsystems.komercokit.core.IApiStorage

class ApiStorage(
        private val database: ApiDatabase
) : IApiStorage {

    override fun getLastBlockHeight(): Long? {
        return database.lastBlockHeightDao().getLastBlockHeight()?.height
    }

    override fun saveLastBlockHeight(lastBlockHeight: Long) {
        database.lastBlockHeightDao().insert(LastBlockHeight(lastBlockHeight))
    }

    override fun saveAccountState(state: AccountState) {
        database.balanceDao().insert(state)
    }

    override fun getAccountState(): AccountState? {
        return database.balanceDao().getAccountState()
    }

}
