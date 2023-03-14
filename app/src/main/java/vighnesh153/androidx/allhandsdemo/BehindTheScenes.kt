package vighnesh153.androidx.allhandsdemo

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselScope
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.TabDefaults
import androidx.tv.material3.TabRowDefaults
import androidx.tv.material3.Text
import coil.compose.AsyncImage

//val pageColor = Color.Red
val pageColor = Color(0xff18171a)

data class MovieImage(val url: String, val aspect: String)
data class Movie(val name: String, val images: List<MovieImage>, val root: String)
data class MovieCollection(val label: String, val items: List<Movie>)

val Movie.description: String
    get() = "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham..."


@Composable
fun AlignmentCenter(
    modifier: Modifier = Modifier,
    horizontalAxis: Boolean = false,
    verticalAxis: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (horizontalAxis) Arrangement.Center else Arrangement.Start,
        verticalAlignment = if (verticalAxis) Alignment.CenterVertically else Alignment.Top,
    ) {
        content()
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TabRow(
    selectedTabIndex: Int,
    content: @Composable () -> Unit
) {
    androidx.tv.material3.TabRow(
        selectedTabIndex = selectedTabIndex,
        separator = { Spacer(modifier = Modifier.width(4.dp)) },
        modifier = Modifier.padding(top = 20.dp),
        indicator = @Composable { tabPositions ->
            tabPositions.getOrNull(selectedTabIndex)?.let {
                TabRowDefaults.PillIndicator(
                    currentTabPosition = it,
                    inactiveColor = Color(0xFFE5E1E6),
                )
            }
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Tab(
    isSelected: Boolean,
    onFocus: () -> Unit,
    text: String,
) {
    androidx.tv.material3.Tab(
        selected = isSelected,
        onFocus = { onFocus() },
        colors = TabDefaults.pillIndicatorTabColors(
            contentColor = LocalContentColor.current,
            selectedContentColor = Color(0xFF313033)
        )
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun ImageCard(
    movie: Movie,
    aspectRatio: Float = 16f / 9,
) {
    val aspect = if (aspectRatio == 16f / 9) "orientation/vod_art_16x9" else "orientation/vod_art_2x3"
    val scaleMax = if (aspectRatio == 16f / 9) 1.1f else 1.025f
    val cardWidth = if (aspectRatio == 16f / 9) 200.dp else 172.dp

    var isFocused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(12.dp)
    val borderColor by animateColorAsState(targetValue = if (isFocused) Color.White.copy(alpha = 0.8f) else Color.Transparent)
    val scale by animateFloatAsState(targetValue = if (isFocused) scaleMax else 1f)

    Column(
        modifier = Modifier
            .width(cardWidth)
            .scale(scale)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .border(2.dp, borderColor, shape)
                .clip(shape)
                .onFocusChanged { isFocused = it.isFocused }
                .focusable()
        ) {
//            AsyncImage(
//                model = getMovieImageUrl(movie, aspect = aspect),
//                contentDescription = null
//            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = movie.name,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Button(text: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(50))
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 15.dp)
            .focusable(),
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(25.dp),
            tint = Color.Black
        )
        Text(text = text, fontSize = 12.sp)
    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun Carousel(
    slidesCount: Int,
    content: @Composable CarouselScope.(itemIndex: Int) -> Unit
) {
    val carouselState: CarouselState = remember { CarouselState() }

    androidx.tv.material3.Carousel(
        slideCount = slidesCount,
        carouselState = carouselState,
        modifier = Modifier.height(340.dp).fillMaxWidth(),
        carouselIndicator = {
            CarouselDefaults.IndicatorRow(
                slideCount = slidesCount,
                activeSlideIndex = carouselState.activeSlideIndex,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 58.dp, bottom = 16.dp),
            )
        }
    ) { itemIndex ->
        content(itemIndex)
    }
}

@Composable
fun CarouselImage(movie: Movie, aspect: String = "orientation/iconic_16x9") {
    val navigationGradient = Brush.verticalGradient(
        colors = listOf(pageColor, Color.Transparent),
        startY = 0f,
        endY = 200f
    )
    var height by remember {
        mutableStateOf(0f)
    }

    val navigationGradientBottom = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            pageColor
        ),
        startY = 50f,
        endY = height,
    )

    val horizontalGradient = Brush.horizontalGradient(
        colors = listOf(pageColor, Color.Transparent),
        startX = 1400f,
        endX = 900f,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { height = it.size.height.toFloat() }
    ) {
        AsyncImage(
            model = getMovieImageUrl(
                movie = movie,
                aspect = aspect
            ),
            contentScale = ContentScale.FillWidth,
            alignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(navigationGradientBottom)
                .background(navigationGradient)
                .background(horizontalGradient)
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CarouselScope.CarouselSlide(
    title: String,
    description: String,
    background: @Composable () -> Unit,
    actions: @Composable () -> Unit
) {
    CarouselItem(
        background = {
            background()
        }
    ) {
        Column(
            modifier = Modifier.padding(start = 58.dp, top = 180.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 40.sp
            )

            Spacer(height = 16.dp)

            Text(
                text = description,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.width(500.dp),
            )

            Spacer(height = 15.dp)

            actions()
        }
    }
}

@Composable
fun TvLazyRow(
    title: String,
    items: List<Movie>,
    drawItem: @Composable (movie: Movie) -> Unit
) {
    val paddingLeft = 58.dp
    var hasFocus by remember { mutableStateOf(false) }

    Column(modifier = Modifier.onFocusChanged { hasFocus = it.hasFocus }) {
        Text(
            text = title,
            color = if (hasFocus) Color.White else Color.White.copy(alpha = 0.8f),
            fontSize = 14.sp,
            modifier = Modifier.padding(start = paddingLeft)
        )

        Spacer(height = 12.dp)

        androidx.tv.foundation.lazy.list.TvLazyRow(
            contentPadding = PaddingValues(horizontal = paddingLeft),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(items) { movie ->
                drawItem(movie)
            }
        }
    }
}

@Composable
fun Spacer(
    width: Dp? = null,
    height: Dp? = null
) {
    var modifier: Modifier = Modifier
    if (width != null) {
        modifier = modifier.width(width)
    }
    if (height != null) {
        modifier = modifier.height(height)
    }
    Spacer(modifier)
}

@Composable
fun TvLazyColumn(
    items: List<@Composable () -> Unit>
) {
    androidx.tv.foundation.lazy.list.TvLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(pageColor)
    ) {
        item {
            Box {
                val one = items.getOrNull(1)
                if (one !== null) {
                    one()
                }
                val zero = items.getOrNull(0)
                if (zero !== null) {
                    zero()
                }
            }
            Spacer(height = 50.dp)
        }

        item {
            val two = items.getOrNull(2)
            if (two !== null) {
                two()
            }
            Spacer(height = 35.dp)
        }

        item {
            val three = items.getOrNull(3)
            if (three !== null) {
                three()
            }
            Spacer(height = 35.dp)
        }

        item {
            val four = items.getOrNull(4)
            if (four !== null) {
                four()
            }
            Spacer(height = 35.dp)
        }

        item {
            val five = items.getOrNull(5)
            if (five !== null) {
                five()
            }
            Spacer(height = 50.dp)
        }
    }
}

fun getMovieImageUrl(
    movie: Movie,
    aspect: String = "orientation/backdrop_16x9"
): String =
    movie
        .images
        .find { image -> image.aspect == aspect }?.url ?:
        movie.images.first().url
