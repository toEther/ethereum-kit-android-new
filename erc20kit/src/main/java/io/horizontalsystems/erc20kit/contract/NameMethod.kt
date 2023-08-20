package io.horizontalsystems.erc20kit.contract

import io.horizontalsystems.komercokit.contracts.ContractMethod

class NameMethod: ContractMethod() {
    override var methodSignature = "name()"
    override fun getArguments() = listOf<Any>()
}
