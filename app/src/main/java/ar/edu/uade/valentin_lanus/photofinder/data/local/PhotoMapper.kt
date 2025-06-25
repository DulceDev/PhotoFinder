package ar.edu.uade.valentin_lanus.photofinder.data.local

import ar.edu.uade.valentin_lanus.photofinder.data.model.Photo
import ar.edu.uade.valentin_lanus.photofinder.data.model.Urls
import ar.edu.uade.valentin_lanus.photofinder.data.model.User

fun Photo.toEntity(): PhotoEntity = PhotoEntity(
    id = id,
    imageUrlSmall = urls.small,
    imageUrlRegular = urls.regular,
    imageUrlFull = urls.full,
    description = description,
    userName = user.name
)

fun PhotoEntity.toPhoto(): Photo = Photo(
    id = id,
    description = description,
    likes = 0,
    urls = Urls(
        small = imageUrlSmall,
        regular = imageUrlRegular,
        full = imageUrlFull
    ),
    user = User(name = userName)
)