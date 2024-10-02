package vn.herosoft.printer_bitmap_bluetooth

import android.Manifest
import android.app.Activity
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
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.ui.ScanningActivity

/**
 * ReceiptBitmapGenerator is responsible for generating a bitmap image
 * of a receipt, including customer details, receipt items, and a logo.
 * It provides a builder pattern to allow for easy customization of the receipt.
 *
 * @Author Nguyen Trong Luan - HeroSolution Vietnam
 */
open class ReceiptBitmapGenerator {

    // Customer details
    private var customerName: String = ""
    private var customerPhone: String = ""
    private var customerAddress: String = ""

    // Logo of the receipt
    private lateinit var logo: Drawable

    // List of receipt items
    private val listReceipt: ArrayList<Receipt> = ArrayList()

    // Footer text for the receipt
    private var footerText: String = ""

    // Diameter of the receipt printer
    private var diameter: Int = 0
    private var bitmap: Bitmap? = null

    // Constant for receipt size
    companion object {
        val _38MM = 57
    }

    /**
     * Builder class for constructing a ReceiptBitmapGenerator with customizable fields.
     */
    class Builder {

        // Instance of ReceiptBitmapGenerator to be configured
        private val generator = ReceiptBitmapGenerator()

        /**
         * Set the diameter of the receipt printer.
         *
         * @param diameter The diameter of the printer in millimeters.
         */
        fun setDiameter(diameter: Int): Builder  {
            generator.diameter = diameter
            return this
        }

        /**
         * Set the customer details (name, phone, and address) for the receipt.
         *
         * @param name The customer's name.
         * @param phone The customer's phone number.
         * @param address The customer's address.
         * @return Builder The current builder instance.
         */
        fun setCustomerDetails(name: String, phone: String, address: String): Builder {
            generator.customerName = name
            generator.customerPhone = phone
            generator.customerAddress = address
            return this
        }

        /**
         * Set the logo for the receipt.
         *
         * @param logo The Drawable representing the logo to be printed on the receipt.
         * @return Builder The current builder instance.
         */
        fun setLogo(logo: Drawable): Builder {
            generator.logo = logo
            return this
        }

        /**
         * Add an item to the receipt.
         *
         * @param receipt The Receipt object representing the item to be added.
         * @return Builder The current builder instance.
         */
        fun addReceiptItem(receipt: Receipt): Builder {
            generator.listReceipt.add(receipt)
            return this
        }

        /**
         * Set the footer text of the receipt.
         *
         * @param footer The footer text.
         * @return Builder The current builder instance.
         */
        fun setFooterText(footer: String): Builder {
            generator.footerText = footer
            return this
        }

        /**
         * Build the receipt as a Bitmap image.
         *
         */
        fun build(): Builder {
            generator.bitmap = generator.generateReceipt(diameter = generator.diameter)
            return this
        }
        /**
         * Generate the receipt as a Bitmap image.
         *
         * @return Bitmap The generated receipt as a Bitmap.
         */
        fun getBitmap(): Bitmap? {
            if(generator.bitmap == null)
                throw (Throwable("Bitmap not set =.="))
            return generator.bitmap
        }

        /**
         * Print the generated receipt using the Printooth library.
         *
         * @param context The context for printing the receipt.
         */
        fun printReceipt(context: Context) {
            Printooth.init(context)

            // Permissions required for Bluetooth and networking
            val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }

            val REQUEST_CODE_PERMISSIONS = 1001

            /**
             * Check and request the required permissions for Bluetooth.
             *
             * @param activity The activity context.
             * @param onPermissionsGranted A callback to be executed if all permissions are granted.
             */
            fun checkAndRequestPermissions(activity: Context, onPermissionsGranted: () -> Unit) {
                val missingPermissions = REQUIRED_PERMISSIONS.filter {
                    ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
                }

                if (missingPermissions.isNotEmpty()) {
                    ActivityCompat.requestPermissions(
                        activity as Activity,
                        missingPermissions.toTypedArray(),
                        REQUEST_CODE_PERMISSIONS
                    )
                } else {
                    // All permissions are granted
                    onPermissionsGranted()
                }
            }

