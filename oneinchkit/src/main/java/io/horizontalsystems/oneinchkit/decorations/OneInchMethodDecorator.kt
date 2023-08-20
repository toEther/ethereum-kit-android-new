package io.horizontalsystems.oneinchkit.decorations

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.core.IMethodDecorator
import io.horizontalsystems.oneinchkit.contracts.OneInchContractMethodFactories

class OneInchMethodDecorator(private val contractMethodFactories: OneInchContractMethodFactories) : IMethodDecorator {

    override fun contractMethod(input: ByteArray): ContractMethod? =
        contractMethodFactories.createMethodFromInput(input)

}
