package io.vinter.lostpet.ui.list

import io.vinter.lostpet.network.form.FilterForm

class State{

    var lastFilter: FilterForm? = null
    var lastSearchQuery: String? = null
    var lastRequestType: RequestType? = null

}