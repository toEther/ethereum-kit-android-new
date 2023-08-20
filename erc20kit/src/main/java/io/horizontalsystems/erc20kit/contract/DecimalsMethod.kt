package io.horizontalsystems.erc20kit.contract

import io.horizontalsystems.komercokit.contracts.ContractMethod

class DecimalsMethod: ContractMethod() {
    override var methodSignature = "decimals()"
    override fun getArguments() = listOf<Any>()
}
