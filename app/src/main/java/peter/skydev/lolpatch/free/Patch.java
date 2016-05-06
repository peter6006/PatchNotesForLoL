package peter.skydev.lolpatch.free;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import main.java.riotapi.RiotApiException;
import method.StaticDataMethod;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.LayoutParams;

public class Patch extends Activity {
//TODO Cuando haya una galeria en el parche, crear un scroll view horizontal con imagenes

    String lang, serv, url;
    String[] version;
    LayoutParams params;
    View linearLayout;
    Button bWeb;
    ImageView fullScreenContainer, buttonDownload;
    Bitmap bmpToDownload;
    String nameToDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch);

        params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        linearLayout = findViewById(R.id.parche);
        bWeb = (Button) findViewById(R.id.buttonWebViewParche);

        Bundle getData = getIntent().getExtras();
        lang = getData.getString("language");
        serv = getData.getString("server");
        buttonDownload = (ImageView) findViewById(R.id.buttonD);
        fullScreenContainer = (ImageView) findViewById(R.id.full_screen_container);
        fullScreenContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullScreenContainer.setImageDrawable(null);
                fullScreenContainer.setVisibility(View.GONE);
                buttonDownload.setVisibility(View.GONE);
            }
        });

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile(Patch.this, bmpToDownload, "");
            }
        });

        bWeb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        if (isNetworkAvailable(this)) {
            Load l = new Load();
            l.execute();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("No conexion")
                    .setMessage("You need internet conexion in order to read the patch.")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent myIntent = new Intent(Patch.this, MainActivity.class);
                            Patch.this.startActivity(myIntent);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void onBackPressed() {
        if (fullScreenContainer.getVisibility() == View.VISIBLE) {
            fullScreenContainer.setImageDrawable(null);
            fullScreenContainer.setVisibility(View.GONE);
            buttonDownload.setVisibility(View.GONE);
        } else {
            finish();
            Intent myIntent = new Intent(Patch.this, MainActivity.class);
            Patch.this.startActivity(myIntent);
        }
    }

    public static void saveFile(Context context, Bitmap b, String picName) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    class Load extends AsyncTask<String, String, String> {
        ProgressDialog progDailog;
        Document doc = null;
        Element content;
        boolean stop = false;
        String tittle, author;
        Bitmap[] arrSkins = new Bitmap[30];
        Bitmap[] arrIcons = new Bitmap[300];

        // Map<String, Bitmap> arr = new HashMap<String, Bitmap>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(Patch.this);
            progDailog.setMessage("Loading patch...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {

            try {
                try {
                    java.util.List<String> c3 = StaticDataMethod.getDataVersions(serv, "e3bb62be-8188-441b-89e2-4f4050215587");
                    boolean cont = true;
                    int i = 0;
                    String ver = ready(c3);
                    version = ver.split("\\.");
                } catch (RiotApiException e) {
                    e.printStackTrace();
                }


                url = "http://" + serv + ".leagueoflegends.com/" + lang + "/news/game-updates/patch/";
                switch (lang) {
                    case "es":
                        url = url + "notas-de-la-version-" + version[0] + version[1];
                        break;
                    case "en":
                        url = url + "patch-" + version[0] + version[1] + "-notes";
                        break;
                    case "it":
                        url = url + "note-sulla-patch-" + version[0] + version[1];
                        break;
                    case "de":
                        url = url + "patchnotizen-" + version[0] + version[1];
                        break;
                    case "fr":
                        url = url + "notes-de-patch-" + version[0] + version[1];
                        break;
                    case "el":
                        url = url + "simeioseis-enimerosis-" + version[0] + version[1];
                        break;
                    case "hu":
                        url = url + "legujabb-frissites-" + version[0] + version[1];
                        break;
                    case "pl":
                        url = url + "opis-patcha-" + version[0] + version[1];
                        break;
                    case "ro":
                        url = url + "notele-patch-ului-" + version[0] + version[1];
                        break;
                    case "cs":
                        url = url + "poznamky-k-aktualizaci-" + version[0] + version[1];
                        break;
                    case "ru":
                        url = url + "izmeneniya-obnovleniya-" + version[0] + version[1];
                        break;
                    case "tr":
                        url = url + version[0] + version[1] + "-yama-notlari";
                        break;
                    case "jp":
                        url = url + "patchnote" + version[0] + "-" + version[1];
                        break;
                    case "pt":
                        url = url + "notas-da-atualizacao-" + version[0] + version[1];
                        break;
                }

                doc = Jsoup.connect(url).timeout(10000).userAgent("Chrome").postDataCharset("utf-8").get();
                Elements links = doc.getElementsByClass("article-title");
                tittle = links.text();
                Elements links1 = doc.getElementsByClass("posted_by");
                author = links1.text();
                content = doc.getElementById("patch-notes-container");
                int i = 0;
                for (Element link : content.getAllElements()) {
                    if (link.className().startsWith("skins")) {
                        String auxS = link.attr("href").split(".jpg")[0].split("splash/")[1];
                        try {
                            File filePathP = getFileStreamPath(auxS);
                            if (!filePathP.exists()) {
                                URL url1 = new URL(link.attr("href"));
                                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                                connection1.connect();
                                String outputName = auxS;
                                InputStream input1 = connection1.getInputStream();

                                FileOutputStream output1 = openFileOutput(outputName, Context.MODE_PRIVATE);

                                int read1;
                                byte[] data1 = new byte[1024];
                                while ((read1 = input1.read(data1)) != -1)
                                    output1.write(data1, 0, read1);
                                output1.close();
                            } else {
                            }

                        } catch (Exception e) {
                        }
                    }
                    if (link.className().equals("patch-change-block white-stone accent-before")) {
                        String aux1 = null;
                        String aux2 = null;
                        for (Element link2 : link.getAllElements()) {
                            if (link2.className().equals("change-title")) {
                                aux1 = link2.text();
                            }
                            if (link2.className().equals("reference-link")) {
                                aux2 = link2.getElementsByTag("img").attr("src");
                            }
                        }
                        try {
                            File filePathP = getFileStreamPath(aux1);
                            if (!filePathP.exists()) {
                                URL url1 = new URL(aux2.split(".png")[0] + ".png");
                                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                                connection1.connect();
                                String outputName = aux1;
                                InputStream input1 = connection1.getInputStream();

                                FileOutputStream output1 = openFileOutput(outputName, Context.MODE_PRIVATE);

                                int read1;
                                byte[] data1 = new byte[1024];
                                while ((read1 = input1.read(data1)) != -1)
                                    output1.write(data1, 0, read1);
                                output1.close();
                            } else {
                                URL url1 = new URL(aux2.split(".png")[0] + ".png");
                            }

                        } catch (Exception e) {
                        }
                    }
                    if (link.className().equals("my-carousel")) {
                        String aux1 = null;
                        String aux2 = null;
                        for (Element link2 : link.getAllElements()) {
                            if (link2.className().equals("lightbox cboxElement")) {
                                aux1 = link2.getElementsByTag("a").attr("href");
                                String a = aux1.split(".jpg")[0].split("/")[aux1.split(".jpg")[0].split("/").length - 1];
                                try {
                                    File filePathP = getFileStreamPath(a.toLowerCase() + "CAR.png");
                                    if (!filePathP.exists()) {

                                        URL url1 = new URL(aux1);

                                        HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                                        connection1.connect();
                                        String outputName = a.toLowerCase() + "CAR" + ".png";
                                        InputStream input1 = connection1.getInputStream();
                                        FileOutputStream output1 = openFileOutput(outputName, Context.MODE_PRIVATE);

                                        int read1;
                                        byte[] data1 = new byte[1024];
                                        while ((read1 = input1.read(data1)) != -1)
                                            output1.write(data1, 0, read1);
                                        output1.close();
                                    } else {
                                        URL url1 = new URL(aux1 + ".png");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                stop = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
            if (!stop) {
                System.out.println(arrSkins);
                int j = 0;
                int l = 0;
                bWeb.setVisibility(View.VISIBLE);
                try {
                    TextView tv2 = new TextView(Patch.this);
                    tv2.setText(tittle);
                    tv2.setTextSize(40);
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams2.setMargins(0, 5, 0, 0);
                    tv2.setLayoutParams(layoutParams2);
                    ((LinearLayout) linearLayout).addView(tv2);
                    TextView tv3 = new TextView(Patch.this);
                    tv3.setText(author);
                    tv3.setTextSize(20);
                    tv3.setTextColor(Color.parseColor("#ff0000"));
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams3.setMargins(0, 20, 0, 0);
                    tv3.setLayoutParams(layoutParams3);
                    ((LinearLayout) linearLayout).addView(tv3);
                    for (Element link : content.getAllElements()) {
                        if (link.className().contains("header-primary")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText(link.text());
                            tv.setTextSize(35);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(5, 20, 5, 5);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.className().equals("change-title")) {
                            ImageView imageView2 = new ImageView(Patch.this);
                            if (link.text().contains("/")) {

                            } else {
                                System.out.println("IMAGEN: " + link.text());
                                File filePathP = getFileStreamPath(link.text());
                                imageView2.setImageDrawable(Drawable.createFromPath(filePathP.toString()));
                                imageView2.setLayoutParams(new LayoutParams(64, 64));
                                TextView tv4 = new TextView(Patch.this);
                                tv4.setText(link.text());
                                if (!link.select("a[href]").attr("href").equals("")) {
                                    tv4.setTextColor(Color.parseColor("#4949a5"));
                                    final String url = link.select("a[href]").attr("href");
                                    tv4.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            startActivity(browserIntent);
                                        }
                                    });
                                }
                                tv4.setTextSize(24);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(15, 10, 15, 0);
                                tv4.setLayoutParams(layoutParams);
                                LinearLayout layout = new LinearLayout(Patch.this);
                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.removeAllViews();
                                layout.addView(imageView2, layoutParams);
                                layout.addView(tv4, layoutParams);
                                ((LinearLayout) linearLayout).addView(layout);
                            }
                        } else if (link.className().contains("change-detail-title")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText(link.text());
                            tv.setTextSize(20);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(30, 0, 30, 0);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.className().equals("attribute-change")) {
                            String te = "";
                            if (link.toString().contains("class=\"new\"")) {
                                Elements l2 = link.getElementsByClass("new");
                                te = link.text().replaceFirst(l2.text(), "");
                            } else if (link.toString().contains("class=\"removed\"")) {
                                Elements l2 = link.getElementsByClass("removed");
                                te = link.text().replaceFirst(l2.text(), "");
                            } else {
                                te = link.text();
                            }

                            TextView tv = new TextView(Patch.this);
                            tv.setText(te);
                            tv.setTextSize(18);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(45, 0, 45, 24);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.className().equals("blockquote context")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText("\"" + link.text() + "\"");
                            tv.setTextSize(13);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(45, 0, 45, 20);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.className().equals("summary")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText(link.text());
                            tv.setTextSize(14);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(35, 0, 35, 15);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.tagName().equals("ul") && !link.className().contains("arrow-bullets")) {
                            final Elements li = link.select("li");
                            for (int i = 0; i < li.size(); i++) {
                                TextView tv = new TextView(Patch.this);
                                tv.setText("·" + li.get(i).text());
                                tv.setTextSize(14);
                                if (!li.get(i).select("a[href]").attr("href").equals("")) {
                                    tv.setTextColor(Color.parseColor("#0000EE"));
                                    final String url = li.get(i).select("a[href]").attr("href");
                                    tv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            startActivity(browserIntent);
                                        }
                                    });
                                }
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(45, 0, 45, 24);
                                tv.setLayoutParams(layoutParams);
                                ((LinearLayout) linearLayout).addView(tv);
                            }
                        } else if (link.className().startsWith("skins")) {
                            final String auxS = link.attr("href").split(".jpg")[0].split("splash/")[1];
                            try {
                                TextView tv4 = new TextView(Patch.this);
                                tv4.setText(link.attr("title"));
                                tv4.setTextSize(20);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(20, 10, 20, 24);
                                tv4.setLayoutParams(layoutParams);

                                ImageView imageView = new ImageView(Patch.this);
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);

                                // Hay que arreglar esto
                                File f = getFileStreamPath(auxS);
                                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);

                                final Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                                        matrix, true);

                                File filePathP = getFileStreamPath(auxS);

                                imageView.setImageDrawable(Drawable.createFromPath(filePathP.toString()));
                                j++;
                                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                imageView.setPadding(0, -50, 0, 0);
                                ((LinearLayout) linearLayout).addView(tv4);
                                ((LinearLayout) linearLayout).addView(imageView);
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // However it is you load your images
                                        fullScreenContainer.setImageBitmap(bmp);
                                        fullScreenContainer.setVisibility(View.VISIBLE);
//                                        buttonDownload.setVisibility(View.VISIBLE);
                                        bmpToDownload = bmp;
                                        nameToDownload = auxS;
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (link.className().equals("my-carousel")) {

                            try {
                                HorizontalScrollView LL = new HorizontalScrollView(Patch.this);

                                LinearLayout topLinearLayout = new LinearLayout(Patch.this);
                                topLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                topLinearLayout.setPadding(10, 10, 10, 10);
                                Elements asd = link.select("a[href]");

                                String[] aqwe = asd.text().split(" ");
                                for (Element link2 : asd) {

                                    ImageView imageView = new ImageView(Patch.this);
                                    Matrix matrix = new Matrix();
                                    matrix.postRotate(90);

                                    final String a = link2.getElementsByTag("a").attr("href").split(".jpg")[0].split("/")[link2.getElementsByTag("a").attr("href").split(".jpg")[0].split("/").length - 1];

                                    File f = getFileStreamPath(a.toLowerCase() + "CAR" + ".png");
                                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                    Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
                                    final Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                                            matrix, true);

                                    File filePathP = getFileStreamPath(a.toLowerCase() + "CAR" + ".png");

                                    imageView.setImageDrawable(Drawable.createFromPath(filePathP.toString()));
                                    imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                    imageView.setPadding(0, 0, 50, 0);
                                    topLinearLayout.addView(imageView);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // However it is you load your images
                                            fullScreenContainer.setImageBitmap(bmp);
                                            fullScreenContainer.setVisibility(View.VISIBLE);
//                                            buttonDownload.setVisibility(View.VISIBLE);
                                            bmpToDownload = bmp;
                                            nameToDownload = a;
                                        }
                                    });
                                }
                                LL.addView(topLinearLayout);
                                ((LinearLayout) linearLayout).addView(LL);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (NullPointerException e1) {
                    Elements content = doc.getElementsByClass("section-wrapper section-wrapper-primary");
                    for (Element link : content) {
                        if (link.className().contains("header-primary")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText(link.text());
                            tv.setTextSize(40);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(5, 20, 5, 5);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.className().equals("change-title")) {
                            ImageView imageView2 = new ImageView(Patch.this);
                            imageView2.setImageBitmap(arrIcons[l]);
                            l++;
                            imageView2.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

                            TextView tv4 = new TextView(Patch.this);
                            tv4.setText(link.text());
                            tv4.setTextSize(28);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(15, 10, 15, 0);
                            tv4.setLayoutParams(layoutParams);
                            LinearLayout layout = new LinearLayout(Patch.this);
                            layout.setOrientation(LinearLayout.HORIZONTAL);
                            layout.removeAllViews();
                            layout.addView(imageView2, layoutParams);
                            layout.addView(tv4, layoutParams);
                            ((LinearLayout) linearLayout).addView(layout);
                        } else if (link.className().contains("change-detail-title")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText(link.text());
                            tv.setTextSize(20);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(35, 0, 35, 0);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.className().equals("attribute-change")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText(link.text());
                            tv.setTextSize(18);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(50, 0, 50, 24);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.className().equals("blockquote context")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText("\"" + link.text() + "\"");
                            tv.setTextSize(12);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(45, 0, 45, 20);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.className().equals("summary")) {
                            TextView tv = new TextView(Patch.this);
                            tv.setText(link.text());
                            tv.setTextSize(14);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(40, 0, 40, 15);
                            tv.setLayoutParams(layoutParams);
                            ((LinearLayout) linearLayout).addView(tv);
                        } else if (link.tagName().equals("ul") && !link.className().contains("arrow-bullets")) {
                            Elements li = link.select("li");
                            for (int i = 0; i < li.size(); i++) {
                                TextView tv = new TextView(Patch.this);
                                tv.setText("·" + li.get(i).text());
                                tv.setTextSize(14);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                layoutParams.setMargins(50, 0, 50, 24);
                                tv.setLayoutParams(layoutParams);
                                ((LinearLayout) linearLayout).addView(tv);
                            }
                        } else if (link.className().startsWith("skins")) {
                            try {
                                ImageView imageView = new ImageView(Patch.this);
                                imageView.setImageBitmap(arrSkins[j]);
                                j++;
                                imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                                ((LinearLayout) linearLayout).addView(imageView);
                            } catch (Exception e) {
                            }
                        }
                    }
                }
                AdView mAdView = (AdView) findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            } else {
                RelativeLayout relativeLayout = new RelativeLayout(Patch.this);
                RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                TextView tv = new TextView(Patch.this);
                tv.setText("An error has occurred. \nWe can´t find that page :(");
                tv.setTextSize(25);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                tv.setLayoutParams(lp);
                relativeLayout.addView(tv);
                setContentView(relativeLayout, rlp);
            }
            progDailog.dismiss();
        }
    }

    private String ready(java.util.List<String> c3) throws IOException {
        String url2 = "http://na.leagueoflegends.com/en/news/game-updates/patch";
        Document doc = null;
        doc = Jsoup.connect(url2).timeout(10000).userAgent("Chrome").postDataCharset("utf-8").get();
        Elements links = doc.getElementsByClass("article-title");

//        URL u = new URL(url2);
//        URLConnection con = u.openConnection();
//        InputStream in = con.getInputStream();
//        String encoding = con.getContentEncoding();
//        encoding = encoding == null ? "UTF-8" : encoding;
//        String body = IOUtils.toString(in, encoding);
//        String[] version = new String[c3.size()];
//        for (int j = 0; j < c3.size(); j++) {
        String[] version = c3.get(0).split("\\.");
        String[] version2 = c3.get(1).split("\\.");
        String[] version3 = c3.get(2).split("\\.");
        System.out.println(version[0] + version[1]);
        String ret = "";
        if (doc.text().contains("Patch " + version[0] + "." + version[1] + " notes")) {
            System.out.println("TRUE");
            ret = version[0] + "." + version[1];
        } else if (doc.text().contains("Patch " + version2[0] + "." + version2[1] + " notes")) {
            System.out.println("TRUE2");
            ret = version2[0] + "." + version2[1];
        } else if(doc.text().contains("Patch " + version3[0] + "." + version3[1] + " notes")){
            System.out.println("TRUE3");
            ret = version3[0] + "." + version3[1];
        }else{
            System.out.println("FALSE");
        }
//        }
        return ret;
    }

    public boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting() && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
