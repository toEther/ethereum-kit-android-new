package io.horizontalsystems.komercokit.spv.net.tasks

import io.horizontalsystems.komercokit.network.INetwork
import io.horizontalsystems.komercokit.spv.core.ITask
import io.horizontalsystems.komercokit.spv.models.BlockHeader
import java.math.BigInteger

class HandshakeTask(val peerId: String, network: INetwork, blockHeader: BlockHeader) : ITask {
    val networkId: Int = network.id
    val genesisHash: ByteArray = network.genesisBlockHash
    val headTotalDifficulty: BigInteger = blockHeader.totalDifficulty
    val headHash: ByteArray = blockHeader.hashHex
    val headHeight: Long = blockHeader.height
}
