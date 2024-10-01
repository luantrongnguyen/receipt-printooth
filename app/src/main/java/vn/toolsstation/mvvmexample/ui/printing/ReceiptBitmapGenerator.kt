//package vn.toolsstation.mvvmexample.ui.printing
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.Canvas
//import android.graphics.Color
//import android.graphics.Paint
//import android.graphics.Typeface
//import android.graphics.drawable.BitmapDrawable
//import android.graphics.drawable.Drawable
//import vn.toolsstation.mvvmexample.ui.admin.model.Receipt
//
//class ReceiptBitmapGenerator {
//
//    fun generateReceipt(context: Context, customerName: String, customerPhone: String, customerAddress:String , logo: Drawable, listReceipt: ArrayList<Receipt>, footerText: String): Bitmap {
//        val textSize = 25f
//        val lineSpacing = 40f
//        val footerSpacing = 60f
//        //pixels = (mm / 25.4) * DPI
//        val pixels = (57 / 25.4) * 203
//        val extraSpace = 240
//        var lines = 6 // số dòng in thêm ngoài menu
//
//        // Paint objects for the company name and receipt details
//        val textPaint = Paint().apply {
//            color = Color.BLACK
//            setTextSize(16f)
//            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
//        }
//        val header = Paint().apply {
//            color = Color.BLACK
//            setTextSize(24f)
//            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
//        }
//        val detailPaint = Paint().apply {
//            color = Color.BLACK
//            setTextSize(16f)
//            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
//        }
//        val totalPaint = Paint().apply {
//            color = Color.BLACK
//            setTextSize(20f)
//            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
//        }
//        // Paint object for the footer
//        val pricePaint = Paint().apply {
//            color = Color.BLACK
//            setTextSize(16f)
//            textAlign = Paint.Align.RIGHT // Set text alignment to center
//            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
//        }
//        // Paint object for the footer
//        val footerPaint = Paint().apply {
//            color = Color.BLACK
//            setTextSize(24f)
//            textAlign = Paint.Align.CENTER // Set text alignment to center
//            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
//        }
//
//        // Convert the logo drawable to bitmap and get its height
//        val logoBitmap =Bitmap.createScaledBitmap( (logo as BitmapDrawable).bitmap, 80, 80, false)
//        val logoHeight = logoBitmap.height
//
//        // Split the receipt details into lines
//        val numberOfLines = listReceipt.size
//
//        // Calculate total height based on logo and text
//        val totalHeight = (numberOfLines * lineSpacing + logoHeight + 20 + footerSpacing + 20 + textSize*lines + extraSpace ).toInt()
//
//
//        // Set width and create a bitmap with dynamic height
//        val width = 400// Fixed width for receipt
//        val bitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        canvas.drawColor(Color.WHITE) // Set background color to white
//
////        // Draw the company name
////        canvas.drawText(companyName,  (width / 2f), 20f, textPaint)
//
//        // Draw the logo below the company name
//        canvas.drawBitmap(logoBitmap, (width - logoBitmap.width) / 2f, 10f, null)
//
//        // Draw the receipt details line by line below the logo
//        var yPosition = logoHeight + 20f // Start below the logo
//        // Receipt
//        yPosition += lineSpacing
//        canvas.drawText("Receipt", width/2f - 30 , yPosition, header)
//
//        var total = 0
//         yPosition = yPosition + 50f
//        for (receipt in listReceipt) {
//            canvas.drawText(receipt.name + " x " +receipt.quantity , 4f, yPosition, detailPaint)
//
//            canvas.drawText(formatNumberWithCommas(Integer.parseInt(receipt.price.trim())), (width)/1f - 20, yPosition, pricePaint)
//
//            yPosition += lineSpacing
//            total += receipt.price.trim().toInt()
//        }
//        yPosition += lineSpacing
//        canvas.drawText("Tổng cộng", 4f, yPosition, totalPaint)
//
//        canvas.drawText(formatNumberWithCommas(total), (width)/1f - 110, yPosition, totalPaint)
//
//        yPosition += lineSpacing
//        canvas.drawText("Khách hàng: " + customerName, 4f, yPosition, textPaint)
//
//        yPosition += lineSpacing
//        canvas.drawText("SDT: " + customerPhone, 4f, yPosition, textPaint)
//
//        yPosition += lineSpacing
//        canvas.drawText("Add: " + customerAddress, 4f, yPosition, textPaint)
//
//        // Draw the footer text, centered at the bottom\
//        yPosition += footerSpacing // Add some spacing before footer
//        val footerLines = footerText.split("\n")
//        for (footerLine in footerLines) {
//            canvas.drawText(footerLine, (width / 2f), yPosition, footerPaint) // Centered text
//            yPosition += lineSpacing
//
//        }
//        return bitmap
//    }
//    fun formatNumberWithCommas(number: Int): String {
//        return "%,d".format(number)
//    }
//
//}
