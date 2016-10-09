package ua.com.vendetta8247.lollaner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FriendListFragment extends Fragment {

    RecyclerView rv;
    MyRecViewAdapter adapter;

    public FriendListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = (RecyclerView) getActivity().findViewById(R.id.recview);
        adapter = new MyRecViewAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);




        adapter.addItem(new PlayerCard("My Summoners"));
    }

    class MyRecViewAdapter extends RecyclerView.Adapter<MyViewHolder>
    {

        private List<PlayerCard> cardList;

        public MyRecViewAdapter()
        {
            cardList = new ArrayList<>();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View itemView;
            MyViewHolder holder;

//            if(viewType == 1) {
//                itemView = LayoutInflater.from(parent.getContext()).
//                        inflate(R.layout.header_card, parent, false);
//                holder = new MyViewHolder(itemView);
//            }
//            else
//            {
                itemView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.player_card, parent, false);
                holder = new MyViewHolder(itemView);
//            }
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            PlayerCard card = cardList.get(position);
            holder.summonerName.setText(card.summonerName);
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        public void addItem(PlayerCard card)
        {
            cardList.add(card);
            notifyItemInserted(cardList.size()-1);
        }

        public void clearList()
        {
            cardList.clear();
            notifyDataSetChanged();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView summonerName;

        public MyViewHolder(View itemView) {
            super(itemView);
            summonerName = (TextView) itemView.findViewById(R.id.summoner_name);
            ImageView profilePic=(ImageView)itemView.findViewById(R.id.profile_icon);

//get bitmap of the image
            Bitmap imageBitmap= BitmapFactory.decodeResource(getResources(),  R.drawable.braum);
            RoundedBitmapDrawable roundedBitmapDrawable= RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);

//setting radius
            roundedBitmapDrawable.setCornerRadius(100.0f);
            roundedBitmapDrawable.setAntiAlias(true);
            profilePic.setImageDrawable(roundedBitmapDrawable);
        }
    }

    class PlayerCard
    {
        String summonerName;

        public PlayerCard(String summonerName)
        {
            this.summonerName = summonerName;
        }

    }
}
