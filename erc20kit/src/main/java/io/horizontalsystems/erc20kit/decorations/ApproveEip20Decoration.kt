package io.horizontalsystems.erc20kit.decorations

import io.horizontalsystems.komercokit.contracts.ContractEvent
import io.horizontalsystems.komercokit.decorations.TransactionDecoration
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.TransactionTag
import java.math.BigInteger

class ApproveEip20Decoration(
    val contractAddress: Address,
    val spender: Address,
    val value: BigInteger
) : TransactionDecoration() {

    override fun tags(): List<String> =
        listOf(contractAddress.hex, TransactionTag.EIP20_APPROVE)

    companion object {
        val signature = ContractEvent(
            "Approval",
            listOf(
                ContractEvent.Argument.Address,
                ContractEvent.Argument.Address,
                ContractEvent.Argument.Uint256
            )
        ).signature
    }
}
