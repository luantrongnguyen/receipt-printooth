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

You can easily add `ReceiptBitmapGenerator` to your Android project using [JitPack](https://jitpack.io).

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
    implementation 'com.github.luantrongnguyen:receipt-printooth:1.0.0'
}
```

## Usage

### Step 1: Add the `ReceiptBitmapGenerator` class
Include the `ReceiptBitmapGenerator` class in your Android project. The class provides a method `generateReceipt` which takes in parameters like customer details, a list of receipt items, and a footer text.

### Step 2: Generate the Receipt

```kotlin
val receiptBitmapGenerator = ReceiptBitmapGenerator()

val bitmap: Bitmap = receiptBitmapGenerator.generateReceipt(
    context = this,
    customerName = "John Doe",
    customerPhone = "123-456-7890",
    customerAddress = "123 Main St, Cityville",
    logo = yourLogoDrawable, // Company logo
    listReceipt = arrayListOf(
        Receipt(name = "Product 1", quantity = 1, price = "5000"),
        Receipt(name = "Product 2", quantity = 2, price = "3000")
    ),
    footerText = "Thank you for your purchase!"
)

// Use the bitmap (e.g., display or print)


