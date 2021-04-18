package com.syd.elderguard.repository

interface Repository {

    // this override property is for saving network loading status.
    var isLoading: Boolean
}