package io.horizontalsystems.komercokit.decorations

import io.horizontalsystems.komercokit.contracts.ContractEventInstance
import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.contracts.EmptyMethod
import io.horizontalsystems.komercokit.core.ITransactionDecorator
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.InternalTransaction
import java.math.BigInteger

class KomercoDecorator(private val address: Address) : ITransactionDecorator {

    override fun decoration(from: Address?, to: Address?, value: BigInteger?, contractMethod: ContractMethod?, internalTransactions: List<InternalTransaction>, eventInstances: List<ContractEventInstance>): TransactionDecoration? {
        if (from == null || value == null) return null
        if (to == null) return ContractCreationDecoration()

        if (contractMethod != null && contractMethod is EmptyMethod) {
            if (from == address) {
                return OutgoingDecoration(to, value, to == address)
            }

            if (to == address) {
                return IncomingDecoration(from, value)
            }
        }

        return null
    }

}
