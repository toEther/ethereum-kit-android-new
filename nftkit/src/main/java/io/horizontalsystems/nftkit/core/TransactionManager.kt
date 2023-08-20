package io.horizontalsystems.nftkit.core

import io.horizontalsystems.komercokit.core.KomercoKit
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.TransactionData
import io.horizontalsystems.nftkit.contracts.Eip1155SafeTransferFromMethod
import io.horizontalsystems.nftkit.contracts.Eip721SafeTransferFromMethod
import java.math.BigInteger

class TransactionManager(komercoKit: KomercoKit) {
    private val address: Address = komercoKit.receiveAddress

    fun transferEip721TransactionData(contractAddress: Address, to: Address, tokenId: BigInteger) =
        TransactionData(
            to = contractAddress,
            value = BigInteger.ZERO,
            input = Eip721SafeTransferFromMethod(address, to, tokenId, byteArrayOf()).encodedABI()
        )

    fun transferEip1155TransactionData(contractAddress: Address, to: Address, tokenId: BigInteger, value: BigInteger) =
        TransactionData(
            to = contractAddress,
            value = BigInteger.ZERO,
            input = Eip1155SafeTransferFromMethod(address, to, tokenId, value, byteArrayOf()).encodedABI()
        )
}