package io.horizontalsystems.komercokit.core.eip1559

import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.models.DefaultBlockParameter
import io.reactivex.Flowable
import io.reactivex.Single

class Eip1559GasPriceProvider(
        private val evmKit: KomercoKit
) {
    fun feeHistory(
            blocksCount: Long,
            rewardPercentile: List<Int>,
            defaultBlockParameter: DefaultBlockParameter = DefaultBlockParameter.Latest
    ): Flowable<FeeHistory> {
        return evmKit.lastBlockHeightFlowable
                .flatMapSingle {
                    feeHistorySingle(blocksCount, defaultBlockParameter, rewardPercentile)
                }
    }

    fun feeHistorySingle(blocksCount: Long, defaultBlockParameter: DefaultBlockParameter, rewardPercentile: List<Int>): Single<FeeHistory> {
        val feeHistoryRequest = FeeHistoryJsonRpc(
                blocksCount,
                defaultBlockParameter,
                rewardPercentile
        )
        return evmKit.rpcSingle(feeHistoryRequest)
    }
}
