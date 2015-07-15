package asia.covisoft.downloadsample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by USER on 7/15/2015.
 */
public class Utils {
    public final static boolean isConnected( Context context )
    {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
