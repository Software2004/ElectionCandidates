package com.example.electioncandidates;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST_DIALOG = 2;

    private EditText etCandidateName, etCnic, etParty, etElectoralAreaNo, etDetail;
    private ImageView ivProfilePic, ivDialogProfilePic;
    private RadioGroup radioGroup;
    private RadioButton rbNationalAssembly, rbProvincialAssembly;
    private Button btnUploadData;
    private Uri imageUri, dialogImageUri;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCandidateName = findViewById(R.id.etCandidateName);
        etCnic = findViewById(R.id.etCnic);
        etParty = findViewById(R.id.etParty);
        etElectoralAreaNo = findViewById(R.id.etElectoralAreaNo);
        etDetail = findViewById(R.id.etDetail);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        radioGroup = findViewById(R.id.radioGroup);
        rbNationalAssembly = findViewById(R.id.rbNationalAssembly);
        rbProvincialAssembly = findViewById(R.id.rbProvincialAssembly);
        btnUploadData = findViewById(R.id.btnUploadData);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Candidates");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("ProfileImages");

        findViewById(R.id.imgLay).setOnClickListener(view -> openImageChooser(PICK_IMAGE_REQUEST));
        btnUploadData.setOnClickListener(view -> validateAndUploadData());
    }

    private void openImageChooser(int requestCode) {
        startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), "Select Picture"), requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_DIALOG && resultCode == RESULT_OK && data != null && data.getData() != null) {
            dialogImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), dialogImageUri);
                ivDialogProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void validateAndUploadData() {
        String candidateName = etCandidateName.getText().toString().trim();
        String cnic = etCnic.getText().toString().trim();
        String party = etParty.getText().toString().trim();
        String electoralAreaNo = etElectoralAreaNo.getText().toString().trim();
        String detail = etDetail.getText().toString().trim();
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        boolean isValid = true;

        if (TextUtils.isEmpty(candidateName)) {
            etCandidateName.setError("Candidate name is required");
            isValid = false;
        } else {
            etCandidateName.setError(null);
        }

        if (TextUtils.isEmpty(cnic)) {
            etCnic.setError("CNIC is required");
            isValid = false;
        } else {
            etCnic.setError(null);
        }

        if (TextUtils.isEmpty(party)) {
            etParty.setError("Party is required");
            isValid = false;
        } else {
            etParty.setError(null);
        }

        if (TextUtils.isEmpty(electoralAreaNo)) {
            etElectoralAreaNo.setError("Electoral Area No is required");
            isValid = false;
        } else {
            etElectoralAreaNo.setError(null);
        }

        if (TextUtils.isEmpty(detail)) {
            etDetail.setError("Detail is required");
            isValid = false;
        } else {
            etDetail.setError(null);
        }

        if (imageUri == null) {
            Toast.makeText(this, "Please select a profile picture", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        TextView tv = findViewById(R.id.candi);
        if (selectedRadioButtonId == -1) {
            tv.setError("Please select an assembly type");
            Toast.makeText(this, "Please select an assembly type", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else tv.setError(null);

        if (isValid) {
            String assemblyType = selectedRadioButtonId == R.id.rbNationalAssembly ? "National Assembly" : "Provincial Assembly";
            showConfirmationDialog(candidateName, cnic, party, electoralAreaNo, detail, assemblyType);
        }
    }


    private void showConfirmationDialog(String candidateName, String cnic, String party, String electoralAreaNo, String detail, String assemblyType) {
        AppDialogBuilder dialog = new AppDialogBuilder(this);
        dialog.setContentView(R.layout.upload_symbol);
        dialog.switchToWindowContentView(true);
        ivDialogProfilePic = dialog.findViewById(R.id.profile_imageAct1);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        dialog.findViewById(R.id.imgLay).setOnClickListener(view -> openImageChooser(PICK_IMAGE_REQUEST_DIALOG));
        btnConfirm.setOnClickListener(view -> {
            if (dialogImageUri == null) {
                Toast.makeText(this, "Please select a profile picture for the dialog", Toast.LENGTH_SHORT).show();
                return;
            }
            uploadDataToFirebase(candidateName, cnic, party, electoralAreaNo, detail, assemblyType);
            dialog.dismiss();

        });

        dialog.show();
    }

    private void uploadDataToFirebase(String candidateName, String cnic, String party, String electoralAreaNo, String detail, String assemblyType) {

        final StorageReference profilePicRef = storageReference.child(cnic + "_profile.jpg");
        final StorageReference dialogPicRef = storageReference.child(cnic + "_dialog.jpg");
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        profilePicRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String profilePicUrl = uri.toString();
            dialogPicRef.putFile(dialogImageUri).addOnSuccessListener(taskSnapshot1 -> dialogPicRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                String dialogPicUrl = uri1.toString();
                Candidate candidate = new Candidate(
                        candidateName, cnic, party, electoralAreaNo, detail, assemblyType, profilePicUrl, dialogPicUrl
                );
                String assymblytype = assemblyType.substring(0, assemblyType.indexOf(' '));
                databaseReference.child(assymblytype).child(electoralAreaNo).child(cnic).setValue(candidate).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                resetWidgets();
                                Toast.makeText(MainActivity.this, "Data uploaded successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Data upload failed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        });
            }));
        }));
    }

    private void resetWidgets() {
        etCandidateName.setText("");
        etCandidateName.setError(null);

        etCnic.setText("");
        etCnic.setError(null);

        etParty.setText("");
        etParty.setError(null);

        etElectoralAreaNo.setText("");
        etElectoralAreaNo.setError(null);

        etDetail.setText("");
        etDetail.setError(null);

        ivProfilePic.setImageResource(R.drawable.user);
        imageUri = null;

        radioGroup.clearCheck();
    }

}
