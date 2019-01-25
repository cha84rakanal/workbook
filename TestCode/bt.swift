import IOBluetooth
// See https://developer.apple.com/reference/iobluetooth/iobluetoothdevice
// for API details.
class BluetoothDevices {
  func pairedDevices() {
    print("Bluetooth devices:")
    guard let devices = IOBluetoothDevice.pairedDevices() else {
      print("No devices")
      return
    }
    for item in devices {
      if let device = item as? IOBluetoothDevice {
        print("Name: \(device.name)")
        print("Paired?: \(device.isPaired())")
        print("Connected?: \(device.isConnected())")
      }
    }
  }
}

var bt = BluetoothDevices()
bt.pairedDevices()