            // Check if the printer is connected and initiate printing
            checkAndRequestPermissions(context) {
                if (!Printooth.hasPairedPrinter()) {
                    // Start ScanActivity if no printer is connected
                    val intent = Intent(context, ScanningActivity::class.java)
                    context.startActivity(intent)
                } else {
                    // Printer is connected, generate and print the receipt
                    val bitmap = generator.bitmap
                    if(bitmap != null) {
                        val al = java.util.ArrayList<Printable>().apply {
                            add(ImagePrintable.Builder(bitmap).build()) // Add receipt bitmap
                            add(
                                RawPrintable.Builder(byteArrayOf(27, 100, 4)).build()
                            ) // Add raw printable data
                        }
                        Printooth.printer().print(al) // Print using Printooth
                    }else
                        throw (Throwable("Bitmap not set =.="))
                }
            }
        }
    }

    /**
     * Generates the receipt as a Bitmap image.
     *
     * @param diameter The diameter of the receipt printer.
     * @return Bitmap The generated receipt as a Bitmap.
     * @throws Throwable If the diameter is not set.
     */
    private fun generateReceipt(diameter: Int): Bitmap {
        var textSizeLarge = 0f
        var textSizeMedium = 0f
        var textSizeSmall = 0f
        var lineSpacing = 0f
        var footerSpacing = 0f
        var pixels = 0.0 // Pixels for receipt width
        var extraSpace = 0
        var lines = 0

        if (diameter == 0) {
            throw (Throwable("Diameter not set"))
        }

        // Handling different diameter sizes
        if (diameter == _38MM) {
            textSizeLarge = 24f
            textSizeMedium = 20f
            textSizeSmall = 16f
            lineSpacing = 40f
            footerSpacing = 60f
            pixels = (diameter / 25.4) * 190// Width for 57mm paper in pixels
            extraSpace = 240
            lines = 6
        }

        // Paint objects for text formatting
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = textSizeSmall
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val headerPaint = Paint().apply {
            color = Color.BLACK
            textSize = textSizeLarge
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val detailPaint = Paint().apply {
            color = Color.BLACK
            textSize = textSizeSmall
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }
        val totalPaint = Paint().apply {
            color = Color.BLACK
            textSize = textSizeMedium
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD)
        }
        val pricePaint = Paint().apply {
            color = Color.BLACK
            textSize = textSizeSmall
            textAlign = Paint.Align.RIGHT
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }
        val footerPaint = Paint().apply {
            color = Color.BLACK
            textSize = textSizeLarge
            textAlign = Paint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
        }

        // Convert the logo drawable to bitmap
        val logoBitmap = Bitmap.createScaledBitmap((logo as BitmapDrawable).bitmap, 80, 80, false)
        val logoHeight = logoBitmap.height

        // Calculate the height of the bitmap
        val numberOfLines = listReceipt.size
        val totalHeight = (numberOfLines * lineSpacing + logoHeight + footerSpacing + textSizeLarge * lines + extraSpace).toInt()

        // Create the bitmap
        val width = pixels.toInt()
        val bitmap = Bitmap.createBitmap(width, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Draw the logo at the top center
        canvas.drawBitmap(logoBitmap, (width - logoBitmap.width) / 2f, 10f, null)

        // Draw the header (e.g., "Receipt")
        var yPosition = logoHeight + 50f
        canvas.drawText("Receipt", width / 2f - 30, yPosition, headerPaint)

        // Draw receipt details
        var total = 0
        yPosition += 50f
        for (receipt in listReceipt) {
            canvas.drawText("${receipt.name} x ${receipt.quantity}", 4f, yPosition, detailPaint)
            canvas.drawText(formatNumberWithCommas(receipt.price.toInt()), width - width / 20f, yPosition, pricePaint)
            yPosition += lineSpacing
            total += receipt.price.toInt()
        }

        // Draw the total
        yPosition += lineSpacing
        canvas.drawText("Total", 4f, yPosition, totalPaint)
        canvas.drawText(formatNumberWithCommas(total), width - width / 4f, yPosition, totalPaint)

        // Draw customer details
        yPosition += lineSpacing
        canvas.drawText("Customer: $customerName", 4f, yPosition, textPaint)
        yPosition += lineSpacing
        canvas.drawText("Phone: $customerPhone", 4f, yPosition, textPaint)
        yPosition += lineSpacing
        canvas.drawText("Add: $customerAddress", 4f, yPosition, textPaint)

        // Draw the footer text
        yPosition += footerSpacing
        val footerLines = footerText.split("\n")
        for (footerLine in footerLines) {
            canvas.drawText(footerLine, width / 2f, yPosition, footerPaint)
            yPosition += lineSpacing
        }

        return bitmap
    }

    /**
     * Helper function to format numbers with commas.
     *
     * @param number The number to be formatted.
     * @return String The formatted number as a string.
     */
    private fun formatNumberWithCommas(number: Int): String {
        return "%,d".format(number)
    }
}
