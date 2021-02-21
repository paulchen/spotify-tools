package at.rueckgr.spotify.main

import at.rueckgr.spotify.util.ApiFactory
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

fun main() {
    val sourcePlaylistName = "Discover Weekly"
    val destinationPlaylistName = buildDestinationName()

    val spotifyApi = ApiFactory.create()
    val playlistsRequest = spotifyApi.listOfCurrentUsersPlaylists.build()
    val playlists = playlistsRequest.execute()
    val total = playlists.total
    var playlistsProcessed = 0
    var sourcePlaylist: PlaylistSimplified? = null
    while (playlistsProcessed < total) {
        val playlistsToProcess = spotifyApi.listOfCurrentUsersPlaylists.limit(50).offset(playlistsProcessed).build().execute()
        playlistsProcessed += playlistsToProcess.items.size

        val possibleSourcePlaylist = playlistsToProcess.items.find { playlist -> playlist.name.equals(sourcePlaylistName) }
        if (possibleSourcePlaylist != null) {
            sourcePlaylist = possibleSourcePlaylist
        }

        if (playlistsToProcess.items.find { playlist -> playlist.name.equals(destinationPlaylistName) } != null) {
            println("Playlist already exists")
            return
        }
    }

    val userId = spotifyApi.currentUsersProfile.build().execute().id
    val destinationPlaylist =
        spotifyApi.createPlaylist(userId, destinationPlaylistName).collaborative(false).public_(false).build().execute()

    val sourcePlaylistItems = spotifyApi.getPlaylistsItems(sourcePlaylist?.id).limit(50).build().execute()
    val playlistItems = sourcePlaylistItems.items.map { item -> item.track.uri }.toTypedArray()
    spotifyApi.addItemsToPlaylist(destinationPlaylist.id, playlistItems).build().execute()
}

private fun buildDestinationName(): String {
    val localDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val date = localDate.format(DateTimeFormatter.ofPattern("YYYYMMdd"))
    return "discover-weekly-$date"
}
