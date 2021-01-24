package com.example.imageuploading__retrofit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;

public class MainActivity extends AppCompatActivity {
EditText editText;
    ImageView imagesV;
    Button uploadButton, selectButton;
    private int IMG_REQUEST = 2;
    private Bitmap bitmap;
    private String encodedImage;
    String apiName; String apiIMage;

    ProgressDialog progressDialog;
    MultipartBody.Part encodedImagePart;
   // private static final String BASE_URL="http://192.168.0.102/thanova/uploadifirst.php";
    String imgName;
    Bitmap decodedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagesV = findViewById(R.id.imageView3);
        uploadButton = findViewById(R.id.upload_IMgae_BUTT);
        selectButton = findViewById(R.id.sel_Image_BUTT);
        editText=findViewById(R.id.shows);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading... ");


        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, IMG_REQUEST);                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              uploadImage();
            }
        });
    }

    private void uploadImage() {
        progressDialog.show();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageInByte=byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(imageInByte,Base64.DEFAULT);
        imgName= String.valueOf(Calendar.getInstance().getTimeInMillis());
      //  byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
      //  decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Toast.makeText(this, encodedImage, Toast.LENGTH_SHORT).show();
        Log.d("encodedImage",encodedImage);
        Log.d("imgName",imgName);

      //  String qw="12345",er="123654";
        ApiInterface apiInterface = RetroClient.getRetrofit().create(ApiInterface.class);
        Call<ResponsePOJO> call = apiInterface.uploadIm(imgName,encodedImage);
        call.enqueue(new Callback<ResponsePOJO>() {

            @Override
            public void onResponse(Call<ResponsePOJO> call, retrofit2.Response<ResponsePOJO> response) {
                progressDialog.dismiss();

                    if (response.isSuccessful()) {

                        Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Not Successfull Response", Toast.LENGTH_SHORT).show();
                        Log.d("code Response", String.valueOf(response.code()));
                        String po=String.valueOf(response.code());

                      //  apiInterface.uploadIm(apiName,apiIMage);
                        editText.setText(po);
                    }
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {
                Log.d("onFailure logs", t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Data any THING", String.valueOf(data));
        InputStream inputStream = null;
        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri pathp = data.getData();
            if (inputStream == null) {
                try {
                    inputStream = getContentResolver().openInputStream(pathp);
                    bitmap = BitmapFactory.decodeStream(inputStream);

                  //  String deco=Base64.decode(encodedImage)
                    imagesV.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            /*
            String[] imageProjection = {MediaStore.Images.Media.DATA};
            Log.d("Image Projection  :", String.valueOf(imageProjection));
            CursorLoader cursorLoader = new CursorLoader(
                    getApplicationContext(),
                    pathp, imageProjection,
                    null, null, null);
            Log.d("Cursor  :", String.valueOf(cursorLoader));
            Cursor cursor=cursorLoader.loadInBackground();
            if (cursor!=null) {
                 int column_index  = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                encodedImage = cursor.getString(column_index);
            }
                Log.d("Data any THING", String.valueOf(data));
            cursor.close();
            */
        }
    }
}
