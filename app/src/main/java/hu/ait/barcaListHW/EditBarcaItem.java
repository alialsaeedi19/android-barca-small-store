package hu.ait.barcaListHW;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import hu.ait.barcaListHW.data.BarcaItem;
import io.realm.Realm;

public class EditBarcaItem extends AppCompatActivity {
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
        setContentView(R.layout.activity_edit_todo);

        //toolbar

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().hasExtra(MainActivity.KEY_TODO_ID)) {
            String itemID = getIntent().getStringExtra(MainActivity.KEY_TODO_ID);
            barcaItem = getRealm().where(BarcaItem.class)
                    .equalTo("itemID", itemID)
                    .findFirst();
        }

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
                saveTodo();
            }
        });

        if (barcaItem != null) {
            itemName.setText(barcaItem.getItemName());
            itemPrice.setText(String.valueOf(barcaItem.getItemPrice()));
            ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                    R.array.category_item_array, android.R.layout.simple_spinner_item);

            //checking which category it is
            int index = 0;
            for (int i =0; i<categoryAdapter.getCount(); i++){
                String itemCat = barcaItem.getItemCategory();
                String listItem = (String)categoryAdapter.getItem(i);
                if(itemCat.equals(listItem)){
                    index = i;
                    i=categoryAdapter.getCount();
                }
            }

            itemCategory.setSelection(index);
            itemDescription.setText(barcaItem.getItemDescription());
            purchased.setChecked(barcaItem.isPurchased());
        }
    }

    public Realm getRealm() {
        return ((MainApplication) getApplication()).getRealmTodo();
    }

    private void saveTodo() {
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
            barcaItem.setItemName(itemName.getText().toString());
            barcaItem.setItemCategory(itemCategory.getSelectedItem().toString());
            barcaItem.setItemPrice(price);
            barcaItem.setItemDescription(itemDescription.getText().toString());
            barcaItem.setPurchased(purchased.isChecked());
            getRealm().commitTransaction();

            Intent intentResult = new Intent();
            intentResult.putExtra(KEY_TODO, barcaItem.getItemID());
            setResult(RESULT_OK, intentResult);
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
