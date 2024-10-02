package vn.herosoft.printer_bitmap_bluetooth.printooth.data.printable

import vn.herosoft.printer_bitmap_bluetooth.printooth.data.printer.Printer

interface Printable {
    fun getPrintableByteArray(printer: Printer): List<ByteArray>
}
