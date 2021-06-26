package postpc.amitkadosh.rachelsandwichapp

import java.util.*

enum class Status {
    WAITING, INPROGRESS, READY, DONE
}


data class Order(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var pickles: Int = 0,
    var hummus: Boolean = false,
    var tahini: Boolean = false,
    var comment: String = "",
    var status: Status = Status.WAITING
)