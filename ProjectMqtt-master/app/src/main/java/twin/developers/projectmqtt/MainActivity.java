package twin.developers.projectmqtt;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    private Mqtt mqttManager;
    private List<Alumnos> ListAlumnos = new ArrayList<Alumnos>();
    ArrayAdapter<Alumnos> arrayAdapterAlumnos;
    EditText Nombre,Edad;
    Button Agregar, Eliminar;
    ListView lvListado;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Nombre = findViewById(R.id.txtNombre);
        Edad = findViewById(R.id.txtEdad);
        Agregar = findViewById(R.id.btnAgregar);
        Eliminar = findViewById(R.id.btnEliminar);
        lvListado = findViewById(R.id.gridList);
        inicializarFireBase();
        listarDatos();

        Agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ed = Edad.getText().toString();
                int edad = Integer.parseInt(ed);
                int edadmin = 18;
                if(edad >= edadmin){
                    Alumnos alumnos = new Alumnos();
                    alumnos.setIdMatricula(UUID.randomUUID().toString());
                    alumnos.setNombre(Nombre.getText().toString());
                    alumnos.setEdad(Edad.getText().toString());
                    databaseReference.child("Matricula").child(alumnos.getIdMatricula()).setValue(alumnos);
                    mqttManager.publishMessage("Alumno Matriculado");
                }else {
                    Toast.makeText(MainActivity.this, "No cumple con la edad", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ListAlumnos.isEmpty()) {
                    Alumnos alumnoEliminar = ListAlumnos.get(0); // Puedes cambiar la lógica aquí
                    String idAlumnoEliminar = alumnoEliminar.getIdMatricula();

                    // Eliminar alumno de Firebase
                    databaseReference.child("Matricula").child(idAlumnoEliminar).removeValue();
                    mqttManager.publishMessage("Alumno Eliminado");
                } else {
                    // Manejo si la lista está vacía
                    Toast.makeText(MainActivity.this, "No hay alumnos para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mqttManager = new Mqtt(getApplicationContext());
        mqttManager.connectToMqttBroker();
    }
    private void listarDatos() {
        databaseReference.child("Matricula").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ListAlumnos.clear();
                for (DataSnapshot objs : snapshot.getChildren()){
                    Alumnos li =objs.getValue(Alumnos.class);
                    ListAlumnos.add(li);
                    arrayAdapterAlumnos =new ArrayAdapter<Alumnos>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,ListAlumnos);
                    lvListado.setAdapter(arrayAdapterAlumnos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void inicializarFireBase(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference =firebaseDatabase.getReference();
    }
}