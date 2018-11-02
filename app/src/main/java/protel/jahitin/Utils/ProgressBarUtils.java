package protel.jahitin.Utils;

import android.view.View;
import android.widget.ProgressBar;

public class ProgressBarUtils {

    public void showLoadingIndicator(ProgressBar loadingIndicator){
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    public void hideLoadingIndicator(ProgressBar loadingIndicator){
        loadingIndicator.setVisibility(View.GONE);
    }
}
