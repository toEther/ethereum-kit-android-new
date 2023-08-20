package io.horizontalsystems.komercokit.decorations

import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.TransactionTag
import java.math.BigInteger

class IncomingDecoration(
    val from: Address,
    val value: BigInteger
) : TransactionDecoration() {

    override fun tags(): List<String> =
        listOf(TransactionTag.EVM_COIN, TransactionTag.EVM_COIN_INCOMING, TransactionTag.INCOMING)

}
