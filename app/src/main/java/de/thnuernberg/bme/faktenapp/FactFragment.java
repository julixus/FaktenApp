package de.thnuernberg.bme.faktenapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FactFragment extends Fragment {

    private String title;
    private String text;
    private int imageResId;
    private int profileResId;

    Button btnLike;

    Button btnDislike;

    public FactFragment() { }

    // Optional: Factory-Methode mit Argumenten
    public static FactFragment newInstance(String title, String text, int imageResId) {
        FactFragment fragment = new FactFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("text", text);
        args.putInt("imageResId", imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFactInteractionListener {
        void onFactLiked();
        void onFactDisliked();
    }

    private OnFactInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            text = getArguments().getString("text");
            imageResId = getArguments().getInt("imageResId");
            profileResId = getArguments().getInt("profileResId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fact, container, false);

        TextView titleView = view.findViewById(R.id.fact_title);
        TextView textView = view.findViewById(R.id.fact_text);
        ImageView imageView = view.findViewById(R.id.fact_image);
        btnLike = view.findViewById(R.id.btn_like);
        btnDislike = view.findViewById(R.id.btn_dislike);

        titleView.setText(title);
        textView.setText(text);
        imageView.setImageResource(imageResId);

        btnLike.setOnClickListener(v -> {
            if (listener != null) listener.onFactLiked();
            //Toast.makeText(getContext(), "Gefällt mir!", Toast.LENGTH_SHORT).show();
        });

        btnDislike.setOnClickListener(v -> {
            if (listener != null) listener.onFactDisliked();
            //Toast.makeText(getContext(), "Gefällt mir nicht!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFactInteractionListener) {
            listener = (OnFactInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFactInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


}
