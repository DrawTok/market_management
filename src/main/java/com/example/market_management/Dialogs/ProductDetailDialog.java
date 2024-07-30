package com.example.market_management.Dialogs;

import com.example.market_management.Models.Product;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class ProductDetailDialog {
    @FXML
    private TextField idField;
    @FXML
    private TextField barcodeField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField unitPriceField;
    @FXML
    private TextField unitField;
    @FXML
    private TextField inStockField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField vendorField;
    @FXML
    private TextField createdAtField;
    @FXML
    private TextField statusField;
    @FXML
    private ImageView barcodeImageView;

    public void setProduct(Product product) {
        idField.setText(String.valueOf(product.getId()));
        barcodeField.setText(product.getBarcode());
        nameField.setText(product.getName());
        categoryField.setText(product.getCategory().getName());
        unitPriceField.setText(String.valueOf(product.getUnitPrice()));
        unitField.setText(product.getUnit());
        inStockField.setText(String.valueOf(product.getInStock()));
        descriptionField.setText(product.getDescription());
        vendorField.setText(product.getVendor().getName());
        createdAtField.setText(String.valueOf(product.getCreateAt()));
        statusField.setText(product.isStatus() ? "Active" : "Inactive");

        try {
            BufferedImage barcodeImage = createBarcode(product.getBarcode());
            barcodeImageView.setImage(SwingFXUtils.toFXImage(barcodeImage, null));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage createBarcode(String barcodeText) throws WriterException {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, 300, 150);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    @FXML
    private void handleClose() {
        idField.getScene().getWindow().hide();
    }
}
