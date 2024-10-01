//package vn.toolsstation.mvvmexample.ui.printing
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.res.Resources
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.BitmapDrawable
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Button
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.DefaultAlpha
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.core.content.ContextCompat
//import vn.toolsstation.mvvmexample.R
//
//
//class PrintingMainActivity: ComponentActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            Body()
//        }
//    }
//}
////private fun getReceipt(context: Context): Bitmap{
////    // Initialize list of printables
////    Log.d("xxx", "printSomeImages ")
////    val al = ArrayList<Printable>()
////    val resources: Resources = context.getResources()
////
////    //getContext();
////    val image = BitmapFactory.decodeResource(resources, R.drawable.test)
////    val logoBitmap =Bitmap.createScaledBitmap( ( ContextCompat.getDrawable(context, R.drawable.test) as BitmapDrawable).bitmap, 420, 420, false)
////    return logoBitmap
////}
////private fun printReceipt(bitmap: Bitmap) {
////    val al = java.util.ArrayList<Printable>()
////    al.add(ImagePrintable.Builder(bitmap).build())
////    al.add(RawPrintable.Builder(byteArrayOf(27, 100, 4)).build())
////    Printooth.printer().print(al)
////}
//
//
//
//@Composable
//fun Body(){
//    val status = remember { mutableStateOf("Connect") }
//    val context = LocalContext.current
//    val scanningActivityLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // Handle the result here
//            val data: Intent? = result.data
//            // Process the returned data
//            Printooth.printer().printingCallback = object : PrintingCallback {
//                override fun connectingWithPrinter() {
//                    Log.d("Printooth", "connectingWithPrinter")
//                }
//
//                override fun printingOrderSentSuccessfully() {
//                    Log.d("Printooth", "printingOrderSentSuccessfully")
//                }  //printer was received your printing order successfully.
//
//                override fun connectionFailed(error: String) {
//                    Log.d("Printooth", "connectionFailed: $error")
//                }
//
//                override fun disconnected() {
//                    Log.d("Printooth", "Disconnected")
//                }
//
//                override fun onError(error: String) {
//                    Log.d("Printooth", "onError: $error")
//                }
//
//                override fun onMessage(message: String) {
//                    Log.d("Printooth", "onMessage: $message")
//                }
//            }
//            status.value = "Connected"
//        }
//    }
//    LaunchedEffect(Unit) {
//        if(Printooth.hasPairedPrinter()) {
//            status.value = "Connected"
//        }
//        else
//            status.value = "Connect"
//    }
//    Scaffold( modifier = Modifier
//        .fillMaxSize()
//        .background(Color(0xff121212)), containerColor = Color(0xff121212),
//        content = { padding ->
//            Column {
//                Image(
//                    // first parameter of our Image
//                    // is our image path which we have created
//                    // above
//                    painter =  painterResource(id = R.drawable.icon),
//                    contentDescription = "Sample Image",
//
//                    // below line is used for creating a modifier for our image
//                    // which includes image size, padding and border
//                    modifier = Modifier
//                        .width(300.dp)
//                        .padding(16.dp),
//
//                    // below line is used to give
//                    // alignment to our image view.
//                    alignment = Alignment.Center,
//
//                    // below line is used to scale our image
//                    // we are using center crop for it.
//                    contentScale = ContentScale.Crop,
//
//                    // below line is used to define the opacity of the image.
//                    // Here, it is set to the default alpha value, DefaultAlpha.
//                    alpha = DefaultAlpha,
//                )
//                Button(onClick = { val intent = Intent(context, ScanningActivity::class.java)
//                    scanningActivityLauncher.launch(intent)}) {
//                    Text(text = status.value)
//                }
//                Button(onClick = { Toast.makeText(context,Printooth.hasPairedPrinter().toString(),Toast.LENGTH_SHORT).show()}) {
//                    Text(text = "Check connection")
//                }
//                Image(
//                    bitmap = getReceipt(context = context).asImageBitmap(),
//                    contentDescription = "some useful description",
//                )
//                Button(onClick = { printReceipt(getReceipt(context = context))}) {
//                    Text(text = "Print sample")
//                }
//            }
//        }
//
//    )
//
//}
//
//
//@Preview
//@Composable
//fun PreviewMessageCard() {
//    Body()
//}