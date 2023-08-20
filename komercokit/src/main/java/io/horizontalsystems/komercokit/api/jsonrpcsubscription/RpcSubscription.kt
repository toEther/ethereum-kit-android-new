package io.horizontalsystems.komercokit.api.jsonrpcsubscription

import com.google.gson.Gson
import io.horizontalsystems.komercokit.api.core.RpcSubscriptionResponse

abstract class RpcSubscription<T>(val params: List<Any>) {
    protected abstract val typeOfResult: Class<T>

    fun parse(response: RpcSubscriptionResponse, gson: Gson): T {
        return gson.fromJson(response.params.result.toString(), typeOfResult)
    }
}
