package sg.edu.np.ignight.Map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import sg.edu.np.ignight.R;

public class UserPreferredFragment extends Fragment {
    private ArrayList<LocationObject> locList;
    private ArrayList<String> userPrefList;

    public UserPreferredFragment(ArrayList<String> userPrefList, ArrayList<LocationObject> locList){
        this.userPrefList = userPrefList;
        this.locList = locList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://madignight-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference("location");
        locList = new ArrayList<>();

        Log.d("prefsize2", String.valueOf(userPrefList.size()));
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_preferred, container, false);

        // Filters off locations based on user's dating location preferences and stores in location list to be displayed
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot categoryNode: snapshot.getChildren()){
                    for (String loc: userPrefList){
                        Log.d("userPref", loc);
                        Log.d("prefsize3", String.valueOf(userPrefList.size()));
                        if (categoryNode.getKey().equals(loc) && categoryNode.child("1").exists()){
                            for (DataSnapshot locNode : categoryNode.getChildren()){
                                String Name = locNode.child("Name").getValue().toString();
                                String Desc = locNode.child("Description").getValue().toString();
                                String Addr = locNode.child("Address").getValue().toString();
                                String imgUri = locNode.child("imgUri").getValue().toString();

                                locList.add(new LocationObject(Name, Desc, loc, Addr, imgUri));

                            }
                        }
                    }

                }

                RecyclerView recyclerView = view.findViewById(R.id.userPrefRecyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                recyclerView.setAdapter(new MapAdapter(locList, view.getContext()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Log.d("prefsize2", String.valueOf(userPrefList.size()));
//        ArrayList<LocationObject> filteredLocs = new ArrayList<>();
//
//        Log.d("locsize", String.valueOf(locList.size()));
//        for (LocationObject locObj : locList){
//            Log.d("catname", locObj.getCategory());
//            for (String loc : userPrefList){
//                if (locObj.getCategory().equals(loc)){
//                    filteredLocs.add(locObj);
//                }
//            }
//        }
//        RecyclerView recyclerView = view.findViewById(R.id.userPrefRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
//        recyclerView.setAdapter(new MapAdapter(filteredLocs, view.getContext()));
       return view;
    }



}