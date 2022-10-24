package com.example.afinal;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class sales extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();// intancia de firestore
    String idSales; //Variable que contendra las ventas
    String idSeller; // variable que contendra
    String venta;
    double comision;
    double contadorComision=0;
    String  totalcomision;
    String email;
    String name ;
    String phone;
    String comision2 ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        EditText dateSales = findViewById(R.id.etdateSales);
        EditText saleValue = findViewById(R.id.etsaleValue);
        Button saveSales = findViewById(R.id.btnsaveSales);
        EditText emailSellerSearch = findViewById(R.id.etemailSellerSearch);

        saveSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSales(dateSales.getText().toString(),saleValue.getText().toString(),emailSellerSearch.getText().toString());
            }

            private void saveSales(String sDateSales, String sSaleValue, String sEmailSellerSearch) {
                venta = saleValue.getText().toString();

                if ( parseInt(venta) >= 10000000 ) {
                    comision = 0.02 * (Double.parseDouble(venta)) ;
                    contadorComision = contadorComision + comision;
                    totalcomision = String.valueOf(contadorComision);
                    //Se busca el documento Seller para editarlo
                    db.collection("Seller")
                            .whereEqualTo("emailSeller", emailSellerSearch.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (!task.getResult().isEmpty()) {//Si encontró el documento
                                            for (QueryDocumentSnapshot document : task.getResult()) {

                                                // se llama el id del documento para encadenar cada registro del documento
                                                idSeller=document.getId();
                                                email = document.getString("emailSeller");
                                                name = document.getString("nameSeller");
                                                phone = document.getString("phoneSeller");
                                                comision2 = document.getString("totalCommisionSeller");
//                                                Toast.makeText(getApplicationContext(),"email: "+ email +" name: "+name+" Phone "+phone+" Comision "+comision2,Toast.LENGTH_LONG).show();
                                            }
                                            Map<String, Object> Seller = new HashMap<>();// Tabla cursor
                                            Seller.put("emailSeller", email);
                                            Seller.put("nameSeller",name);
                                            Seller.put("phoneSeller", phone);
                                            Seller.put("totalCommisionSeller",totalcomision );

                                            db.collection("Seller").document(idSeller)
                                                    .set(Seller)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getApplicationContext(),""+idSeller,Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("customer", "Error writing document", e);
                                                        }
                                                    });





                                            Toast.makeText(getApplicationContext(),"ingresamos la venta ",Toast.LENGTH_LONG).show();
                                            db.collection("Sales")
                                                    .whereEqualTo("emailSales",sEmailSellerSearch )
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                            if (task.isSuccessful()) {
//                                                                if (task.getResult().isEmpty()) {//No encontró el documento
                                                            //Guardar los datos del venta(Seller)
                                                            Map<String, Object> Sales = new HashMap<>();// Tabla cursor
                                                            Sales.put("emailSales", sEmailSellerSearch);
                                                            Sales.put("dateSales", sDateSales);
                                                            Sales.put("saleValue", sSaleValue);
//



                                                            db.collection("Sales")
                                                                    .add(Sales)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
//                                              Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                                                            Toast.makeText(getApplicationContext(),"venta agregada exitosamente",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
//                                              Log.w(TAG, "Error adding document", e);
                                                                            Toast.makeText(getApplicationContext(),"Error! ",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    });
//                                                                }
//                                                                else
//                                                                {
//                                                                    Toast.makeText(getApplicationContext(),"ID del Vendedor existente",Toast.LENGTH_SHORT).show();
//                                                                }
//                                                            }
                                                        }
                                                    });
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(),"Vendedor no existe",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
//                            }
                            });


                }
                else{
                    Toast.makeText(getApplicationContext(),"ingrese valor superior a   10'000.000",Toast.LENGTH_SHORT).show();
                }




            }
        });



    }
}