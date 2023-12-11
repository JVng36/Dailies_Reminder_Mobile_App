package com.example.dailiesandroidapp

//This class is used to set and get the data from database
class Model {
    //var id: Int? = null
    var title: String? = null
    var date: String? = null
    var time: String? = null

    constructor()
    constructor(title: String?, date: String?, time: String?) { //id: Int?,
        //this.id = id
        this.title = title
        this.date = date
        this.time = time
    }
}