package vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers

open class Size57x38:SizeParser {
    override val width: Int
        get() = 400
    override val textSizeHeader: Float
        get() = 24f
    override val textSizeBody: Float
        get() = 20f
    override val textSizeFooter: Float
        get() = 22f
}