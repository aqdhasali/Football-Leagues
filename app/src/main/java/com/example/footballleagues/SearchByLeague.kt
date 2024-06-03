package com.example.footballleagues

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.footballleagues.ui.theme.FootballLeaguesTheme
import com.example.footballleagues.ui.theme.Poppins
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL


//initializing the list to save the clubs
var clubs:  MutableList<Club> = mutableListOf()

class SearchByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
           SearchByLeagueGUI()
        }
    }
}

//Content for search by league activity
@Composable
fun SearchByLeagueGUI() {
    // initializing variables
    var leagueName by rememberSaveable { mutableStateOf("") }
    var leagueDetails by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0BCFF))
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        )
    {
        Row{
            //Textfield for the user to enter the league name
            TextField(value = leagueName,
                onValueChange = {leagueName = it},
                label = { Text(text = "Enter League Name") }
                )
        }

        Row(modifier = Modifier.padding(10.dp)){
            //Button to retrieve the league details from the API
            ElevatedButton(onClick = { scope.launch {
                leagueDetails  = fetchClubs(leagueName)
            }
            },
            modifier = Modifier
                .height(42.dp),
            shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    text = "Retrieve Clubs",
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6650a4)
                )
            }

            Spacer(modifier = Modifier.width(7.dp))

            //Button to save the league details to the database
            ElevatedButton(onClick = { scope.launch {
                clubs.forEach{club ->
                    clubDAO.insert(club)
                }
            }

            },
                modifier = Modifier
                    .height(42.dp),
                shape = RoundedCornerShape(5.dp)) {
                Text(
                    text = "Save Clubs To Database",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6650a4)
                )
            }
        }

        Box(modifier = Modifier.background(Color.White,shape = RoundedCornerShape(5.dp)),

        ){
            //Display all the clubs with details
            Text(
                text = leagueDetails,
                modifier = Modifier.verticalScroll(rememberScrollState()),
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6650a4)
            )
        }

    }
}



suspend fun fetchClubs(league:String):String{
    val url_string  = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=${league.replace(" ", "_")}"
    val url = URL(url_string)
    var con: HttpURLConnection = url.openConnection() as HttpURLConnection

    //collecting the json string from the web service
    val stringBuilder  = StringBuilder()

    // running launched coroutine code in a thread
    withContext(Dispatchers.IO){
        var bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bufferedReader.readLine()
        while (line != null){
            stringBuilder.append(line + "\n")
            line = bufferedReader.readLine()
        }
    }

    //Getting the available by clubs
    Log.d("String Builder","$stringBuilder")
    val allClubs  = parseJSON(stringBuilder)
    Log.d("Clubs","$allClubs")

    return allClubs
}
//Function to parse json data and to retrieve the clubs
fun parseJSON(stringBuilder : StringBuilder): String{

    //parsing json data using the string builder
    val json = JSONObject(stringBuilder.toString())
    //string builder to store the details of the clubs
    val allClubs = StringBuilder()

    //getting the teams array
    val jsonArray = json.getJSONArray("teams")
    for (i in 0 until jsonArray.length()) {
        //traversing and getting the appropriate details
        val team = jsonArray.getJSONObject(i)
        val teamID = team.getString("idTeam")
        val teamName = team.getString("strTeam")
        val teamShortName  = team.getString("strTeamShort")
        val teamAlternateName = team.getString("strAlternate")
        val formedYear = team.getString("intFormedYear")
        val league = team.getString("strLeague")
        val stadium = team.getString("strStadium")
        val keywords = team.getString("strKeywords")
        val stadiumThumbnail = team.getString("strStadiumThumb")
        val stadiumCapacity = team.getString("intStadiumCapacity")
        val teamWebsite = team.getString("strWebsite")
        val teamJersey = team.getString("strTeamJersey")
        val teamLogo = team.getString("strTeamLogo")

        //declaring a club club object
        val clubObject = Club(teamID.toInt(),teamName,teamShortName,teamAlternateName,
            formedYear.toInt(),league,stadium,keywords,stadiumThumbnail,stadiumCapacity.toInt(),teamWebsite,teamJersey,teamLogo)

        //add the club to the clubs list
        clubs.add(clubObject)

        allClubs.append("${i + 1} \" Team ID : $teamID\n Team Name : $teamName\"\n Team Short Name : $teamShortName\"\n Team Alternate Name : $teamAlternateName\"\n" +
                " Team Formed Year :  $formedYear\"\n League : $league\"\n Stadium : $stadium\"\n Keywords : $keywords\"\n Stadium Thumbnail : $stadiumThumbnail\"\n Stadium Capacity : $stadiumCapacity\"\n" +
                " Team Website : $teamWebsite\"\n Team Jersey : $teamJersey\"\n Team Jersey :  $teamLogo \n\n  ")

    }

    return allClubs.toString()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    FootballLeaguesTheme {
       SearchByLeagueGUI()
    }
}