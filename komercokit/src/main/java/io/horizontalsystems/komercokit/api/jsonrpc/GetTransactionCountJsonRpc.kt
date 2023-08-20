package io.horizontalsystems.komercokit.api.jsonrpc

import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.DefaultBlockParameter

class GetTransactionCountJsonRpc(
        @Transient val address: Address,
        @Transient val defaultBlockParameter: DefaultBlockParameter
) : LongJsonRpc(
        method = "eth_getTransactionCount",
        params = listOf(address, defaultBlockParameter)
)
