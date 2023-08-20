package io.horizontalsystems.erc20kit.contract

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.models.Address
import java.math.BigInteger

class ApproveMethod(val spender: Address, val value: BigInteger) : ContractMethod() {

    override val methodSignature = ApproveMethod.methodSignature
    override fun getArguments() = listOf(spender, value)

    companion object {
        const val methodSignature = "approve(address,uint256)"
    }

}
