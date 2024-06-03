package com.example.footballleagues

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.footballleagues.ui.theme.FootballLeaguesTheme
import com.example.footballleagues.ui.theme.Poppins
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL




class ViewTeamJersey : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewTeamJerseyGUI()
        }
    }
}


//content for viewing team jersey
@Composable
fun ViewTeamJerseyGUI(){
    //initializing variables
    val scope = rememberCoroutineScope()
    var club by rememberSaveable { mutableStateOf("") }
    var clubID by rememberSaveable { mutableStateOf("") }
    var clubJersey by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var leagueNames by rememberSaveable { mutableStateOf(emptyList<String>())}
    var teamNames by rememberSaveable { mutableStateOf<Map<String, String>>(emptyMap()) }
    val context  = LocalContext.current
    var clubName by rememberSaveable { mutableStateOf("") }
    var selectedButton by rememberSaveable { mutableStateOf("") }


    //Fetching the league names first from the API
    LaunchedEffect(Unit){
        leagueNames = fetchLeagues().split("\n")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0BCFF))
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        //Asking the user to select the appropriate league
        Text(
            text = "Select the Appropriate League",
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color(0xFF6650a4)
        )
        Box(
            modifier = Modifier
                .height(250.dp)
                .width(450.dp)
        ){
            LazyColumn(
                modifier = Modifier
                    .height(265.dp)
                    .width(440.dp)
                    .padding(5.dp)
                    .border(BorderStroke(2.dp, Color(0xFF6650a4))),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally


            ){
                items(leagueNames.size){ index ->
                    val name = leagueNames[index]

                    //Displaying all the available leagues for the user to select
                    Button(onClick = { scope.launch {
                        Log.d("Selected Name" , "$name")
                        try{
                            //getting the team names by passing the league names
                            teamNames = fetchTeamsInLeague(name)
                            selectedButton = name
                            Log.d("teamNames" , "$teamNames")
                        } catch (e: Exception){
                            Toast.makeText(context,
                                "Internal Server Error Occurred While Fetching Data",
                                Toast.LENGTH_SHORT).show()
                        }
                    } },
                        modifier = Modifier
                            .width(270.dp)
                            .height(50.dp)
                            .padding(4.dp),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if(name == selectedButton) Color.Green else Color(0xFF6650a4)
                        )

                    ) {
                        Text(
                            text = name,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.ExtraLight,
                            color = Color.White
                            )
                    }
                }
            }
        }


    // Textfield for the user to enter the club
        Row {
            TextField(
                value = club,
                onValueChange = { club = it },
                textStyle = TextStyle(
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                ),
                label = { Text(text = "Enter Club Name") },
            )
        }
        Row{
            Button(onClick = { scope.launch {
                try{
                    val matchingTeam = teamNames.keys.find { it.contains(club, ignoreCase = true) }
                    if (matchingTeam != null){
                        //getting the club ID of the matching team of the passed string
                        clubID = teamNames[matchingTeam].toString()
                        Log.d("Club ID","$clubID")
                        //getting the club jersey link by passing the club ID
                        clubJersey = fetchClubJersey(clubID).split("\n")
                        // declaring the exact and correct club name
                        clubName = matchingTeam
                    }

                } catch (e: Exception){
                    Toast.makeText(context,
                        "Internal Server Error Occurred While Fetching Data",
                        Toast.LENGTH_SHORT).show()
                }
            } },
                modifier = Modifier
                    .width(140.dp)
                    .padding(5.dp)
                    .height(42.dp),
                shape = RoundedCornerShape(4.dp))
            {
                Text(
                    text = "Search",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 17.sp,
                    color = Color.White
                )
            }
        }

        Log.d("Before","Before the lazy column")
        //Displaying the club name
        Text(
            text = clubName,
            fontFamily = Poppins,
            fontWeight = FontWeight.ExtraLight,
            fontSize = 15.sp
            )
        //Displaying the jerserys
        LazyColumn(
            modifier = Modifier
                .height(290.dp)
                .width(440.dp)
                .padding(5.dp)
                .border(BorderStroke(2.dp, Color(0xFF6650a4))),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Log.d("Inside","Inside the lazy column")
            items(clubJersey.size){index->
                //getting the jersey links from clubJersey and displaying using AsyncImage - https://developer.android.com/develop/ui/compose/graphics/images/loading
                val jerseyLink = clubJersey[index]
                AsyncImage(
                    model = jerseyLink,
                    contentDescription = "Jersey"
                )
            }
        }
        Log.d("After","After the lazy column")


    }

}


