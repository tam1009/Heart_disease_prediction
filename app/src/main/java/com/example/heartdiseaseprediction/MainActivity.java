package com.example.heartdiseaseprediction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.heartdiseaseprediction.ml.Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;
    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    TextView result;

    // variable for Text view.
    private TextView retrieveHeartRate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button predict;
        predict = findViewById(R.id.predict);
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText age, sex, cp, restbps, chol, fbs, rest_ecg, heart_rate, agina, oldpeak, slope;
                    age = findViewById(R.id.age);
                    sex = findViewById(R.id.sex);
                    cp = findViewById((R.id.cp));
                    restbps = findViewById(R.id.restbps);
                    chol = findViewById(R.id.chol);
                    fbs = findViewById(R.id.fbs);
                    rest_ecg = findViewById(R.id.rest_ecg);
                    heart_rate = findViewById(R.id.heart_rate);
                    agina = findViewById(R.id.agina);
                    oldpeak = findViewById(R.id.oldpeak);
                    slope = findViewById(R.id.slope);

                    Integer Age= Integer.parseInt(age.getText().toString());
                    Integer Sex= Integer.parseInt(sex.getText().toString());
                    Integer Cp= Integer.parseInt(cp.getText().toString());
                    Integer Rest_bps= Integer.parseInt(restbps.getText().toString());
                    Integer Chol = Integer.parseInt(chol.getText().toString());
                    Integer Fbs= Integer.parseInt(fbs.getText().toString());
                    Integer Rest_ecg= Integer.parseInt(rest_ecg.getText().toString());
                    Float floatHeart_rate = Float.parseFloat(heart_rate.getText().toString());
                    Integer Heart_rate= Math.round(floatHeart_rate);
                    Integer Agina = Integer.parseInt(agina.getText().toString());
                    Float Oldpeak= Float.parseFloat(oldpeak.getText().toString());
                    Integer Slope = Integer.parseInt(slope.getText().toString());

                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(11*4);
                    byteBuffer.order(ByteOrder.nativeOrder()); // order in little endian format
                    byteBuffer.putInt(Age);
                    byteBuffer.putInt(Sex);
                    byteBuffer.putInt(Cp);
                    byteBuffer.putInt(Rest_bps);
                    byteBuffer.putInt(Chol);
                    byteBuffer.putInt(Fbs);
                    byteBuffer.putInt(Rest_ecg);
                    byteBuffer.putInt(Heart_rate);
                    byteBuffer.putInt(Agina);
                    byteBuffer.putFloat(Oldpeak);
                    byteBuffer.putInt(Slope);
                    byteBuffer.rewind();
                    // Creates inputs for reference.
                    Model model = Model.newInstance(getApplicationContext());

                    // Creates inputs for reference.
                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 11}, DataType.FLOAT32);
                    inputFeature0.loadBuffer(byteBuffer);

                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                    TextView tv= findViewById(R.id.result);
                    float[] data1=outputFeature0.getFloatArray();
                    if (data1[0] >= data1[1])
                    {
                        tv.setText("Dangerous");
                    }
                    else {tv.setText("Normal");}
                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    // TODO Handle the exception
                }

                }
            }
        );
        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get
        // reference for our database.
        databaseReference = firebaseDatabase.getReference("data");

        // initializing our object class variable.
        retrieveHeartRate = findViewById(R.id.heart_rate);

        // calling method
        // for getting data.
        getdata();

    }
    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        databaseReference.child("XE5i2ck8y5PvyCRphIYQqruWewD2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                String value = String.valueOf(snapshot.child("beat").getValue());

                // after getting the value we are setting
                // our value to our text view in below line.
                retrieveHeartRate.setText(value);
                //calPrediction();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(MainActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
//    private void calPrediction(Integer Age, Integer Sex, Integer Cp, Integer Rest_bps, Integer Chol, Integer Fbs,
//                               Integer Rest_ecg, Integer Heart_rate, Integer Agina, Float Oldpeak, Integer Slope)
//    {
//        try {
//
//            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(11*4);
//            byteBuffer.order(ByteOrder.nativeOrder()); // order in little endian format
//            byteBuffer.putInt(Age);
//            byteBuffer.putInt(Sex);
//            byteBuffer.putInt(Cp);
//            byteBuffer.putInt(Rest_bps);
//            byteBuffer.putInt(Chol);
//            byteBuffer.putInt(Fbs);
//            byteBuffer.putInt(Rest_ecg);
//            byteBuffer.putInt(Heart_rate);
//            byteBuffer.putInt(Agina);
//            byteBuffer.putFloat(Oldpeak);
//            byteBuffer.putInt(Slope);
//            byteBuffer.rewind();
//            // Creates inputs for reference.
//            Model model = Model.newInstance(getApplicationContext());
//
//            // Creates inputs for reference.
//            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 11}, DataType.FLOAT32);
//            inputFeature0.loadBuffer(byteBuffer);
//
//            // Runs model inference and gets result.
//            Model.Outputs outputs = model.process(inputFeature0);
//            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//            TextView tv= findViewById(R.id.result);
//            float[] data1=outputFeature0.getFloatArray();
//            if (data1[0] >= data1[1])
//            {
//                tv.setText("Dangerous");
//            }
//            else {tv.setText("Normal");}
//            // Releases model resources if no longer used.
//            model.close();
//        } catch (IOException e) {
//            // TODO Handle the exception
//        }
//    }
}