package vn.herosoft.printer_bitmap_bluetooth.printooth.data.converter

import vn.herosoft.printer_bitmap_bluetooth.printooth.data.converter.Converter

/**
 * Default converter
 */
class DefaultConverter : Converter() {
    override fun convert(input: Char): Byte {
        if (input == '€') {
            return (0x80).toByte()
        }
        return input.toByte()
    }
}