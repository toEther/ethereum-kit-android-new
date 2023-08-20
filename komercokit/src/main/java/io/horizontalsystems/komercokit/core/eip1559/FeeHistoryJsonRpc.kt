package io.horizontalsystems.komercokit.core.eip1559

import io.horizontalsystems.komercokit.api.jsonrpc.JsonRpc
import io.horizontalsystems.komercokit.models.DefaultBlockParameter

class FeeHistoryJsonRpc(
        @Transient val blocksCount: Long,
        @Transient val defaultBlockParameter: DefaultBlockParameter,
        @Transient val rewardPercentile: List<Int>,
) : JsonRpc<FeeHistory>(
        method = "eth_feeHistory",
        params = listOf(blocksCount, defaultBlockParameter, rewardPercentile)
) {
    @Transient
    override val typeOfResult = FeeHistory::class.java
}
