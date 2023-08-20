package io.horizontalsystems.uniswapkit.v3.quoter

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.models.Address
import java.math.BigInteger

class QuoteExactOutputSingleMethod(
    val tokenIn: Address,
    val tokenOut: Address,
    val fee: BigInteger,
    val amountOut: BigInteger,
    val sqrtPriceLimitX96: BigInteger,
) : ContractMethod() {
    override val methodSignature = "quoteExactOutputSingle(address,address,uint24,uint256,uint160)"
    override fun getArguments() = listOf(tokenIn, tokenOut, fee, amountOut, sqrtPriceLimitX96)

}
