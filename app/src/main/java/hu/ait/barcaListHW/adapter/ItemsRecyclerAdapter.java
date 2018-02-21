package hu.ait.barcaListHW.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.ait.barcaListHW.MainActivity;
import hu.ait.barcaListHW.R;
import hu.ait.barcaListHW.data.BarcaItem;
import hu.ait.barcaListHW.touch.BarcaTouchHelperAdapter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class ItemsRecyclerAdapter
        extends RecyclerView.Adapter<ItemsRecyclerAdapter.ViewHolder>
        implements BarcaTouchHelperAdapter {

    private List<BarcaItem> ItemsList;

    private Context context;

    private Realm realmItem;


    public ItemsRecyclerAdapter(Context context, Realm realmItem) {
        this.context = context;
        this.realmItem = realmItem;

        RealmResults<BarcaItem> ItemResult = realmItem.where(BarcaItem.class).findAll().sort("itemName", Sort.ASCENDING);

        ItemsList = new ArrayList<BarcaItem>();

        for (int i = 0; i < ItemResult.size(); i++) {
            ItemsList.add(ItemResult.get(i));

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_row, parent, false);

        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.itemName.setText(ItemsList.get(position).getItemName());
        holder.purchased.setChecked(ItemsList.get(position).isPurchased());

        String price = String.valueOf(ItemsList.get(position).getItemPrice());
        holder.priceLabel.setText(price);

        String category = ItemsList.get(position).getItemCategory();
        holder.categoryLabel.setText(category);
        switch(category) {
            case "T-Shirt" :
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.shirt));
                break;

            case "Short" :
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.short2));
                break;
            case "Shoes" :
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.shoes));
                break;
            case "Socks" :
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.socks));
                break;
            case "Ball" :
                holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ball));
                break;

            default :
                break;
        }
//        Drawable myDrawable = getResources().getDrawable(R.drawable.imageView1);
//holder.imageView.setImageDrawable();

        holder.purchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realmItem.beginTransaction();
                ItemsList.get(holder.getAdapterPosition()).setPurchased(holder.purchased.isChecked());
                realmItem.commitTransaction();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).openEditActivity(holder.getAdapterPosition(),
                        ItemsList.get(holder.getAdapterPosition()).getItemID()
                );
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).deleteItem(holder.getAdapterPosition(),
                        ItemsList.get(holder.getAdapterPosition()).getItemID()
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return ItemsList.size();
    }


    public void addItem(BarcaItem barcaItem) {

        ItemsList.add(0, barcaItem);

        notifyItemInserted(0);
    }

    public void updateItem(String itemID, int positionToEdit) {
        BarcaItem barcaItem = realmItem.where(BarcaItem.class)
                .equalTo("itemID", itemID)
                .findFirst();

        ItemsList.set(positionToEdit, barcaItem);

        notifyItemChanged(positionToEdit);
    }


    @Override
    public void onItemDismiss(int position) {
        realmItem.beginTransaction();
        ItemsList.get(position).deleteFromRealm();
        realmItem.commitTransaction();


        ItemsList.remove(position);

        // refreshes the whole list
        //notifyDataSetChanged();
        // refreshes just the relevant part that has been deleted
        notifyItemRemoved(position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        /*ItemsList.add(toPosition, ItemsList.get(fromPosition));
        ItemsList.remove(fromPosition);*/

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(ItemsList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(ItemsList, i, i - 1);
            }
        }


        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox purchased;
        private TextView itemName;
        private Button deleteButton;
        private ImageView imageView;
        private EditText categoryLabel;
        private EditText priceLabel;

        public ViewHolder(View itemView) {
            super(itemView);

            purchased = (CheckBox) itemView.findViewById(R.id.purchased);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            categoryLabel = (EditText) itemView.findViewById(R.id.category_label);
            priceLabel = (EditText) itemView.findViewById(R.id.price_label);

        }
    }
    public void deleteAll_BarcaItems(){
        realmItem.beginTransaction();
        realmItem.deleteAll();
        realmItem.commitTransaction();


        ItemsList.clear();
        notifyDataSetChanged();
    }

}
