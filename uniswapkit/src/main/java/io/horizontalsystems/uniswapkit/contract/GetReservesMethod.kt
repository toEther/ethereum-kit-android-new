package io.horizontalsystems.uniswapkit.contract

import io.horizontalsystems.komercokit.contracts.ContractMethod

class GetReservesMethod : ContractMethod() {

    override val methodSignature = "getReserves()"
    override fun getArguments() = listOf<Any>()

}
