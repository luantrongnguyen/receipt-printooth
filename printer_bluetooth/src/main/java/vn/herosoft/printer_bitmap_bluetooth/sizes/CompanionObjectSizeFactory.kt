package vn.herosoft.printer_bitmap_bluetooth.sizes

import vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers.Size
import vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers.Size57x38
import vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers.Size80x45
import vn.herosoft.printer_bitmap_bluetooth.sizes.sizeParsers.SizeParser


class CompanionObjectSizeFactory : SizeParserFactory {
    companion object : SizeParserFactory{
        override fun createFromSize(size: Size): SizeParser =
            when (size) {
                Size._57x38 -> Size57x38()
                Size._80x45 -> Size80x45()
                else -> throw Exception("Are you a monkey?")
            }
    }


    override fun createFromSize(size: Size): SizeParser  =
        CompanionObjectSizeFactory.createFromSize(size)

}