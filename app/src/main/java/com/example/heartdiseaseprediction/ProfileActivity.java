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

public class ProfileActivity extends AppCompatActivity {
    // creating a variable for
    // our Firebase Database.
    FirebaseDatabase firebaseDatabase;
    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    TextView result;

    // variable for Text view.
    private TextView retrieveHeartRate;

    public void scaleTensorBuffer(TensorBuffer tensorBuffer, float[] mean, float[] variance) {
        float[] floatValues = tensorBuffer.getFloatArray();
        for (int i = 0; i < floatValues.length; i++) {
            float stdDev = (float) Math.sqrt(variance[i]);
            floatValues[i] = (floatValues[i] - mean[i]) / stdDev;
        }
        tensorBuffer.loadArray(floatValues);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button predict;
        predict = findViewById(R.id.predict);

        TextView result;
        result = findViewById(R.id.result);

        float[] mean = {-6.8565e-08F, -5.0756e-08F,-5.4763e-08F,
                1.6562e-07F,-1.5583e-08F, -1.2466e-08F, -6.6784e-08F,
                -5.3205e-08F, -2.7604e-08F, -4.2742e-08F, 9.7059e-08F};
        float[] variance = {1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F, 1.0000F,
                1.0000F, 1.0000F};

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    EditText age, sex, cp, restbps, chol, fbs, rest_ecg, heart_rate, agina, oldpeak, slope;
//                    age = findViewById(R.id.age);
//                    sex = findViewById(R.id.sex);
//                    cp = findViewById((R.id.cp));
//                    restbps = findViewById(R.id.restbps);
//                    chol = findViewById(R.id.chol);
//                    fbs = findViewById(R.id.fbs);
//                    rest_ecg = findViewById(R.id.rest_ecg);
//                    heart_rate = findViewById(R.id.heart_rate);
//                    agina = findViewById(R.id.agina);
//                    oldpeak = findViewById(R.id.oldpeak);
//                    slope = findViewById(R.id.slope);
//
//                    Integer Age= Integer.parseInt(age.getText().toString());
//                    Integer Sex= Integer.parseInt(sex.getText().toString());
//                    Integer Cp= Integer.parseInt(cp.getText().toString());
//                    Integer Rest_bps= Integer.parseInt(restbps.getText().toString());
//                    Integer Chol = Integer.parseInt(chol.getText().toString());
//                    Integer Fbs= Integer.parseInt(fbs.getText().toString());
//                    Integer Rest_ecg= Integer.parseInt(rest_ecg.getText().toString());
//                    Float floatHeart_rate = Float.parseFloat(heart_rate.getText().toString());
//                    Integer Heart_rate= Math.round(floatHeart_rate);
//                    Integer Agina = Integer.parseInt(agina.getText().toString());
//                    Float Oldpeak= Float.parseFloat(oldpeak.getText().toString());
//                    Integer Slope = Integer.parseInt(slope.getText().toString());
//
//                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(11*4);
//                    byteBuffer.order(ByteOrder.nativeOrder()); // order in little endian format
//                    byteBuffer.putInt(Age);
//                    byteBuffer.putInt(Sex);
//                    byteBuffer.putInt(Cp);
//                    byteBuffer.putInt(Rest_bps);
//                    byteBuffer.putInt(Chol);
//                    byteBuffer.putInt(Fbs);
//                    byteBuffer.putInt(Rest_ecg);
//                    byteBuffer.putInt(Heart_rate);
//                    byteBuffer.putInt(Agina);
//                    byteBuffer.putFloat(Oldpeak);
//                    byteBuffer.putInt(Slope);
//                    byteBuffer.rewind();
//
//                    // Creates inputs for reference.
//                    Model model = Model.newInstance(getApplicationContext());
//
//                    // Creates inputs for reference.
//                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 11}, DataType.FLOAT32);
//                    inputFeature0.loadBuffer(byteBuffer);
//                    scaleTensorBuffer(inputFeature0, mean, variance);
//                    // Runs model inference and gets result.
//                    Model.Outputs outputs = model.process(inputFeature0);
//                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                    TextView tv= findViewById(R.id.result);
//                    float[] data1=outputFeature0.getFloatArray();
//                    if (data1[0] >= data1[1])
//                    {
//                        tv.setText("Normal");
//                    }
//                    else {tv.setText("Dangerous");}
//                    // Releases model resources if no longer used.
//                    model.close();
//                } catch (IOException e) {
//                    // TODO Handle the exception
//                }

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
        //getdata();

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
                if (value == "")
                    value = "100";
                // after getting the value we are setting
                // our value to our text view in below line.
                retrieveHeartRate.setText(value);
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

//                Integer Age = Integer.parseInt(age.getText().toString());
//                Integer Sex = Integer.parseInt(sex.getText().toString());
//                Integer Cp = Integer.parseInt(cp.getText().toString());
//                Integer Rest_bps = Integer.parseInt(restbps.getText().toString());
//                Integer Chol = Integer.parseInt(chol.getText().toString());
//                Integer Fbs = Integer.parseInt(fbs.getText().toString());
//                Integer Rest_ecg = Integer.parseInt(rest_ecg.getText().toString());
//                Float floatHeart_rate = Float.parseFloat(heart_rate.getText().toString());
//                Integer Heart_rate = Math.round(floatHeart_rate);
//                Integer Agina = Integer.parseInt(agina.getText().toString());
//                Float Oldpeak = Float.parseFloat(oldpeak.getText().toString());
//                Integer Slope = Integer.parseInt(slope.getText().toString());
                try {
                    Predict(49, 0, 3, 160, 180, 0, 0, 156, 0, 1, 2);
                }
                catch(IOException e)
                {
                    }
                }
                //Predict(Age, Sex, Cp, Rest_bps, Chol, Fbs, Rest_ecg, Heart_rate, Agina, Oldpeak, Slope);



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(ProfileActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Predict(int Age, int Sex, int Cp, int Restbps, int Chol,
                         int Fbs, int Rest_ecg, int Heart_rate, int Agina, float Oldpeak, int Slope) throws IOException {
        Model model = Model.newInstance(getApplicationContext());
        float[] floatArr = {(float)Age, (float)Sex, (float)Cp, (float)Restbps, (float)Chol, (float)Fbs
            , (float)Rest_ecg, (float)Heart_rate, (float)Agina, (float)Oldpeak, (float)Slope};
        // Creates inputs for reference.
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 11}, DataType.FLOAT32);
        inputFeature0.loadArray(floatArr);
//        inputFeature0.loadBuffer(byteBuffer);
//        scaleTensorBuffer(inputFeature0, mean, variance);
        // Runs model inference and gets result.
        Model.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
        TextView tv= findViewById(R.id.result);
        float[] data1=outputFeature0.getFloatArray();
        if (data1[0] >= data1[1])
        {
            tv.setText("Normal");
        }
        else {tv.setText("Dangerous");}
        // Releases model resources if no longer used.
        model.close();
    }
}