package io.horizontalsystems.komercokit.core

import io.horizontalsystems.komercokit.api.jsonrpc.GasPriceJsonRpc
import io.reactivex.Single

class LegacyGasPriceProvider(
        private val evmKit: KomercoKit
) {
    fun gasPriceSingle(): Single<Long> {
        return evmKit.rpcSingle(GasPriceJsonRpc())
    }
}