//function to fetching  teams by passing the leagues
suspend fun fetchTeamsInLeague(league: String):Map<String,String>{
    val url_string  = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=${league.replace(" ", "_")}"
    val url = URL(url_string)
    var con: HttpURLConnection = url.openConnection() as HttpURLConnection

    //collecting the json from the webservice
    val stringBuilder = StringBuilder()

    //running launched coroutine code in a Thread
    withContext(Dispatchers.IO){
        var bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bufferedReader.readLine()
        while (line != null){
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }
    }

    val allTeamNames = parseJSONTeamNames(stringBuilder)

    return allTeamNames
}
//retrieving the team names
fun parseJSONTeamNames(stringBuilder: StringBuilder): Map<String,String>{
    //parsing json data using string builder
    val json = JSONObject(stringBuilder.toString())
    //for storing the team names
    val allTeamNames = mutableMapOf<String, String>()

    //getting the teams array
    val jsonArray = json.getJSONArray("teams")
    for(i in 0 until jsonArray.length()){
        val league = jsonArray.getJSONObject(i)
        //getting the team name and team ID
        val name = league.getString("strTeam")
        val id  = league.getString("idTeam")
        //adding the team name and id into a map
        allTeamNames[name] = id + "\n"
    }

    return allTeamNames
}


//function to fetch the all the leagues from the API
suspend fun fetchLeagues(): String{
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/all_leagues.php"
    val url = URL(url_string)
    var con: HttpURLConnection = url.openConnection() as HttpURLConnection

    //collecting the json from the webservice
    val stringBuilder = StringBuilder()

    //running launched coroutine code in a Thread
    withContext(Dispatchers.IO){
        var bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bufferedReader.readLine()
        while (line != null){
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }
    }

    val allLeagueNames = parseJSONLeagueNames(stringBuilder)

    return allLeagueNames
}

//function to parse the json data and retrieve league names
fun parseJSONLeagueNames(stringBuilder: StringBuilder):String{
    //parsing json using the string builder
    val json = JSONObject(stringBuilder.toString())
    // for storing the league names
    val allLeagueNames = StringBuilder()

    //getting the leagues
    val jsonArray = json.getJSONArray("leagues")
    for(i in 0 until jsonArray.length()){
        val league = jsonArray.getJSONObject(i)
        val name = league.getString("strLeague")
        allLeagueNames.append(name).append("\n")
    }

    return allLeagueNames.toString()

}

//function to get the club jersey by passing the ID
suspend fun fetchClubJersey(id:String):String{
    val url_string= "https://www.thesportsdb.com/api/v1/json/3/lookupequipment.php?id=$id"
    val url = URL(url_string)
    var con: HttpURLConnection = url.openConnection() as HttpURLConnection

    //collecting the json from the webservice
    val stringBuilder = StringBuilder()

    //running launched coroutine code in a thread
    withContext(Dispatchers.IO){
        var bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bufferedReader.readLine()
        while (line != null){
            stringBuilder.append(line)
            line = bufferedReader.readLine()
        }
    }

    val clubJerseys = parsonJSONForJersey(stringBuilder)

    return clubJerseys
}

//fucntion to parse json data and retrieve the club jerseys
fun parsonJSONForJersey(stringBuilder: StringBuilder): String{
    //parsing json using the string builder
    val json = JSONObject(stringBuilder.toString())
    // to store the club jersey
    val clubJerseys = StringBuilder()

    //getting the equipment array
    val jsonArray = json.getJSONArray("equipment")
    for(i in 0 until jsonArray.length()){
        val team = jsonArray.getJSONObject(i)
        val jersey = team.getString("strEquipment")
        clubJerseys.append(jersey).append("\n")
    }

    return clubJerseys.toString()

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview5() {
    FootballLeaguesTheme {
        ViewTeamJerseyGUI()
    }
}