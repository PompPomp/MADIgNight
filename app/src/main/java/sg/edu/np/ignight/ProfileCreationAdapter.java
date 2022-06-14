package sg.edu.np.ignight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileCreationAdapter extends RecyclerView.Adapter<ProfileCreationViewHolder> {

    public static ArrayList<String> data;
    Context c;
    public ProfileCreationAdapter(Context c, ArrayList<String> data){
        this.c =c;
        this.data = data;
    }


    @NonNull
    @Override
    public ProfileCreationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.interest_layout, parent, false );

        return new ProfileCreationViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileCreationViewHolder holder, int position) {
        String context = data.get(position);
        holder.interest.setText(context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}