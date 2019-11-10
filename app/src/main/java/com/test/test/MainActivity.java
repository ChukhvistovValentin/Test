package com.test.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.test.test.adapters.ItemAdapter;
import com.test.test.loaders.LoadJsonTask;
import com.test.test.models.ItemModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public final static String testUrl = "http://test.php-cd.attractgroup.com/test.json";
    private final int PERMISSION_REQUEST = 1;
    private final String[] PERMISSIONS = {
            Manifest.permission.INTERNET
    };
    private ArrayList<ItemModel> searchModels = new ArrayList<>();
    private ItemAdapter adapter = new ItemAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // values -> layouts...
        setContentView(R.layout.main_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // если это планшет ???
        initDrawer(toolbar);

        // проверка разрешений к интернету
        if (hasPermission())
            onLoad();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                onSearch(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                    return;
                }
            }
            onLoad();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * проверка разрешений
     *
     * @return состояние разрешений
     */
    private Boolean hasPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * загружаем данные
     */
    private void onLoad() {
        RecyclerView rv = findViewById(R.id.rvList);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        LoadJsonTask loadTask = new LoadJsonTask(testUrl, new LoadJsonTask.JsonModelsListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(ArrayList<ItemModel> models) {
                if (models != null) // завершили скачивание и парсинг
                    adapter.update(searchModels = models); // отображаем
            }

            @Override
            public void onError(Exception e) {

            }
        });
        loadTask.execute();
    }

    /**
     * поиск по содержимому
     *
     * @param search поиск (начальные буквы)
     */
    private void onSearch(String search) {
        if (search.trim().equals("")) { // отмена поиска
            adapter.update(searchModels); // все данные отображаем
            return;
        }

        // список всех элементов
        ArrayList<ItemModel> searchResult = new ArrayList<>();
        String s = search.toLowerCase(); // что ищем в нижний регистр
        for (ItemModel item : searchModels) { // ищем в списке...
            if (item.getName().toLowerCase().contains(s)) {
                searchResult.add(item); // есть совпадение по имени - добавляем
            }
        }
        adapter.update(searchResult); // отображаем список который подходит
    }

    /**
     * отображаем панель
     *
     * @param toolbar тулбар
     */
    private void initDrawer(Toolbar toolbar) {
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null) { // разметка с панелькой
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            // навигация..
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    drawer.closeDrawer(GravityCompat.START); // по нажатию закрываем
                    return false;
                }
            });
        }
    }

}
