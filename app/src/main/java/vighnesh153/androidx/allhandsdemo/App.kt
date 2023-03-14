package vighnesh153.androidx.allhandsdemo

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api


@Composable
fun App() {
    TvLazyColumn(
        items = listOf(
            { TopNavigation() },
            { FeaturedCarousel() },
            {
                movieCollections.forEach { movieCollection ->
                    AppRow(
                        label = movieCollection.label,
                        movies = movieCollection.items
                    )
                    Spacer(height = 35.dp)
                }
            },
        )
    )
}



@Composable
fun TopNavigation() {
    val tabs = listOf("Home", "Categories", "Movies", "Shows", "Favourites")
    var selectedTabIndex by remember { mutableStateOf(0) }

    AlignmentCenter(horizontalAxis = true) {
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, tabLabel ->
                Tab(
                    isSelected = selectedTabIndex == index,
                    onFocus = { selectedTabIndex = index },
                    text = tabLabel
                )
            }
        }
    }


}



@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedCarousel(movies: List<Movie> = featuredCarouselMovies) {
    Carousel(
        slidesCount = movies.size,
    ) { itemIndex ->
        val movie = movies[itemIndex]

        CarouselSlide(
            title = movie.name,
            description = movie.description,
            background = {
                CarouselImage(movie)
            },
            actions = {
                Button(
                    text = "Watch on YouTube",
                    icon = Icons.Outlined.ArrowRight
                )
            }
        )
    }
}


@Composable
fun AppRow(
    label: String,
    movies: List<Movie>,
) {
    TvLazyRow(
        title = label,
        items = movies,
        drawItem = { movie ->
            ImageCard(
                movie,
//                aspectRatio = 2f / 3
            )
        }
    )
}



















































