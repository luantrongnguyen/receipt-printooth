# receipt-printooth
A library to do a bitmap receipt easier!
# ReceiptBitmapGenerator

`ReceiptBitmapGenerator` is a Kotlin class designed to dynamically generate a receipt as a bitmap image. This is useful for printing receipts from within an Android application.

## Features
- Generate a receipt bitmap with customer details, receipt items, and a footer message.
- Automatically calculates and formats the total price.
- Customizable with company logo and text styling.
- Supports line-by-line printing for receipt items.

## Installation

### Step 1: Add JitPack repository

Add the JitPack repository to your project-level `build.gradle` file:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

## Step 2: Add the dependency in your app-level build.gradle file, add the following dependency:

```build.gradle
dependencies {
    implementation 'com.github.luantrongnguyen:receipt-printooth:1.1.0'
}
```

## Usage

### Step 1: Add the `ReceiptBitmapGenerator` class
Include the `ReceiptBitmapGenerator` class in your Android project. The class provides a method `generateReceipt` which takes in parameters like customer details, a list of receipt items, and a footer text.

### Step 2: Generate the Receipt


```kotlin
// Step 1: Create an instance of the ReceiptBitmapGenerator using the Builder pattern
val receiptBitmapGenerator = ReceiptBitmapGenerator.Builder()
    .setDiameter(ReceiptBitmapGenerator._38MM) // Set the printer diameter
    .setCustomerDetails("Trong Luan", "123456789", "123 GoVap Vietnam")
    .setLogo(context.getDrawable(R.drawable.logo)) // Set your logo drawable
    .setFooterText("Thank you for your purchase!")
    .addReceiptItem(Receipt("Product 1", 2, 15000))
    .addReceiptItem(Receipt("Product 2", 1, 5000))
    .build()
// Step 2: Print the receipt
// Make sure you have connected to your Bluetooth Printer device
// The printReceipt method will check for necessary permissions and use Printooth to print the generated bitmap.
receiptBitmapGenerator.printReceipt(context)
```
## Contributing
Contributions are welcome! Please create a new issue or pull request for any changes or improvements.

