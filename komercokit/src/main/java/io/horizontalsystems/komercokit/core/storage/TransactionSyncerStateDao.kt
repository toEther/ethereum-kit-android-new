package io.horizontalsystems.komercokit.core.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.horizontalsystems.komercokit.models.TransactionSyncerState

@Dao
interface TransactionSyncerStateDao {

    @Query("SELECT * FROM `TransactionSyncerState` WHERE syncerId = :syncerId LIMIT 1")
    fun get(syncerId: String) : TransactionSyncerState?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(transactionSyncerState: TransactionSyncerState)

}
