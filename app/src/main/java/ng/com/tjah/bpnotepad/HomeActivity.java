package ng.com.tjah.bpnotepad;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ng.com.tjah.bpnotepad.db.DaoMaster;
import ng.com.tjah.bpnotepad.db.DaoSession;
import ng.com.tjah.bpnotepad.db.MeasureDao;
import ng.com.tjah.bpnotepad.db.Measure;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;


public class HomeActivity extends AppCompatActivity {
    TextView from, to;
    ImageButton search;
    RecyclerView myList;
    List<Measure> measureList;
    LinearLayoutManager llm;
    CommonAdapter<Measure> commonAdapter;
    Date fromD, toD;
    SimpleDateFormat myFmt;
    LinearLayout emptyLog;
    private AdView mBannerAd;


    //db
    DaoMaster.DevOpenHelper helper;
    SQLiteDatabase db;
    DaoMaster daoMaster;
    DaoSession daoSession;
    MeasureDao mDAO;

    InterstitialAd mInterstitialAd;
    FloatingActionButton mMyButton;


    public HomeActivity() {
        // Required empty public constructor
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return super.onSupportNavigateUp();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // Inflate the layout for this fragment

        ActionBar ab = getSupportActionBar();
        ab.setTitle(Html.fromHtml("<font color=#ffffff>" + "BP Notepad" + "</font>"));
        //ab.setIcon(getResources().getDrawable(R.drawable.ic_action_back));
//        if (ab != null) {
//            ab.setDisplayHomeAsUpEnabled(true);
//        }


        MobileAds.initialize(this,
                "ca-app-pub-7015642679712581~7078103301");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7015642679712581/2775185776");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

        fromD = DateUtil.getBeginningOfThisWeek();
        toD = DateUtil.getEndOfThisWeek();
        from = findViewById(R.id.his_list_from);
        to = findViewById(R.id.his_list_to);
        search = findViewById(R.id.his_list_search);
        myFmt = new SimpleDateFormat("dd-MM-yy");
        from.setText(myFmt.format(fromD));
        to.setText(myFmt.format(toD));
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPick(from);
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPick(to);
            }
        });

        //   mMyButton = findViewById(R.id.add_bp);
