package net.kteam.pixelarthunter

import java.io.Serializable

class User: Serializable{

    private var id:Int
    fun getId(): Int {
        return id
    }
    fun setId(value:Int) {
        id = value
    }

    var name:String= ""

    var email:String =""


    private var score:Int
    fun getScore(): Int {
        return score
    }
    fun setScore(value:Int) {
        score = value
    }


    constructor(){
        this.id = 0
        this.name = ""
        this.email = ""
        this.score = 0
    }
    constructor(id: Int,name:String,email:String,score: Int){
        this.id = id
        this.name = name
        this.email = email
        this.score = score
    }

}