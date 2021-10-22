package fr.cours.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import fr.cours.myapplication.Adapteur.Recycler;
import fr.cours.myapplication.Adapteur.TrucAdapteur;
import fr.cours.myapplication.BO.Article;
import fr.cours.myapplication.BO.Truc;
import fr.cours.myapplication.Fragment.MonFragment;
import fr.cours.myapplication.Fragment.MonFragmentDeux;
import fr.cours.myapplication.dao.AppDataBase;
import fr.cours.myapplication.dao.ConnectionRoom;
import fr.cours.myapplication.services.MyService;


public class MainActivity extends AppCompatActivity implements MonFragment.OnFragmentInteractionListener, ServiceConnection {

    private static final String TAG = "CUTcut";
    private ActivityResultLauncher<Intent> arl;
    private Article article = new Article();
    private String nomArticle;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Article> articles;

    public static final String CLE_INTRA = "intra";
    public static final String CLE_INTER = "inter";
    public static final String NOM_FICHIER = "inter";
    public static final String FICHIER = "fichier.txt";
    public static final String CONTENUE = "exemple de texte dans le fichier";

    Button btnHandler = null;
    ProgressBar pb = null;

    private final static String JSON = "{\"Pays\":"+"[\"France\","+"\"Suisse\"]}";

    private WebView webView = null;

    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG,"Nous sommes dans la fonction onCreate");
        Log.e(TAG,"erreur");
        Log.d(TAG,"debug");
        Log.v(TAG,"verbeuse");
        Log.w(TAG,"warning");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHandler = findViewById(R.id.btn_achete);
        pb = findViewById(R.id.pd_demo);

        androidx.appcompat.widget.Toolbar myToolBar = findViewById(R.id.toolBar);
        setSupportActionBar(myToolBar);

        // ..........................................//

        mRecyclerView = findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        articles = new ArrayList<>();

        articles.add(new Article("Chocolatine","1.00","pour les amoureux de chocolat",4.0F));
        articles.add(new Article("Croissant","0.80","pour les amoureux de beurre",5.0F));
        articles.add(new Article("Chouquette","0.20","pour les petit creux",4.5F));
        articles.add(new Article("Sablé","1.00","Comme le lemblas",2.5F));

        mAdapter = new Recycler(articles);
        mRecyclerView.setAdapter(mAdapter);

        // ..........................................//

        arl = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                            Intent data = result.getData();
                            resultActivity(data,result.getResultCode());
                        }else {
                            Toast.makeText(MainActivity.this, "Error ...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // ..........................................//

        try {
            JSONObject jsonObject = new JSONObject(JSON);
            JSONArray pays = jsonObject.getJSONArray("Pays");
            for (int i =0 ; i<pays.length();i++){
                Log.i(TAG, "Json : "+ pays.get(i));
               // JSONObject object = pays.getJSONObject(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // ..........................................//

        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);

        IMapController mapController = map.getController();
        mapController.setZoom(17.0);

        GeoPoint startPoint = new GeoPoint(49.135522,-0.295526);
        mapController.setCenter(startPoint);

        ArrayList<OverlayItem> items = new ArrayList<>();
        items.add(new OverlayItem("Eglise","Bel ouvrage",startPoint));

       ItemizedIconOverlay<OverlayItem> mOverlay = new ItemizedIconOverlay<>(this, items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                Log.i(TAG, "Simple clic");
                return false;
            }

            @Override
            public boolean onItemLongPress(int index, OverlayItem item) {
                Log.i(TAG, "Clic long");
                return false;
            }
        });

        mOverlay.setDrawFocusedItem(true);
        map.getOverlays().add(mOverlay);


        // ..........................................//

        webView = findViewById(R.id.Ma_Web_View);

        // cette ligne permet au lien utilisé Javscript car désactivé automatiquement par Webview
        webView.getSettings().setJavaScriptEnabled(true);

        //.......OPTION AVEC LIEN EXTERNE.......//
        webView.loadUrl("https://www.youtube.com/");

        //.......OPTION AVEC LIEN INTERNE.......//
        String contenu = "Page html";
        webView.loadData(contenu,"text/html","UTF-8");
                // si la page html contient du JavaScript
        webView.addJavascriptInterface(new JavaScriptPerso(this),"Android");

        // cette fonction permet de rester sur mon ihm et non allez sur le site
        webView.setWebViewClient(new WebViewClient(){
            @Override
            // si on clic sur un lien du site on reste dans la WebView
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                webView.loadUrl(request.getUrl().toString());
                return true;
            }
        });


    }

    // ..........................................//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_configuration:
                Toast.makeText(this, "Configuration", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_recherche:
                Toast.makeText(this, "Recherche", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    // ..........................................//

    // ...........^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^..............//
        //....cette fonction vas avec la WebView permet de revenir en arriere... //
        //.........si on as cliquer sur un lien dans le lien....................//
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){

            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class JavaScriptPerso{
        Context context = null;

        public JavaScriptPerso(Context context){
            this.context = context;
        }

        @JavascriptInterface
        public void showToastPerso(String saisie){
            Toast.makeText(context, "saisie", Toast.LENGTH_SHORT).show();
        }
    }

    // ..........................................//

    private void resultActivity(Intent data, int resultCode) {
        Log.d(TAG, "resultCode: " + resultCode);
        Log.d(TAG, "data: " + data.getStringExtra("info"));
    }

    @Override
    protected void onStart() {
        Log.i(TAG,"appeller aprés on create ou on restart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"en cours");
        super.onResume();

        List<Truc> listeTruc = new ArrayList<Truc>();

        listeTruc.add(new Truc("Machin","machin"));
        listeTruc.add(new Truc("Machin2","machin2"));
        listeTruc.add(new Truc("Machin3","machin3"));

        TrucAdapteur adapteur = new TrucAdapteur(this,R.layout.presentation_ligne,listeTruc);

       ListView liste = findViewById(R.id.ma_liste);
       liste.setAdapter(adapteur);

        // ..........................................//

       new Thread(new Runnable() {
           @Override
           public void run() {
               // recuperation ou création d'utilisateur

               AppDataBase bdd = ConnectionRoom.getConnexion(MainActivity.this);

               bdd.trucDAORoom().insert();

               List<Truc> list = bdd.trucDAORoom().getAll();

               for (Truc item : list){
                   Log.i(TAG, "run: "+ item);
               }

           }
       }).start();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "juste avant qu'une autre activité ne prenne le main et pendant onResume");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "arrete l'activité pendant la pause pour libéré de la memoire");
        super.onStop();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void onClickBouton(View view) {
        nomArticle = article.getNom();
        String urlArticle = article.getUrl();
        Toast.makeText(this, "ceci est un toast " + nomArticle, Toast.LENGTH_SHORT).show();
        Intent intention = new Intent(this,CourseActivity.class);
        intention.putExtra("urlArticle", urlArticle);
        startActivity(intention);
    }

    //.....................................................//

    public void onClickSms(View view) {

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE},14540);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        nomArticle = article.getNom();
        if (requestCode == 14540){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                SmsManager manager = SmsManager.getDefault();
                manager.sendTextMessage("0606060606",null,"Penser a acheter : " + nomArticle ,null,null);
            }
        }
    }

    //.....................................................//

    @Override
    public void onFragmentInteraction(Intent intent) {

        MonFragmentDeux MFD = (MonFragmentDeux) getSupportFragmentManager().findFragmentById(R.id.fragment_two);
        MFD.miseAJourTextView(intent.getStringExtra("phrase"));
    }

    //.....................................................//

    public void onClickHello(View view) {

        Intent intent = new Intent(this,HelloActivity.class);
       arl.launch(intent);

    }

    //.....................................................//

    public void onClickintra(View view) {

        EditText et = findViewById(R.id.et_intra);
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CLE_INTRA,et.getText().toString());
        editor.commit();

    }

    public void onClickinter(View view) {

        EditText et = findViewById(R.id.et_inter);
        SharedPreferences sp = getSharedPreferences(NOM_FICHIER,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CLE_INTER,et.getText().toString());
        editor.commit();
    }

    //.....................................................//

    public void onClickCibleActivity(View view) {
        Intent intent = new Intent(this,CibleActivity.class);
        startActivity(intent);
    }

    //.....................................................//

    private StringBuilder resultat = new StringBuilder();

    public void onClickRead(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FileInputStream fis = openFileInput(FICHIER);
                    byte[] buffer = new byte[1024];

                    while (fis.read(buffer) != -1){

                        resultat.append(new String(buffer));
                    }
                    fis.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Toast.makeText(MainActivity.this, resultat.toString(),Toast.LENGTH_SHORT).show();
    }

    public void onClickSave(View view) {

        // ici le Thread permet de faire un traitement long sans bloqué IHM
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FileOutputStream fos = openFileOutput(FICHIER,MODE_PRIVATE);
                    fos.write(CONTENUE.getBytes(StandardCharsets.UTF_8));
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //.....................................................//
    // utilisation d'un service distant
    public void onClickServerDistant(View view){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected()){

            // appel au services distant via un thread asinc
        }else {
            Toast.makeText(this, "Pas internet", Toast.LENGTH_SHORT).show();
        }

    }

    //.....................................................//
    // créer un demande de requette avec chargerSpinner() dans le if puis
    private void chargerSpinner(){

        List<Truc> list = new ArrayList<>();// ici mettre la class ex: friend
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null);

        while (cursor.moveToNext()){

            Truc truc = new Truc();
            truc.setId(Integer.valueOf(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))));
            truc.setLibelle(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
            list.add(truc);
        }
        ArrayAdapter ad = new ArrayAdapter<Truc>(this,R.layout.mon_spinner,list);
        Spinner spinner = findViewById(R.id.spinner_contact);
        spinner.setAdapter(ad);
    }

    //.....................................................//
    MonHandler handler = new MonHandler();

    public void onclickAchat(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msgDebut = new Message();
                msgDebut.what = 1 ;
                handler.sendMessage(msgDebut);

                for (int i = 0;i<=10;i++){

                    Message msgEnCours = new Message();
                    msgDebut.what = 2 ;
                    handler.sendMessage(msgEnCours);

                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        Log.e(TAG,"ERREUR : " + e.getMessage());
                    }
                }

                Message msgFin = new Message();
                msgDebut.what = 3 ;
                handler.sendMessage(msgFin);

            }
        }).start();
    }

    //.....................................................//
    // fonction connection au service unidirectionnelle
    public void onclickBtnService(View view){

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }

    @Override
    // fonction automatique lors de la connection au service en bidirectionnel
    public void onServiceConnected(ComponentName name, IBinder service) {

        String st = ((MyService)((MyService.MonBinder)service).getServices()).serviceSurMesure();

        // ce code ce met dans un btn
        Intent intent = new Intent(this, MyService.class);
        bindService(intent,this,BIND_AUTO_CREATE);

    }

    @Override
    // fonction automatique lors de la déconnection au service en bidirectionnel
    public void onServiceDisconnected(ComponentName name) {

        // ce code ce met dans un btn
        unbindService(this);
    }

    public void onClickBtnUn(View view) {

        Intent intent = new Intent();
        intent.setAction("fr.eni.event");
        sendBroadcast(intent);
        Log.i("ALB","onClickBtnUn" );

    }

    public void onClickBtnDeux(View view) {

        Intent intent = new Intent("fr.eni.event");
        sendBroadcast(intent);
        Log.i("ALB","onClickBtnDeux" );
    }


    // permet a differant thread de cummuniqué avec le thread principal
    class MonHandler extends Handler{

        public void handleMessage(Message msg){

            super.handleMessage(msg);

            switch (msg.what){

                case 1 : btnHandler.setEnabled(false);break;
                case 2 : pb.setProgress(msg.arg1);break;
                case 3 : btnHandler.setEnabled(true);break;
            }
        }
    }

    //.....................................................//

    public void onClickAsync(View view) {

        Async async = new Async();
        async.execute();
    }

    class Async extends AsyncTask<Void,Integer,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this,"c'est partie !!!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(Void... voids) {

            for (int i = 0;i<=10;i++){

                publishProgress(i);
                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    Log.e(TAG,"ERREUR : " + e.getMessage());
                }
            }

            return "c'est terminer";
        }

        protected String doInBackground(String... strings){

            HttpURLConnection httpURLConnection = null;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                URL url = new URL("https://google.fr"+ strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream is = httpURLConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int unChar;
                while ((unChar = isr.read()) != -1){

                    stringBuilder.append((char)unChar);
                }
                httpURLConnection.disconnect();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            pb.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb.setProgress(0);
            Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
        }
    }

}