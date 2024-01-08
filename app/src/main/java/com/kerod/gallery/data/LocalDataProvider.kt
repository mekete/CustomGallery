package com.kerod.gallery.data

import com.kerod.gallery.R

object LocalDataProvider {

    private val childMediaList:List<Media> =

        listOf(
        Media(
            id = 8L,
            mediaFileList = listOf(LocalDataProvider.getDefaultUserAccount()),
            fileName = "Your update on Google Play Store is live!",
            favorite = true,
            mediaType = MediaType.CAMERA,
            createdAt = "3 hours ago",
        ),
        Media(
            id = 2L,
            mediaFileList = listOf(LocalDataProvider.getDefaultUserAccount()),
            fileName = "Bonjour from Paris",
            mediaType = MediaType.MOVIE,
            favorite = true,
            createdAt = "1 hour ago",
        ),
    )

    val imageFolderList = listOf(
        Media(
            id = 0L,
            fileName = "Image Package shipped!",
            createdAt = "20 mins ago",
            favorite = true,
            mediaFileList = childMediaList,
        ),
        Media(
            id = 9L,
            fileName = "Image(No subject)",
            createdAt = "3 hours ago",
            mediaType = MediaType.MOVIE,
            mediaFileList = childMediaList,//.shuffled(),
        ),
        Media(
            id = 11L,
            fileName = " Image Free money",
            createdAt = "3 hours ago",
            mediaType = MediaType.CAMERA,
            mediaFileList = childMediaList,//.shuffled(),
        )
    )
    val movieFolderList = listOf(
        Media(
            id = 0L,
            fileName = "Movie 1",
            createdAt = "20 mins ago",
            favorite = true,
            mediaFileList = childMediaList,
        ),
        Media(
            id = 9L,
            fileName = "(Movie)",
            createdAt = "3 hours ago",
            mediaType = MediaType.MOVIE,
            mediaFileList = childMediaList,//.shuffled(),
        ),
        Media(
            id = 11L,
            fileName = "Movie Free money",
            createdAt = "3 hours ago",
            mediaType = MediaType.CAMERA,
            mediaFileList = childMediaList,//.shuffled(),
        )
    )

    /**
     * Get an [Media] with the given [id].
     */
    fun get(id: Long): Media? {
        return imageFolderList.firstOrNull { it.id == id }
    }


    fun getDefaultUserAccount() = //allUserMedia.first()
        Media(
            id = 1L,
            fileName = "Jeff",
            email = "hikingfan@gmail.com",
            size = 200,
            avatar = R.drawable.avatar_1,
            favorite = true
        )
}
