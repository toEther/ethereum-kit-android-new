package io.horizontalsystems.erc20kit.events

import io.horizontalsystems.komercokit.contracts.ContractEventInstance
import io.horizontalsystems.komercokit.models.Address
import java.math.BigInteger

class ApproveEventInstance(
    contractAddress: Address,
    val owner: Address,
    val spender: Address,
    val value: BigInteger
) : ContractEventInstance(contractAddress)
