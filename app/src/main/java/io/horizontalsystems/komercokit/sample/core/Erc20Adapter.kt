package io.horizontalsystems.komercokit.sample.core

import android.content.Context
import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.core.signer.Signer
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.FullTransaction
import io.horizontalsystems.komercokit.models.GasPrice
import io.horizontalsystems.komercokit.sample.modules.main.Erc20Token
import io.reactivex.Single
import java.math.BigDecimal

class Erc20Adapter(
    context: Context,
    token: Erc20Token,
    private val komercoKit: KomercoKit,
    private val signer: Signer
) : Erc20BaseAdapter(context, token, komercoKit) {

    override fun send(address: Address, amount: BigDecimal, gasPrice: GasPrice, gasLimit: Long): Single<FullTransaction> {
        val valueBigInteger = amount.movePointRight(decimals).toBigInteger()
        val transactionData = erc20Kit.buildTransferTransactionData(address, valueBigInteger)

        return komercoKit
            .rawTransaction(transactionData, gasPrice, gasLimit)
            .flatMap { rawTransaction ->
                val signature = signer.signature(rawTransaction)
                komercoKit.send(rawTransaction, signature)
            }
    }

    fun allowance(spenderAddress: Address): Single<BigDecimal> {
        return erc20Kit.getAllowanceAsync(spenderAddress).map { allowance -> allowance.toBigDecimal().movePointLeft(decimals) }
    }

}
