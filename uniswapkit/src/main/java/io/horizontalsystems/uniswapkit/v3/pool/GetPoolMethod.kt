package io.horizontalsystems.uniswapkit.v3.pool

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.models.Address
import java.math.BigInteger

class GetPoolMethod(
    private val tokenA: Address,
    private val tokenB: Address,
    private val fee: BigInteger
) : ContractMethod() {

    override val methodSignature = "getPool(address,address,uint24)"
    override fun getArguments() = listOf(tokenA, tokenB, fee)

}
