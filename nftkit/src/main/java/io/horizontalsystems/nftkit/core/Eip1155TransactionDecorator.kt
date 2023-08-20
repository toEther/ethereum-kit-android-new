package io.horizontalsystems.nftkit.core

import io.horizontalsystems.komercokit.contracts.ContractEventInstance
import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.core.ITransactionDecorator
import io.horizontalsystems.komercokit.decorations.TransactionDecoration
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.InternalTransaction
import io.horizontalsystems.nftkit.contracts.Eip1155SafeTransferFromMethod
import io.horizontalsystems.nftkit.decorations.OutgoingEip1155Decoration
import io.horizontalsystems.nftkit.events.Eip1155TransferEventInstance
import java.math.BigInteger

class Eip1155TransactionDecorator(
    private val userAddress: Address
) : ITransactionDecorator {

    override fun decoration(
        from: Address?,
        to: Address?,
        value: BigInteger?,
        contractMethod: ContractMethod?,
        internalTransactions: List<InternalTransaction>,
        eventInstances: List<ContractEventInstance>
    ): TransactionDecoration? {
        if (from == null || to == null || value == null || contractMethod == null) return null

        return when {
            contractMethod is Eip1155SafeTransferFromMethod && from == userAddress -> {
                OutgoingEip1155Decoration(
                    contractAddress = to,
                    to = contractMethod.to,
                    tokenId = contractMethod.tokenId,
                    value = contractMethod.value,
                    sentToSelf = contractMethod.to == userAddress,
                    tokenInfo = eventInstances.mapNotNull { it as? Eip1155TransferEventInstance }.firstOrNull { it.contractAddress == to }?.tokenInfo
                )
            }
            else -> null
        }
    }
}