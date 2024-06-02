package com.example.newsapp20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp20.ui.theme.NewsApp20Theme
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
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

// TODO: Split the headline data and latest news data

@Composable
fun NewsScreen(newsViewModel: NewsViewModel = viewModel()) {
    val newsResponse by newsViewModel.newsState

    val listState = rememberLazyListState()
    val showHeadline by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < 200
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showHeadline,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                Text("Headline News", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), modifier = Modifier
                    .padding(
                    start = 10.dp,
                    top = 30.dp,
                    end = 16.dp,
                    bottom = 4.dp
                ))
                newsResponse?.articles?.firstOrNull()?.let { article ->
                    HeadlineArticle(article)
                }
            }
        }

        Text("Latest News", style = MaterialTheme.typography.displaySmall, modifier = Modifier.padding(16.dp))

        LazyColumn(state = listState) {
            newsResponse?.articles?.drop(1)?.let { articles ->
                items(articles) { article ->
                    NewsListItem(article)
                }
            }
        }
    }
}

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
                    .border(
                        border = BorderStroke(1.dp, Color.Gray),
                        shape = RoundedCornerShape(8.dp)
                    ),
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
                    "${article.author ?: "Unknown"}",
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

@Composable
fun NewsListItem(article: Article) {
    val publishedDate = ZonedDateTime.parse(article.publishedAt)
    val formattedDate = DateTimeFormatter.ofPattern("d MMM, yyyy").format(publishedDate)

    Row(modifier = Modifier.padding(8.dp)) {
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

            Row(verticalAlignment = Alignment.CenterVertically,
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
                        "${article.author ?: "Unknown"}",
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
}