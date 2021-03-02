package io.marcofracassi

data class Product(
    val name: String,
    val description: String?,
    val image: String?,
    val availableQuantity: Int?,
    val bookedQuantity: Int?) {

    var price: Int = 0

    fun withPrice(value: Int) : Product {
        price = value
        return this
    }

    fun validate() {
        notNull(description, "Description")
        notNull(availableQuantity, "AvailableQuantity")
        notNull(bookedQuantity, "BookedQuantity")
        if(bookedQuantity!! > availableQuantity!!){
            throw IllegalArgumentException("BookedQuantity must be less than or equal to AvailableQuantity")
        }
    }

    private fun notNull(obj: Any?, label: String) {
        if(obj == null){
            throw IllegalArgumentException("$label must be valued")
        }
    }
}

