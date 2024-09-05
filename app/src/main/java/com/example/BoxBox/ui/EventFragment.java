package com.example.BoxBox.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.BoxBox.R;
import com.example.BoxBox.VideoActivity;
import com.example.BoxBox.databinding.FragmentEventBinding;
import com.example.BoxBox.set.MyApplication;
import com.example.BoxBox.set.SetDir;
import com.example.BoxBox.set.SetList;
import com.example.BoxBox.set.Setpath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment {


    private FragmentEventBinding binding;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView listView;          // 검색을 보여줄 리스트변수
    private List<String> list;          // 데이터를 넣은 리스트변수
    private String path;                // 디렉토리 위치
    private File Dir;

    private TextView textdate;
    private String td;

    private ArrayList<String> ggglist;

    private ArrayList<String> selist;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> arraylist;

    private SharedPreferences pref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentEventBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setHasOptionsMenu(true);

        this.InitializeLayout();

        return root;
    }

    public void InitializeLayout() {

        if (getActivity() != null){
            pref = MyApplication.getSharedPreferences();

        }else{
            Log.e("FragmentError", "Activity is null");
        }

        textdate = getActivity().findViewById(R.id.dateText);
        td = textdate.getText().toString();
        td = td.replace("-","");
        Log.d("TAG", td);

        listView = (ListView) binding.listViewE;

        mSwipeRefreshLayout = (SwipeRefreshLayout) binding.srlE;

        path = new Setpath().getEpath();
        ggglist = new SetList().getlist(new SetDir().makeDir(path));  //Event 파일리스트 불러오기

        selist = new ArrayList<String>();
        this.CompoundCheck();

        // 리스트를 생성한다.
        list = new ArrayList<String>();
        list.addAll(selist);

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);

        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);

        if(list.isEmpty()){
            Toast.makeText(getActivity(), "영상을 찾을수 없습니다", Toast.LENGTH_SHORT).show();
        }

        this.fragAction();

    }

    public void fragAction(){

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshfile();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selected_item = path + "/" + (String)adapterView.getItemAtPosition(i);
                Log.d("TAG", selected_item);
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                intent.putExtra("Name", selected_item);
                startActivity(intent);

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                final String selected_item = path + "/" + (String)adapterView.getItemAtPosition(i);


                Log.d("TAG", selected_item);
                AlertDialog.Builder dlg = new AlertDialog.Builder( getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
                dlg.setTitle(""+(String)adapterView.getItemAtPosition(i)+"");
                dlg.setMessage("삭제하시겠습니까");


                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "delete",
                                Toast.LENGTH_SHORT).show();

                        new SetDir().fileDelete(selected_item);
                        refreshfile();

                    }
                });

                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                    }
                });

                dlg.show();
                return true;
            }
        });

    }
    public void CompoundCheck(){

        if((pref.getBoolean("ckFbutton", true) == true) && (pref.getBoolean("ckRbutton", true) == true)){
            Log.d("TAG", "search FR");
            for(int i = 0;i < ggglist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (ggglist.get(i).toLowerCase().contains(td.toLowerCase()) && (ggglist.get(i).toLowerCase().contains("F".toLowerCase()) || ggglist.get(i).toLowerCase().contains("R".toLowerCase())))
                {
                    Log.d("TAG", "search FR");
                    // 검색된 데이터를 리스트에 추가한다.
                    selist.add(ggglist.get(i));
                }
            }
        }
        if((pref.getBoolean("ckFbutton", true) == true) && (pref.getBoolean("ckRbutton", true) == false)){
            Log.d("TAG", "search F");
            for(int i = 0;i < ggglist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (ggglist.get(i).toLowerCase().contains("F".toLowerCase()) && ggglist.get(i).toLowerCase().contains(td.toLowerCase()))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    selist.add(ggglist.get(i));
                }
            }
        }
        if((pref.getBoolean("ckFbutton", true) == false) && (pref.getBoolean("ckRbutton", true) == true)){
            Log.d("TAG", "search R");
            for(int i = 0;i < ggglist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (ggglist.get(i).toLowerCase().contains("R".toLowerCase()) && ggglist.get(i).toLowerCase().contains(td.toLowerCase()))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    selist.add(ggglist.get(i));
                }
            }
        }
        if((pref.getBoolean("ckFbutton", true) == false) && (pref.getBoolean("ckRbutton", true) == false)){
            Log.d("TAG", "No searching");
        }

    }

    public void refreshfile() {
        ggglist = new SetList().getlist(new SetDir().makeDir(path));
        refreshlist();
    }

    public void refreshlist(){
        selist.clear();
        list.clear();
        arraylist.clear();

        td = textdate.getText().toString();
        td = td.replace("-","");

        CompoundCheck();
        list.addAll(selist);
        arraylist.addAll(list);

        if(list.isEmpty()){
            Toast.makeText(getActivity(), "영상을 찾을수 없습니다", Toast.LENGTH_SHORT).show();
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText.toLowerCase()))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();

    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = s;
                search(text);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {

        switch(item.getItemId()) {
            case R.id.action_search:
                Toast.makeText(getActivity(), "search", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_refresh:
                refreshfile();
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    public void refresh(){                                     //??
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}