package io.horizontalsystems.erc20kit.core

import io.horizontalsystems.erc20kit.contract.AllowanceMethod
import io.horizontalsystems.erc20kit.contract.ApproveMethod
import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.core.toRawHexString
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.DefaultBlockParameter
import io.horizontalsystems.komercokit.models.TransactionData
import io.reactivex.Single
import java.math.BigInteger

class AllowanceManager(
        private val komercoKit: KomercoKit,
        private val contractAddress: Address,
        private val address: Address
) {

    fun allowance(spenderAddress: Address, defaultBlockParameter: DefaultBlockParameter): Single<BigInteger> {
        return komercoKit
                .call(contractAddress, AllowanceMethod(address, spenderAddress).encodedABI(), defaultBlockParameter)
                .map { result ->
                    BigInteger(result.sliceArray(0..31).toRawHexString(), 16)
                }
    }

    fun approveTransactionData(spenderAddress: Address, amount: BigInteger): TransactionData {
        return TransactionData(contractAddress, BigInteger.ZERO, ApproveMethod(spenderAddress, amount).encodedABI())
    }

}
