package mdrew.ballwall

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform