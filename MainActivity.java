package com.example.unstoppable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                          ArrayList<File> songs=fetchSongs(Environment.getExternalStorageDirectory());
                          int n;
                          if(songs==null)
                              n=0;
                          else
                              n=songs.size();
                          String s="There are "+n+" songs";
                          Toast toast=Toast.makeText(MainActivity.this,s,s.length());
                          toast.show();
                          String[] gaane=new String[n];
                          for(int i=0;i<gaane.length;i++)
                          {
                              gaane[i] = songs.get(i).getName().replace(".mp3", "");
                          }
                          ArrayAdapter adapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, gaane)
                          {
                              @NonNull
                              @Override
                              public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                  View view=super.getView(position, convertView, parent);
                                  TextView text=(TextView) view.findViewById(android.R.id.text1);
                                  text.setTextColor(Color.BLACK);
                                  return view;
                              }
                          };
                          listView=findViewById(R.id.meraListView0);
                          listView.setAdapter(adapter);
                          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                  Intent intent=new Intent(MainActivity.this,PlaySong.class);
                                  String currentSong=listView.getItemAtPosition(i).toString();
                                  intent.putExtra("currentSong",currentSong);
                                  intent.putExtra("position",i);
                                  intent.putExtra("songList",songs);
                                  startActivity(intent);
                              }
                          });

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    public ArrayList<File> fetchSongs(File fp)
    {
        ArrayList<File> a=new ArrayList<>();
        File[] file=fp.listFiles();
        if(file==null)
            return a;
        for(File f:file)
        {
            if(f.isDirectory()&&!f.isHidden())
                a.addAll(fetchSongs(f));
            else if(f.getName().endsWith(".mp3")&&!f.getName().startsWith("."))
                a.add(f);
        }
        return a;
    }
}
