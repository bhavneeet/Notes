package othello.com.example.bhavneetsingh.Triads.User;

import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class NamesFilter<T> extends Filter {

    ArrayAdapter<T>adapter;
    ArrayList<T>originalList,filteredList;

    public NamesFilter(ArrayAdapter<T> adapter, ArrayList<T> originalList, ArrayList<T> filteredList) {
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = filteredList;
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
    filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            // Your filtering logic goes in here
            for (final T item : originalList) {
                if (item.toString().toLowerCase().contains(filterPattern)) {
                    filteredList.add(item);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.clear();
        adapter.addAll((List) results.values);
        adapter.notifyDataSetChanged();
    }
}
