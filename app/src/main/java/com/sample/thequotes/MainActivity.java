package com.sample.thequotes;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        ScrollView scrollView = findViewById(R.id.scrollView);
        linearLayout = findViewById(R.id.linearLayout);
        Button addButton = findViewById(R.id.addButton);
        EditText dataEditText = findViewById(R.id.dataEditText);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews(); // Очистка предыдущих данных

                List<String> dataList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String data = snapshot.getValue(String.class);
                    dataList.add(data);
                }

                Collections.reverse(dataList);

                for (String data : dataList) {
                    addDataToScrollView(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newData = dataEditText.getText().toString();

                if (!newData.isEmpty()) {
                    addDataToFirebase(newData);
                    dataEditText.setText("");
                }
            }
        });


    }

    private void addDataToScrollView(String data) {
        TextView textView = new TextView(this);
        textView.setText(data);
        textView.setTextSize(20);
        textView.setTypeface(getResources().getFont(R.font.montserratmedium));
        //textView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 0, 0, 16);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);
    }

    private void addDataToFirebase(String newData) {
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(newData);
    }

}
