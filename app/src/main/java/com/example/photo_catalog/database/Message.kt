package com.example.photo_catalog.database

public class Messages {
    lateinit var label: String
    lateinit var description: String
    lateinit var imageUrl: String

    constructor(){
        
    }

    constructor(label: String, description: String, imageUrl: String){
        this.label = label
        this.description = description
        this.imageUrl = imageUrl
    }
}