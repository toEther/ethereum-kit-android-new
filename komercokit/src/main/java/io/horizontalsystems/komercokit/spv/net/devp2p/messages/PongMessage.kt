package io.horizontalsystems.komercokit.spv.net.devp2p.messages

import io.horizontalsystems.komercokit.core.hexStringToByteArray
import io.horizontalsystems.komercokit.spv.net.IInMessage
import io.horizontalsystems.komercokit.spv.net.IOutMessage

class PongMessage() : IInMessage, IOutMessage {

    constructor(payload: ByteArray) : this()

    override fun encoded(): ByteArray {
        return payload
    }

    override fun toString(): String {
        return "Pong"
    }

    companion object {
        val payload = "C0".hexStringToByteArray()
    }
}
