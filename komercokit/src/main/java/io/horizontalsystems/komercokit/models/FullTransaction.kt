package io.horizontalsystems.komercokit.models

import io.horizontalsystems.komercokit.decorations.TransactionDecoration

class FullTransaction(
    val transaction: Transaction,
    val decoration: TransactionDecoration
)
