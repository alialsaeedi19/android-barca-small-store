package hu.ait.barcaListHW;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.UUID;

import hu.ait.barcaListHW.data.BarcaItem;
import io.realm.Realm;

public class AddBarcaItem extends AppCompatActivity {

    public static final String KEY_TODO = "KEY_TODO";
    private EditText itemName;
    private Spinner itemCategory;
    private EditText itemPrice;
    private EditText itemDescription;

    private CheckBox purchased;
    private BarcaItem barcaItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_barca_item);

        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.my_animation);

        View myView = findViewById(R.id.myView);

        myView.startAnimation(myAnim);


        //toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        itemName = (EditText) findViewById(R.id.item_name);
        itemCategory = (Spinner) findViewById(R.id.item_category);
        itemPrice = (EditText) findViewById(R.id.item_price);
        itemDescription = (EditText) findViewById(R.id.item_description);

        // making the keyboard soft
        softKeyboard(itemDescription);
        softKeyboard(itemName);
        softKeyboard(itemPrice);

        //category list adjusting
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_item_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategory.setAdapter(adapter);

        purchased = (CheckBox) findViewById(R.id.purchased);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });

//        if (barcaItem != null) {
//            itemName.setText(barcaItem.getItemName());
//            purchased.setChecked(barcaItem.isPurchased());
//        }
    }

    public Realm getRealm() {
        return ((MainApplication) getApplication()).getRealmTodo();
    }

    private void saveItem() {
        if ("".equals(itemName.getText().toString())) {
            itemName.setError("can not be empty");

        } else if ("".equals(itemPrice.getText().toString())) {
            itemPrice.setError("can not be empty");

        } else if ("".equals(itemDescription.getText().toString())) {
            itemDescription.setError("can not be empty");
        } else {
            double price = 0;
            try {
                price = Double.parseDouble(itemPrice.getText().toString());

            } catch (NumberFormatException e) {
                itemPrice.setError("it has to be numbers not letters!");
                return;
            }

            getRealm().beginTransaction();
            BarcaItem barcaItem = getRealm().createObject(BarcaItem.class, UUID.randomUUID().toString());
            barcaItem.setItemName(itemName.getText().toString());
            barcaItem.setItemPrice(price);
            barcaItem.setItemCategory(itemCategory.getSelectedItem().toString());
            barcaItem.setItemDescription(itemDescription.getText().toString());
            barcaItem.setPurchased(purchased.isChecked());
            getRealm().commitTransaction();

            Intent intentResult = new Intent(AddBarcaItem.this, MainActivity.class);
            intentResult.putExtra("barcaItem", barcaItem);
            startActivity(intentResult);
            finish();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void softKeyboard(EditText editText) {
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.requestFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }
}
