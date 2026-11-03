package com.example.launcher;

import android.app.*;
import android.os.*;
import android.content.*;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import java.io.*;

public class MainActivity extends Activity {

    private static final String GAME =
            "com.catsbit.oxidesurvivalisland";

    private File plugins;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        plugins = new File(getFilesDir(), "plugins");
        plugins.mkdirs();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(30, 30, 30, 30);
        root.setBackgroundColor(0xFF17191E);

        TextView title = new TextView(this);
        title.setText("OXIDE LAUNCHER");
        title.setTextSize(26);
        title.setTextColor(-1);
        root.addView(title);

        TextView game = new TextView(this);
        game.setText("Oxide: Survival Island");
        game.setTextColor(0xFFAAAAAA);
        game.setTextSize(18);
        root.addView(game);

        Button add = new Button(this);
        add.setText("Добавить свой плагин");
        add.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.setType("*/*");
            i.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(i, 1);
        });
        root.addView(add);

        Button launch = new Button(this);
        launch.setText("ЗАПУСТИТЬ ИГРУ");
        launch.setOnClickListener(v -> {
            Intent i = getPackageManager()
                    .getLaunchIntentForPackage(GAME);

            if (i != null) {
                startActivity(i);
            } else {
                Toast.makeText(this,
                        "Игра не установлена",
                        Toast.LENGTH_SHORT).show();
            }
        });
        root.addView(launch);

        setContentView(root);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);

        if (request == 1 && result == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                File file = new File(plugins,
                        "plugin_" + System.currentTimeMillis() + ".so");

                InputStream in =
                        getContentResolver().openInputStream(uri);

                FileOutputStream out = new FileOutputStream(file);

                byte[] buffer = new byte[8192];
                int n;

                while ((n = in.read(buffer)) > 0) {
                    out.write(buffer, 0, n);
                }

                in.close();
                out.close();

                Toast.makeText(this,
                        "Плагин добавлен",
                        Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(this,
                        "Ошибка добавления",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
