package com.example.karim.muzzafapp.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.karim.muzzafapp.Adapter.GVAdapter;
import com.example.karim.muzzafapp.Model.ImagesData;
import com.example.karim.muzzafapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class VideoAct extends AppCompatActivity {

    private static int SELECT_PHOTO=77;
    GridView gridView;
    List<String> images=new ArrayList<>();
    GVAdapter adapter;
    SQLiteDatabase db;
    private List<String> fileNameList;
    private List<String> fileDoneList;
    private StorageReference mStorage;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("UserVideos");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mStorage = FirebaseStorage.getInstance().getReference();
        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        gridView=findViewById(R.id.gv);

        getData();
    }

    public void finish(View view) {
        finish();
    }
    private void getData() {
        images=new ArrayList<>();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                images.clear();
                for(DataSnapshot dt:dataSnapshot.getChildren()){
                    if(dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        for(DataSnapshot dt1:dt.getChildren()) {
                            ImagesData imagesData = dt1.getValue(ImagesData.class);
                            images.addAll(imagesData.getImages());
                        }
                    }
                }
                adapter=new GVAdapter(VideoAct.this,images);
                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void add(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("video/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    List<Uri> uriList = new ArrayList<>();

    private void setDataToRV(Uri fileUri) {
        File file = new File(fileUri.getPath());
        String fileName = file.getName();
        fileNameList.add(fileName);
        fileDoneList.add("uploading");
        uriList.add(fileUri);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int totalItemSelected = data.getClipData().getItemCount();
                for (int i = 0; i < totalItemSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    setDataToRV(fileUri);
                }
                UploadToFireBase();
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                setDataToRV(fileUri);
            }
        }
    }

    List<String>downloadUrl;
    private void UploadToFireBase() {

        if (fileNameList.size() == 0)
            Toast.makeText(this, "no images selected ", Toast.LENGTH_SHORT).show();
        else {
            final ProgressDialog progressDialog = new ProgressDialog(this,
                    R.style.ThemeOverlay_AppCompat_Dialog_Alert);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            downloadUrl = new ArrayList<>();
            for (int i = 0; i < fileNameList.size(); i++) {
                StorageReference fileUpload = mStorage.child("Videos").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(fileNameList.get(i));
                final int finalI = i;
                fileUpload.putFile(uriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileDoneList.remove(finalI);
                        fileDoneList.add("done");
                        Task<Uri> task=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl.add(uri.toString());
                                if (finalI + 1 == fileNameList.size()) {
                                    ImagesData imagesData = new ImagesData();
                                    imagesData.setImages(downloadUrl);
                                    mReference.child(FirebaseAuth.getInstance().getUid()).child(Calendar.getInstance().getTime().toString()).setValue(imagesData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                        }
                                    });
                                }
                            }
                        });

                    }
                });
            }

        }


    }
}
