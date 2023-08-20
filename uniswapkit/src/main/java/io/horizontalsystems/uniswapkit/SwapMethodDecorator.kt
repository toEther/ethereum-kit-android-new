package io.horizontalsystems.uniswapkit

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.core.IMethodDecorator
import io.horizontalsystems.uniswapkit.contract.SwapContractMethodFactories

class SwapMethodDecorator(private val contractMethodFactories: SwapContractMethodFactories) : IMethodDecorator {

    override fun contractMethod(input: ByteArray): ContractMethod? =
        contractMethodFactories.createMethodFromInput(input)

}
