package vn.toolsstation.mvvmexample.ui.admin.view.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vn.toolsstation.mvvmexample.ui.admin.view.component.OrderCard
import vn.toolsstation.mvvmexample.ui.admin.viewModel.ReceiptViewModel

@Composable
fun  SeeOrdersPage(myViewModel: ReceiptViewModel = viewModel())  {
    val items by myViewModel.ordersData.collectAsState()
    LaunchedEffect(Unit) {
        myViewModel.retrieveAgentsData()
    }
    LazyColumn( modifier = Modifier.padding(bottom = 64.dp),verticalArrangement = Arrangement.spacedBy(8.dp),) {
        items(items.size) { item ->
            OrderCard(items[item])
        }

    }
}
