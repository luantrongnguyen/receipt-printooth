package vn.toolsstation.mvvmexample.ui.admin.view.component

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import vn.herosoft.printer_bitmap_bluetooth.Receipt
import vn.herosoft.printer_bitmap_bluetooth.ReceiptBitmapGenerator
import vn.herosoft.printer_bitmap_bluetooth.ReceiptBitmapGenerator.Companion._38MM
import vn.toolsstation.mvvmexample.R
import vn.toolsstation.mvvmexample.ui.admin.model.ReceiptDetail
import vn.toolsstation.mvvmexample.ui.admin.model.Receipts
import vn.toolsstation.mvvmexample.ui.admin.viewModel.ReceiptDetailViewModel

//fun getReceipt(order:Receipts,context: Context,list:List<ReceiptDetail>):ReceiptBitmapGenerator.Builder {
//    // Initialize list of printables
//
//    return receiptBitmapGenerator
//}

@Composable
fun OrderCard(order: Receipts) {
    val openDialog = remember {
        mutableStateOf(false)
    }
    val detailOrderId = remember {
        mutableStateOf(0)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier
            .padding(16.dp)
            .clickable {
                detailOrderId.value = order.idReceipt
                openDialog.value = true
            }) {
            Text(
                text = "Order ID: ${order.idReceipt}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Customer Name: ${order.nameCustomer}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Customer Address: ${order.deliveryAddress}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Customer Email: ${order.email}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Created Date: ${order.createdAt}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if(openDialog.value) {
            SeeOrdersPage(order = order,id = detailOrderId.value, onDismissRequest = { openDialog.value = false })
        }else{
            null
        }

    }

}
@Composable
fun  SeeOrdersPage(order:Receipts,myViewModel: ReceiptDetailViewModel = viewModel(),onDismissRequest: () -> Unit,id:Int)  {
    val items by myViewModel.ordersData.collectAsState()
    LaunchedEffect(Unit) {
        myViewModel.retrieveReceiptData(id)
    }
    val context = LocalContext.current
    val logo = ContextCompat.getDrawable(context, R.drawable.logo)

    val footerText = "Thank you for your purchase!\nVisit us again.\nwww.thegovape.com"
    val receiptBitmapGenerator = ReceiptBitmapGenerator.Builder()
        .setDiameter(_38MM)
        .setCustomerDetails(order.nameCustomer, order.phone, order.deliveryAddress)
        .setLogo(logo!!)
    for(i in items){
        receiptBitmapGenerator.addReceiptItem(Receipt(i.name, i.quantity, i.price.trim().toInt()))
    }

    receiptBitmapGenerator.setFooterText(footer = footerText)
    receiptBitmapGenerator.build()

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)) {

                Image(
                    bitmap = receiptBitmapGenerator.getBitmap()!!.asImageBitmap(),
                    contentDescription = "some useful description",
                )
                Button(modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),onClick = { receiptBitmapGenerator.printReceipt(context) }) {
                    Text(text = "Print sample")
                }
            }
        }
    }
}
