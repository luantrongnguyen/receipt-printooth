package vn.toolsstation.mvvmexample.ui.home.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import vn.toolsstation.mvvmexample.R

class HomeActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Body()
        }
    }
}
@Composable
fun Body(){
    val tab = remember { mutableStateOf(0) }
    Scaffold( modifier = Modifier.fillMaxSize().background(Color(0xff121212)), containerColor = Color(0xff121212),
        content = { padding ->
            ConstraintLayout(modifier = Modifier.padding(padding)) {
                val (header, body) = createRefs()
                Row {
                    Image(
                        // first parameter of our Image
                        // is our image path which we have created
                        // above
                        painter =  painterResource(id = R.drawable.icon),
                        contentDescription = "Sample Image",

                        // below line is used for creating a modifier for our image
                        // which includes image size, padding and border
                        modifier = Modifier
                            .width(300.dp)
                            .padding(16.dp),

                        // below line is used to give
                        // alignment to our image view.
                        alignment = Alignment.Center,

                        // below line is used to scale our image
                        // we are using center crop for it.
                        contentScale = ContentScale.Crop,

                        // below line is used to define the opacity of the image.
                        // Here, it is set to the default alpha value, DefaultAlpha.
                        alpha = DefaultAlpha,
                    )
                }

            }
        }
    )
}
@Preview
@Composable
fun PreviewMessageCard() {
    Body()
}