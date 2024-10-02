package vn.herosoft.printer_bitmap_bluetooth.printooth

import android.annotation.SuppressLint
import android.content.Context
import vn.herosoft.printer_bitmap_bluetooth.printooth.data.PairedPrinter
import vn.herosoft.printer_bitmap_bluetooth.printooth.data.printer.DefaultPrinter
import vn.herosoft.printer_bitmap_bluetooth.printooth.data.printer.Printer
import vn.herosoft.printer_bitmap_bluetooth.printooth.utilities.Printing
import io.paperdb.Paper

@SuppressLint("StaticFieldLeak")
object Printooth {

    private var context: Context? = null
    private var printing: Printing? = null

    fun init(context: Context) {
        Paper.init(context)
        Printooth.context = context
    }

    fun printer(printer: Printer, pairedPrinter: PairedPrinter): Printing = Printing(
            printer,
            pairedPrinter,
            context ?: error("You must call Printooth.init()")
    )

    fun printer(printer: Printer): Printing = Printing(
            printer,
            getPairedPrinter() ?: error("No paired printer saved, Save one and retry!!"),
            context ?: error("You must call Printooth.init()")
    )

    fun printer(pairedPrinter: PairedPrinter) = Printing(
            DefaultPrinter(),
            pairedPrinter,
            context ?: error("You must call Printooth.init()")
    )

    fun printer(): Printing {
        return if (printing == null) {
            printing = Printing(
                    DefaultPrinter(),
                    getPairedPrinter()
                            ?: error("No paired printer saved, Save one and retry!!"),
                    context ?: error("You must call Printooth.init()")
            )
            printing!!
        } else {
            printing!!
        }
    }

    fun setPrinter(name: String?, address: String) = PairedPrinter.setPairedPrinter(PairedPrinter(name, address))

    fun getPairedPrinter(): PairedPrinter? = PairedPrinter.getPairedPrinter()

    fun hasPairedPrinter(): Boolean = PairedPrinter.getPairedPrinter() != null

    fun removeCurrentPrinter() = PairedPrinter.removePairedPrinter()

}