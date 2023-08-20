package io.horizontalsystems.erc20kit.contract

import io.horizontalsystems.komercokit.contracts.ContractMethod

class SymbolMethod: ContractMethod() {
    override var methodSignature = "symbol()"
    override fun getArguments() = listOf<Any>()
}
