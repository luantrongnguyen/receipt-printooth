package vn.herosoft.printer_bitmap_bluetooth.sizes

import vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers.Size
import vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers.TemplateParser

interface SizeParserFactory {
    fun createFromSize(size: Size): TemplateParser
}
