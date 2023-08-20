package io.horizontalsystems.nftkit.contracts

import io.horizontalsystems.komercokit.contracts.ContractMethod
import io.horizontalsystems.komercokit.models.Address
import java.math.BigInteger

class Eip1155SafeTransferFromMethod(
    val from: Address,
    val to: Address,
    val tokenId: BigInteger,
    val value: BigInteger,
    val data: ByteArray
) : ContractMethod() {

    override val methodSignature = Companion.methodSignature
    override fun getArguments() = listOf(from, to, tokenId, value, data)

    companion object {
        const val methodSignature = "safeTransferFrom(address,address,uint256,uint256,bytes)"
    }

}