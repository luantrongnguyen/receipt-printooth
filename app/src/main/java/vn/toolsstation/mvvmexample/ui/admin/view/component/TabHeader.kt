package vn.toolsstation.mvvmexample.ui.admin.view.component


import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.shape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import vn.toolsstation.mvvmexample.R

@Composable
fun Header(modifier: Modifier, onTabSelected:(Int) -> Unit ) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val tabIndex = remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        onTabSelected(0)
    }
    Row(modifier = modifier){
        Button(
            modifier = Modifier
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        color = Color(0xffeb3678),
                        radius = 8.dp
                    )
                ).weight(1f).padding(start = 8.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = if(tabIndex.value == 0) Color(0xffeb3678) else Color(0xffffffff),
                contentColor = if(tabIndex.value == 1) Color(0xffeb3678) else Color(0xffffffff),
            ),
            border = BorderStroke(1.dp, Color(0xffeb3678))
            ,

            onClick = {
                tabIndex.value = 0
                onTabSelected(0)
            },

            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Tạo Sản Phẩm")
        }
        Button(
            modifier = Modifier
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        color = Color(0xffeb3678),
                        radius = 8.dp
                    )
                ).weight(1f)
                .padding(start = 8.dp, end = 8.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = if(tabIndex.value == 1) Color(0xffeb3678) else Color(0xffffffff),
                contentColor = if(tabIndex.value == 0) Color(0xffeb3678) else Color(0xffffffff),
            ),
            shape = RoundedCornerShape(8.dp)
            ,
            border = BorderStroke(1.dp, Color(0xffeb3678))
            ,
            onClick = {
                tabIndex.value = 1
                onTabSelected(1)
            },
        ) {
            Text(text = "Xem Đơn Hàng")
        }
    }


}