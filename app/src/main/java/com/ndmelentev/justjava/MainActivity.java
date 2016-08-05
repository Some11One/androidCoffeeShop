package com.ndmelentev.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        // whipped cream
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean addWhippedCream = whippedCreamCheckBox.isChecked();

        // chocolate
        CheckBox chocolateCreamCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean addChocolate = chocolateCreamCheckBox.isChecked();

        // name
        EditText nameEditText = (EditText) findViewById(R.id.name_edit_text);
        String name = nameEditText.getText().toString();

        // calculating price
        int price = calculatePrice(addWhippedCream, addChocolate);

        // getting order
        String orderSummary = createOrderSummary(price, addWhippedCream, addChocolate, name);

        // e-mail order
        composeEmail(new String[]{"ndmelentev@yandex.ru"}, getString(R.string.email_subject), orderSummary);

        // printing order
        // displayMessage(orderSummary);
    }

    /**
     * This method is called when 'plus' button is pressed
     */
    public void increment(View view) {

        // check coffee state
        if (quantity >= 100) {
            Toast.makeText(this, getString(R.string.no_more_coffee), Toast.LENGTH_SHORT).show();
            return;
        }

        // it is ok to increment coffee
        displayQuantity(++quantity);
    }

    /**
     * This method is called when 'minus' button is pressed
     */
    public void decrement(View view) {

        // check coffee state
        if (quantity <= 1) {
            Toast.makeText(this, getString(R.string.no_more_coffee), Toast.LENGTH_SHORT).show();
            return;
        }

        // it is ok to decrement coffee
        displayQuantity(--quantity);
    }

    /**
     * This method displays the given quantity value on the screen.
     *
     * @param number quantity to display
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + quantity);
    }

    //  HELPERS

    /**
     * Calculating the coffee price
     *
     * @param addChocolate    add chocolate cost to coffee price
     * @param addWhippedCream add whipped cream cost to coffee price
     * @return total price
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {

        // prices
        int whippedCreamPrice = addWhippedCream ? 1 : 0;
        int addChocolatePrice = addChocolate ? 2 : 0;
        int coffeePrice = 5;

        // calculating total price
        return quantity * (coffeePrice + whippedCreamPrice + addChocolatePrice);
    }

    /**
     * Order summary
     *
     * @param price of the order
     * @return text summary
     */
    private String createOrderSummary(int price, boolean addWhippedCream, boolean addChocolate, String name) {
        String priceMessage = getString(R.string.order_summary_name, name);
        return getString(R.string.order_summary_name, name)+
                "\n" + getString(R.string.order_summary_whipped_cream) + " " + addWhippedCream +
                "\n" + getString(R.string.order_summary_chocolate) + " " + addChocolate +
                "\n" + getString(R.string.order_summary_quantity) + " " + quantity +
                "\n" + getString(R.string.order_summary_total) + " $" + price + "" +
                "\n" + getString(R.string.thank_you);
    }

    /**
     * Helper methods to send an order using e-mail
     *
     * @param addresses of places to mail to
     * @param subject of e-mail
     */
    public void composeEmail(String[] addresses, String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}