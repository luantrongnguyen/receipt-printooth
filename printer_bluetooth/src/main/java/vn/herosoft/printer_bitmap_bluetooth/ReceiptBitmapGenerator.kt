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
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import vn.herosoft.printer_bitmap_bluetooth.printooth.Printooth
import vn.herosoft.printer_bitmap_bluetooth.printooth.data.printable.ImagePrintable
import vn.herosoft.printer_bitmap_bluetooth.printooth.data.printable.Printable
import vn.herosoft.printer_bitmap_bluetooth.printooth.ui.ScanningActivity
import java.util.LinkedList
import java.util.Queue

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
    private val bitmapsToPrint: ArrayList<Bitmap> = ArrayList() // Queue for printing bitmaps

    // Constant for receipt size
    companion object {
        const val _38MM = 57
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
        fun setDiameter(diameter: Int): Builder {
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
         * Build the receipt and generate bitmaps for printing.
         */
        fun build(): Builder {
            generator.generateReceiptParts()
            return this
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
                    // Printer is connected, print all parts in the queue
                    printNextBitmap()
                }
            }
        }

        /**
         * Print the next bitmap in the queue.
         */
        private fun printNextBitmap() {
            if (generator.bitmapsToPrint.isNotEmpty()) {
                val al = java.util.ArrayList<Printable>().apply {
                    for ( i in 1..generator.bitmapsToPrint.size)
                        add(ImagePrintable.Builder(generator.bitmapsToPrint[i-1]).build()) // Add receipt bitmap
                    // Add raw printable data
                }
                Printooth.printer().print(al)
            }
        }
    }

    /**
     * Generates the receipt parts (header, body, footer) as Bitmap images.
     */
    private fun generateReceiptParts() {
        val headerBitmap = generateHeader()
        val bodyBitmaps = generateBody() // Generates multiple bitmaps for the body
        val footerBitmap = generateFooter()

        // Add all bitmaps to the print queue
        bitmapsToPrint.add(headerBitmap)
        bitmapsToPrint.addAll(bodyBitmaps)
        bitmapsToPrint.add(footerBitmap)
    }

    var yPosition = 10f
    /**
     * Generates the header part of the receipt as a Bitmap image.
     *
     * @return Bitmap The generated header as a Bitmap.
     */
    private fun generateHeader(): Bitmap {
        val width = (diameter / 25) * 190
        val bitmap = Bitmap.createBitmap(width, 50, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Draw header text
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText("Receipt", width/2f - 30, 30f, paint)
        return bitmap
    }

    /**
     * Generates the body part of the receipt as a list of Bitmap images.
     *
     * @return List<Bitmap> The list of generated body items as Bitmaps.
     */
    private fun generateBody(): List<Bitmap> {
        val width = (diameter / 25) * 190
        val bodyBitmaps = mutableListOf<Bitmap>()
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 20f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }

        var currentBitmap: Bitmap? = null
        var canvas: Canvas? = null
        val itemsPerPage = 20 // Number of items per page

        for ((index, receipt) in listReceipt.withIndex()) {
            if (index % itemsPerPage == 0) { // Create a new bitmap for a new page
                currentBitmap = Bitmap.createBitmap(width, 55*10, Bitmap.Config.ARGB_8888)
                canvas = Canvas(currentBitmap)
                canvas.drawColor(Color.WHITE)
                yPosition = 30f
                bodyBitmaps.add(currentBitmap) // Add the newly created bitmap to the list
            }

            // Draw each item on the current page
            canvas?.drawText("${receipt.name} x ${receipt.quantity}", 0f, yPosition.toFloat(), textPaint)
            yPosition += 30 // Move yPosition for the next item
        }
        yPosition = 10f
        return bodyBitmaps
    }

    /**
     * Generates the footer part of the receipt as a Bitmap image.
     *
     * @return Bitmap The generated footer as a Bitmap.
     */
    private fun generateFooter(): Bitmap {
        val width = (diameter / 25) * 190
        val bitmap = Bitmap.createBitmap(width, 6*25 + 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        var total = 0
        yPosition += 10f
        for (receipt in listReceipt) {
            total += receipt.price.toInt()
        }
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 20f
            typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
        }
        canvas.drawText("Total", 4f, yPosition, textPaint)
        canvas.drawText(formatNumberWithCommas(total), width - width / 4f, yPosition, textPaint)

        // Draw customer details
        yPosition += 20f
        canvas.drawText("Customer: $customerName", 4f, yPosition, textPaint)
        yPosition += 20f
        canvas.drawText("Phone: $customerPhone", 4f, yPosition, textPaint)
        yPosition += 20f
        canvas.drawText("Add: $customerAddress", 4f, yPosition, textPaint)

        // Draw the footer text
        yPosition += 40f
        val footerLines = footerText.split("\n")
        for (footerLine in footerLines) {
            canvas.drawText(footerLine, width / 2f, yPosition, textPaint)
            yPosition += 20f
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
