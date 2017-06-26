package ua.com.vendetta8247.lollaner;

import android.graphics.drawable.Drawable;



public class FriendListCard
{
    public Drawable image;
    public String summonerName;

    public FriendListCard(Drawable image, String summonerName)
    {
        this.image = image;
        this.summonerName = summonerName;
    }
}