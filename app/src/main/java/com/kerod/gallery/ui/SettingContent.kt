package com.kerod.gallery.ui


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kerod.gallery.state.GalleryUiState

@Composable
fun SettingsScreen(

    modifier: Modifier = Modifier,
    currentView: String = GalleryRoute.IMAGE,
    galleryUiState: GalleryUiState,
    closeMediaListScreen: () -> Unit,
    navigateToMediaListScreen: (Long, String, Int, String) -> Unit,

    ) {
    Box(modifier = modifier.fillMaxSize()) {


        Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            BackHandler {
                closeMediaListScreen()
            }

            Text(text = "Select Theme:", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 8.dp))
            val selectedOption = remember { mutableStateOf("Dark") }
            Text(text = "Selection not completed yet. Change theme from the settings. The app will respond then", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = selectedOption.value == "Dark", onClick = { selectedOption.value = "Dark" })
                    Text("Dark")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(

                        selected = selectedOption.value == "Light", onClick = { selectedOption.value = "Light" })
                    Text("Light")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {

                    RadioButton(selected = selectedOption.value == "System", onClick = { selectedOption.value = "System" })
                    Text("System")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


        }
    }
}
