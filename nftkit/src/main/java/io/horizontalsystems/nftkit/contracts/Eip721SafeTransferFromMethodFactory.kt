package io.horizontalsystems.nftkit.contracts

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.contracts.ContractMethodFactory
import io.horizontalsystems.komercokit.contracts.ContractMethodHelper
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.spv.core.toBigInteger

class Eip721SafeTransferFromMethodFactory : ContractMethodFactory {
    override val methodId = ContractMethodHelper.getMethodId(Eip721SafeTransferFromMethod.methodSignature)

    override fun createMethod(inputArguments: ByteArray): ContractMethod {
        val from = Address(inputArguments.copyOfRange(12, 32))
        val to = Address(inputArguments.copyOfRange(44, 64))
        val tokenId = inputArguments.copyOfRange(64, 96).toBigInteger()
        val data = inputArguments.copyOfRange(96, 128)

        return Eip721SafeTransferFromMethod(from, to, tokenId, data)
    }
}