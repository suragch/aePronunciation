package com.aepronunciation.ipa;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // disable rotation for smaller devices
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // prevent system keyboard from appearing
        editText = (IpaEditText) findViewById(R.id.etInputWindow);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }

        // load keyboard fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment keyboardInputFragment = new KeyboardInputFragment();
        transaction.replace(R.id.keyboard_frame, keyboardInputFragment);
        transaction.commit();
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void copyText() {

        CharSequence text = editText.getText();
        if (TextUtils.isEmpty(text)) {
            return;
        }

        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("IPA text", text);
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(this, getString(R.string.keyboard_input_toast_text_copied), Toast.LENGTH_SHORT).show();
    }

    private void clearText() {

        if (editText.getText().length() < CONFIRM_DELETE_TEXT_LENGTH) {
            editText.setText("");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.keyboard_menu_alert_message));
        builder.setCancelable(true);
        builder.setPositiveButton(
                getString(R.string.keyboard_menu_alert_clear_all_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        editText.setText("");
                    }
                });

        builder.setNegativeButton(
                getString(R.string.dialog_cancel_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void shareText() {

        CharSequence text = editText.getText().toString();
        if (TextUtils.isEmpty(text)) {
            return;
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.keyboard_menu_share_chooser_title)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        int start = Math.max(editText.getSelectionStart(), 0);
        int end = Math.max(editText.getSelectionEnd(), 0);
        editText.getText().replace(Math.min(start, end), Math.max(start, end),
                keyString, 0, keyString.length());
    }

    @Override
    public void onBackspace() {
        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }
}
