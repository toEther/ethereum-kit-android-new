package io.horizontalsystems.komercokit.network

import io.horizontalsystems.komercokit.spv.models.BlockHeader

interface INetwork {
    val id: Int
    val genesisBlockHash: ByteArray
    val checkpointBlock: BlockHeader
    val blockTime: Long
}