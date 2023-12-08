package com.mohdiop.finkup.classes

import com.mohdiop.finkup.database.Fink

interface FinkListener {
    fun onFinkClickListener(fink: Fink)
}