package vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers

open class  Size80x45:SizeParser {
    override val width: Int
        get() = 700
    override val textSizeHeader: Float
        get() = 28f
    override val textSizeBody: Float
        get() = 22f
    override val textSizeFooter: Float
        get() = 24f
}