package ng.com.tjah.bpnotepad;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ng.com.tjah.bpnotepad.db.DaoMaster;
import ng.com.tjah.bpnotepad.db.DaoSession;
import ng.com.tjah.bpnotepad.db.MeasureDao;
import ng.com.tjah.bpnotepad.db.Measure;

public class newMeasure extends AppCompatActivity {

    EditText timeOfRecord, dateOfRecord;
    EditText sysT, diaT, pulT;
    private static final String TAG = newMeasure.class.getSimpleName();



    Date selectedTime, selectedDate, selected_time_date;
    short sys, dia, pul;
    MeasureDao mDAO;

    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;
    InterstitialAd mInterstitialAd;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_measure);
        this.setTitle("Add New Reading");

        ActionBar ab = getSupportActionBar();
        //  ab.setTitle(Html.fromHtml("<font color=#ffffff>" + "Contact a Doctor" + "</font>"));
        //ab.setIcon(getResources().getDrawable(R.drawable.ic_action_back));
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        timeOfRecord=findViewById(R.id.time_bp);
        dateOfRecord=findViewById(R.id.date_bp);
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.SECOND,0);
        selectedTime=calendar.getTime();
        selectedDate=calendar.getTime();
        DateFormat timeBp = new SimpleDateFormat("h:mm a");
        timeOfRecord.setText(timeBp.format(selectedTime));
        timeOfRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPick((TextView) v);
            }
        });
        DateFormat dateBp = new SimpleDateFormat("dd-MM-yyyy");
        dateOfRecord.setText(dateBp.format(selectedDate));
        dateOfRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialogPick((TextView) v);
            }
        });
        sysT=findViewById(R.id.bp_etext);
        diaT = findViewById(R.id.bp_etext_dia);
        pulT = findViewById(R.id.bp_etext_pul);
        initGreenDao();

        MobileAds.initialize(this,
                getString(R.string.appUnitId));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId( getString(R.string.tere_ID));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }


        mInterstitialAd.setAdListener(new AdListener(){

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d("errorcode", "" + errorCode);
                // Code to be executed when an ad request fails.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());

                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                },10000);

            }
        });

    }

    public void onSaveMeasure(View view)
    {
        if(checkEntry())
        {
            sys=Short.parseShort(sysT.getText().toString());
            dia= Short.parseShort(diaT.getText().toString());
            pul= Short.parseShort(pulT.getText().toString());
            // SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            SimpleDateFormat sd=new SimpleDateFormat("dd-MM-yyyy");
            try {
                selectedTime=sdf.parse(timeOfRecord.getText().toString());
                selectedDate=sd.parse(dateOfRecord.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(newMeasure.this);
            builder.setTitle("Confirm data")
                    .setMessage("BP Reading (Sys): "+sys+ "mmHg " + ", " +
                            "\nBP Reading (Dia): " + dia + "mmHg " + ", " +
                            "\nPulse : " + pul + "BPM " + ", " +
                            "\nTime: "+ timeOfRecord.getText().toString() +  "," +
                            " \nDate: " + dateOfRecord.getText().toString())

                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Measure newMeasure=new Measure(null,sys, dia,pul,selectedTime.getTime(),selectedDate.getTime());
                            List<Measure> ml=mDAO.queryBuilder().where(MeasureDao.Properties.Time.eq(selectedTime)).list();
                            if(ml.size()==0)
                            {
                                mDAO.insert(newMeasure);
                                sysT.setText("");
                                diaT.setText("");
                                pulT.setText("");
                                Toast.makeText(newMeasure.this,"Saved successfully.",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                mDAO.insertOrReplace(newMeasure);
                                Toast.makeText(newMeasure.this,"Updated successfully.",Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            builder.create().show();



        }
        else
        {
            Toast.makeText(this,"Please enter correct data.",Toast.LENGTH_LONG).show();
        }
    }


    public void onLeaveMeasure(View view)
    {
//        Intent intent = new Intent(newMeasure.this, MainActivity_Hyper.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

//        Intent intent = new Intent(newMeasure.this, MainActivity_Hyper.class);
//        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    public boolean checkEntry()
    {
        if(sysT.length()<2) return false;
        return(sysT.length() >= 2);
    }

    private void showDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int min = calendar.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(newMeasure.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM;
                if(hourOfDay < 12){
                    AM_PM = "AM";
                }else{
                    AM_PM = "PM";
                }

                if(hourOfDay > 12){
                    hourOfDay = hourOfDay - 12;
                }


                time.append(" " + hourOfDay + ":" + minute + " " + AM_PM);
                timeText.setText(time);
            }
        }, hour, min, false);

        timePickerDialog.show();
    }

    private void showDateDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        final int year_ = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(newMeasure.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//                if(year>year_)year=year_;
//                if(monthOfYear>month)monthOfYear=month;
//                if(dayOfMonth>day) dayOfMonth=day;
                time.append(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
                timeText.setText(time.toString());
            }
        }, year_, month, day);
        datePickerDialog.show();
    }



    private void initGreenDao()
    {
        helper = new DaoMaster.DevOpenHelper(this, "bp-monitor", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mDAO=daoSession.getMeasureDao();
    }

    @Override public void onDestroy()
    {
        daoSession.clear();
        daoSession=null;
        db.close();
        helper.close();
        super.onDestroy();
    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
