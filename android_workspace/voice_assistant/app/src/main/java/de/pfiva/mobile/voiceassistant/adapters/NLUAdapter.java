package de.pfiva.mobile.voiceassistant.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.pfiva.data.model.Feedback;
import de.pfiva.data.model.NLUData;
import de.pfiva.data.model.snips.SnipsOutput;
import de.pfiva.mobile.voiceassistant.Constants;
import de.pfiva.mobile.voiceassistant.R;
import de.pfiva.mobile.voiceassistant.activities.DetailedActivity;

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
        private TextView userQueryTimestamp;
        private TextView userQueryFeedbackAvailability;

        public NLUViewHolder(View itemView) {
            super(itemView);
            itemView = itemView;

            userQueryTextView = itemView.findViewById(R.id.pfiva_user_query);
            userQueryTimestamp = itemView.findViewById(R.id.pfiva_user_query_timestamp);
            userQueryFeedbackAvailability = itemView.findViewById(R.id.pfiva_user_query_feedback_availability);
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
        final NLUData nluData = this.nluData.get(position);

        SnipsOutput snipsOutput = nluData.getSnipsOutput();
        if(snipsOutput != null) {
            holder.userQueryTextView.setText(snipsOutput.getInput());
            holder.userQueryTimestamp.setText(snipsOutput.getTimestamp());
            Feedback feedback = nluData.getFeedback();
            if(feedback.getUserResponse() != null && !feedback.getUserResponse().isEmpty()) {
                holder.userQueryFeedbackAvailability.setTextColor(Color.GREEN);
                holder.userQueryFeedbackAvailability.setText(R.string.feedback_available);
            } else {
                holder.userQueryFeedbackAvailability.setTextColor(Color.RED);
                holder.userQueryFeedbackAvailability.setText(R.string.feedback_not_available);
            }
        }

        holder.userQueryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra(Constants.NLUDATA_KEY, nluData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nluData.size();
    }
}