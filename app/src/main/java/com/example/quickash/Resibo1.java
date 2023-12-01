package com.example.quickash;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Resibo1 extends AppCompatActivity {

    TextView name, id, amount, value, datetime;
    Button done, pdf;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat, simpleDateFormat1;
    String date, date1, user_name, user_id, user_val;
    public final int REQUEST_CODE = 100;
    int pageWidth = 720;
    int pageHeight = 1200;
    Bitmap imageBitmap, scaledImageBitmap;
    boolean doublePress = false;
    double e;
    private String stringFilePath;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resibo2);

        name = findViewById(R.id.textView2);
        id = findViewById(R.id.textView4);
        amount = findViewById(R.id.textView5);
        value = findViewById(R.id.textView3);
        datetime = findViewById(R.id.textView6);
        done = findViewById(R.id.da);
        pdf = findViewById(R.id.button);
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy, HH:mm");
        date = simpleDateFormat.format(calendar.getTime());
        simpleDateFormat1 = new SimpleDateFormat("MM-dd-yyyy, HH-mm-ss");
        date1 = simpleDateFormat1.format(calendar.getTime());

        Intent intent = getIntent();
        user_name = intent.getStringExtra("name");
        user_id = intent.getStringExtra("id");
        user_val = intent.getStringExtra("value");
        e = Double.valueOf(user_val);

        name.setText(user_name);
        id.setText(user_id);
        value.setText("-₱" + String.format("%.2f", e));
        datetime.setText(date);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
                    buttonCreatePDF();
                } else {
                    requestAllPermission();
                }
            }
        });
    }

    public void buttonCreatePDF(){

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Paint paint = new Paint();
        stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/Receipt_"+date1+".pdf";
        file = new File(stringFilePath);

        int x = 10, y = 25;
        String c = value.getText().toString();
        String d = datetime.getText().toString();
        String re = "Name: "+user_name+"\n"+"Acc ID: "+user_id+"\n"+"Amount: "+c+"\n"+"Transaction: "+"Cash Out"+"\n"+"Date and Time: "+d;

        for (String line:re.split("\n")){
            page.getCanvas().drawText(line,x,y, paint);

            y+=paint.descent()-paint.ascent();
        }
        pdfDocument.finishPage(page);
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(Resibo1.this, "Creating PDF", Toast.LENGTH_SHORT).show();
            Toast.makeText(Resibo1.this, "Done", Toast.LENGTH_SHORT).show();
            try {
                PdfReader pdfReader = new PdfReader(file.getPath());
                String stringParse = PdfTextExtractor.getTextFromPage(pdfReader,1).trim();
                pdfReader.close();
                //TODO: Debugging
//                Toast.makeText(Resibo1.this, stringParse, Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(Resibo1.this, "Error in Reading", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(Resibo1.this, "Error Creating PDF", Toast.LENGTH_SHORT).show();
        }
        pdfDocument.close();
    }

    private void requestAllPermission() {
        ActivityCompat.requestPermissions(Resibo1.this, new String[]{READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Resibo1.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}