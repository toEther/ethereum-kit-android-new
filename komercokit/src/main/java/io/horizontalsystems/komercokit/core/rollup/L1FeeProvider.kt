package io.horizontalsystems.komercokit.core.rollup

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.core.TransactionBuilder
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.GasPrice
import io.horizontalsystems.komercokit.models.RawTransaction
import io.horizontalsystems.komercokit.spv.core.toBigInteger
import io.reactivex.Single
import java.math.BigInteger

class L1FeeProvider(
        private val evmKit: KomercoKit,
        private val contractAddress: Address
) {

    class L1FeeMethod(val transaction: ByteArray) : ContractMethod() {
        override val methodSignature = "getL1Fee(bytes)"
        override fun getArguments() = listOf(transaction)
    }

    fun getL1Fee(gasPrice: GasPrice, gasLimit: Long, to: Address, value: BigInteger, data: ByteArray): Single<BigInteger> {
        val rawTransaction = RawTransaction(gasPrice, gasLimit, to, value, 1, data)
        val encoded = TransactionBuilder.encode(rawTransaction, null, evmKit.chain.id)
        val feeMethodABI = L1FeeMethod(encoded).encodedABI()

        return evmKit.call(contractAddress, feeMethodABI)
                .map { it.sliceArray(IntRange(0, 31)).toBigInteger() }
    }

}
