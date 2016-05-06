package peter.skydev.lolpatch.free;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity {
    RadioButton na, euw, eune, lan, las, br, jp, ru, tr, oce, en, es, it, de, fr, el, hu, pl, ro, cs, pt, jp2, ru2, tr2;
    Button p, op;
    String serv, lang;
    CheckBox cbN;
    boolean cb;
    ImageView im1, im3;
    SharedPreferences prefs;
    public static final String MYPREFSID = "MyPrefs001";
    public static final int actMode = Activity.MODE_PRIVATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = this.getSharedPreferences("peter.skydev.lolpatch", MainActivity.MODE_PRIVATE);
        serv = prefs.getString("server", "na");
        lang = prefs.getString("language", "en");
        cb = prefs.getBoolean("cb", false);

        p = (Button) findViewById(R.id.newp);
        // op = (Button) findViewById(R.id.oldp);
        cbN = (CheckBox) findViewById(R.id.chbxN);

        im1 = (ImageView) findViewById(R.id.t);
        im3 = (ImageView) findViewById(R.id.s);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://twitter.com/Pedro_Rios606");
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        cbN.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startService();
                }else{
                    stopService();
                }
            }
        });

        na = (RadioButton) findViewById(R.id.RbOpcion1);
        euw = (RadioButton) findViewById(R.id.RbOpcion2);
        eune = (RadioButton) findViewById(R.id.RbOpcion3);
        lan = (RadioButton) findViewById(R.id.RbOpcion4);
        las = (RadioButton) findViewById(R.id.RbOpcion5);
        br = (RadioButton) findViewById(R.id.RbOpcion6);
        ru = (RadioButton) findViewById(R.id.RbOpcion8);
        tr = (RadioButton) findViewById(R.id.RbOpcion9);
        oce = (RadioButton) findViewById(R.id.RbOpcion10);

        en = (RadioButton) findViewById(R.id.RbOpcion11);
        es = (RadioButton) findViewById(R.id.RbOpcion12);
        it = (RadioButton) findViewById(R.id.RbOpcion13);
        de = (RadioButton) findViewById(R.id.RbOpcion14);
        fr = (RadioButton) findViewById(R.id.RbOpcion15);
        el = (RadioButton) findViewById(R.id.RbOpcion16);
        hu = (RadioButton) findViewById(R.id.RbOpcion17);
        pl = (RadioButton) findViewById(R.id.RbOpcion18);
        ro = (RadioButton) findViewById(R.id.RbOpcion19);
        cs = (RadioButton) findViewById(R.id.RbOpcion110);
        pt = (RadioButton) findViewById(R.id.RbOpcion111);
        jp2 = (RadioButton) findViewById(R.id.RbOpcion112);
        ru2 = (RadioButton) findViewById(R.id.RbOpcion113);
        tr2 = (RadioButton) findViewById(R.id.RbOpcion114);

        if (Build.VERSION.SDK_INT >= 23) {
            try {
                na.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                euw.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                eune.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                lan.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                las.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                br.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                ru.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                tr.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                oce.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));

                en.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                es.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                it.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                de.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                fr.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                el.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                hu.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                pl.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                ro.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                cs.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                pt.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                jp2.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                ru2.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));
                tr2.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));

                cbN.setButtonTintList(ColorStateList.valueOf(getColor(R.color.colorDark)));

            } catch (Exception e) {

            }
        }

        updateFromSavedState();

        p.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkChecked()) {
                    if(isServiceRunning(".PatchService", MainActivity.this)){
//                        stopService();
                        startService();
                    }
                    saveDataFromCurrentState();
                    Intent myIntent = new Intent(MainActivity.this, Patch.class);
                    myIntent.putExtra("server", serv);
                    myIntent.putExtra("language", lang);
                    MainActivity.this.startActivity(myIntent);
                    finish();

                }else{
                     Toast.makeText(MainActivity.this, "You have to select both server and language", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // op.setOnClickListener(new OnClickListener() {
        // @Override
        // public void onClick(View view) {
        // saveDataFromCurrentState();
        // Toast.makeText(MainActivity.this, "server: " + serv + " - Language: "
        // + lang, Toast.LENGTH_SHORT).show();
        // Intent myIntent = new Intent(MainActivity.this, OldPatch.class);
        // myIntent.putExtra("server", serv);
        // myIntent.putExtra("language", lang);
        // MainActivity.this.startActivity(myIntent);
        // finish();
        // }
        // });

        na.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "na";
                    prefs.edit().putString("server", "na");
                    en.setVisibility(View.VISIBLE);
                    es.setVisibility(View.GONE);
                    it.setVisibility(View.GONE);
                    de.setVisibility(View.GONE);
                    fr.setVisibility(View.GONE);
                    el.setVisibility(View.GONE);
                    hu.setVisibility(View.GONE);
                    pl.setVisibility(View.GONE);
                    ro.setVisibility(View.GONE);
                    cs.setVisibility(View.GONE);
                    pt.setVisibility(View.GONE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                    uncheck();
                }
            }
        });

        if (na.isChecked()) {
            en.setVisibility(View.VISIBLE);
            es.setVisibility(View.GONE);
            it.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            fr.setVisibility(View.GONE);
            el.setVisibility(View.GONE);
            hu.setVisibility(View.GONE);
            pl.setVisibility(View.GONE);
            ro.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            pt.setVisibility(View.GONE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.GONE);
            tr2.setVisibility(View.GONE);
        }

        euw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "euw";
                    prefs.edit().putString("server", "euw");
                    en.setVisibility(View.VISIBLE);
                    es.setVisibility(View.VISIBLE);
                    it.setVisibility(View.VISIBLE);
                    de.setVisibility(View.VISIBLE);
                    fr.setVisibility(View.VISIBLE);
                    el.setVisibility(View.GONE);
                    hu.setVisibility(View.GONE);
                    pl.setVisibility(View.GONE);
                    ro.setVisibility(View.GONE);
                    cs.setVisibility(View.GONE);
                    pt.setVisibility(View.GONE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                    uncheck();
                }
            }
        });

        if (euw.isChecked()) {
            en.setVisibility(View.VISIBLE);
            es.setVisibility(View.VISIBLE);
            it.setVisibility(View.VISIBLE);
            de.setVisibility(View.VISIBLE);
            fr.setVisibility(View.VISIBLE);
            el.setVisibility(View.GONE);
            hu.setVisibility(View.GONE);
            pl.setVisibility(View.GONE);
            ro.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            pt.setVisibility(View.GONE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.GONE);
            tr2.setVisibility(View.GONE);
        }

        eune.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "eune";
                    prefs.edit().putString("server", "eune");
                    en.setVisibility(View.VISIBLE);
                    es.setVisibility(View.GONE);
                    it.setVisibility(View.GONE);
                    de.setVisibility(View.GONE);
                    fr.setVisibility(View.GONE);
                    el.setVisibility(View.VISIBLE);
                    hu.setVisibility(View.VISIBLE);
                    pl.setVisibility(View.VISIBLE);
                    ro.setVisibility(View.VISIBLE);
                    cs.setVisibility(View.VISIBLE);
                    pt.setVisibility(View.GONE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                    uncheck();
                }
            }
        });

        if (eune.isChecked()) {
            en.setVisibility(View.VISIBLE);
            es.setVisibility(View.GONE);
            it.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            fr.setVisibility(View.GONE);
            el.setVisibility(View.VISIBLE);
            hu.setVisibility(View.VISIBLE);
            pl.setVisibility(View.VISIBLE);
            ro.setVisibility(View.VISIBLE);
            cs.setVisibility(View.VISIBLE);
            pt.setVisibility(View.GONE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.GONE);
            tr2.setVisibility(View.GONE);
        }

        lan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "lan";
                    prefs.edit().putString("server", "lan");
                    en.setVisibility(View.GONE);
                    es.setVisibility(View.VISIBLE);
                    it.setVisibility(View.GONE);
                    de.setVisibility(View.GONE);
                    fr.setVisibility(View.GONE);
                    el.setVisibility(View.GONE);
                    hu.setVisibility(View.GONE);
                    pl.setVisibility(View.GONE);
                    ro.setVisibility(View.GONE);
                    cs.setVisibility(View.GONE);
                    pt.setVisibility(View.GONE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                    uncheck();
                }
            }
        });

        if (lan.isChecked()) {
            en.setVisibility(View.GONE);
            es.setVisibility(View.VISIBLE);
            it.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            fr.setVisibility(View.GONE);
            el.setVisibility(View.GONE);
            hu.setVisibility(View.GONE);
            pl.setVisibility(View.GONE);
            ro.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            pt.setVisibility(View.GONE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.GONE);
            tr2.setVisibility(View.GONE);
        }

        las.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "las";
                    prefs.edit().putString("server", "las");
                    en.setVisibility(View.GONE);
                    es.setVisibility(View.VISIBLE);
                    it.setVisibility(View.GONE);
                    de.setVisibility(View.GONE);
                    fr.setVisibility(View.GONE);
                    el.setVisibility(View.GONE);
                    hu.setVisibility(View.GONE);
                    pl.setVisibility(View.GONE);
                    ro.setVisibility(View.GONE);
                    cs.setVisibility(View.GONE);
                    pt.setVisibility(View.GONE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                    uncheck();
                }
            }
        });

        if (las.isChecked()) {
            en.setVisibility(View.GONE);
            es.setVisibility(View.VISIBLE);
            it.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            fr.setVisibility(View.GONE);
            el.setVisibility(View.GONE);
            hu.setVisibility(View.GONE);
            pl.setVisibility(View.GONE);
            ro.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            pt.setVisibility(View.GONE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.GONE);
            tr2.setVisibility(View.GONE);
        }

        br.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "br";
                    prefs.edit().putString("server", "br");
                    en.setVisibility(View.GONE);
                    es.setVisibility(View.GONE);
                    it.setVisibility(View.GONE);
                    de.setVisibility(View.GONE);
                    fr.setVisibility(View.GONE);
                    el.setVisibility(View.GONE);
                    hu.setVisibility(View.GONE);
                    pl.setVisibility(View.GONE);
                    ro.setVisibility(View.GONE);
                    cs.setVisibility(View.GONE);
                    pt.setVisibility(View.VISIBLE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                    uncheck();
                }
            }
        });

        if (br.isChecked()) {
            en.setVisibility(View.GONE);
            es.setVisibility(View.GONE);
            it.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            fr.setVisibility(View.GONE);
            el.setVisibility(View.GONE);
            hu.setVisibility(View.GONE);
            pl.setVisibility(View.GONE);
            ro.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            pt.setVisibility(View.VISIBLE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.GONE);
            tr2.setVisibility(View.GONE);
        }

        // jp.setOnCheckedChangeListener(new
        // CompoundButton.OnCheckedChangeListener() {
        //
        // @Override
        // public void onCheckedChanged(CompoundButton buttonView, boolean
        // isChecked) {
        // if (isChecked) {
        // serv = "jp";
        // prefs.edit().putString("server", "jp");
        // en.setVisibility(View.GONE);
        // es.setVisibility(View.GONE);
        // it.setVisibility(View.GONE);
        // de.setVisibility(View.GONE);
        // fr.setVisibility(View.GONE);
        // el.setVisibility(View.GONE);
        // hu.setVisibility(View.GONE);
        // pl.setVisibility(View.GONE);
        // ro.setVisibility(View.GONE);
        // cs.setVisibility(View.GONE);
        // pt.setVisibility(View.GONE);
        // jp2.setVisibility(View.VISIBLE);
        // ru2.setVisibility(View.GONE);
        // tr2.setVisibility(View.GONE);
        // uncheck();
        // }
        // }
        // });
        //
        // if (jp.isChecked()) {
        // en.setVisibility(View.GONE);
        // es.setVisibility(View.GONE);
        // it.setVisibility(View.GONE);
        // de.setVisibility(View.GONE);
        // fr.setVisibility(View.GONE);
        // el.setVisibility(View.GONE);
        // hu.setVisibility(View.GONE);
        // pl.setVisibility(View.GONE);
        // ro.setVisibility(View.GONE);
        // cs.setVisibility(View.GONE);
        // pt.setVisibility(View.GONE);
        // jp2.setVisibility(View.VISIBLE);
        // ru2.setVisibility(View.GONE);
        // tr2.setVisibility(View.GONE);
        // }

        ru.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "ru";
                    prefs.edit().putString("server", "ru");
                    en.setVisibility(View.GONE);
                    es.setVisibility(View.GONE);
                    it.setVisibility(View.GONE);
                    de.setVisibility(View.GONE);
                    fr.setVisibility(View.GONE);
                    el.setVisibility(View.GONE);
                    hu.setVisibility(View.GONE);
                    pl.setVisibility(View.GONE);
                    ro.setVisibility(View.GONE);
                    cs.setVisibility(View.GONE);
                    pt.setVisibility(View.GONE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.VISIBLE);
                    tr2.setVisibility(View.GONE);
                    uncheck();
                }
            }
        });

        if (ru.isChecked()) {
            en.setVisibility(View.GONE);
            es.setVisibility(View.GONE);
            it.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            fr.setVisibility(View.GONE);
            el.setVisibility(View.GONE);
            hu.setVisibility(View.GONE);
            pl.setVisibility(View.GONE);
            ro.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            pt.setVisibility(View.GONE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.VISIBLE);
            tr2.setVisibility(View.GONE);
        }

        tr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "tr";
                    prefs.edit().putString("server", "tr");
                    en.setVisibility(View.GONE);
                    es.setVisibility(View.GONE);
                    it.setVisibility(View.GONE);
                    de.setVisibility(View.GONE);
                    fr.setVisibility(View.GONE);
                    el.setVisibility(View.GONE);
                    hu.setVisibility(View.GONE);
                    pl.setVisibility(View.GONE);
                    ro.setVisibility(View.GONE);
                    cs.setVisibility(View.GONE);
                    pt.setVisibility(View.GONE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.GONE);
                    tr2.setVisibility(View.VISIBLE);
                    uncheck();
                }
            }
        });

        if (tr.isChecked()) {
            en.setVisibility(View.GONE);
            es.setVisibility(View.GONE);
            it.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            fr.setVisibility(View.GONE);
            el.setVisibility(View.GONE);
            hu.setVisibility(View.GONE);
            pl.setVisibility(View.GONE);
            ro.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            pt.setVisibility(View.GONE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.GONE);
            tr2.setVisibility(View.VISIBLE);
        }

        oce.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    serv = "oce";
                    prefs.edit().putString("server", "oce");
                    en.setVisibility(View.VISIBLE);
                    es.setVisibility(View.GONE);
                    it.setVisibility(View.GONE);
                    de.setVisibility(View.GONE);
                    fr.setVisibility(View.GONE);
                    el.setVisibility(View.GONE);
                    hu.setVisibility(View.GONE);
                    pl.setVisibility(View.GONE);
                    ro.setVisibility(View.GONE);
                    cs.setVisibility(View.GONE);
                    pt.setVisibility(View.GONE);
                    jp2.setVisibility(View.GONE);
                    ru2.setVisibility(View.GONE);
                    tr2.setVisibility(View.GONE);
                    uncheck();
                }
            }
        });

        if (oce.isChecked()) {
            en.setVisibility(View.VISIBLE);
            es.setVisibility(View.GONE);
            it.setVisibility(View.GONE);
            de.setVisibility(View.GONE);
            fr.setVisibility(View.GONE);
            el.setVisibility(View.GONE);
            hu.setVisibility(View.GONE);
            pl.setVisibility(View.GONE);
            ro.setVisibility(View.GONE);
            cs.setVisibility(View.GONE);
            pt.setVisibility(View.GONE);
            jp2.setVisibility(View.GONE);
            ru2.setVisibility(View.GONE);
            tr2.setVisibility(View.GONE);
        }

        en.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "en";
                    prefs.edit().putString("lang", "en");
                }
            }
        });

        es.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "es";
                    prefs.edit().putString("lang", "es");
                }
            }
        });

        it.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "it";
                    prefs.edit().putString("lang", "it");
                }
            }
        });

        de.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "de";
                    prefs.edit().putString("lang", "de");
                }
            }
        });

        fr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "fr";
                    prefs.edit().putString("lang", "fr");
                }
            }
        });

        el.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "el";
                    prefs.edit().putString("lang", "el");
                }
            }
        });

        hu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "hu";
                    prefs.edit().putString("lang", "hu");
                }
            }
        });

        pl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "pl";
                    prefs.edit().putString("lang", "pl");
                }
            }
        });

        ro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "ro";
                    prefs.edit().putString("lang", "ro");
                }
            }
        });

        cs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "cs";
                    prefs.edit().putString("lang", "cs");
                }
            }
        });

        pt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "pt";
                    prefs.edit().putString("lang", "pt");
                }
            }
        });

        jp2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "jp";
                    prefs.edit().putString("lang", "jp");
                }
            }
        });

        ru2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "ru";
                    prefs.edit().putString("lang", "ru");
                }
            }
        });

        tr2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lang = "tr";
                    prefs.edit().putString("lang", "tr");
                }
            }
        });
    }

    public void onBackPressed() {
        saveDataFromCurrentState();
        finish();
    }

    protected void saveDataFromCurrentState() {
        SharedPreferences myPrefs22 = getSharedPreferences(MYPREFSID, actMode);
        SharedPreferences.Editor myEditor22 = myPrefs22.edit();
        myEditor22.putString("server", serv);
        myEditor22.putString("language", lang);
        myEditor22.putBoolean("cb", cb);
        System.out.println(cb);
        myEditor22.commit();
    }

    protected void updateFromSavedState() {
        SharedPreferences myPrefs = getSharedPreferences(MYPREFSID, actMode);
        if ((myPrefs != null) && (myPrefs.contains("server"))) {
            serv = myPrefs.getString("server", "na");
            lang = myPrefs.getString("language", "en");
            cb = myPrefs.getBoolean("cb", false);
            System.out.println(cb);
            cbN.setChecked(cb);

            switch (serv) {
                case "na":
                    na.setChecked(true);
                    break;
                case "euw":
                    euw.setChecked(true);
                    break;
                case "eune":
                    eune.setChecked(true);
                    break;
                case "lan":
                    lan.setChecked(true);
                    break;
                case "las":
                    las.setChecked(true);
                    break;
                case "br":
                    br.setChecked(true);
                    break;
                case "jp":
                    jp.setChecked(true);
                    break;
                case "ru":
                    ru.setChecked(true);
                    break;
                case "tr":
                    tr.setChecked(true);
                    break;
                case "oce":
                    oce.setChecked(true);
                    break;
            }
            switch (lang) {
                case "es":
                    es.setChecked(true);
                    break;
                case "en":
                    en.setChecked(true);
                    break;
                case "it":
                    it.setChecked(true);
                    break;
                case "de":
                    de.setChecked(true);
                    break;
                case "fr":
                    fr.setChecked(true);
                    break;
                case "el":
                    el.setChecked(true);
                    break;
                case "hu":
                    hu.setChecked(true);
                    break;
                case "pl":
                    pl.setChecked(true);
                    break;
                case "ro":
                    ro.setChecked(true);
                    break;
                case "cs":
                    cs.setChecked(true);
                    break;
                case "ru":
                    ru2.setChecked(true);
                    break;
                case "tr":
                    tr2.setChecked(true);
                    break;
                case "jp":
                    jp2.setChecked(true);
                    break;
                case "pt":
                    pt.setChecked(true);
                    break;
            }
        }
    }

    public static boolean isServiceRunning(String serviceClassName, Context ctx) {
        final ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (RunningServiceInfo runningServiceInfo : services) {
            System.out.println("Servicios: " + runningServiceInfo.service.getClassName());
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)) {
                return true;
            }
        }
        return false;
    }

    public void share() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String sAux = "Read the Patch Notes with this!\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=peter.skydev.lolpatch.free \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
        }
    }

    private void startService() {
//        if (!isServiceRunning(".PatchService", MainActivity.this)) {
            cb = true;
            Intent intent2 = new Intent(MainActivity.this, PatchService.class);
            intent2.putExtra("server", serv);
            intent2.putExtra("lang", lang);
            MainActivity.this.startService(intent2);
            saveDataFromCurrentState();
//        }
    }

    private void stopService() {
//        if (isServiceRunning(".PatchService", MainActivity.this)) {
            cb = false;
            System.out.println("f");
            Intent intent2 = new Intent(MainActivity.this, PatchService.class);
            MainActivity.this.stopService(intent2);
            saveDataFromCurrentState();
//        }
    }

    public void uncheck() {
        en.setChecked(false);
        es.setChecked(false);
        it.setChecked(false);
        de.setChecked(false);
        fr.setChecked(false);
        el.setChecked(false);
        hu.setChecked(false);
        pl.setChecked(false);
        ro.setChecked(false);
        cs.setChecked(false);
        pt.setChecked(false);
        jp2.setChecked(false);
        ru2.setChecked(false);
        tr2.setChecked(false);
    }

    public boolean checkChecked() {
        boolean ret = true;
        if (en.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (es.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (it.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (de.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (fr.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (el.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (hu.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (pl.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (ro.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (cs.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (pt.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (jp2.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (ru2.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        if (tr2.isChecked()) {
            ret = true;
            return ret;
        } else {
            ret = false;
        }
        return false;
    }
}