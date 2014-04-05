package m4ges.controllers;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface UITrick {
    public void showToast(CharSequence toastMessage, int toastDuration, Stage stage);
    public void showAlertBox(String alertBoxTitle, String alertBoxMessage, String alertBoxButtonText, Stage stage);
    public void openUri(String uri);
    public int[] getScreenSize();
    public String getMacAddress();

}
