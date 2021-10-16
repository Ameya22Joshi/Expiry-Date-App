package com.example.expirydate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    EditText productname, quantity ;
    ImageView submitbtn;
    Toolbar toolbar;
    EditText mandate;
    EditText expdate;
    DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;
    ImageView imageView1;
    ImageView imageView2;
    TextView numofdays;
    Button buttondays;
    public static EditText bcodeedittext;
    ImageView bcodeimg;
//    private int mDate, mMonth, mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        toolbar = findViewById(R.id.toolbar2);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        mandate = findViewById(R.id.editTextDate);
        expdate = findViewById(R.id.editTextDate2);
        imageView1 = findViewById(R.id.calimg);
        imageView2 = findViewById(R.id.calimg2);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());
        mandate.setText(date);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, android.R.style.Theme_DeviceDefault_Dialog,dateSetListener1,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                datePickerDialog.show();
            }
        });

        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                mandate.setText(date);
            }
        };

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, android.R.style.Theme_DeviceDefault_Dialog,dateSetListener2,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                datePickerDialog.show();
            }
        });

        dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                expdate.setText(date);
            }
        };

        numofdays = findViewById(R.id.resultdays);
        buttondays = findViewById(R.id.button2);
        buttondays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idate = mandate.getText().toString();
                String fdate = expdate.getText().toString();

                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date1 = simpleDateFormat1.parse(idate);
                    Date date2 = simpleDateFormat1.parse(fdate);

                    long startDate = date1.getTime();
                    long endDate = date2.getTime();

                    if (startDate <= endDate){
                       Period period = new Period(startDate,endDate, PeriodType.dayTime());
                       int years = period.getYears();
                       int months = period.getMonths();

                       int days = period.getDays();
                       numofdays.setText(days + " Days");

                    }
                    else {
                        Toast.makeText(AddActivity.this, "Manufacturing Date should not be larger than Expiry Date", Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });



//        imageView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar Cal = Calendar.getInstance();
//                mDate = Cal.get(Calendar.DATE);
//                mMonth = Cal.get(Calendar.MONTH);
//                mYear = Cal.get(Calendar.YEAR);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        mandate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
//                    }
//                },mYear,mMonth,mDate);
//                datePickerDialog.show();
//            }
//        });
//        imageView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar Cal = Calendar.getInstance();
//                mDate = Cal.get(Calendar.DATE);
//                mMonth = Cal.get(Calendar.MONTH);
//                mYear = Cal.get(Calendar.YEAR);
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        expdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
//                    }
//                },mYear,mMonth,mDate);
//                datePickerDialog.show();
//            }
//        });

        bcodeedittext = (EditText) findViewById(R.id.bcode);
        bcodeimg = (ImageView) findViewById(R.id.bscanimg);

        bcodeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ScanCodeActivity.class));

            }
        });


        productname = findViewById(R.id.nameofproduct);
        quantity = findViewById(R.id.quantity);

        submitbtn = findViewById(R.id.imgsubmitbtn);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addproduct(productname.getText().toString().trim(),
                        numofdays.getText().toString().trim(),
                        Integer.valueOf(quantity.getText().toString().trim()));

                Intent intent = new Intent(AddActivity.this , MainActivity.class);
                startActivity(intent);
                finish();

            }
        });




    }



}