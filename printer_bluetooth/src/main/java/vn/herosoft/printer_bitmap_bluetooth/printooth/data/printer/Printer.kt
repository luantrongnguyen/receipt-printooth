package vn.herosoft.printer_bitmap_bluetooth.printooth.data.printer

import vn.herosoft.printer_bitmap_bluetooth.printooth.data.PrintingImagesHelper
import vn.herosoft.printer_bitmap_bluetooth.printooth.data.converter.Converter

abstract class Printer {
    var initPrinterCommand = initInitPrinterCommand()
    var justificationCommand = initJustificationCommand()
    var fontSizeCommand = initFontSizeCommand()
    var emphasizedModeCommand = initEmphasizedModeCommand()
    var underlineModeCommand = initUnderlineModeCommand()
    var characterCodeCommand = initCharacterCodeCommand()
    var feedLineCommand = initFeedLineCommand()
    var lineSpacingCommand = initLineSpacingCommand()
    var printingImagesHelper: PrintingImagesHelper = initPrintingImagesHelper()
    var converter: Converter = useConverter()

    abstract fun initInitPrinterCommand(): ByteArray
    abstract fun initJustificationCommand(): ByteArray
    abstract fun initFontSizeCommand(): ByteArray
    abstract fun initEmphasizedModeCommand(): ByteArray
    abstract fun initUnderlineModeCommand(): ByteArray
    abstract fun initCharacterCodeCommand(): ByteArray
    abstract fun initFeedLineCommand(): ByteArray
    abstract fun initLineSpacingCommand(): ByteArray
    abstract fun initPrintingImagesHelper(): PrintingImagesHelper
    abstract fun useConverter(): Converter
}