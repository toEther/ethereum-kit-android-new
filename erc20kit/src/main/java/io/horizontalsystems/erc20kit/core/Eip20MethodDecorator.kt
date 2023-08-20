package io.horizontalsystems.erc20kit.core

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.contracts.ContractMethodFactories
import io.horizontalsystems.komercokit.core.IMethodDecorator

class Eip20MethodDecorator(
    private val contractMethodFactories: ContractMethodFactories
) : IMethodDecorator {

    override fun contractMethod(input: ByteArray): ContractMethod? =
        contractMethodFactories.createMethodFromInput(input)

}
