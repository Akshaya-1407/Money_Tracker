package com.example.trackermoney.feature.add_expense

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trackermoney.R
import com.example.trackermoney.Utils
import com.example.trackermoney.data.model.ExpenseEntity
import com.example.trackermoney.viewmodel.AddExpenseViewModel
import com.example.trackermoney.viewmodel.AddExpenseViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AddExpense(navController: NavController){
    val viewModel = AddExpenseViewModelFactory(LocalContext.current).create(AddExpenseViewModel::class.java)
    val coroutineScope = rememberCoroutineScope()
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (nameRow, list, card, topBar) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_topbar),
                contentDescription = "TopBar",
                modifier = Modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            {
                Image(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterStart)
                )

                Text(
                    text = "Add Expense",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.Center)
                )
                Image(
                    imageVector = Icons.Default.MoreVert,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
            DataForm(modifier = Modifier
                .padding(top = 40.dp)
                .constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, onAddExpenseClick = {
                    coroutineScope.launch {
                        if (viewModel.addExpense(it)){
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DataForm(modifier: Modifier,onAddExpenseClick:(model : ExpenseEntity) -> Unit){
    val name  = remember { mutableStateOf("") }
    val amount = remember { mutableStateOf("") }
    val date = remember { mutableLongStateOf(0L) }
    val dateDialogVisibility = remember{ mutableStateOf(false)}
    val category = remember { mutableStateOf("") }
    val type = remember { mutableStateOf("") }
    Column(modifier = modifier
        .padding(16.dp)
        .fillMaxWidth()
        .shadow(16.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(Color.White)
        .padding(16.dp)
        .verticalScroll(rememberScrollState())
    ) {

        Text(text = "Name", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = name.value, onValueChange = {
            name.value = it
        },modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp))

        Text(text = "Amount", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = amount.value, onValueChange = {
            amount.value = it
        },modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp))

        Text(text = "Date", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        OutlinedTextField(value = if(date.longValue == 0L) "" else{
            Utils.formatDateToHumanReadableForm(date.value)
        },
            onValueChange = {},
            label = { Text("DD/MM/YYYY") },
            modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 12.dp)
            .clickable { dateDialogVisibility.value = true },
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledBorderColor = Color.Gray,
                disabledLabelColor = Color.Gray,
                disabledLeadingIconColor = Color.Gray,
                disabledTrailingIconColor = Color.Gray,
            )
        )

        Text(text = "Category", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp, bottom = 12.dp), fontWeight = FontWeight.Bold)
        ExpenseDropDown(
            listOf("Grocery","Diary","Fruits","Salary","Pocket Money","Rent","Bills","Ro","Extra"),
            onItemSelected = {
                category.value = it
            },
        )
        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "Type", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(top = 8.dp, bottom = 12.dp), fontWeight = FontWeight.Bold)
        ExpenseDropDown(
            listOf("Income","Expense"),
            onItemSelected = {
                type.value = it
            }
        )
        Spacer(modifier = Modifier.size(8.dp))

        Button(onClick = {
//            val model = ExpenseEntity(
//                null,
//                name.value,
//                amount.value.toDoubleOrNull()?:0.0,
//                Utils.formatDateToHumanReadableForm(date.longValue).toLong(),
//                category.value,
//                type.value
//            )
//            onAddExpenseClick(model)
            try {
                val model = ExpenseEntity(
                    id = null,
                    name.value,
                    amount = amount.value.toDoubleOrNull() ?: 0.0,
                    date = date.value,
                    category = category.value,
                    type = type.value
                )
                onAddExpenseClick(model)
            } catch (e: Exception) {
                Log.d("AddExpenseError", "Error creating ExpenseEntity: ${e.message}")
            }
        },modifier = Modifier.fillMaxWidth()) {
            Text(text = "Add Expense", fontSize = 14.sp)
        }
    }
    if(dateDialogVisibility.value){
        ExpenseDatePickerDialog(onDateSelected = {
            date.longValue = it
            dateDialogVisibility.value = false },
            onDismiss = {
                dateDialogVisibility.value = false
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date : Long) -> Unit,
    onDismiss: () -> Unit
){
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0
    DatePickerDialog(
        onDismissRequest = {onDismiss()},
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(selectedDate)
            }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(listOfItems: List<String>, onItemSelected: (String) -> Unit){
    val expanded = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf<String>(listOfItems[0]) }

    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = {expanded.value = it}) {
        TextField(value = selectedItem.value,onValueChange = {selectedItem.value = it},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            }
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = {  }){
            listOfItems.forEach {
                DropdownMenuItem(text = { Text(text = it) }, onClick = {
                    selectedItem.value = it
                    onItemSelected(selectedItem.value)
                    expanded.value = false }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ShowAddExpense(){
    AddExpense(rememberNavController())
}