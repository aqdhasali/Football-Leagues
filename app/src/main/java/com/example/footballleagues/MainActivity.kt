//Demo Video Link - https://drive.google.com/file/d/13sU6M-68H4FqCLIQ17DYlNK1vrR7oJKR/view?usp=sharing

package com.example.footballleagues

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.footballleagues.ui.theme.FootballLeaguesTheme
import com.example.footballleagues.ui.theme.Poppins
import kotlinx.coroutines.launch


lateinit var db:AppDatabase
lateinit var leagueDAO: LeagueDAO
lateinit var clubDAO: ClubDAO


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initializing the database
        db = Room.databaseBuilder(this,
            AppDatabase::class.java, "league-database").addMigrations().build()
        //initializing the DAO classes
        leagueDAO = db.getLeagueDao()
        clubDAO = db.getClubDao()


        setContent {
            GUI()
        }
    }
}


// Content in the main screen
@Composable
fun GUI() {
    // creating and initializing variables
    val context =  LocalContext.current // initialize context
    val scope = rememberCoroutineScope() //initialize coroutine scope


    Column(
        modifier  = Modifier
            .fillMaxSize()
            .background(Color(0xFFD0BCFF)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ){

        Text(
            text = "FOOTBALL LEAGUES & CLUBS. ",
            modifier  = Modifier.padding(30.dp),
            fontFamily = Poppins,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF6650a4)
        )
        //Button to add leagues to database
        ElevatedButton(
            onClick = {
                scope.launch {
                    // Inserting the details of the leagues into the database once the button is clicked.
                    leagueDAO.insert(
                        League(4328,"English Premier League","Soccer","Premier League, EPL"),
                        League(4329, "English League Championship", "Soccer","Championship"),
                        League(4330, "Scottish Premier League","Soccer","Scottish Premiership, SPFL"),
                        League(4331,"German Bundesliga","Soccer","Bundesliga, Fußball-Bundesliga"),
                        League(4332,"Italian Serie A","Soccer","Serie A"),
                        League(4334,"French League","Soccer","Ligue 1 Conforama"),
                        League(4335,"Spanish La Liga","Soccer","LaLiga Santander, La Liga"),
                        League(4336,"Greek Superleague Greece","Soccer",""),
                        League(4337,"Dutch Eredivisie","Soccer","Eredivisie"),
                        League(4338, "Belgian First Division A", "Soccer", "Jupiler Pro League"),
                        League(4339, "Turkish Super Lig", "Soccer", "Super Lig"),
                        League(4340, "Danish Superliga", "Soccer", ""),
                        League(4344, "Portuguese Primeira Liga", "Soccer", "Liga NOS"),
                        League(4346, "American Major League Soccer", "Soccer", "MLS, Major League Soccer"),
                        League(4347, "Swedish Allsvenskan", "Soccer", "Fotbollsallsvenskan"),
                        League(4350, "Mexican Primera League", "Soccer", "Liga MX"),
                        League(4351, "Brazilian Serie A", "Soccer", ""),
                        League(4354, "Ukrainian Premier League", "Soccer", ""),
                        League(4355, "Russian Football Premier League", "Soccer", "Чемпионат России по футболу"),
                        League(4356, "Australian A-League", "Soccer", "A-League"),
                        League(4358, "Norwegian Eliteserien", "Soccer", "Eliteserien"),
                        League(4359, "Chinese Super League", "Soccer", "")
                    )
                }
            },
            modifier = Modifier
                .height(42.dp)
                .width(250.dp),
            shape = RoundedCornerShape(5.dp)

        ) {
            Text(
                text = "Add Leagues To DB",
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6650a4)
            )
        }

        Spacer(modifier = Modifier.height(7.dp))
        //button to search clubs by league
        ElevatedButton(
            onClick = {

                val intent  = Intent(context, SearchByLeague::class.java)
                context.startActivity(intent)
                      },
            modifier = Modifier
                .height(42.dp)
                .width(250.dp),
            shape = RoundedCornerShape(5.dp)


        ) {
            Text(
                text = "Search For Clubs By League",
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6650a4)
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        //button to search for clubs
        ElevatedButton(
            onClick = {
                val intent  = Intent(context, SearchClubs::class.java)
                context.startActivity(intent)
                      },
            modifier = Modifier
                .height(42.dp)
                .width(250.dp),
            shape = RoundedCornerShape(5.dp)

        ) {
            Text(
                text = "Search For Clubs",
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6650a4)
            )
        }

        Spacer(modifier = Modifier.height(7.dp))
        //Button to View the jerseys
        ElevatedButton(
            onClick = {
                val intent  = Intent(context, ViewTeamJersey::class.java)
                context.startActivity(intent)
                      },
            modifier = Modifier
                .height(42.dp)
                .width(250.dp),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                text = "View Jerseys",
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF6650a4)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FootballLeaguesTheme {
        GUI()
    }
}