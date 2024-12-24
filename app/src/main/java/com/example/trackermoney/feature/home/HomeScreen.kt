package com.example.trackermoney.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trackermoney.R
import com.example.trackermoney.Utils
import com.example.trackermoney.data.model.ExpenseEntity
import com.example.trackermoney.ui.theme.Zinc
import com.example.trackermoney.viewmodel.HomeViewModel
import com.example.trackermoney.viewmodel.HomeViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class)
@Composable
fun HomeScreen(navController: NavController){
    val viewModel : HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val(nameRow,list,card,topBar,add) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar), contentDescription = "TopBar",
                modifier = Modifier.constrainAs(topBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Box (
                modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp, start = 16.dp, end = 16.dp)
                .constrainAs(nameRow){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
            }){
                Column {
                    Text(text = "Good Afternoon", fontSize = 16.sp, color = Color.White)
                    Text(text = "Akshaya", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White )
                }
                Image(
                    imageVector = Icons.Default.Notifications,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "More",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val balance = viewModel.getBalance(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val expense = viewModel.getTotalExpense(state.value)
            CardItem(modifier = Modifier
                .constrainAs(card){
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },balance,income,expense)
//            Text(text = "hello",color = Color.Green)
            TransactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(list){
                        top.linkTo(card.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    }, list = state.value
            )

            Image(painter = painterResource(R.drawable.ic_add1),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(add){
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }

                    .padding(bottom = 15.dp, end = 15.dp)
                        .size(48.dp)
                    .clickable {
                        navController.navigate("/addExpense")
                    }

            )
        }
    }
}

@Composable
fun CardItem(modifier: Modifier, balance: String, income: String, expense: String) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .shadow(8.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Zinc)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(text = "Total Balance", fontSize = 16.sp, color = Color.White)
                Text(text = "Rs.$balance", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Image(
                imageVector = Icons.Default.MoreVert,
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "More",
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth().weight(1f))
        {
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterStart),
                title = "Income",
                amount = "Rs.$income",
                icon = Icons.Default.KeyboardArrowDown
            )
            CardRowItem(
                modifier = Modifier.align(Alignment.CenterEnd),
                title = "Expense",
                amount = "Rs.$expense",
                icon = Icons.Default.KeyboardArrowUp
            )
        }
    }
}


@Composable
fun TransactionList(modifier: Modifier, list: List<ExpenseEntity>,title: String = "Recent Transactions"){
//    Text(text = "hello",color = Color.Green)
    LazyColumn(modifier = modifier
        .padding(16.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = title, fontSize = 20.sp)
                Text(
                    text = "See All",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }
        items(list){item ->
            TransactionItem(
                title = item.title,
                amount = item.amount.toString(),
                icon = if (item.type == "Income") R.drawable.ic_income else R.drawable.ic_expense,
                date = Utils.formatDateToHumanReadableForm(item.date),
                color = if (item.type == "Income") Color.Green else Color.Red
            )
        }
    }
}


@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, icon: ImageVector) {
    Column(
        modifier = modifier
    ){
        Row {
            Image(
                imageVector = icon,
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = title, fontSize = 16.sp, color = Color.White)
        }
        Text(text = amount, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun TransactionItem(title: String, amount: String, icon: Int, date: String,color: Color){
//    Text(text = "hello",color = Color.Green)
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = date, fontSize = 12.sp)
            }
        }
        if(color == Color.Green){
            Text(text = "+$amount", fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterEnd), color = color)
        }
        else{
            Text(text = "-$amount", fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterEnd), color = color)
        }

    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview(){
    HomeScreen(rememberNavController())
}