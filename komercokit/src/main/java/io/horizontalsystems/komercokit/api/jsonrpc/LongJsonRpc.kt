package io.horizontalsystems.komercokit.api.jsonrpc

open class LongJsonRpc(
        method: String, params: List<Any>
) : JsonRpc<Long>(method, params) {
    @Transient
    override val typeOfResult = Long::class.java
}
