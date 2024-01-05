package tj.itservice.movie.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import tj.itservice.movie.data.MovieResult
import tj.itservice.movie.di.Repository
import javax.inject.Inject


class PagingSource @Inject constructor(private val postRepository: Repository) : PagingSource<Int, MovieResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieResult> {
        val pageNum = params.key ?: 1

        return try {
            val response = postRepository.getPopularMovie(pageNum)
            LoadResult.Page(
                data = response.results,
                prevKey = if (pageNum == 1) null else pageNum - 1,
                nextKey = if (response.results.isEmpty()) null else pageNum + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(Exception("Failed to load"))
        }
    }


    override fun getRefreshKey(state: PagingState<Int, MovieResult>): Int? = with(state) {
        return anchorPosition?.let { position ->
            closestPageToPosition(position)?.prevKey?.plus(1)
                ?: closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
