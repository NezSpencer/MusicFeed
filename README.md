# MusicFeed
Music feed application with endless scrolling and search designed with Jetpack compose

This project fetches songs from this API, https://www.mocky.io/v2/5df79b1f320000f4612e011e and displays them in a 2 by X gridlist. Although the response from this api is not paginated and does not allow for search/filtering with queries, this app simulates both, allowing for pagination up to page 5. 
It also allows users to filter the feed using their search inputs. 

UI -> Jetpack compose

HTTP -> Retrofit

Serialization/Deserialization -> Moshi

Threading -> Coroutines

Dependency Injection -> Hilt
