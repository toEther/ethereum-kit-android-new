package io.horizontalsystems.erc20kit.core

import io.horizontalsystems.erc20kit.contract.ApproveMethod
import io.horizontalsystems.erc20kit.contract.TransferMethod
import io.horizontalsystems.erc20kit.decorations.ApproveEip20Decoration
import io.horizontalsystems.erc20kit.decorations.OutgoingEip20Decoration
import io.horizontalsystems.erc20kit.events.TransferEventInstance
import io.horizontalsystems.komercokit.contracts.ContractEventInstance
import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.core.ITransactionDecorator
import io.horizontalsystems.komercokit.decorations.TransactionDecoration
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.InternalTransaction
import java.math.BigInteger

class Eip20TransactionDecorator(
    private val userAddress: Address
) : ITransactionDecorator {

    override fun decoration(from: Address?, to: Address?, value: BigInteger?, contractMethod: ContractMethod?, internalTransactions: List<InternalTransaction>, eventInstances: List<ContractEventInstance>): TransactionDecoration? {
        if (from == null || to == null || value == null || contractMethod == null) return null

        if (contractMethod is TransferMethod && from == userAddress) {
            return OutgoingEip20Decoration(
                to,
                contractMethod.to,
                contractMethod.value,
                contractMethod.to == userAddress,
                eventInstances.mapNotNull { it as? TransferEventInstance }.firstOrNull { it.contractAddress == to }?.tokenInfo
            )
        }

        if (contractMethod is ApproveMethod) {
            return ApproveEip20Decoration(
                to,
                contractMethod.spender,
                contractMethod.value
            )
        }

        return null
    }

}
