package io.kayt.kayt.sample.domain.entity

data class User(val id : Int, val firstName : String, val lastName : String, val profilePicture : String, val device : Device)

data class Device (val type : DeviceType, val model : String, val batteryPercentage : Int)

sealed interface DeviceType {
    data object Android : DeviceType
    data object iOS : DeviceType
}