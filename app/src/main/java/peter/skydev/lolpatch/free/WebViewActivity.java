package peter.skydev.lolpatch.free;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.webview);
        Bundle getData = getIntent().getExtras();
        final String p = getData.getString("p");
        System.out.println("URL: " + p);
        browser = (WebView) findViewById(R.id.webkit);
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl(p);
    }
}
