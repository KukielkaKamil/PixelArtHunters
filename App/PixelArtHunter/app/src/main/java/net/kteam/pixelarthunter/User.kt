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
    var description: String =""
    var pixels:Int = 0
    var isAdmin:Boolean = false


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
        this.description =""
        this.pixels = 0
        this.score = 0
        this.isAdmin = false
    }
    constructor(id: Int,name:String,email:String,description:String,score: Int,pixels:Int,isAdmin:Int){
        this.id = id
        this.name = name
        this.email = email
        this.description = description
        this.score = score
        this.pixels = pixels
        this.isAdmin = isAdmin == 1
    }

}