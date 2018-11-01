package com.example.user.anogrammizer;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public Map<String, ArrayList<String>> word_dict = new HashMap<>();

    private String convertStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = is.read();
        while (i != -1) {
            baos.write(i);
            i = is.read();
        }
        return baos.toString();
    }

    private String sortLetters(String word){
        char c[] = word.toCharArray();
        Arrays.sort(c);
        return Arrays.toString(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button searchWordBtn = (Button) findViewById(R.id.searchWordBtn);

        Resources r = getResources();
        InputStream is = r.openRawResource(R.raw.dictbase);

        String words_text = null;
        try {
            words_text = convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert words_text != null;

        String words[] = words_text.split("\\r?\\n");

        for (String word:words) {
            String key = sortLetters(word);
            if (!word_dict.containsKey(key)){
                word_dict.put(key, new ArrayList<String>());
            }
            Objects.requireNonNull(word_dict.get(key)).add(word);
        }

        OnClickListener oclSearchWordBtn = new OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText anogrammaEdt = (EditText) findViewById(R.id.anogrammaEdt);
                String anogramma = anogrammaEdt.getText().toString();
                if (!anogramma.isEmpty()){
                    String key = sortLetters(anogramma);
                    if (word_dict.containsKey(key)){
                        ArrayList<String> values = word_dict.get(key);
                        ListView resultList = (ListView)findViewById(R.id.resultList);
                        assert values != null;
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(),
                                android.R.layout.simple_list_item_1, values);
                        resultList.setAdapter(adapter);
                    }
                }

            }
        };
        searchWordBtn.setOnClickListener(oclSearchWordBtn);
    }
}
