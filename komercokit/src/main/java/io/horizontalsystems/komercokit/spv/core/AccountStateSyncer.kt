package io.horizontalsystems.komercokit.spv.core

import io.horizontalsystems.komercokit.core.ISpvStorage
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.spv.models.AccountStateSpv
import io.horizontalsystems.komercokit.spv.models.BlockHeader
import io.horizontalsystems.komercokit.spv.net.handlers.AccountStateTaskHandler
import io.horizontalsystems.komercokit.spv.net.tasks.AccountStateTask

class AccountStateSyncer(private val storage: ISpvStorage,
                         private val address: Address) : AccountStateTaskHandler.Listener {

    interface Listener {
        fun onUpdate(accountState: AccountStateSpv)
    }

    var listener: Listener? = null

    fun sync(taskPerformer: ITaskPerformer, blockHeader: BlockHeader) {
        taskPerformer.add(AccountStateTask(address, blockHeader))
    }

    override fun didReceive(accountState: AccountStateSpv, address: Address, blockHeader: BlockHeader) {
        storage.saveAccountSate(accountState)
        listener?.onUpdate(accountState)
    }

}
