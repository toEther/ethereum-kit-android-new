package io.horizontalsystems.komercokit.contracts

import io.horizontalsystems.komercokit.models.Address

open class ContractEventInstance(val contractAddress: Address) {

    open fun tags(userAddress: Address): List<String> = listOf()

}
