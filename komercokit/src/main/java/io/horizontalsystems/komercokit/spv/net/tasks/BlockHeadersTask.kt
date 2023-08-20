package io.horizontalsystems.komercokit.spv.net.tasks

import io.horizontalsystems.komercokit.spv.core.ITask
import io.horizontalsystems.komercokit.spv.models.BlockHeader

class BlockHeadersTask(val blockHeader: BlockHeader, val limit: Int, val reverse: Boolean = false) : ITask