package com.example.newsapp20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.newsapp20.ui.theme.NewsApp20Theme

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

@Composable
fun NewsScreen(newsViewModel: NewsViewModel = viewModel()) {
    val newsResponse by newsViewModel.newsState

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Headline News", style = MaterialTheme.typography.displaySmall, modifier = Modifier.padding(bottom = 16.dp))
        newsResponse?.articles?.firstOrNull()?.let { article ->
            HeadlineArticle(article)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Latest News", style = MaterialTheme.typography.displaySmall, modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn {
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
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        article.urlToImage?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = article.title,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(article.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(article.description ?: "", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun NewsListItem(article: Article) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        article.urlToImage?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = article.title,
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(article.title, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        Text(article.description ?: "", style = MaterialTheme.typography.bodyMedium)
    }
}