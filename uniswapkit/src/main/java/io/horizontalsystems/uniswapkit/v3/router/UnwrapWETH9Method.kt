package io.horizontalsystems.uniswapkit.v3.router

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.contracts.ContractMethodFactory
import io.horizontalsystems.komercokit.contracts.ContractMethodHelper
import io.horizontalsystems.komercokit.models.Address
import java.math.BigInteger

class UnwrapWETH9Method(
    val amountMinimum: BigInteger,
    val recipient: Address
) : ContractMethod() {
    override val methodSignature = Companion.methodSignature
    override fun getArguments() = listOf(amountMinimum, recipient)

    companion object {
        private const val methodSignature = "unwrapWETH9(uint256,address)"
    }

    class Factory : ContractMethodFactory {
        override val methodId = ContractMethodHelper.getMethodId(methodSignature)

        override fun createMethod(inputArguments: ByteArray): ContractMethod {
            val parsedArguments = ContractMethodHelper.decodeABI(
                inputArguments, listOf(
                    BigInteger::class,
                    Address::class,
                )
            )

            return UnwrapWETH9Method(
                amountMinimum = parsedArguments[0] as BigInteger,
                recipient = parsedArguments[1] as Address,
            )
        }
    }
}
