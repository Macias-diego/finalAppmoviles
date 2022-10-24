package com.example.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class seller extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();// intancia de firestore
    String idSeller; //Variable que contendra el id de cada  cliente (customer)
    String condicionEliminar  ;
    double condicionEliminarNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        EditText emailSeller = findViewById(R.id.etemailSeller);
        EditText nameSeller = findViewById(R.id.etnameSeller);
        EditText phoneSeller = findViewById(R.id.etphoneSeller);
        EditText totalCommision = findViewById(R.id.etTotalCommision);
        Button btnsaveSeller = findViewById(R.id.btnsave);
        Button btnseachSeller = findViewById(R.id.btnsearch);
        Button btneditSeller = findViewById(R.id.btnedit);
        Button btndeleteSeller = findViewById(R.id.btndelete);

        btndeleteSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //confirmación del borrar
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(seller.this);
                alertDialogBuilder.setMessage("Está seguro de eliminar el cliente con i: "+ nameSeller.getText().toString()+"?");
                alertDialogBuilder.setPositiveButton("Sí",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //Se eliminara elñ cliente con el idCustomer respectivo
                                condicionEliminar = totalCommision.getText().toString();
                                condicionEliminarNum = Double.parseDouble(condicionEliminar);
                                if(condicionEliminarNum == 0){
                                    db.collection("Seller").document(idSeller)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    //Log.d("cliente", "DocumentSnapshot successfully deleted!");
                                                    emailSeller.setText("");
                                                    nameSeller.setText("");
                                                    phoneSeller.setText("");
                                                    totalCommision.setText("");
                                                    Toast.makeText(getApplicationContext(),"Vendedor Eliminado...",Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("cliente", "Error deleting document", e);
                                                }
                                            });
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"no se puede borrar por que tiene ventas",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                alertDialogBuilder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        btneditSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> Seller = new HashMap<>();// Tabla cursor

                Seller.put("emailSeller", emailSeller.getText().toString());
                Seller.put("nameSeller", nameSeller.getText().toString());
                Seller.put("phoneSeller", phoneSeller.getText().toString());
                Seller.put("totalCommisionSeller",totalCommision.getText().toString());

                db.collection("Seller").document(idSeller)
                        .set(Seller)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Vendedor actualizado...",Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("customer", "Error writing document", e);
                            }
                        });
            }
        });
        btnseachSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Seller")
                        .whereEqualTo("emailSeller", emailSeller.getText().toString())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {//Si encontró el documento
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            idSeller=document.getId();
                                            nameSeller.setText(document.getString("nameSeller"));
                                            phoneSeller.setText(document.getString("phoneSeller"));
                                            totalCommision.setText(document.getString("totalCommisionSeller"));
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"El nombre del vendedor no existe el Vendedores",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        btnsaveSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSeller(emailSeller.getText().toString(),nameSeller.getText().toString(),phoneSeller.getText().toString());

            }
        });

    }

    private void saveSeller(String sEmailSeller, String sNameSeller, String sPhoneSeller) {
        db.collection("Seller")
                .whereEqualTo("emailSeller", sEmailSeller)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {//No encontró el documento
                                //Guardar los datos del cliente(Seller)
                                Map<String, Object> Seller = new HashMap<>();// Tabla cursor
                                Seller.put("emailSeller", sEmailSeller);
                                Seller.put("nameSeller", sNameSeller);
                                Seller.put("phoneSeller", sPhoneSeller);
                                Seller.put("totalCommisionSeller", "0");


                                db.collection("Seller")
                                        .add(Seller)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
//                                              Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                Toast.makeText(getApplicationContext(),"Vendedor agregado correctamente...",Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                                              Log.w(TAG, "Error adding document", e);
                                                Toast.makeText(getApplicationContext(),"Error! Vendedor no se guardó...",Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"ID del Vendedor existente",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });



    }
}