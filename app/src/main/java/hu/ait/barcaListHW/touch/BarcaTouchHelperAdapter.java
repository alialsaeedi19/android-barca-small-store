package hu.ait.barcaListHW.touch;

public interface BarcaTouchHelperAdapter {

    void onItemDismiss(int position);

    void onItemMove(int fromPosition, int toPosition);
}
