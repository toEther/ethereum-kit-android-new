package io.horizontalsystems.uniswapkit.v3

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.core.IMethodDecorator
import io.horizontalsystems.uniswapkit.v3.contract.UniswapV3ContractMethodFactories

class UniswapV3MethodDecorator(private val contractMethodFactories: UniswapV3ContractMethodFactories) :
    IMethodDecorator {

    override fun contractMethod(input: ByteArray): ContractMethod? =
        contractMethodFactories.createMethodFromInput(input)

}
