package at.rueckgr.spotify.main

import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Exactly one argument expected")
        exitProcess(1)
    }
    exitProcess(
        when(args[0]) {
            "discover-weekly-cloner" -> DiscoverWeeklyCloner().run()
            "recently-played" -> RecentlyPlayed().run()
            else -> {
                println("Unknown module ${args[0]}")
                1
            }
        }
    )
}
