package com.example.audit.dto

class AuditEventDto  {
    var uuid: String = ""
        set(value){ field = value}
        get() {return field}
    var type: String = ""
        set(value){ field = value}
        get() {return field}
    var service: String = ""
        set(value){ field = value}
        get() {return field}
    var message: String = ""
        set(value){ field = value}
        get() {return field}


    constructor(){}
    constructor(uuid: String, type: String, service: String, message: String){
        this.uuid = uuid
        this.type = type
        this.service = service
        this.message = message
    }

}
