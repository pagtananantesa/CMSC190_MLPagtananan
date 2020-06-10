package com.example.a5imageprocessing;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity implements SingleChoiceDialog.SingleChoiceLister {

    RelativeLayout startPage;
    Button startButton;

    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int MULTIPLE_PERMISSIONS = 1;

    //**********************LISTVIEWS**************************


    ListView listView;
    String title[] = {"ADD ENTRY", "SEARCH ENTRY", "EXPORT RECORDS", "CLEAR ALL RECORDS"};
    String desc[] = {"Add a single entry per character", "Search a single entry, View all entries", "Outputs .csv file", ""};
    int logos[] = {R.drawable.ic_action_add, R.drawable.ic_action_search, R.drawable.ic_action_export, R.drawable.ic_action_delete};

    ListView listView2;
    String title2[] = {"BACK TO HOME","DIMENSION", "ANTHOCYANIN"};
    String desc2[] = {"","Leaf width and height", "Intensity of anthocyanin present on the leaf"};
    int logos2[] = {R.drawable.ic_action_backpage,R.drawable.ruler, R.drawable.antho};

    ListView listView3;
    String title3[] = {"BACK","SEARCH AN ENTRY", "VIEW ALL ENTRIES"};
    String desc3[] = {"","", ""};
    int logos3[] = {R.drawable.ic_action_backpage,R.drawable.single, R.drawable.taken};



    //**********other UI**************************
    TextView directory;

    //ADD ENTRY
    LinearLayout cameraView;
    TextView titleView2;
    AutoCompleteTextView accessionIDEdit;
    EditText plotNoEdit;
    EditText plantIDEdit;
    Button okButton;
    ImageView imageView;
    ImageButton captureButton;
    Button galleryButton;
    Button backButton2;
    TextView resultText;
    Button retakeButton;
    Button saveButton;

    Button selectGradeButton;


    //SINGLE ENTRY (SEARCH & DELETE)
    Button backButton;
    Button searchButton;
    TextView titleView;
    EditText fieldPlantID;
    LinearLayout prompt;


    //EDIT ENTRY
    LinearLayout editView;
    AutoCompleteTextView accessionIDEdit2;
    EditText plotNoEdit2;
    EditText plantIDEdit2;
    Button okButton2;
    Button backButton3;



    //****************VARIABLES*********************

    String[] listAccNo = new String[] {"GRIF 14189","PI 115507","PI 115964","PI 116064","PI 116677","PI 163270","PI 164358","PI 164458","PI 164721","PI 169660","PI 169663","PI 175909","PI 179997","PI 180000","PI 182299","PI 182300"
            ,"PI 183356","PI 188816","PI 193599","PI 212627","PI 213191","PI 224690","PI 226529","PI 227254","PI 251506","PI 279873","PI 320502","PI 362727","PI 381173","PI 381272","PI 381275","PI 381276"
            ,"PI 386008","PI 386257","PI 386266","PI 386269","PI 419158","PI 478377","PI 508503","PI 593817","PI 593842","PI 593851","PI 593858","PI 593862","PI 593869","PI 595220","PI 639118","PI 639119"
            ,"PI 639120","PI 639125","JP 33220","JP 33232","JP 33234","JP 33237","JP 33242","JP 33263","JP 33265","JP 33267","JP 33276","JP 33292","JP 33295","JP 33328","JP 33351","JP 33365"
            ,"JP 33398","JP 33400","JP 33405","JP 33406","JP 33409","JP 33415","JP 33419","JP 33421","JP 33453","JP 68151","JP 68152","JP 68156","JP 68157","JP 68158","JP 68159","JP 68160"
            ,"JP 68165","JP 68173","JP 71135","JP 71136","JP 71137","JP 71138","JP 71140","JP 71142","JP 71143","JP 72034","JP 72036","JP 72037","JP 72038","JP 74225","JP 74228","JP 74234"
            ,"JP 74236","11633","12777","12775","152268","152308","152307","152309","152310","152558","152589","152306","PHL 8703","CASINO 901","CHECKMATE","LIGHTNING"
            ,"MAYUMI","PHL 12038","PHL 12053","PHL 12058","PHL 12157","PHL 12232","PHL 1353","PHL 1519","PHL 1579","PHL 1602","PHL 16039","PHL 1620","PHL 1625","PHL 2778","PHL 2791","PHL 3117"
            ,"PHL 3296","PHL 3367","PHL 3460","PHL 3577","PHL 4566","PHL 4600","PHL 4841","PHL 4874","PHL 5302","PHL 5307","PHL 5427","PHL 5447","PHL 5507","PHL 5709","PHL 5764","PHL 5834"
            ,"PHL 5855","PHL 5981","PHL 60102","PHL 6388","PHL 8818","PHL 9095","PHL 9382","PHL 9384","PHL 9386","PHL 9387","PHL 9388","PHL 9390","PHL 9391","PHL 9392","PHL 9393","PHL 9394"
            ,"PHL 9397","PHL 9405","PHL 9406","Abar","Balbalusa","DLP","Ilocos Green","Kirit","Mamburao","Mara","Concepcion","EG203","PHL 2789","PHL 3394","PHL 9044 - 1","PHL 3466"
            ,"PHL 5199","PHL 5403","IPB Inbred 1","IPB Inbred 2","IPB Inbred 3","IPB Inbred 4","89-022","Alicia (KSC)","Batac Long Purple (KSC)","Black Beauty (Yates)","CA T1M PN-65","Eggplant Long Purple (Ramgo)","Violeta (KSC)","Violeta (KSHC)","GB 55061","GB 55130"
            ,"GB 55332","GB 55334","GB 55372","GB 55454","GB 55468","GB 55469","GB 55491","GB 55531","GB 55578","GB 55591","GB 55622","GB 55673","GB 55680","GB 55694","GB 55695","GB 56007"
            ,"GB 56012","GB 56015","GB 56016","GB 56016-A","GB 56021","GB 56046","GB 56048","GB 56060","GB 56076","GB 56079","GB 56082","GB 56095","GB 56102","GB 56105","GB 56116","GB 56129"
            ,"GB 56163","GB 56180","GB 56190","GB 56193-A","GB 56193-B","GB 59156","GB 59175","GB 59199","GB 59214","GB 59257","GB 59295","GB 59301","GB 59314","GB 59329","GB 59339","GB 59354"
            ,"GB 59356","GB 59395","GB 59407","GB 59469","GB 59477","GB 59722","GB 59734","GB 59735","GB 59736","GB 59738","GB 59749","GB 59795","GB 59868","GB 59870","GB 59902","GB 59995"
            ,"GB 60057","GB 60734","GB 60802","GB 60970","GB 61713","GB 62093","GB 63018","GB 63415","GB 63589","GB 63853","GB 63874","GB 63958","GB 64044","GB 64109","GB 64161","PHL 11991"
            ,"PHL 11995","PHL 12006","PHL 12188","PHL 12190-A","PHL 12190-B","PHL 12192","PHL 12192-B","PHL 12196","PHL 12197","PHL 12198","PHL 12203","PHL 12205","PHL 12212","PHL 12214-C","PHL 12220","PHL 12227-1"
            ,"PHL 12227-3","PHL 12227-A","PHL 12235-1","PHL 12235-2","PHL 12239","PHL 12241","PHL 12241-1","PHL 12242","PHL 12249","PHL 12254","PHL 12256-A","PHL 12259-A","PHL 12260","PHL 12261","PHL 12263","PHL 12266"
            ,"PHL 12272","PHL 12273","PHL 12274","PHL 12277","PHL 12278-1","PHL 12280","PHL 12288","PHL 12290","PHL 12293","PHL 12294","PHL 12296","PHL 12302","PHL 12329","PHL 12333","PHL 21538","PHL 5909"
            ,"PHL 5910","PHL 5932","PHL 5933","PHL 5951","PHL 5962","PHL 5976","PHL 5983","PHL 5988","PHL 6004","PHL 6049","PHL 6066","PHL 6080","PHL 6091","PHL 6105","PHL 6105-A","PHL 6122"
            ,"PHL 6123","PHL 6127","PHL 6346","PHL 6417","PHL 7010","PHL 7074","PHL 7075","PHL 7188","PHL 8257","PHL 8319","PHL 8355","PHL 8422","PHL 8479","PHL 8514","PHL 8682","PHL 8790"
            ,"PHL 8823","PHL 8888","PHL 8971","PHL 9044","PHL 9050","PHL 9064","PHL 9075","PHL 9095","PHL 9343","PHL 9399","GB-00224","GB-00225","GB-00226","GB-00227","GB-00228","GB-00229"
            ,"GB-00230","GB-00231","GB-00232","GB-00233","GB-00254","GB-00255","GB-00256","GB-00257","GB-00258","GB-00259","GB-00260","GB-00261","GB-00262","GB-00263","PH-0014","PH-0030"
            ,"PH-01850","PH-02026","PH-02822","PH-03025","PH-04610","PH-04698","PH-05306","PH-07806","PH-07807","PH-09686","PH-09717","PH-11424","PH-12730","PH-12981","PH-14941","PH-14944"
    };


    private static final int PERMISSION_CODE = 1000;
    int width;
    int height;

    double rectWidth=0;
    double rectHeight=0;
    int antho=0;

    int isEdit=0;
    int isGallery=0;
    int isOption=0;
    int isCharacter=0;
    int isBack=0;
    int isResult=0;
    String searchInput;

    String strAccNo;
    String strPlantID;
    String strPlotNo;


    //****************DATABASE**********************
    DatabaseHelper myDB = new DatabaseHelper(this);



    //*****************CLASS FOR ADAPTER************************
    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String rTitle[];
        String rDesc[];
        int rLogos[];

        MyAdapter (Context c, String title[], String desc[], int logos[]){
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDesc = desc;
            this.rLogos = logos;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row,parent,false);
            ImageView image = row.findViewById(R.id.image);
            TextView title = row.findViewById(R.id.textView1);
            TextView desc = row.findViewById(R.id.textView2);



            image.setImageResource(rLogos[position]);
            title.setText(rTitle[position]);
            desc.setText(rDesc[position]);

            return row;
        }
    }



    //********************METHOOOOOOOOOOOOODS***********************************************
    @Override
    public boolean onKeyDown(int key_code, KeyEvent key_event) {
        if (key_code== KeyEvent.KEYCODE_BACK) {
            super.onKeyDown(key_code, key_event);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        OpenCVLoader.initDebug();

        directory = (TextView)findViewById(R.id.textView3);

        startPage = (RelativeLayout)findViewById(R.id.startPage);
        startButton = (Button)findViewById(R.id.button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPage.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
        });

        //**********ADD's UI***************
        cameraView = (LinearLayout)findViewById(R.id.linearlayout3);
        titleView2 = (TextView)findViewById(R.id.textView8);
        plotNoEdit = (EditText) findViewById(R.id.editText8);
        accessionIDEdit = (AutoCompleteTextView) findViewById(R.id.editText4);
        accessionIDEdit.setAdapter(new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, listAccNo));
        plantIDEdit = (EditText) findViewById(R.id.editText9);
        InputMethodManager inputMethodManager = (InputMethodManager) HomeActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if(HomeActivity.this.getCurrentFocus() != null)
        {
            inputMethodManager.hideSoftInputFromWindow(HomeActivity.this.getCurrentFocus().getWindowToken(), 0);
        }
        okButton = (Button)findViewById(R.id.button26);
        imageView = (ImageView)findViewById(R.id.imageView2);
        captureButton = (ImageButton)findViewById(R.id.imageButton2);
        galleryButton = (Button)findViewById(R.id.button27);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGallery = 1;
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
        backButton2 = (Button)findViewById(R.id.button24);
        resultText = (TextView)findViewById(R.id.textView10);
        retakeButton = (Button)findViewById(R.id.button23);
        saveButton = (Button)findViewById(R.id.button19);

        selectGradeButton = (Button)findViewById(R.id.button25);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGallery=0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission((Manifest.permission.CAMERA)) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA};
                        requestPermissions(permission, PERMISSION_CODE);
                    }else{
                        System.out.println("ERROR");
                    }
                }else{
                    System.out.println("ERROR");
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isBack==1){
                    directory.setText("MENU");
                    isBack=0;
                    isCharacter=0;
                    plantIDEdit.setError(null);
                    plantIDEdit.setText(null);
                    plotNoEdit.setError(null);
                    plotNoEdit.setText(null);
                    accessionIDEdit.setText(null);
                    accessionIDEdit.setError(null);
                    cameraView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);

                    if(isResult==1){
                        imageView.setVisibility(View.GONE);
                        titleView2.setText("ENTER THE FOLLOWING:");
                    }

                    isResult=0;

                }else if(isBack==2){
                        directory.setText("MENU > ADD ENTRY > DIMENSION");

                    isBack=1;
                    imageView.setVisibility(View.GONE);
                    captureButton.setVisibility(View.GONE);
                    galleryButton.setVisibility(View.GONE);
                    directory.setVisibility(View.VISIBLE);
                    titleView2.setText("ENTER THE FOLLOWING:");
                    plantIDEdit.setVisibility(View.VISIBLE);
                    plotNoEdit.setVisibility(View.VISIBLE);
                    accessionIDEdit.setVisibility(View.VISIBLE);
                    okButton.setVisibility(View.VISIBLE);
                    backButton2.setVisibility(View.VISIBLE);

                }else if(isBack==3){
                    isBack=1;
                    resultText.setVisibility(View.GONE);
                    retakeButton.setVisibility(View.GONE);
                    saveButton.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    titleView2.setText("ENTER THE FOLLOWING:");
                    plantIDEdit.setVisibility(View.VISIBLE);
                    plotNoEdit.setVisibility(View.VISIBLE);
                    accessionIDEdit.setVisibility(View.VISIBLE);
                    okButton.setVisibility(View.VISIBLE);

                    //***********imageview ibalik sa default
                }

            }
        });

        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    rectHeight = 0;
                    rectWidth = 0;
                    titleView2.setText("LEAF DIMENSION");



                resultText.setVisibility(View.GONE);
                retakeButton.setVisibility(View.GONE);
                saveButton.setVisibility(View.GONE);
                backButton2.setVisibility(View.GONE);

                //imageView ibalik sa default
                imageView.setImageResource(R.drawable.ic_image_black_24dp);
                captureButton.setVisibility(View.VISIBLE);

            }
        });

        selectGradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment singleChoiceDialog = new SingleChoiceDialog();
                singleChoiceDialog.setCancelable(false);
                singleChoiceDialog.show(getSupportFragmentManager(), "Single Choice Dialog");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strPlantID != null && strAccNo != null && strPlotNo != null) {

                        int noOfEntries = myDB.totalNumber();
                        addData(strPlantID, strAccNo, strPlotNo, rectWidth, rectHeight, noOfEntries);

                    isResult=0;
                    imageView.setImageResource(R.drawable.ic_image_black_24dp);
                    plantIDEdit.setText(null);
                    plotNoEdit.setText(null);
                    accessionIDEdit.setText(null);
                    resultText.setText(null);
                    resultText.setVisibility(View.GONE);
                    retakeButton.setVisibility(View.GONE);
                    saveButton.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
                    cameraView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });

        //**************SEARCH's UI***********************
        backButton = (Button)findViewById(R.id.button15);
        searchButton = (Button)findViewById(R.id.button14);
        titleView = (TextView)findViewById(R.id.textView5);
        fieldPlantID = (EditText)findViewById(R.id.editText7);
        prompt = (LinearLayout)findViewById(R.id.linearlayout2);


        //**************EDIT's UI***********************
        editView  = (LinearLayout)findViewById(R.id.linearlayout4);
        plotNoEdit2 = (EditText) findViewById(R.id.editText88);
        accessionIDEdit2 = (AutoCompleteTextView) findViewById(R.id.editText44);
        accessionIDEdit2.setAdapter(new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, listAccNo));
        plantIDEdit2 = (EditText) findViewById(R.id.editText99);
        okButton2 = (Button)findViewById(R.id.button266);
        okButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    boolean isUpdate = myDB.updateEntry(plantIDEdit2.getText().toString(),accessionIDEdit2.getText().toString(),plotNoEdit2.getText().toString());
                    if(isUpdate==true){
                        Toast.makeText(HomeActivity.this,"Successfully edited an entry!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(HomeActivity.this,"Something went wrong!", Toast.LENGTH_LONG).show();

                    }
                    plantIDEdit2.setError(null);
                    plantIDEdit2.setText(null);
                    accessionIDEdit2.setError(null);
                    accessionIDEdit2.setText(null);
                    plotNoEdit2.setError(null);
                    plotNoEdit2.setText(null);
                    editView.setVisibility(View.GONE);
                    prompt.setVisibility(View.VISIBLE);
            }
        });
        backButton3 = (Button)findViewById(R.id.button244);
        backButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantIDEdit2.setError(null);
                plantIDEdit2.setText(null);
                accessionIDEdit2.setError(null);
                accessionIDEdit2.setText(null);
                plotNoEdit2.setError(null);
                plotNoEdit2.setText(null);
                editView.setVisibility(View.GONE);
                prompt.setVisibility(View.VISIBLE);
            }
        });

        //***********HOME PAGE*****************
        listView= (ListView)findViewById(R.id.listview);
        MyAdapter myAdapter = new MyAdapter(this, title, desc, logos);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.GONE);
                //listView2.setVisibility(View.VISIBLE);
                directory.setVisibility(View.VISIBLE);
                if(position==0){
                    isOption=1; //add
                    directory.setText("MENU > ADD ENTRY");
                    cameraView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.ic_image_black_24dp);
                    isBack=1;
                    okButton.setVisibility(View.VISIBLE);
                    backButton2.setVisibility(View.VISIBLE);
                    titleView2.setText("ENTER THE FOLLOWING:");
                    plantIDEdit.setVisibility(View.VISIBLE);
                    plotNoEdit.setVisibility(View.VISIBLE);
                    accessionIDEdit.setVisibility(View.VISIBLE);
                }else if(position==1){
                    isOption=2; //search
                    directory.setText("MENU > SEARCH ENTRY");
                    listView3.setVisibility(View.VISIBLE);

                }else if(position==2){
                    isOption=4; //delete
                    directory.setText("MENU > EXPORT RECORDS");
                    checkPermissions();
                    //if(isCharacter==1){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new ExportDatabaseCSVTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }else{
                            new ExportDatabaseCSVTask().execute();
                        }

                    directory.setText("MENU");
                    listView.setVisibility(View.VISIBLE);

                }else if(position==3){
                   isOption=3; //export
                    directory.setText("MENU > CLEAR ALL RECORDS");
                    if(myDB.isDBEmpty()){
                        Toast.makeText(HomeActivity.this,"Database is EMPTY =(", Toast.LENGTH_LONG).show();
                        listView.setVisibility(View.VISIBLE);
                    }else{
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage("Are you sure you want to DELETE ALL entries under DIMENSION?")
                                .setCancelable(false)
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
                                        builder.setMessage("Are you REALLY sure you want to DELETE ALL entries under DIMENSION?")
                                                .setCancelable(false)
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
                                                        builder.setMessage("Are you REALLY REALLY sure you want to DELETE ALL entries under DIMENSION?")
                                                                .setCancelable(false)
                                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        myDB.deleteListContents();
                                                                        Toast.makeText(HomeActivity.this,"Successfully deleted ALL the records!", Toast.LENGTH_LONG).show();
                                                                    }
                                                                })
                                                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.cancel();
                                                                    }
                                                                });
                                                        android.app.AlertDialog alert = builder.create();
                                                        alert.setTitle("FINAL ALERT");
                                                        alert.show();
                                                    }
                                                })
                                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        android.app.AlertDialog alert = builder.create();
                                        alert.setTitle("ALERT 2");
                                        alert.show();
                                        }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.setTitle("ALERT");
                        alert.show();
                    }
                    listView.setVisibility(View.VISIBLE);
                    directory.setText("MENU");
                }


            }
        });






        //***************SEARCH SPECIFIC OPTIONS******************************************
        listView3= (ListView)findViewById(R.id.listview3);
        MyAdapter myAdapter3 = new MyAdapter(this, title3, desc3, logos3);
        listView3.setAdapter(myAdapter3);
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){    //back to listView2
                    listView3.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE); //listView2
                    isCharacter=0;
                    directory.setText("MENU");
                }else if(position==1){      //search AN entry
                    directory.setVisibility(View.GONE);
                    //if(isCharacter==1){
                        if(myDB.isDBEmpty()) {
                            Toast.makeText(HomeActivity.this, "Database is EMPTY =(", Toast.LENGTH_LONG).show();
                        }else if(!myDB.isDBEmpty()){
                            listView3.setVisibility(View.GONE);
                            prompt.setVisibility(View.VISIBLE);
                            titleView.setText("SEARCH >>> DIMENSION");
                        }



                }else if(position==2){      //view ALL entries
                        if(myDB.isDBEmpty()){
                            Toast.makeText(HomeActivity.this,"Database is EMPTY =(", Toast.LENGTH_LONG).show();
                            listView3.setVisibility(View.VISIBLE);
                        }else{
                            Intent intent = new Intent(HomeActivity.this, ViewListContents.class);
                            startActivity(intent);
                        }

                }
            }
        });

    }

    @Override
    public void onBackPressed() { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        Bitmap imageBitmap = null;
        if(isGallery==1){
            if (requestCode == 1) {

                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        try{
                            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            //imageBitmap.compress()
                            //use the bitmap as you like

                          }catch(IOException e){
                            e.printStackTrace();
                          }

                    }
                }

            }
        }else{
            imageBitmap = (Bitmap)data.getExtras().get("data");
        }

        imageView.setImageBitmap(imageBitmap);


        Mat rgba = new Mat();
        height = 0;
        width = 0;

        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inDither = false;
        option.inSampleSize = 4;

        height = imageBitmap.getHeight();
        width = imageBitmap.getWidth();

        Utils.bitmapToMat(imageBitmap, rgba);


        isResult=1;
        isBack=3;

            getDimension(imageView,rgba);
            captureButton.setVisibility(View.GONE);
            galleryButton.setVisibility(View.GONE);
            backButton2.setVisibility(View.VISIBLE);
            titleView2.setText("RESULT");
            resultText.setVisibility(View.VISIBLE);
            retakeButton.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(HomeActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // all permissions are granted.
                } else {

                    //permissions missing

                }
                return;
            }
        }
    }

    private void ShareFile(int isChar) {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "/codesss/");
        String fileName="";
        if(isChar==1){
            fileName = "Dimension.csv";
        }else if(isChar==2){
            fileName = "Anthocyanin_Content.csv";
        }

        File sharingGifFile = new File(exportDir, fileName);
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("application/csv");
        Uri uri = Uri.fromFile(sharingGifFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share CSV"));
    }

    public class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> {

        private final ProgressDialog dialog = new ProgressDialog(HomeActivity.this);
        DatabaseHelper dbhelper;
        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Exporting database...");
            this.dialog.show();
            dbhelper = new DatabaseHelper(HomeActivity.this);
        }

        protected Boolean doInBackground(final String... args) {

            File exportDir = new File(Environment.getExternalStorageDirectory(), "/CSV/");
            if (!exportDir.exists()) { exportDir.mkdirs(); }

            File file = new File(exportDir, "Dimension.csv");
            try {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                Cursor curCSV = dbhelper.raw();
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext()) {
                    String arrStr[]=null;
                    String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                    for(int i=0;i<curCSV.getColumnNames().length;i++)
                    {
                        mySecondStringArray[i] =curCSV.getString(i);
                    }
                    csvWrite.writeNext(mySecondStringArray);
                }
                csvWrite.close();
                curCSV.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        protected void onPostExecute(final Boolean success) {
            if (this.dialog.isShowing()) { this.dialog.dismiss(); }
            if (success) {
                Toast.makeText(HomeActivity.this, "Export successful!", Toast.LENGTH_SHORT).show();

                //ShareGif();
            } else {
                Toast.makeText(HomeActivity.this, "Export failed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void okHandler(View view){
        directory.setVisibility(View.GONE);

        if(TextUtils.isEmpty(plantIDEdit.getText()) || TextUtils.isEmpty(plotNoEdit.getText()) || TextUtils.isEmpty(accessionIDEdit.getText())) {
            if(TextUtils.isEmpty(accessionIDEdit.getText())){
                accessionIDEdit.setError("Accession no. is required!");
            }else if(TextUtils.isEmpty(plantIDEdit.getText())) {
                plantIDEdit.setError("Plant ID is required!");
            }else if(TextUtils.isEmpty(plotNoEdit.getText())) {
                plantIDEdit.setError("Plot no. is required!");
            }

        }else if((/*==1 &&*/ !myDB.isDBEmpty() && myDB.colExists(plantIDEdit.getText().toString())==true)){
                plantIDEdit.setError("Plant ID exists already!");

        }else{
            strAccNo = accessionIDEdit.getText().toString();
            strPlantID = plantIDEdit.getText().toString();
            strPlotNo = plotNoEdit.getText().toString();



            okButton.setVisibility(View.GONE);
            accessionIDEdit.setVisibility(View.GONE);
            plantIDEdit.setVisibility(View.GONE);
            plotNoEdit.setVisibility(View.GONE);

            if(isResult==1){
                isBack=3;
                titleView2.setText("RESULT");
                imageView.setVisibility(View.VISIBLE);
                resultText.setVisibility(View.VISIBLE);
                saveButton.setVisibility(View.VISIBLE);
                retakeButton.setVisibility(View.VISIBLE);
            }else{
                isBack=2;
                captureButton.setVisibility(View.VISIBLE);
                //galleryButton.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);

                    titleView2.setText("LEAF DIMENSION");
                    //********IMAGE PROCESSING METHOD CALL for getting dimension

            }

        }

    }



    @Override
    public void onPositiveButtonClicked(String[] list, int position) {
        if(position ==0) {
            antho = 1;
        }else if(position ==1){
            antho = 3;
        }else if(position ==2){
            antho = 5;
        }else if(position ==3){
            antho = 7;
        }else if(position ==4){
            antho = 9;
        }
        resultText.setText("Anthocyanin coloration: " + String.format("%d",antho));
        selectGradeButton.setVisibility(View.GONE);
        resultText.setVisibility(View.VISIBLE);
        retakeButton.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
        backButton2.setVisibility(View.VISIBLE);

    }

    public void addData(String plantID, String accNo, String plotNo, double width, double length, int noOfEntries){
        //if(plantID!=null && accNo!=null){
           boolean insertData = myDB.addData(plantID,accNo,plotNo,width,length, noOfEntries);
            if(insertData==true){
                Toast.makeText(HomeActivity.this,"Successfully added to the record!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(HomeActivity.this,"Something went wrong; entry not added :(", Toast.LENGTH_LONG).show();
            }
        //}

    }


    private String SaveImage(Bitmap finalBitmap) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("output", Context.MODE_PRIVATE);
        File mypath=new File(directory,"output-" + strPlantID + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private String SaveImage3(Bitmap finalBitmap) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("input", Context.MODE_PRIVATE);
        File mypath=new File(directory,"input-"+strPlantID + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private String SaveImage2(Bitmap finalBitmap, String name) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("processing", Context.MODE_PRIVATE);
        File mypath=new File(directory,name+"-"+strPlantID+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public void getDimension(View v, Mat rgb) {
        Bitmap grayBitmap3 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
        Utils.matToBitmap(rgb, grayBitmap3);    //contours
        SaveImage3(grayBitmap3);

        Mat lab = new Mat();
        Imgproc.cvtColor(rgb, lab, Imgproc.COLOR_RGB2Lab);

//        Bitmap grayBitmap4 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(lab, grayBitmap4);    //contours
//        SaveImage2(grayBitmap4,"lab");

        List<Mat> channels2 = new ArrayList<Mat>();
        Core.split(lab, channels2);
        Mat a = channels2.get(1);
        Mat b = channels2.get(2);

        CLAHE clahe3 = Imgproc.createCLAHE(0.01, new org.opencv.core.Size(4,4)); //4
        Mat claheMat3 = new Mat();
        clahe3.apply(channels2.get(0), claheMat3);

        claheMat3.copyTo(channels2.get(0));

        Core.merge(channels2,lab);
//        Bitmap grayBitmap5 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(lab, grayBitmap5);    //contours
//        SaveImage2(grayBitmap5,"labclahe");

        Mat rgb3 = new Mat();
        Imgproc.cvtColor(lab, rgb3, Imgproc.COLOR_Lab2RGB);


        Mat yub = new Mat();
        Imgproc.cvtColor(rgb3, yub, Imgproc.COLOR_RGB2YUV);

//        Bitmap grayBitmap6 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(yub, grayBitmap6);    //contours
//        SaveImage2(grayBitmap6,"yuv");

        List<Mat> channels = new ArrayList<Mat>(3);
        Core.split(yub, channels);
       // Mat y = channels.get(0);

        Mat equ2 = new Mat();
        Imgproc.equalizeHist(channels.get(0), equ2);

        Core.merge(channels, yub);

//        Bitmap grayBitmap7 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(yub, grayBitmap7);    //contours
//        SaveImage2(grayBitmap7,"yuvequ");

        Mat rgb2 = new Mat();
        Imgproc.cvtColor(yub, rgb2, Imgproc.COLOR_YUV2BGR);



        Mat grayMat3 = new Mat();
        Imgproc.cvtColor(rgb2, grayMat3, Imgproc.COLOR_RGB2GRAY);

//        Bitmap grayBitmap8 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(grayMat3, grayBitmap8);    //contours
//        SaveImage2(grayBitmap8,"grayscale");

        Mat otsuMat2 = new Mat();
        Imgproc.threshold(grayMat3, otsuMat2,  0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY_INV);  //THRESH VALUE BAGUHIN

//        Bitmap grayBitmap9 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(otsuMat2, grayBitmap9);    //contours
//        SaveImage2(grayBitmap9,"otsu");

        Mat openMat = new Mat();
        Mat closeMat = new Mat();
        Mat kernel = new Mat(new Size(3, 3), CvType.CV_8UC1, new Scalar(255));
        Imgproc.morphologyEx(otsuMat2, openMat, Imgproc.MORPH_OPEN, kernel);
//        Bitmap grayBitmap10 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(openMat, grayBitmap10);    //contours
//        SaveImage2(grayBitmap10,"open");
        Imgproc.morphologyEx(openMat, closeMat, Imgproc.MORPH_CLOSE, kernel);
//        Bitmap grayBitmap11 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(closeMat, grayBitmap11);    //contours
//        SaveImage2(grayBitmap11,"close");

        Mat hierarchy = new Mat();
        Mat cannyEdges = new Mat();
        List<MatOfPoint> contourList = new ArrayList<MatOfPoint>();


        Imgproc.Canny(closeMat, cannyEdges, 10, 100);
        Bitmap grayBitmap12= Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
        Utils.matToBitmap(cannyEdges, grayBitmap12);    //contours
        SaveImage2(grayBitmap12,"canny");
        Imgproc.findContours(cannyEdges, contourList, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);


        Mat contours = new Mat();
        contours.create(cannyEdges.rows(), cannyEdges.cols(), CvType.CV_8UC3);

        for (int i = 0; i < contourList.size(); i++) {
            Imgproc.drawContours(contours, contourList, i,new Scalar(0, 255, 0), -1);
        }

        Bitmap grayBitmap13 = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
        Utils.matToBitmap(contours, grayBitmap13);    //contours
        SaveImage2(grayBitmap13,"contours");

        Rect rect = new Rect();
        Rect rectLargest = new Rect();

        double tempWidth=0;
        double tempHeight=0;
        double refWidth=0;
        double currArea =0;
        double largestArea = 0;
        int indexLargest = 0;

        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        MatOfPoint2f approx = new MatOfPoint2f();
        int isRef = 0;
        int indRef = 0;

        for(int i=0 ; i < contourList.size(); i++) {
            rect = Imgproc.boundingRect(contourList.get(i));
            MatOfPoint contour = contourList.get(i);
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(matOfPoint2f, approx, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long count = approx.total();

            if (count == 4) {
                if (isRef != 1){
                    Imgproc.rectangle(rgb, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0));
                    refWidth = rect.height;
                    indRef = i;
                    break;
                }
            }
        }

        for(int i=0 ; i < contourList.size(); i++) {
            rect = Imgproc.boundingRect(contourList.get(i));
            MatOfPoint contour = contourList.get(i); //
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(matOfPoint2f, approx, Imgproc.arcLength(matOfPoint2f, true) * 0.02, true);
            long count = approx.total();
            currArea = rect.area();

            if (currArea > largestArea && count != 4 && i != indRef){
                largestArea = currArea;
                indexLargest = i;
                break;
            }
        }

        rectLargest = Imgproc.boundingRect(contourList.get(indexLargest));
        Imgproc.rectangle(rgb, new Point(rectLargest.x, rectLargest.y), new Point(rectLargest.x + rectLargest.width, rectLargest.y + rectLargest.height),new Scalar(0, 255, 0));
        tempHeight = rectLargest.height;
        tempWidth = rectLargest.width;

        rectWidth = ((tempWidth/refWidth)*10.16);
        rectHeight =((tempHeight/refWidth)*10.16);

        double temp = 0;
        if(rectWidth > rectHeight){
            temp = rectHeight;
            rectHeight = rectWidth;
            rectWidth = temp;
            temp = 0;
        }

        resultText.setText("Width:" + String.format("%.1f", rectWidth) +" cm \nHeight: " + String.format("%.1f", rectHeight) + " cm");
        Bitmap grayBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565);
        Utils.matToBitmap(rgb, grayBitmap);    //contours
        SaveImage(grayBitmap);
        imageView.setImageBitmap(grayBitmap);
    }



    public void backHandler(View view){
        fieldPlantID.setError(null);
        fieldPlantID.setText(null);
        directory.setVisibility(View.VISIBLE);
        if(isOption==2){
            prompt.setVisibility(View.GONE);
            listView3.setVisibility(View.VISIBLE);
//        }else if(isOption==3){
//            prompt.setVisibility(View.GONE);
//            listView4.setVisibility(View.VISIBLE);
        }
    }



    public void searchHandler(View view){
        if(TextUtils.isEmpty(fieldPlantID.getText())) {
            fieldPlantID.setError("Plant ID is required!");
        }else{
            searchInput = fieldPlantID.getText().toString();
            if(isOption==2) {
                Cursor data = null;
                //if (isCharacter == 1) {
                   if(myDB.colExists(searchInput)==false){
                      Toast.makeText(HomeActivity.this, "Does NOT exist =(", Toast.LENGTH_LONG).show();
                   } else if(myDB.colExists(searchInput)==true){
                        data = myDB.searchAnEntry(searchInput);
                        while (data.moveToNext()) {
                            String pID = data.getString(0);
                            final String accNo = data.getString(1);
                            final String plotNo = data.getString(2);
                            String wid = data.getString(3);
                            String len = data.getString(4);
                            String date = data.getString(5);
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
                            builder.setMessage("PLANT ID.: "+ pID +"\nACCESSION NO.: "+ accNo+"\nPLOT NO.: "+ plotNo + "\nLEAF WIDTH: "+ wid + " cm\nLEAF HEIGHT: "+ len +" cm" + "\nDATE: " + date)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
                                            builder.setMessage("Are you sure you want to DELETE this entry?")
                                                    .setCancelable(false)
                                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            myDB.deleteAnEntry(searchInput);
                                                            Toast.makeText(HomeActivity.this,"Successfully deleted an entry!", Toast.LENGTH_LONG).show();
                                                            prompt.setVisibility(View.GONE);
                                                            listView3.setVisibility(View.VISIBLE);
                                                        }
                                                    })
                                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            android.app.AlertDialog alert = builder.create();
                                            alert.setTitle("ALERT");
                                            alert.show();

                                        }
                                    })
                                    .setNegativeButton("EDIT", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            editView.setVisibility(View.VISIBLE);
                                            //plantIDEdit2.setFocusable(false);
                                            plantIDEdit2.setText(searchInput);
                                            accessionIDEdit2.setText(accNo);
                                            plotNoEdit2.setText(plotNo);
                                        }
                                    });
                            android.app.AlertDialog alert = builder.create();
                            alert.setTitle("INFO");
                            alert.show();
                        }
                    }
            }
            fieldPlantID.setText(null);
            listView.setVisibility(View.VISIBLE);
        }

    }

}
