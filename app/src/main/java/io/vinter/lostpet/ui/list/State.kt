package io.vinter.lostpet.ui.list

import io.vinter.lostpet.network.form.FilterForm

class State{

    enum class FragmentState {NORMAL, LOADING, ERROR}

    var lastFilter: FilterForm? = null
    var lastSearchQuery: String? = null
    var lastRequestType: RequestType? = null

}