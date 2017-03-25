package ua.com.vendetta8247.lollaner;

/**
 * Created by Y500 on 28.11.2016.
 */

public class CurrentGameSummoner {
    String summonerName;
    int summonerId;
    int championId;
    int summonerSpellId1, summonerSpellId2;
    int teamId;
    String flexDivision, flexTier;
    String soloDivision, soloTier;

    int totalGames=0, wonGames=0, lostGames=0;
    int totalChampionKills=0, totalDeaths=0, totalAssists=0;
    int totalDamageDealt=0, totalGoldEarned=0;
    int totalMinions = 0;

    int totalTurretsKilled = 0;
    int totalDamageTaken = 0;

    int maxChampionsKilled = 0;
    int maxNumDeaths = 0;

    boolean selected = false;



    public CurrentGameSummoner(String summonerName, int summonerId, int championId, int summonerSpellId1, int summonerSpellId2, int teamId
            ,String flexDivision, String flexTier, String soloDivision, String soloTier
    )
    {
        this.summonerName = summonerName;
        this.summonerId = summonerId;
        this.championId = championId;
        this.summonerSpellId1 = summonerSpellId1;
        this.summonerSpellId2 = summonerSpellId2;
        this.teamId = teamId;
        this.flexDivision = flexDivision;
        this.flexTier = flexTier;
        this.soloDivision = soloDivision;
        this.soloTier = soloTier;
    }

    public void addLeagueStats (int totalGames, int wonGames, int lostGames, int totalChampionKills,
                                int totalDeaths, int totalAssists, int totalDamageDealt, int totalGoldEarned, int totalMinions,
                                int totalTurretsKilled, int totalDamageTaken, int maxChampionsKilled, int maxNumDeaths)
    {
        this.totalGames = totalGames;
        this.wonGames = wonGames;
        this.lostGames = lostGames;
        this.totalChampionKills = totalChampionKills;
        this.totalDeaths = totalDeaths;
        this.totalAssists = totalAssists;
        this.totalDamageDealt = totalDamageDealt;
        this.totalGoldEarned = totalGoldEarned;
        this.totalMinions = totalMinions;

        this.totalTurretsKilled = totalTurretsKilled;
        this.totalDamageTaken = totalDamageTaken;
        this.maxChampionsKilled = maxChampionsKilled;
        this.maxNumDeaths = maxNumDeaths;
    }
}
