package com.aepronunciation.ipa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class KeyboardInputActivity extends AppCompatActivity implements KeyboardInputFragment.KeyboardInputListener {


    private static final int CONFIRM_DELETE_TEXT_LENGTH = 20;

    IpaEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_input);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // disable rotation for smaller devices
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // prevent system keyboard from appearing
        editText = findViewById(R.id.etInputWindow);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        }

        // load keyboard fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment keyboardInputFragment = new KeyboardInputFragment();
        transaction.replace(R.id.keyboard_frame, keyboardInputFragment);
        transaction.commit();
    }

    private void copyText() {

        CharSequence text = editText.getText();
        if (TextUtils.isEmpty(text)) {
            return;
        }


        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("IPA text", text);
        if (clipboard == null) {
            return;
        }
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.keyboard_input_toast_text_copied), Toast.LENGTH_SHORT).show();
    }

    private void clearText() {

        Editable text = editText.getText();
        if (text == null) return;
        if (text.length() < CONFIRM_DELETE_TEXT_LENGTH) {
            editText.setText("");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);
        builder.setMessage(getString(R.string.keyboard_menu_alert_message));
        builder.setCancelable(true);
        builder.setPositiveButton(
                getString(R.string.keyboard_menu_alert_clear_all_button),
                (dialog, id) -> editText.setText(""));

        builder.setNegativeButton(
                getString(R.string.dialog_cancel_button),
                (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void shareText() {

        CharSequence text = editText.getText();
        if (text == null) {
            return;
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text.toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.keyboard_menu_share_chooser_title)));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_keyboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_copy:
                copyText();
                return true;
            case R.id.action_clear:
                clearText();
                return true;
            case R.id.action_share:
                shareText();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onKeyTouched(String keyString) {
        Editable text = editText.getText();
        if (text == null) return;
        int start = Math.max(editText.getSelectionStart(), 0);
        int end = Math.max(editText.getSelectionEnd(), 0);
        text.replace(Math.min(start, end), Math.max(start, end),
                keyString, 0, keyString.length());
        editText.setSelection(editText.getSelectionEnd());
    }

    @Override
    public void onBackspace() {
        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }
}
