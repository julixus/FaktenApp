package de.thnuernberg.bme.faktenapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FactAdapter extends RecyclerView.Adapter<FactAdapter.FactViewHolder> {

    private List<Fact> factList;
    private Context context;
    private FactsTable dbHelper;

    public FactAdapter(Context context, List<Fact> facts, FactsTable dbHelper) {
        this.context = context;
        this.factList = facts;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public FactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fact, parent, false);
        return new FactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FactViewHolder holder, int position) {
        Fact fact = factList.get(position);
        holder.title.setText(fact.title);
        holder.text.setText(fact.text);

        holder.deleteButton.setOnClickListener(v -> {
            dbHelper.deleteFact(fact.id);
            factList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, factList.size());
        });

        try {
            holder.image.setImageURI(Uri.parse(fact.imagePath));
        } catch (Exception e) {
            holder.image.setImageResource(R.drawable.hai); // Fallback-Bild
        }

    }

    @Override
    public int getItemCount() {
        return factList.size();
    }

    public static class FactViewHolder extends RecyclerView.ViewHolder {
        TextView title, text;
        ImageView image;
        Button deleteButton;

        public FactViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.fact_title);
            text = itemView.findViewById(R.id.fact_text);
            image = itemView.findViewById(R.id.fact_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
