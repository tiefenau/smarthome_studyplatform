package de.pfiva.mobile.voiceassistant.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.pfiva.data.model.NLUData;
import de.pfiva.mobile.voiceassistant.R;
import de.pfiva.mobile.voiceassistant.adapters.NLUAdapter;
import de.pfiva.mobile.voiceassistant.network.RetrofitClientInstance;
import de.pfiva.mobile.voiceassistant.web.NLUDataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NLUAdapter nluAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        NLUDataService nluDataService = RetrofitClientInstance
                .getRetrofitInstance().create(NLUDataService.class);

        Call<List<NLUData>> nluCall = nluDataService.getNLUData();
        nluCall.enqueue(new Callback<List<NLUData>>() {
            @Override
            public void onResponse(Call<List<NLUData>> call, Response<List<NLUData>> response) {
                progressDialog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<NLUData>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,
                        "Something went wrong...Please try later!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void generateDataList(List<NLUData> dataList) {
        recyclerView = findViewById(R.id.nluRecyclerView);
        nluAdapter = new NLUAdapter(this, dataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(nluAdapter);
    }
}
