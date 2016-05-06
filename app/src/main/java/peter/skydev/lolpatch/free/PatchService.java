package peter.skydev.lolpatch.free;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import org.apache.commons.io.IOUtils;

import main.java.riotapi.RiotApiException;
import method.StaticDataMethod;

public class PatchService extends Service {
    private static final String MYPREFSID = "MyPrefs001";
    private static final int actMode = Activity.MODE_PRIVATE;
    private String patch;
    Timer timer = new Timer();

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        TimerTask hourlyTask = new TimerTask() {
            @Override
            public void run() {
                getSharedPreferences("peter.skydev.lolpatch", MainActivity.MODE_PRIVATE);
                updateFromSavedState();
                try {
                    if (isNetworkAvailable(PatchService.this)) {
                        java.util.List<String> c3 = StaticDataMethod.getDataVersions("euw", "e3bb62be-8188-441b-89e2-4f4050215587");
                        String[] v = c3.get(0).split("\\.");
                        if (check(v)) {
                            if (patch.equals("0.0")) {
                                patch = v[0] + "." + v[1];
                                saveDataFromCurrentState();
                            } else {
                                if (ready(v[0], v[1])) {
                                    Intent myIntent = new Intent(PatchService.this, MainActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(PatchService.this, 0, myIntent,
                                            Intent.FLAG_ACTIVITY_NEW_TASK);

                                    NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    NotificationCompat.Builder ncomp = new NotificationCompat.Builder(PatchService.this);
                                    ncomp.setContentTitle("New patch!");
                                    ncomp.setContentText("Touch here to read it");
                                    ncomp.setTicker("New patch avaliable, read the notes now.");
                                    ncomp.setSmallIcon(R.drawable.ic_stat_icono_app_sin_fondo);
                                    ncomp.setAutoCancel(true);
                                    ncomp.setContentIntent(pendingIntent);
                                    nManager.notify((int) System.currentTimeMillis(), ncomp.build());
                                    patch = v[0] + "." + v[1];
                                    saveDataFromCurrentState();
                                } else {

                                }
                            }
                        }
                    }
                } catch (RiotApiException e) {
                } catch (NullPointerException e) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(hourlyTask, 0l, 60000 * 30);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer.purge();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    private boolean check(String[] v) {
        String[] p = patch.split("\\.");
        System.out.println("P: " + Integer.parseInt(p[0]));
        System.out.println("V: " + Integer.parseInt(v[0]));
        if (Integer.parseInt(v[0]) > Integer.parseInt(p[0])) {
            return true;
        } else {
            if (Integer.parseInt(v[0]) == Integer.parseInt(p[0])) {
                if (Integer.parseInt(v[1]) > Integer.parseInt(p[1])) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private boolean ready(String s1, String s) throws IOException {
        String url2 = "http://" + "na" + ".leagueoflegends.com/" + "en";

        URL u = new URL(url2);

        URLConnection con = u.openConnection();
        InputStream in = con.getInputStream();
        String encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        String body = IOUtils.toString(in, encoding);

        if (body.contains("/en/news/game-updates/patch/") || body.contains("patch")) {
            return true;
        } else {
            return false;
        }
    }

    protected void saveDataFromCurrentState() {
        SharedPreferences myPrefs22 = getSharedPreferences(MYPREFSID, actMode);
        SharedPreferences.Editor myEditor22 = myPrefs22.edit();
        myEditor22.putString("patch", patch);
        myEditor22.commit();
    }

    protected void updateFromSavedState() {
        SharedPreferences myPrefs = getSharedPreferences(MYPREFSID, actMode);
        if (myPrefs != null) {
            patch = myPrefs.getString("patch", "0.0");
        }
    }

}

