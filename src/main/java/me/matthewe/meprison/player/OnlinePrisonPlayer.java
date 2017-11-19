package me.matthewe.meprison.player;

import me.matthewe.meprison.chat.rank.Rank;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class OnlinePrisonPlayer implements PrisonPlayer {
    private final UUID uuid;
    private String name;
    private double balance;
    private boolean isFirstSession;
    private WarpCooldown warpCooldown;
    private boolean isWarping;
    private Rank rank;
    private Map<String, Object> statisticMap;
    private String language;

    public OnlinePrisonPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.statisticMap = new HashMap<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean isFirstSession() {
        return isFirstSession;
    }

    @Override
    public void setFirstSession(boolean firstSession) {
        this.isFirstSession = firstSession;
    }

    @Override
    public void loadStatistics(String statistics) {
        byte[] decodedBytes = Base64.getDecoder().decode(statistics);
        String jsonString = new String(decodedBytes, Charset.forName("UTF-8"));
        try {
            JSONObject jsonObject1 = (JSONObject) new JSONParser().parse(jsonString);
            JSONObject jsonObject = (JSONObject) jsonObject1.get("statistics");
            for (Object o : jsonObject.keySet()) {
                Object o1 = jsonObject.get(o);
                statisticMap.put((String) o, o1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getStatisticJson() {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        for (String s : statisticMap.keySet()) {
            jsonObject1.put(s,statisticMap.get(s));
        }
        jsonObject.put("statistics", jsonObject1);
        return jsonObject.toJSONString();
    }

    @Override
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public WarpCooldown getWarpCooldown() {
        return warpCooldown;
    }

    @Override
    public void setWarpCooldown(WarpCooldown warpCooldown) {
        this.warpCooldown = warpCooldown;
    }

    @Override
    public boolean isWarping() {
        return isWarping;
    }

    @Override
    public void setWarping(boolean warping) {
        this.isWarping = warping;
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public void setRank(Rank rank) {
        this.rank = rank;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }
}
