package io.horizontalsystems.komercokit.spv.net.tasks

import io.horizontalsystems.komercokit.models.Address
import io.horizontalsystems.komercokit.spv.core.ITask
import io.horizontalsystems.komercokit.spv.models.BlockHeader

class AccountStateTask(val address: Address, val blockHeader: BlockHeader) : ITask
