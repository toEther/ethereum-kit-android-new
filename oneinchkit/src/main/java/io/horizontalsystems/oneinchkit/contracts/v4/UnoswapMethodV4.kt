package io.horizontalsystems.oneinchkit.contracts.v4

import io.horizontalsystems.komercokit.contracts.Bytes32Array
import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.models.Address
import java.math.BigInteger

class UnoswapMethodV4(
        val srcToken: Address,
        val amount: BigInteger,
        val minReturn: BigInteger,
        val params: Bytes32Array
) : ContractMethod() {

    override val methodSignature = Companion.methodSignature

    override fun getArguments() = listOf(srcToken, amount, minReturn, params)

    companion object {
        val methodSignature = "unoswap(address,uint256,uint256,bytes32[])"
    }

}
