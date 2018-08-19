package de.pfiva.mobile.voiceassistant.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.snips.SnipsOutput;
import de.pfiva.mobile.voiceassistant.R;

public class NLUAdapter extends RecyclerView.Adapter<NLUAdapter.NLUViewHolder> {

    private List<NLUData> nluData;
    private Context context;

    public NLUAdapter(Context context, List<NLUData> nluData) {
        this.context = context;
        this.nluData = nluData;
    }

    class NLUViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private TextView userQueryTextView;

        public NLUViewHolder(View itemView) {
            super(itemView);
            itemView = itemView;

            userQueryTextView = itemView.findViewById(R.id.pfiva_user_query);
        }
    }

    @Override
    public NLUViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.nludata_row, parent, false);
        return new NLUViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NLUViewHolder holder, int position) {
        SnipsOutput snipsOutput = nluData.get(position).getSnipsOutput();
        if(snipsOutput != null) {
            holder.userQueryTextView.setText(snipsOutput.getInput());
        }
    }

    @Override
    public int getItemCount() {
        return nluData.size();
    }
}
