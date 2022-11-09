# Spotify tools

[![Build Status](https://travis-ci.org/paulchen/spotify-tools.svg?branch=main)](https://travis-ci.org/paulchen/spotify-tools)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=paulchen_spotify-tools&metric=alert_status)](https://sonarcloud.io/dashboard?id=paulchen_spotify-tools)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

A set of tools designed to make everyday life with Spotify easier. Based on https://github.com/thelinmichael/spotify-web-api-java

Currently, the following tools are available:

* **DiscoveryWeeklyCloner**: Copies the contents of the "Discover Weekly" playlist to a new playlist discover-weekly-YYYYMMDD. 
As Spotify creates that playlist every Monday, that date is always the monday.
If a playlist with such a name already exists, nothing will be done.

* **RecentlyPlayed**: Reads the 50 most recently played tracks from Spotify and logs them.

Compile and install with `./gradlew installDist`.

Copy `spotify.properties.dist` to `spotify.properties` and adapt it accordingly.

Run it with

* `build/install/SpotifyTools/bin/SpotifyTools discover-weekly-cloner` or
* `build/install/SpotifyTools/bin/SpotifyTools recently-played`
