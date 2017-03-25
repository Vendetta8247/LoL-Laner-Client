package ua.com.vendetta8247.lollaner;

import android.graphics.drawable.Drawable;



public class FriendListCard
{
    public Drawable image;
    public String summonerName;
    int status;

    public FriendListCard(Drawable image, String summonerName, int status)
    {
        this.image = image;
        this.summonerName = summonerName;
        this.status = status;
    }
}