package io.horizontalsystems.komercokit.spv.net.les.messages

import io.horizontalsystems.komercokit.spv.core.toLong
import io.horizontalsystems.komercokit.spv.net.IInMessage
import io.horizontalsystems.komercokit.spv.net.les.TrieNode
import io.horizontalsystems.komercokit.spv.rlp.RLP
import io.horizontalsystems.komercokit.spv.rlp.RLPList

class ProofsMessage(data: ByteArray) : IInMessage {

    var requestID: Long = 0
    var bv: Long = 0
    var nodes: MutableList<TrieNode> = mutableListOf()

    init {
        val params = RLP.decode2(data)[0] as RLPList
        this.requestID = params[0].rlpData.toLong()
        this.bv = params[1].rlpData.toLong()
        val rlpList = params[2] as RLPList
        if (rlpList.isNotEmpty()) {
            for (rlpNode in rlpList) {
                nodes.add(TrieNode(rlpNode as RLPList))
            }
        }
    }

    override fun toString(): String {
        return "Proofs [requestID: $requestID; bv: $bv; nodes: [${nodes.joinToString(separator = ", ") { it.toString() }}]]"
    }

}
