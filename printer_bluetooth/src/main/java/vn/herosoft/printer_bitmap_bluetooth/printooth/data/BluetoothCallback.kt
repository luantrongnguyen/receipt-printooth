package vn.herosoft.printer_bitmap_bluetooth.printooth.data

/**
 * Created by Omar on 20/12/2017.
 */

interface BluetoothCallback {
    fun onBluetoothTurningOn()
    fun onBluetoothOn()
    fun onBluetoothTurningOff()
    fun onBluetoothOff()
    fun onUserDeniedActivation()
}
