package io.horizontalsystems.komercokit.spv.net.devp2p.messages

import io.horizontalsystems.komercokit.core.hexStringToByteArray
import io.horizontalsystems.komercokit.spv.net.IInMessage

class PingMessage() : IInMessage {

    constructor(payload: ByteArray) : this()

    override fun toString(): String {
        return "Ping"
    }

    companion object {
        val payload = "C0".hexStringToByteArray()
    }
}
