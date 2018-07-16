package com.example.mitch.androidpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.FileOutputStream;
import java.sql.Array;

public class MainActivity extends AppCompatActivity {
    private CanvasView can;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        can = (CanvasView) findViewById(R.id.signature_canvas);
    }

    public void clearCanvas(View v) {
        can.clearCanvas();
    }

    private String[] colors = new String[] { "Black", "Blue", "Red"};

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.changeColors);
        Spinner spinner = (Spinner) item.getActionView();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, colors);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch (item) {
                    case "Blue":
                        can.changeColor(Color.BLUE);
                        break;
                    case "Red":
                        can.changeColor(Color.RED);
                        break;
                    case "Black":
                        can.changeColor(Color.BLACK);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Bitmap bitmap = can.getBitmap();
                try {
                    FileOutputStream fos = new FileOutputStream("/storage/emmc/DCIM/Camera");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {

                }
                break;
        }
        return true;
    }
}

