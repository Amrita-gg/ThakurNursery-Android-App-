package com.example.thakur_nursery.activities;

import static com.itextpdf.layout.properties.TextAlignment.CENTER;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thakur_nursery.R;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillGenerateActivity extends AppCompatActivity {

    private EditText customerNameEditText;
    private EditText phoneNoEditText;
    private EditText addressEditText;
    private EditText itemDescriptionEditText;
    private EditText itemPriceEditText;
    private LinearLayout itemListLayout;

    private List<Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_generate);

        customerNameEditText = findViewById(R.id.customer_name);
        phoneNoEditText = findViewById(R.id.phone_no);
        addressEditText = findViewById(R.id.address);
        itemDescriptionEditText = findViewById(R.id.item_description);
        itemPriceEditText = findViewById(R.id.item_price);
        Button generatePdfButton = findViewById(R.id.generate_pdf_button);
        Button addItemButton = findViewById(R.id.add_item_button);
        itemListLayout = findViewById(R.id.item_list);

        addItemButton.setOnClickListener(v -> addItem());
        generatePdfButton.setOnClickListener(v -> generatePdf());
    }

    private void addItem() {
        String description = itemDescriptionEditText.getText().toString();
        String priceString = itemPriceEditText.getText().toString();

        if (description.isEmpty() || priceString.isEmpty()) {
            showToast("Please enter both item description and price.");
            return;
        }

        // Create new item
        BigDecimal price;
        try {
            price = new BigDecimal(priceString);
        } catch (NumberFormatException e) {
            showToast("Invalid price format.");
            return;
        }

        items.add(new Item(description, price));

        // Create a TextView to show the added item
        TextView itemTextView = new TextView(this);
        itemTextView.setText(description + " - ₹" + price);
        itemTextView.setPadding(0, 8, 0, 8); // Add some padding
        itemListLayout.addView(itemTextView);

        // Clear input fields
        itemDescriptionEditText.setText("");
        itemPriceEditText.setText("");
    }

    private void generatePdf() {
        // Retrieve user input
        String customerName = customerNameEditText.getText().toString();
        String phoneNo = phoneNoEditText.getText().toString();
        String address = addressEditText.getText().toString();

        // Create directory for PDF
        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Thakur_Receipt/";
        File directory = new File(directoryPath);
        if (!directory.exists() && !directory.mkdirs()) {
            showToast("Failed to create directory.");
            return;
        }

        // Create a new PDF file
        String pdfFileName = "ThakurNursery_Bill_" + System.currentTimeMillis() + ".pdf";
        File pdfFile = new File(directory, pdfFileName);

        try {
            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Load Thakur logo from drawable and convert to ImageData
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.thakur_logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bitmapData = stream.toByteArray();

            ImageData imageData = ImageDataFactory.create(bitmapData);
            Image logo = new Image(imageData);
            logo.setHeight(100);  // Set the height of the logo
            logo.setWidth(100); // Maintain aspect ratio

            // Add a title
            Paragraph title = new Paragraph("THAKUR RECEIPT")
                    .setFontSize(24)
                    .setFontColor(new DeviceRgb(0, 128, 0))
                    .setBold(); // Green color

            // Create a table with two columns for the logo and title
            Table headerTable = new Table(new float[]{1, 3});
            headerTable.setWidth(UnitValue.createPercentValue(100));

            // Add the title to the first cell
            Cell titleCell = new Cell().add(title);
            titleCell.setBorder(Border.NO_BORDER);  // No border for clean look
            titleCell.setVerticalAlignment(VerticalAlignment.MIDDLE);  // Vertically center
            headerTable.addCell(titleCell);

            // Add the logo to the second cell
            Cell logoCell = new Cell().add(logo);
            logoCell.setBorder(Border.NO_BORDER);  // No border for clean look
            logoCell.setHorizontalAlignment(HorizontalAlignment.RIGHT);  // Align to the right
            logoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);  // Vertically center
            headerTable.addCell(logoCell);

            // Add the header table to the document
            document.add(headerTable);

            // Add the date and time of the receipt
            String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
            Paragraph dateTimeParagraph = new Paragraph("Date & Time: " + currentDateTime)
                    .setFontSize(12)
                    .setFontColor(new DeviceRgb(0, 128, 0))
                    .setBold()
                    .setMarginTop(10); // Add some space after the header
            document.add(dateTimeParagraph);

            // Add Thakur Nursery Information
            Table nurseryInfoTable = new Table(new float[]{1, 2});
            nurseryInfoTable.setWidth(UnitValue.createPercentValue(100));
            nurseryInfoTable.addCell(new Cell().add(new Paragraph("Organization Name:").setBold()));
            nurseryInfoTable.addCell(new Cell().add(new Paragraph("Thakur Nursery")));
            nurseryInfoTable.addCell(new Cell().add(new Paragraph("Address:").setBold()));
            nurseryInfoTable.addCell(new Cell().add(new Paragraph("Thakur nursery, opposite gracious society, next to Takshila building 1, Mahakali Caves Road, Andheri East, Mumbai, Maharashtra, India")));
            nurseryInfoTable.addCell(new Cell().add(new Paragraph("Contact:").setBold()));
            nurseryInfoTable.addCell(new Cell().add(new Paragraph("+91 9876543210")));
            nurseryInfoTable.addCell(new Cell().add(new Paragraph("Email:").setBold()));
            nurseryInfoTable.addCell(new Cell().add(new Paragraph("contact@thakurnursery.com")));
            document.add(nurseryInfoTable);

            // Add some spacing
            document.add(new Paragraph(" "));


            // Add some spacing
            document.add(new Paragraph(" "));

            // Add Customer Information
            Table customerInfoTable = new Table(new float[]{1, 2});
            customerInfoTable.setWidth(UnitValue.createPercentValue(100));
            customerInfoTable.addCell(new Cell().add(new Paragraph("Customer Name:").setBold()));
            customerInfoTable.addCell(new Cell().add(new Paragraph(customerName)));
            customerInfoTable.addCell(new Cell().add(new Paragraph("Phone No:").setBold()));
            customerInfoTable.addCell(new Cell().add(new Paragraph(phoneNo)));
            customerInfoTable.addCell(new Cell().add(new Paragraph("Address:").setBold()));
            customerInfoTable.addCell(new Cell().add(new Paragraph(address)));
            document.add(customerInfoTable);

            // Add some spacing
           // document.add(new Paragraph(" "));

            // Add Bill Details Table
            float[] billColumnWidths = {1f, 3f, 1f};
            Table billDetailsTable = new Table(billColumnWidths);
            billDetailsTable.setWidth(UnitValue.createPercentValue(100));
            billDetailsTable.addCell(new Cell().add(new Paragraph("S.No").setBold()));
            billDetailsTable.addCell(new Cell().add(new Paragraph("Item Description").setBold()));
            billDetailsTable.addCell(new Cell().add(new Paragraph("Price").setBold()));

            // Add items to the table
            int serialNo = 1;
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Item item : items) {
                billDetailsTable.addCell(new Cell().add(new Paragraph(String.valueOf(serialNo++))));
                billDetailsTable.addCell(new Cell().add(new Paragraph(item.getDescription())));
                billDetailsTable.addCell(new Cell().add(new Paragraph("₹" + item.getPrice())));
                totalAmount = totalAmount.add(item.getPrice());
            }

            // Add Total row
            billDetailsTable.addCell(new Cell(1, 2).add(new Paragraph("Total").setBold()));
            billDetailsTable.addCell(new Cell().add(new Paragraph("₹" + totalAmount).setFontColor(new DeviceRgb(0, 128, 0))));

            document.add(billDetailsTable);

            // Add some spacing
            document.add(new Paragraph(" "));

            // Add Footer
            Paragraph footer = new Paragraph("Thank you for your purchase!\nVisit us again at Thakur Nursery.")
                    .setFontSize(12)
                    .setFontColor(new DeviceRgb(0, 128, 0))
                    .setTextAlignment(CENTER)
                    .setBold();

            document.add(footer);

            // Closing the document
            document.close();

            // Notify the user
            showToast("PDF saved to " + pdfFile.getAbsolutePath());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            showToast("Error: " + e.getMessage());
        }
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(BillGenerateActivity.this, message, Toast.LENGTH_LONG).show());
    }

    // Item class to hold item details
    private static class Item {
        private String description;
        private BigDecimal price;

        public Item(String description, BigDecimal price) {
            this.description = description;
            this.price = price;
        }

        public String getDescription() {
            return description;
        }

        public BigDecimal getPrice() {
            return price;
        }
    }
}
