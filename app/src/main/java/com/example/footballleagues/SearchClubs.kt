package com.example.footballleagues


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.footballleagues.ui.theme.FootballLeaguesTheme
import com.example.footballleagues.ui.theme.Poppins
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class SearchClubs : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchClubGUI()
        }
    }
}

//Content for search clubs activity
@Composable
fun SearchClubGUI() {

    //Declaring and initializing variables
    var text by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var clubNames by rememberSaveable { mutableStateOf<List<String>>(emptyList()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0BCFF))
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row{
            //Textfield to enter the club name or league name
            TextField(
                value = text,
                onValueChange = {text = it},
                modifier = Modifier.width(250.dp),
                label = { Text(text = "Enter Club Name ") }
            )

            ElevatedButton(onClick = {

                try{
                    scope.launch {
                        //getting the club using the getClubByName from the clubDAO class
                        val clubName = withContext(Dispatchers.IO) {
                            clubDAO.getClubByName(text)
                        }

                        //getting the league using the clubNameByLeague from the clubDAO class
                        val clubNameByLeague = withContext(Dispatchers.IO) {
                            clubDAO.getClubByLeagueName(text)
                        }

                        //setting the appropriate club name that is retrieved
                        clubNames = if (clubName.isNotEmpty()) {
                            clubName
                        } else {
                            clubNameByLeague
                        }
                    }
                } catch(e: Exception){
                    Toast.makeText(context,
                        "Club not found in the database",
                        Toast.LENGTH_SHORT).show()
                }

            },
                modifier = Modifier
                    .width(120.dp)
                    .padding(5.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Search",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    fontSize = 17.sp,
                    color = Color(0xFF6650a4)
                )
            }
        }

        //Lazy Column to display the club name with the club logo
        LazyColumn {
            items(clubNames.size) { index ->
                //getting the club name
                val clubName = clubNames[index]
                //variable for the logo url
                var logoUrl by rememberSaveable { mutableStateOf("") }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    LaunchedEffect(clubNames){
                        //getting the logo url using getTeamLogo method from the club DAO class
                        logoUrl = clubDAO.getTeamLogo(clubName)
                    }


                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = clubName,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = Color(0xFF6650a4),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.width(100.dp))
                    //displaying the logo url using Async Image - Implemented using the coil library  -  https://developer.android.com/develop/ui/compose/graphics/images/loading
                    AsyncImage(model = logoUrl, contentDescription = "Club Logo")
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview4() {
    FootballLeaguesTheme {
        SearchClubGUI()
    }
}