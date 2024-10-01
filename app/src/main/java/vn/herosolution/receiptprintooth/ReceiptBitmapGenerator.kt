package vn.herosolution.receiptprintooth

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

class ReceiptBitmapGenerator private constructor(
    private val context: Context,
    private val customerName: String,
    private val customerPhone: String,
    private val customerAddress: String,
    private val logo: Drawable,
    private val footerText: String
) {
    private val listReceipt: MutableList<Receipt> = mutableListOf()
    private var yPosition = 0f
    private val lineSpacing = 40f

    // Initialize Paint objects
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 16f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val detailPaint = Paint().apply {
        color = Color.BLACK
        textSize = 16f
        typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL)
    }
    private val pricePaint = Paint().apply {
        color = Color.BLACK
        textSize = 16f
        textAlign = Paint.Align.RIGHT
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }
    private val footerPaint = Paint().apply {
        color = Color.BLACK
        textSize = 24f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
    }

    class Builder(private val context: Context) {
        private var customerName: String = ""
        private var customerPhone: String = ""
        private var customerAddress: String = ""
        private var logo: Drawable? = null
        private var footerText: String = ""
        private val listReceipt: MutableList<Receipt> = mutableListOf()

        fun setCustomerName(name: String) = apply { this.customerName = name }
        fun setCustomerPhone(phone: String) = apply { this.customerPhone = phone }
        fun setCustomerAddress(address: String) = apply { this.customerAddress = address }
        fun setLogo(logo: Drawable) = apply { this.logo = logo }
        fun setFooterText(text: String) = apply { this.footerText = text }
        fun addReceipt(receipt: Receipt) = apply { listReceipt.add(receipt) }

        fun build(): ReceiptBitmapGenerator {
            requireNotNull(logo) { "Logo is required" }
            val generator = ReceiptBitmapGenerator(
                context,
                customerName,
                customerPhone,
                customerAddress,
                logo!!,
                footerText
            )
            generator.listReceipt.addAll(listReceipt)
            return generator
        }
    }

    fun appendLine(canvas: Canvas, text: String, paint: Paint) {
        canvas.drawText(text, 10f, yPosition, paint)
        yPosition += lineSpacing
    }

    fun generate(): Bitmap {
        val width = 400
        val height = calculateTotalHeight()

        // Create the bitmap and the canvas
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Draw the logo
        val logoBitmap = Bitmap.createScaledBitmap((logo as BitmapDrawable).bitmap, 80, 80, false)
        canvas.drawBitmap(logoBitmap, (width - logoBitmap.width) / 2f, 10f, null)

        // Append lines dynamically using the appendLine method
        yPosition = logoBitmap.height + 20f
        appendLine(canvas, "Receipt", textPaint)

        // Loop through receipts and append them
        for (receipt in listReceipt) {
            appendLine(canvas, "${receipt.name} x ${receipt.quantity}", detailPaint)
            appendLine(canvas, formatNumberWithCommas(receipt.price), pricePaint)
        }

        // Append footer
        val footerLines = footerText.split("\n")
        for (footerLine in footerLines) {
            appendLine(canvas, footerLine, footerPaint)
        }

        return bitmap
    }

    private fun calculateTotalHeight(): Int {
        val baseHeight = 240 // Adjust base height
        val lines = listReceipt.size + footerText.split("\n").size + 6
        return (lines * lineSpacing + baseHeight).toInt()
    }

    private fun formatNumberWithCommas(number: Int): String {
        return "%,d".format(number)
    }
}