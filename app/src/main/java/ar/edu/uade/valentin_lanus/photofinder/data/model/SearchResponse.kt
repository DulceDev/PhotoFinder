package ar.edu.uade.valentin_lanus.photofinder.data.model

data class SearchResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<Photo>
)