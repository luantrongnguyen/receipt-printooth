package vn.herosoft.printer_bitmap_bluetooth.printooth.ui

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import vn.herosoft.printer_bitmap_bluetooth.printooth.Printooth
import vn.herosoft.printer_bitmap_bluetooth.printooth.data.DiscoveryCallback
import vn.herosoft.printer_bitmap_bluetooth.R
import vn.herosoft.printer_bitmap_bluetooth.databinding.ActivityScanningBinding
import vn.herosoft.printer_bitmap_bluetooth.printooth.utilities.Bluetooth

class ScanningActivity : AppCompatActivity() {
    private lateinit var bluetooth: Bluetooth
    private var devices = ArrayList<BluetoothDevice>()
    private lateinit var adapter: BluetoothDevicesAdapter
    private lateinit var binding: ActivityScanningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = BluetoothDevicesAdapter(this)
        bluetooth = Bluetooth(this)
        setup()
    }

    private fun setup() {
        initViews()
        initListeners()
        initDeviceCallback()
    }

    private fun initDeviceCallback() {
        bluetooth.setDiscoveryCallback(object : DiscoveryCallback {
            override fun onDiscoveryStarted() {
                binding.refreshLayout.isRefreshing = true
                devices.clear()
                devices.addAll(bluetooth.pairedDevices)
                adapter.notifyDataSetChanged()
            }

            override fun onDiscoveryFinished() {
                binding.refreshLayout.isRefreshing = false
            }

            override fun onDeviceFound(device: BluetoothDevice) {
                if (!devices.contains(device)) {
                    devices.add(device)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onDevicePaired(device: BluetoothDevice) {
                Printooth.setPrinter(device.name, device.address)
                Toast.makeText(this@ScanningActivity, "Device Paired", Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()
                setResult(Activity.RESULT_OK)
                this@ScanningActivity.finish()
            }

            override fun onDeviceUnpaired(device: BluetoothDevice) {
                Toast.makeText(this@ScanningActivity, "Device unpaired", Toast.LENGTH_SHORT).show()
                val pairedPrinter = Printooth.getPairedPrinter()
                if (pairedPrinter != null && pairedPrinter.address == device.address)
                    Printooth.removeCurrentPrinter()
                devices.remove(device)
                adapter.notifyDataSetChanged()
                bluetooth.startScanning()
            }

            override fun onError(message: String) {
                Toast.makeText(this@ScanningActivity, "Error while pairing", Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initListeners() {
        binding.refreshLayout.setOnRefreshListener { bluetooth.startScanning() }
        binding.printers.setOnItemClickListener { _, _, i, _ ->
            val device = devices[i]
            if (device.bondState == BluetoothDevice.BOND_BONDED) {
                Printooth.setPrinter(device.name, device.address)
                setResult(Activity.RESULT_OK)
                this@ScanningActivity.finish()
            } else if (device.bondState == BluetoothDevice.BOND_NONE)
                bluetooth.pair(devices[i])
            adapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding.printers.adapter = adapter
    }

    override fun onStart() {
        super.onStart()

        runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            bluetooth.onStart()
            if (!bluetooth.isEnabled)
                bluetooth.enable()
            Handler().postDelayed({
                bluetooth.startScanning()
            }, 1000)
        }

    }

    override fun onStop() {
        super.onStop()
        bluetooth.onStop()
    }

    companion object {
        const val SCANNING_FOR_PRINTER = 115
    }

    inner class BluetoothDevicesAdapter(context: Context) : ArrayAdapter<BluetoothDevice>(context, android.R.layout.simple_list_item_1) {
        override fun getCount(): Int {
            return devices.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return LayoutInflater.from(context)
                    .inflate(R.layout.bluetooth_device_row, parent, false).apply {
                        findViewById<TextView>(R.id.name).text = if (devices[position].name.isNullOrEmpty()) devices[position].address else devices[position].name
                        findViewById<TextView>(R.id.pairStatus).visibility = if (devices[position].bondState != BluetoothDevice.BOND_NONE) View.VISIBLE else View.INVISIBLE
                        findViewById<TextView>(R.id.pairStatus).text = when (devices[position].bondState) {
                            BluetoothDevice.BOND_BONDED -> "Paired"
                            BluetoothDevice.BOND_BONDING -> "Pairing.."
                            else -> ""
                        }
                        findViewById<ImageView>(R.id.pairedPrinter).visibility = if (Printooth.getPairedPrinter()?.address == devices[position].address) View.VISIBLE else View.GONE
                    }
        }
    }
}
