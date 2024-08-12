package com.example.newsapp20

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp20.ui.theme.NewsApp20Theme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewsApp20Theme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ){
                    NewsScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsScreen(newsViewModel: NewsViewModel = viewModel()) {
    val headlineNewsResponse by newsViewModel.headlineNewsState
    val latestNewsResponse by newsViewModel.latestNewsState

    val listState = rememberLazyListState()

    LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
        // Headline News Section
        item {
            Column {
                Text(
                    "Headline News",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 28.dp,
                        end = 16.dp,
                        bottom = 4.dp
                    )
                )
                headlineNewsResponse?.articles?.firstOrNull()?.let { article ->
                    HeadlineArticle(article)
                }
            }
        }

        // Latest News Section Title
        stickyHeader {
            Surface {
                Text(
                    "Latest News",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 8.dp,
                            top = 28.dp,
                            end = 16.dp,
                            bottom = 4.dp
                        )
                )
            }
        }

        // Latest News Items
        latestNewsResponse?.articles?.let { articles ->
            items(articles) { article ->
                NewsListItem(article)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HeadlineArticle(article: Article) {
    val publishedDate = ZonedDateTime.parse(article.publishedAt)
    val formattedDate = DateTimeFormatter.ofPattern("d MMM, yyyy").format(publishedDate)

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        article.urlToImage?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = article.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(article.title, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.W500))
        Spacer(modifier = Modifier.height(4.dp))
        Text(article.description ?: "", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_person_24),
                    contentDescription = null,
                    tint = Color.Gray, // Change icon color to gray
                    modifier = Modifier.size(24.dp) // Set ic
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    article.author ?: "Unknown",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W400),
                    color = Color.Gray
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_calendar_today_24),
                    contentDescription = null,
                    tint = Color.Gray, // Change icon color to gray
                    modifier = Modifier.size(24.dp) // Set ic
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.W400),
                    color = Color.Gray
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsListItem(article: Article) {
    val publishedDate = ZonedDateTime.parse(article.publishedAt)
    val formattedDate = DateTimeFormatter.ofPattern("d MMM, yyyy").format(publishedDate)

    Row(verticalAlignment = Alignment.CenterVertically
            ,modifier = Modifier.padding(8.dp)) {
        article.urlToImage?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = article.title,
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ){
            Text(
                article.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W500))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_person_24),
                        contentDescription = null,
                        tint = Color.Gray, // Change icon color to gray
                        modifier = Modifier.size(18.dp) // Set ic
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        article.author ?: "Unknown",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.baseline_calendar_today_24),
                        contentDescription = null,
                        tint = Color.Gray, // Change icon color to gray
                        modifier = Modifier.size(18.dp) // Set ic
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formattedDate,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.W400,
                            fontSize = 12.sp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}