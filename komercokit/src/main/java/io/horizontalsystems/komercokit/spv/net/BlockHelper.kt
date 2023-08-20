package io.horizontalsystems.komercokit.spv.net

import io.horizontalsystems.komercokit.core.ISpvStorage
import io.horizontalsystems.komercokit.network.INetwork
import io.horizontalsystems.komercokit.spv.models.BlockHeader

class BlockHelper(val storage: ISpvStorage, val network: INetwork) {

    val lastBlockHeader: BlockHeader
        get() = storage.getLastBlockHeader() ?: network.checkpointBlock

}
