package com.example.myapp;


/** import static androidx.constraintlayout.motion.utils.Oscillator.TAG;*/

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapp.ml.Model;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;


/** @noinspection deprecation*/

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = CameraActivity.class.getSimpleName();

    Button selectBtn, predictBtn, captureBtn;
    ImageView imageView;
    Bitmap bitmap;

    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_and_gallery);

        bottomNavigationView = findViewById(R.id.bottom_navigation_pantry);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onOptionsItemSelected);

        selectBtn = findViewById(R.id.selectBtn);
        predictBtn = findViewById(R.id.predictBtn);
        captureBtn = findViewById(R.id.captureBtn);
        imageView = findViewById(R.id.imageView);

        //permission
        getPermission();

        String[] label = new String[1001];
        int cnt = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("label.txt")));
            String line = bufferedReader.readLine();
            while (line != null) {
                label[cnt] = line;
                cnt++;
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 12);
            }
        });

        predictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bitmap == null) {
                    Toast.makeText(CameraActivity.this, "Please select or capture an image first.", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    Model model = Model.newInstance(CameraActivity.this);

                    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 32, 32, 3}, DataType.FLOAT32);

                    Bitmap input=Bitmap.createScaledBitmap(bitmap,32,32,true);
                    TensorImage image=new TensorImage(DataType.FLOAT32);
                    image.load(input);
                    ByteBuffer byteBuffer=image.getBuffer();
                    inputFeature0.loadBuffer(byteBuffer);


                    // Runs model inference and gets result.
                    Model.Outputs outputs = model.process(inputFeature0);
                    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                    String resultText = (label[getMax(outputFeature0.getFloatArray())] + "");
                    Intent intent = new Intent(CameraActivity.this, PantryActivity.class);
                    intent.putExtra("prediction", resultText);
                    startActivity(intent);

                    // Releases model resources if no longer used.
                    model.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId(); // Get the item ID

        bottomNavigationView.getMenu().findItem(itemId).setChecked(true);

        if (itemId == R.id.home) {
            Intent intent = new Intent(CameraActivity.this, CameraActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.proportion) {
            Intent intent = new Intent(CameraActivity.this, ProportionProActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.planner) {
            Intent intent = new Intent(CameraActivity.this, MealPlannerActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(CameraActivity.this, ProfileActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    int getMax(float[] arr) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[max]) max = i;
        }
        return max;
    }

    void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, 11);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 11) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    this.getPermission();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10 && resultCode == RESULT_OK){
            if(data!=null){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    if(bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Log.e(TAG, "Bitmap is null after loading from URI");
                        // Handle null bitmap appropriately
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error loading bitmap from URI: " + e.getMessage());
                    // Handle the exception appropriately
                }
            } else {
                Log.e(TAG, "Data is null in onActivityResult");
                // Handle null data appropriately
            }
        }
        else if(requestCode==12 && resultCode == RESULT_OK){
            if(data != null && data.getExtras() != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
                if(bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "Bitmap is null from camera capture");
                    // Handle null bitmap appropriately
                }
            } else {
                Log.e(TAG, "Data or extras are null in onActivityResult");
                // Handle null data or extras appropriately
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

