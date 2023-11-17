package com.example.mycrudprj

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mycrudprj.AddUser.AddUserScreen
import com.example.mycrudprj.Data.User
import com.example.mycrudprj.ViewModelPackage.UserEvent
import com.example.mycrudprj.ViewModelPackage.UserViewModel
import com.example.mycrudprj.ui.theme.MyCRUDPrjTheme
import com.example.mycrudprj.ui.theme.Purple200
import com.example.mycrudprj.util.Routes
import com.example.mycrudprj.util.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

// This annotation comes from dagger-hilt. It is necessary if we want to inject dependency in an Android component
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyCRUDPrjTheme {
                // A surface container using the 'background' color from the theme
                //HomeScreen()
                EntryPoint()
            }
        }
    }
}

@Composable
fun EntryPoint(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.USER_LIST
    ){
        composable(Routes.USER_LIST){
            HomeScreen(onNavigate = {navController.navigate(it.route)})
        }
        composable(Routes.ADD_USER + "?userId={userId}",
            arguments = listOf(
                navArgument(name = "userId"){
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ){
            AddUserScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
    }
}

@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: UserViewModel = hiltViewModel()
){
    var users = viewModel.users.collectAsState(initial = emptyList())
    var newUser = remember { mutableStateOf("") }
    var emptyState = remember {mutableStateOf(false)}
    //Log.d("userLog", "$users")
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true){
        /*
        Collect UI events
        Execute this block independent of composable function
        Composable function is executed every single time our User list updates, so we don't want to subscribe to UI event every single time UI updates
        */
        viewModel.uiEvent.collect{event ->
            // this will be triggered for every single event we send into this UI event channel
            when(event){
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit // Don't do anything
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // to do
                viewModel.OnEvent(UserEvent.onAddUserClick)
            }) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colors.primary)
        ) {
            Text(
                text = "User List App",
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                color = Color.Cyan,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.size(20.dp))

            Button(
                onClick = {
                    viewModel.OnEvent(UserEvent.OnDeleteAllClick)
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(13, 91, 225)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp, bottom = 5.dp, end = 20.dp)
            ) {
                Text(text = "Delete All",
                    color = Color.White,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.size(20.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                items(users.value) { user ->
                    Row(
                        modifier = Modifier.padding(start = 10.dp,
                            top = 2.dp,
                            bottom = 2.dp,
                            end = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UserCard(
                            user = user,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.OnEvent(UserEvent.OnUserClick(user))
                                },
                            onEvent = viewModel::OnEvent
                        )
                        // Below is direct method
                        /*
                    Text(
                        text = user.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    IconButton(
                        onClick = { viewModel.OnEvent(UserEvent.OnDeleteClick(user)) }
                    )
                    {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }*/
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(
    user: User,
    onEvent:(UserEvent) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 1.dp, bottom = 1.dp, end = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = user.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.size(20.dp))

                IconButton(
                    onClick = {
                        //viewModel.OnEvent(UserEvent.OnDeleteClick(user))
                        onEvent(UserEvent.OnDeleteClick(user))
                    },
                    modifier = Modifier
                        .weight(1f)
                )
                {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyCRUDPrjTheme {
        Greeting("Android")
    }
}