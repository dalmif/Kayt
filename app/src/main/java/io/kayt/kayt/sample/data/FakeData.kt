package io.kayt.kayt.sample.data

import io.kayt.kayt.sample.domain.entity.Device
import io.kayt.kayt.sample.domain.entity.DeviceType
import io.kayt.kayt.sample.domain.entity.User

val fakeUsers = buildList<User> {
    repeat(100) {
        add(
            User(
                it + 1,
                "Mohammad",
                "Fallah",
                "https://google.com",
                Device(DeviceType.Android, "SAMSUNG", 12)
            )
        )
    }
}