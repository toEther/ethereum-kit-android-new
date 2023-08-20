package io.horizontalsystems.nftkit.core

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.contracts.ContractMethodFactories
import io.horizontalsystems.komercokit.core.IMethodDecorator

class Eip1155MethodDecorator(
    private val contractMethodFactories: ContractMethodFactories
) : IMethodDecorator {
    override fun contractMethod(input: ByteArray): ContractMethod? {
        return contractMethodFactories.createMethodFromInput(input)
    }
}