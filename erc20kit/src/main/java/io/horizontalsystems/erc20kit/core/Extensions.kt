package io.horizontalsystems.erc20kit.core

import io.horizontalsystems.erc20kit.decorations.ApproveEip20Decoration
import io.horizontalsystems.erc20kit.events.ApproveEventInstance
import io.horizontalsystems.erc20kit.events.TransferEventInstance
import io.horizontalsystems.komercokit.contracts.ContractEventInstance
import io.horizontalsystems.komercokit.core.hexStringToByteArrayOrNull
import io.horizontalsystems.komercokit.core.toRawHexString
import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.models.TransactionLog
import java.math.BigInteger

fun TransactionLog.getErc20EventInstance(): ContractEventInstance? {
    return try {
        if (topics.size != 3) {
            return null
        }

        val signature = topics[0].hexStringToByteArrayOrNull()

        val firstParam = Address(topics[1])
        val secondParam = Address(topics[2])

        when {
            signature.contentEquals(TransferEventInstance.signature) ->
                TransferEventInstance(address, firstParam, secondParam, BigInteger(data.toRawHexString(), 16), null)
            signature.contentEquals(ApproveEip20Decoration.signature) ->
                ApproveEventInstance(address, firstParam, secondParam, BigInteger(data.toRawHexString(), 16))
            else ->
                null
        }
    } catch (error: Throwable) {
        error.printStackTrace()
        null
    }
}
