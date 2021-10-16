package com.example.expirydate;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.expirydate.R;

import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateActivity extends AppCompatActivity {
    EditText productname, quantity;
    ImageView submitbtn;
    Toolbar toolbar1;
    EditText mandate;
    EditText expdate;
    DatePickerDialog.OnDateSetListener dateSetListener1, dateSetListener2;
    ImageView imageView1;
    ImageView imageView2;
    TextView numofdays;
    Button buttondays, delete_button;
    public static EditText bcodeedittext;
    ImageView bcodeimg;
//    private int mDate, mMonth, mYear;
    String id, pname, pdaysleft, pquantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        toolbar1 = findViewById(R.id.toolbar2_2);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mandate = findViewById(R.id.editTextDate_2);
        expdate = findViewById(R.id.editTextDate2_2);
        imageView1 = findViewById(R.id.calimg_2);
        imageView2 = findViewById(R.id.calimg2_2);

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, android.R.style.Theme_DeviceDefault_Dialog, dateSetListener1, year, month, day);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this, android.R.style.Theme_DeviceDefault_Dialog, dateSetListener2, year, month, day);
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

        numofdays = findViewById(R.id.resultdays_2);
        buttondays = findViewById(R.id.button2_2);
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

                    if (startDate <= endDate) {
                        Period period = new Period(startDate, endDate, PeriodType.dayTime());
                        int years = period.getYears();
                        int months = period.getMonths();

                        int days = period.getDays();
                        numofdays.setText(days + " Days");

                    } else {
                        Toast.makeText(UpdateActivity.this, "Manufacturing Date should not be larger than Expiry Date", Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        bcodeedittext = (EditText) findViewById(R.id.bcode_2);
        bcodeimg = (ImageView) findViewById(R.id.bscanimg_2);

        bcodeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));

            }
        });
        productname = findViewById(R.id.nameofproduct_2);
        quantity = findViewById(R.id.quantity_2);
        delete_button = findViewById(R.id.delete_button);

        submitbtn = findViewById(R.id.imgsubmitbtn_2);
        getAndSetIntentData();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(pname);
        }
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                pname = productname.getText().toString().trim();
                pdaysleft = numofdays.getText().toString().trim();
                pquantity = quantity.getText().toString().trim();

                myDB.updateData(id, pname, pdaysleft, pquantity);
                Intent intent = new Intent(UpdateActivity.this , MainActivity.class);
                startActivity(intent);
                finish();

            }

        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });


    }
    void getAndSetIntentData(){
        if (getIntent().hasExtra("id") && getIntent().hasExtra("pname") && getIntent().hasExtra("pdaysleft") && getIntent().hasExtra("pquantity")){
            id = getIntent().getStringExtra("id");
            pname = getIntent().getStringExtra("pname");
            pdaysleft = getIntent().getStringExtra("pdaysleft");
            pquantity = getIntent().getStringExtra("pquantity");

            productname.setText(pname);
            numofdays.setText(pdaysleft);
            quantity.setText(pquantity);

        }else {
            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
    }
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + pname + "?");
        builder.setMessage("Are you sure you want to delete" + pname + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);


                finish();

            }

        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

}