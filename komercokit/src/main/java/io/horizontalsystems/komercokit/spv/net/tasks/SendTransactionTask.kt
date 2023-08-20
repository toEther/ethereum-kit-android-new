package io.horizontalsystems.komercokit.spv.net.tasks

import io.horizontalsystems.komercokit.spv.core.ITask
import io.horizontalsystems.komercokit.models.RawTransaction
import io.horizontalsystems.komercokit.models.Signature

class SendTransactionTask(val sendId: Int,
                          val rawTransaction: RawTransaction,
                          val signature: Signature) : ITask
