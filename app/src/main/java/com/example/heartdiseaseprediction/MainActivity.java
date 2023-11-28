package com.example.heartdiseaseprediction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heartdiseaseprediction.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

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
                    Integer Heart_rate= Integer.parseInt(heart_rate.getText().toString());
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
                    scaleTensorBuffer(inputFeature0, mean, variance);
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
                } catch (IOException e) {
                    // TODO Handle the exception
                }

                }
            }
        );
    }
}