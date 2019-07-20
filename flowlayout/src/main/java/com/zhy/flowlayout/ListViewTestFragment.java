package com.zhy.flowlayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.abslistview.CommonAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhy on 15/9/10.
 */
public class ListViewTestFragment extends Fragment
{

    private List<List<String>> mDatas = new ArrayList<List<String>>();
    private ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_listview, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState)
    {
        initDatas();

        mListView = (ListView) view.findViewById(R.id.id_listview);
        mListView.setAdapter(new CommonAdapter<List<String>>(getActivity(), R.layout.item_for_listview, mDatas)
        {
            Map<Integer, Set<Integer>> selectedMap = new HashMap<Integer, Set<Integer>>();


            @Override
            public void convert(final ViewHolder viewHolder, List<String> strings)
            {
                TagFlowLayout tagFlowLayout = viewHolder.getView(R.id.id_tagflowlayout);

                TagAdapter<String> tagAdapter = new TagAdapter<String>(strings)
                {
                    @Override
                    public View getView(FlowLayout parent, int position, String o)
                    {
                        //can use viewholder
                        TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                                parent, false);
                        tv.setText(o);
                        return tv;
                    }
                };
                tagFlowLayout.setAdapter(tagAdapter);
                //重置状态
                tagAdapter.setSelectedList(selectedMap.get(viewHolder.getItemPosition()));

                tagFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
                {
                    @Override
                    public void onSelected(Set<Integer> selectPosSet)
                    {
                        selectedMap.put(viewHolder.getItemPosition(), selectPosSet);
                    }
                });
            }
        });

    }

    private void initDatas()
    {
        for (int i = 'A'; i < 'z'; i++)
        {
            List<String> itemData = new ArrayList<String>(3);
            for (int j = 0; j < 3; j++)
            {
                itemData.add((char) i + "");
            }
            mDatas.add(itemData);
        }
    }
}
