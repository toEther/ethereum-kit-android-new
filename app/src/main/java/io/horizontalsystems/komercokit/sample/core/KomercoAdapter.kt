package io.horizontalsystems.komercokit.sample.core

import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.core.signer.Signer
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.FullTransaction
import io.horizontalsystems.komercokit.models.GasPrice
import io.reactivex.Single
import java.math.BigDecimal

class KomercoAdapter(
        private val komercoKit: KomercoKit,
        private val signer: Signer
) : KomercoBaseAdapter(komercoKit) {

    private val decimal = 18

    override fun send(
            address: Address,
            amount: BigDecimal,
            gasPrice: GasPrice,
            gasLimit: Long
    ): Single<FullTransaction> {
        val amountBigInt = amount.movePointRight(decimal).toBigInteger()
        val transactionData = komercoKit.transferTransactionData(address, amountBigInt)
        return komercoKit.rawTransaction(transactionData, gasPrice, gasLimit)
                .flatMap { rawTransaction ->
                    val signature = signer.signature(rawTransaction)
                    komercoKit.send(rawTransaction, signature)
                }
    }

}
