package io.horizontalsystems.komercokit.decorations

class ContractCreationDecoration : TransactionDecoration() {

    override fun tags(): List<String> = listOf("contractCreation")

}
