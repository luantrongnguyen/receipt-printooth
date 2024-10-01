package vn.herosolution.receiptprintooth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.ui.ScanningActivity

class ReceiptBitmapGenerator {

    private var customerName: String = ""
    private var customerPhone: String = ""
    private var customerAddress: String = ""
    private lateinit var logo: Drawable
    private val listReceipt: ArrayList<Receipt> = ArrayList()
    private var footerText: String = ""

    // Builder class to allow appending receipt lines and final print execution
    class Builder {
        private val generator = ReceiptBitmapGenerator()

        fun setCustomerDetails(name: String, phone: String, address: String): Builder {
            generator.customerName = name
            generator.customerPhone = phone
            generator.customerAddress = address
            return this
        }

        fun setLogo(logo: Drawable): Builder {
            generator.logo = logo
            return this
        }

        fun addReceiptItem(receipt: Receipt): Builder {
            generator.listReceipt.add(receipt)
            return this
        }

        fun setFooterText(footer: String): Builder {
            generator.footerText = footer
            return this
        }

        fun build(context: Context): Bitmap {
            return generator.generateReceipt(context)
        }

        fun printReceipt(context: Context) {
            Printooth.init(context)

             val REQUIRED_PERMISSIONS = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

             val REQUEST_CODE_PERMISSIONS = 1001

            // Function to check permissions
            fun checkAndRequestPermissions(activity: Context, onPermissionsGranted: () -> Unit) {
                val missingPermissions = REQUIRED_PERMISSIONS.filter {
                    ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
                }

                if (missingPermissions.isNotEmpty()) {
                    ActivityCompat.requestPermissions(
                        context as AppCompatActivity,
                        missingPermissions.toTypedArray(),
                        REQUEST_CODE_PERMISSIONS
                    )
                } else {
                    // All permissions are granted
                    onPermissionsGranted()
                }
            }
            // Check if the printer is connected
            checkAndRequestPermissions(context) {
                if (!Printooth.hasPairedPrinter()) {
                    // If no printer is connected, start the ScanActivity to allow the user to connect
                    val intent = Intent(context, ScanningActivity::class.java)
                    context.startActivity(intent)
                } else {
                    // Printer is connected, proceed with printing the receipt
                    val bitmap = generator.generateReceipt(context)
                    val al = java.util.ArrayList<Printable>()
                    al.add(
                        ImagePrintable.Builder(bitmap).build()
                    ) // Add receipt bitmap for printing
                    al.add(
                        RawPrintable.Builder(byteArrayOf(27, 100, 4)).build()
                    ) // Add raw printable data
                    Printooth.printer().print(al) // Send to Printooth printer
                }
            }
        }
    }

    // Function to generate receipt bitmap
    private fun generateReceipt(context: Context): Bitmap {
        val textSize = 25f
        val lineSpacing = 40f
        val footerSpacing = 60f
        val pixels = (57 / 25.4) * 203 // Pixels for receipt width
        val extraSpace = 240
        var lines = 6

        // Paint objects for text formatting
        val textPaint = Paint().apply {
            color = Color.BLACK
            setTextSize(16f)
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val header = Paint().apply {
            color = Color.BLACK
            setTextSize(24f)
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val detailPaint = Paint().apply {
            color = Color.BLACK
            setTextSize(16f)
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }
        val totalPaint = Paint().apply {
            color = Color.BLACK
            setTextSize(20f)
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        }
        val pricePaint = Paint().apply {
            color = Color.BLACK
            setTextSize(16f)
            textAlign = Paint.Align.RIGHT
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }
        val footerPaint = Paint().apply {
            color = Color.BLACK
            setTextSize(24f)
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
        }

        // Convert the logo drawable to bitmap
        val logoBitmap = Bitmap.createScaledBitmap((logo as BitmapDrawable).bitmap, 80, 80, false)
        val logoHeight = logoBitmap.height
        val numberOfLines = listReceipt.size
        val totalHeight = (numberOfLines * lineSpacing + logoHeight + footerSpacing + textSize * lines + extraSpace).toInt()

        // Create bitmap
        val width = 400
        val bitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Draw logo
        canvas.drawBitmap(logoBitmap, (width - logoBitmap.width) / 2f, 10f, null)

        // Draw receipt details
        var yPosition = logoHeight + 50f
        canvas.drawText("Receipt", width / 2f - 30, yPosition, header)

        var total = 0
        yPosition += 50f
        for (receipt in listReceipt) {
            canvas.drawText("${receipt.name} x ${receipt.quantity}", 4f, yPosition, detailPaint)
            canvas.drawText(formatNumberWithCommas(receipt.price.toInt()), width - 20f, yPosition, pricePaint)
            yPosition += lineSpacing
            total += receipt.price.toInt()
        }

        yPosition += lineSpacing
        canvas.drawText("Tổng cộng", 4f, yPosition, totalPaint)
        canvas.drawText(formatNumberWithCommas(total), width - 110f, yPosition, totalPaint)

        // Draw customer details
        yPosition += lineSpacing
        canvas.drawText("Khách hàng: $customerName", 4f, yPosition, textPaint)
        yPosition += lineSpacing
        canvas.drawText("SDT: $customerPhone", 4f, yPosition, textPaint)
        yPosition += lineSpacing
        canvas.drawText("Add: $customerAddress", 4f, yPosition, textPaint)

        // Draw footer
        yPosition += footerSpacing
        val footerLines = footerText.split("\n")
        for (footerLine in footerLines) {
            canvas.drawText(footerLine, width / 2f, yPosition, footerPaint)
            yPosition += lineSpacing
        }

        return bitmap
    }

    // Format numbers with commas
    private fun formatNumberWithCommas(number: Int): String {
        return "%,d".format(number)
    }
}
