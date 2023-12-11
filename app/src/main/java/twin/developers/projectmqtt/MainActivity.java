package twin.developers.projectmqtt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    private Mqtt mqttManager;
    EditText distancia, velocidad;
    Button btnEnviar;
    TextView resultado;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distancia = findViewById(R.id.txtDistancia);
        velocidad = findViewById(R.id.txtVelocidad);
        btnEnviar = findViewById(R.id.btnPublish);
        resultado = findViewById(R.id.txt_Resultado);

        // Initialize Firebase before accessing the database
        FirebaseApp.initializeApp(this);

        try {
            inicializarFireBase();
        } catch (Exception e) {
            Log.e("FirebaseInit", "Error initializing Firebase", e);
        }

        mqttManager = new Mqtt(getApplicationContext());
        mqttManager.connectToMqttBroker();

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val1 = distancia.getText().toString();
                String val2 = velocidad.getText().toString();

                try {
                    int num1 = Integer.parseInt(val1);
                    int num2 = Integer.parseInt(val2);

                    if (num2 != 0) {
                        int op = num1 / num2;

                        String result = String.valueOf(op);
                        resultado.setText("Llegaras en: " + result);

                        // Publish to MQTT
                        mqttManager.publishMessage(resultado.getText().toString());

                        // Ensure that databaseReference is not null before calling child
                        if (databaseReference != null) {
                            // Write to Firebase
                            databaseReference.child("yourChildKey").setValue(result);
                        } else {
                            Log.e("FirebaseDatabase", "databaseReference is null");
                        }
                    } else {
                        // Handle division by zero
                        resultado.setText("Error: Division by zero");
                    }
                } catch (NumberFormatException e) {
                    // Handle the case where the input is not a valid number
                    resultado.setText("Error: Invalid input");
                }
            }
        });
    }

    private void inicializarFireBase() {
    }


}