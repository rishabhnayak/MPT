package ml.rishabhnayak.mpt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by RAJA on 18-07-2018.
 */

public class AdapterList extends RecyclerView.Adapter<AdapterList.AdapterAllHolder>{
    private Context context;
    private DataPojo[] data;
    public AdapterList(Context context, DataPojo[] data){
        this.context=context;
        this.data=data;
    }

    @Override
    public AdapterAllHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_view_row_item,parent,false);
        return new AdapterAllHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdapterAllHolder holder, final int position) {
        final DataPojo items=data[position];

            String name=items.getName();
            holder.test.setText(name);
            holder.test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });



    }

    @Override
    public int getItemCount() {
        try{return data.length;}
        catch (Exception e){

        }
        return 0;
    }

    public class AdapterAllHolder extends RecyclerView.ViewHolder {

        TextView test;
        public AdapterAllHolder(View itemView) {
            super(itemView);
            test=(TextView) itemView.findViewById(R.id.textViewItem);
        }
    }
}
