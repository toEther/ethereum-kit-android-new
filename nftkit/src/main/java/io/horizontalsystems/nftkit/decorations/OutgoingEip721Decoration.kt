package io.horizontalsystems.nftkit.decorations

import io.horizontalsystems.komercokit.decorations.TransactionDecoration
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.TransactionTag
import io.horizontalsystems.nftkit.models.TokenInfo
import java.math.BigInteger

class OutgoingEip721Decoration(
    val contractAddress: Address,
    val to: Address,
    val tokenId: BigInteger,
    val sentToSelf: Boolean,
    val tokenInfo: TokenInfo?,
) : TransactionDecoration() {

    override fun tags(): List<String> =
        listOf(contractAddress.hex, EIP721_TRANSFER, TransactionTag.tokenOutgoing(contractAddress.hex), TransactionTag.OUTGOING)

    companion object {
        const val EIP721_TRANSFER = "eip721Transfer"
    }
}