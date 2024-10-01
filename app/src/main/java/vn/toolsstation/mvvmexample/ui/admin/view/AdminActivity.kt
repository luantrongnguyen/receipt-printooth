

package vn.toolsstation.mvvmexample.ui.admin.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import vn.toolsstation.mvvmexample.ui.admin.view.component.Header
import vn.toolsstation.mvvmexample.ui.admin.view.pages.SeeOrdersPage


class AdminActivity : ComponentActivity() {

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
    Scaffold(
        content = { padding ->
            ConstraintLayout(modifier = Modifier.padding(padding)) {
                val (header, body) = createRefs()
                Header(modifier = Modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                }, onTabSelected = {
                    tab.value = if(it == 0) 0 else 1
                })
                Column(modifier = Modifier
                    .constrainAs(body) {
                        top.linkTo(header.bottom)
                        centerHorizontallyTo(parent)
                    }
                    .padding(top = 8.dp)) {
                    if(tab.value == 1){
                        SeeOrdersPage()
                    }else{
                        Text(text = "Trang chủ")
                    }
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
