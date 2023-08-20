package io.horizontalsystems.uniswapkit.contract

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.models.Address
import java.math.BigInteger

class SwapExactTokensForETHMethod(
        val amountIn: BigInteger,
        val amountOutMin: BigInteger,
        val path: List<Address>,
        val to: Address,
        val deadline: BigInteger
) : ContractMethod() {

    override val methodSignature = Companion.methodSignature
    override fun getArguments() = listOf(amountIn, amountOutMin, path, to, deadline)

    companion object {
        const val methodSignature = "swapExactTokensForETH(uint256,uint256,address[],address,uint256)"
    }

}
