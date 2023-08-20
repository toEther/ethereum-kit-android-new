package io.horizontalsystems.erc20kit.core

import io.horizontalsystems.erc20kit.contract.BalanceOfMethod
import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.spv.core.toBigInteger
import io.reactivex.Single
import java.math.BigInteger

class DataProvider(
        private val komercoKit: KomercoKit
) : IDataProvider {

    override fun getBalance(contractAddress: Address, address: Address): Single<BigInteger> {
        return komercoKit.call(contractAddress, BalanceOfMethod(address).encodedABI())
                .map { it.sliceArray(IntRange(0, 31)).toBigInteger() }
    }

}
