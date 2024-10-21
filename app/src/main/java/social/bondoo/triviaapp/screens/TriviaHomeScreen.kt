package social.bondoo.triviaapp.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import social.bondoo.triviaapp.components.Questions
import social.bondoo.triviaapp.utils.AppColours

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaHomeScreen(viewModel: QuestionsViewModel = hiltViewModel()) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColours.mDarkPurple,
                ),
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .padding(start = 10.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                },
                title = {
                    Text(
                        text = "Trivia App",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) { MainContent(viewModel) }
    }
}

@Composable
fun MainContent(viewModel: QuestionsViewModel){
    Questions(viewModel)
}


