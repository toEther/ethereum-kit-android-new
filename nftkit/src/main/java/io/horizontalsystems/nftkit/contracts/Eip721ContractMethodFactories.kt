package io.horizontalsystems.nftkit.contracts

import io.horizontalsystems.komercokit.contracts.ContractMethodFactories

object Eip721ContractMethodFactories : ContractMethodFactories() {
    init {
        registerMethodFactories(listOf(Eip721SafeTransferFromMethodFactory()))
    }
}