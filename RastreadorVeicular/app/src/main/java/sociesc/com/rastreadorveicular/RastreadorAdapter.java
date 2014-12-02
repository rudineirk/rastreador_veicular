package sociesc.com.rastreadorveicular;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adrian on 11/25/14.
 */
public class RastreadorAdapter extends RecyclerView.Adapter<RastreadorAdapter.ViewHolder> {

    private List<DataTracker> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewNome;
        public TextView mTextViewSerial;//serial

        public ViewHolder(View v) {
            super(v);
            mTextViewNome = (TextView) v.findViewById(R.id.nameRastreador);
            mTextViewSerial = (TextView) v.findViewById(R.id.serialRastreador);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RastreadorAdapter(List<DataTracker> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rastreador_list, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextViewNome.setText(mDataset.get(position).name);
        holder.mTextViewSerial.setText(mDataset.get(position).serial);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