//        mMyButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//
//
//
//
//    }
//});

        mBannerAd = findViewById(R.id.banner_AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mBannerAd.loadAd(adRequest);


        emptyLog = findViewById(R.id.empty_log);
        myList = findViewById(R.id.his_list_recycle);
        llm = new LinearLayoutManager(this);
        myList.setLayoutManager(llm);
        myList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initGreenDao();
        measureList = mDAO.queryBuilder().where(MeasureDao.Properties.Time.between(fromD.getTime(),
                toD.getTime())).list();
        commonAdapter = new CommonAdapter<Measure>(this, R.layout.measure_item, measureList) {
            @Override
            protected void convert(ViewHolder holder, final Measure measure, final int position) {
                holder.setText(R.id.his_item_sys, measure.getBp_sys() + "");
                holder.setText(R.id.his_item_dia, measure.getBp_dia() + "");
                holder.setText(R.id.his_item_pulse, measure.getBp_pul() + "");
                SimpleDateFormat myFmt = new SimpleDateFormat("hh:mm");
                holder.setText(R.id.his_item_time, myFmt.format(new Date(measure.getTime())));
                SimpleDateFormat myFmtD = new SimpleDateFormat("dd-MM-yy");
                holder.setText(R.id.his_item_date, myFmtD.format(new Date(measure.getDate())));

                holder.setOnLongClickListener(R.id.cardView, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //   Toast.makeText(History.this,String.valueOf(position),Toast.LENGTH_LONG).show();
                        // File delete confirm
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setTitle(HomeActivity.this.getString(R.string.dialog_reading_delete));
                        builder.setPositiveButton(HomeActivity.this.getString(R.string.dialog_action_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mDAO = daoSession.getMeasureDao();
                                        mDAO.deleteByKey(mDAO.getKey(measure));
                                        measureList.clear();
                                        measureList.addAll(mDAO.loadAll());
                                        commonAdapter.notifyDataSetChanged();
                                        Toast.makeText(HomeActivity.this, "record has been deleted", Toast.LENGTH_LONG).show();

                                    }
                                });
                        builder.setCancelable(true);
                        builder.setNegativeButton(HomeActivity.this.getString(R.string.dialog_action_cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();


                        return true;
                    }
                });

                if (getItemCount() == 0) {
                    emptyLog.setVisibility(View.VISIBLE);
                    myList.setVisibility(View.INVISIBLE);
                } else {
                    emptyLog.setVisibility(View.GONE);
                    myList.setVisibility(View.VISIBLE);
                }

                if (measureList.size() == 0) {
                    emptyLog.setVisibility(View.VISIBLE);
                    myList.setVisibility(View.INVISIBLE);
                } else {
                    emptyLog.setVisibility(View.GONE);
                    myList.setVisibility(View.VISIBLE);
                }

            }
        };
        myList.setAdapter(commonAdapter);
        myList.setHasFixedSize(true);
        myList.setItemAnimator(new DefaultItemAnimator());
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromD.getTime() != toD.getTime()) {
                    measureList.clear();
                    List<Measure> m2 = mDAO.queryBuilder().where(MeasureDao.Properties.Time.between(fromD.getTime(), toD.getTime())).list();
                    // Toast.makeText(History.this,"list size:"+m2.size(),Toast.LENGTH_LONG).show();
                    measureList.addAll(m2);
                    commonAdapter.notifyDataSetChanged();


                    if (m2.size() == 0) {
                        emptyLog.setVisibility(View.VISIBLE);
                        myList.setVisibility(View.INVISIBLE);
                        Toast.makeText(HomeActivity.this, "No BP Log Available", Toast.LENGTH_LONG).show();
                    } else {
                        emptyLog.setVisibility(View.GONE);
                        myList.setVisibility(View.VISIBLE);
                    }

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }
            }
        });


//
//
//        addBp = findViewById(R.id.add_bp);
//        addBp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent add = new Intent(History.this,newMeasure.class);
//                startActivity(add);
//            }
//
//        mInterstitialAd.setAdListener(new AdListener(){
//
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                mInterstitialAd.show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Log.d("errorcode", "" + errorCode);
//                // Code to be executed when an ad request fails.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();
//                }
//            }
//        });

        mBannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d("errorcode", "" + errorCode);
                // Code to be executed when an ad request fails.
                showBannerAd();
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
                showBannerAd();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        measureList.clear();
        measureList.addAll(mDAO.queryBuilder().where(MeasureDao.Properties.Time.between(fromD.getTime(), toD.getTime())).list());
        commonAdapter.notifyDataSetChanged();
    }


    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("754DB6521943676637AE86202C5ACE52")
                .build();
        mBannerAd.loadAd(adRequest);

    }


    private void showDialogPick(final TextView timeText) {
        final StringBuffer time = new StringBuffer();
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                time.append(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                timeText.setText(time.toString());
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                if (timeText.getId() == R.id.his_list_from)
                    fromD.setTime(calendar.getTimeInMillis());
                else toD.setTime(calendar.getTimeInMillis());
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_log, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search_log) {
            Intent addBp = new Intent(HomeActivity.this, newMeasure.class);
            startActivity(addBp);

            if (mInterstitialAd.isLoaded()) {
                try {
                    mInterstitialAd.show();
                } catch (Exception e) {
                    Log.e("TAG_E", e.getMessage());
                }
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//
//
//        if (id == R.id.action_signOut) {
//            signOut()
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

    private void initGreenDao() {
        helper = new DaoMaster.DevOpenHelper(HomeActivity.this, "bp-monitor", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mDAO = daoSession.getMeasureDao();
    }

    @Override
    public void onDestroy() {
        daoSession.clear();
        daoSession = null;
        db.close();
        helper.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
