package vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers

interface SizeParser {
    val width:Int
    val textSizeHeader:Float
    val textSizeBody:Float
    val textSizeFooter:Float
}
enum class Size {
    _57x38,
    _80x45
}