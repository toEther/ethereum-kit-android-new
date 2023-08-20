package io.horizontalsystems.komercokit.api.models

class KomercoKitState {
    var accountState: AccountState? = null
    var lastBlockHeight: Long? = null

    fun clear() {
        accountState = null
        lastBlockHeight = null
    }
}
