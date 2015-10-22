package com.delaney.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by stevenyee on 10/14/15.
 */
public class CrimeListFragment extends Fragment {
    private static final int REQUEST_CRIME = 1;
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private Crime mCrime;
    private static final String ARG_CRIME_POSITION = "crime_position";
    private int toUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }
        else{
            Toast.makeText(getActivity(), (toUpdate) + " Inside Update Correct", Toast.LENGTH_SHORT).show();
            mAdapter.notifyItemChanged(toUpdate);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == REQUEST_CRIME) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), "HERE clicked!", Toast.LENGTH_SHORT).show();
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                List<Crime> crimes = crimeLab.getCrimes();
                String adjusted = CrimeFragment.returnId(data);
                Toast.makeText(getActivity(), adjusted + "HERE clicked!", Toast.LENGTH_SHORT).show();
                int size = crimes.size();
                for (int i = 0; i < size; i++) {
                    if (crimes.get(i).getId().toString().equals(adjusted)) {
                        toUpdate = i - 1;
                        Toast.makeText(getActivity(), (toUpdate) + " clicked!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }
    }



    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        public CrimeHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);

        }

        @Override
        public void onClick(View v){
//            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT).show();

            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, REQUEST_CRIME);
        }

        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position){
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);

        }

        @Override
        public int getItemCount(){
            return mCrimes.size();
        }
    }
}
