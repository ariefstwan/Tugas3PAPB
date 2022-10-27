package com.example.tpapb3;

import static android.widget.Toast.makeText;
import static com.bumptech.glide.Glide.with;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ImageView slots1, slots2, slots3;
    TextView sts;
    Button buttons;
    boolean begin = true;
    ArrayList<String> link = new ArrayList<>();
    int slot1, slot2, slot3;
    Random acak = new Random();
    ExecutorService exe1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slots1 = findViewById(R.id.slot1);
        slots2 = findViewById(R.id.slot2);
        slots3 = findViewById(R.id.slot3);
        sts = findViewById(R.id.stss);
        buttons = findViewById(R.id.tombol);
        exe1 = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        buttons.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {if (view.getId() == buttons.getId()) {
                buttons.setText("Berhenti");
                sts.setText("Tekan berhenti");
                if (!begin) {
                    begin = true;
                    exe1.execute(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                final String link =
                                        browsImg("https://mocki.io/v1/821f1b13-fa9a-43aa-ba9a-9e328df8270e");
                                try {
                                    JSONArray jsonArray = new
                                            JSONArray(link);
                                    int i = 0;
                                    while (i < jsonArray.length()) {
                                        JSONObject jsonObject =
                                                jsonArray.getJSONObject(i);
                                        MainActivity.this.link.add(jsonObject.getString("url"));
                                        i++;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                while (begin) {
                                    slot1 = acak.nextInt(3);
                                    slot2 = acak.nextInt(3);
                                    slot3 = acak.nextInt(3);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            with(MainActivity.this).load(MainActivity.this.link.get(slot1)).into(slots2);
                                            with(MainActivity.this).load(MainActivity.this.link.get(slot2)).into(slots1);
                                            with(MainActivity.this).load(MainActivity.this.link.get(slot3)).into(slots3);
                                        }
                                    });
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException
                                            e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    begin = false;
                    if ((slot1 != slot2) || (slot1 != slot3)) {
                        makeText(MainActivity.this, "Belum beruntung, coba lagi!", Toast.LENGTH_SHORT).show();
                    } else {
                        makeText(MainActivity.this,
                                "Anda menang!", Toast.LENGTH_SHORT).show();
                    }
                    buttons.setText("Mulai");
                    sts.setText("Terhenti");
                }
            }
            }
        });
    }

    private String browsImg(String jpg) throws IOException {
        final URL link = new URL(jpg);
        final InputStream went = link.openStream();
        final StringBuilder out = new StringBuilder();
        final byte[] buffer = new byte[1024];
        try {
            int ctr;
            while (-1 != (ctr = went.read(buffer))) {
                out.append(new String(buffer, 0, ctr));
            }
        } catch (IOException e) {
            throw new RuntimeException("", e);
        }
        final String file = out.toString();
        return file;
    }
}