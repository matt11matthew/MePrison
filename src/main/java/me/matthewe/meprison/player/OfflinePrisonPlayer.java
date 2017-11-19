package me.matthewe.meprison.player;

import me.matthewe.meprison.chat.rank.Rank;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Matthew E on 11/18/2017.
 */
public class OfflinePrisonPlayer implements PrisonPlayer {
    private UUID uuid;
    private String name;
    private double balance;
    private long time;
    private boolean isFirstSession;
    private WarpCooldown warpCooldown;
    private Rank rank;
    private Map<String, Object> statisticMap;
    private String language;

    public OfflinePrisonPlayer(PrisonPlayer prisonPlayer) {
        this(prisonPlayer.getUuid());
        this.name = prisonPlayer.getName();
        this.balance = prisonPlayer.getBalance();
        this.isFirstSession = prisonPlayer.isFirstSession();
        warpCooldown = prisonPlayer.getWarpCooldown();
        this.statisticMap = new HashMap<>();
        this.rank = prisonPlayer.getRank();
        this.language = prisonPlayer.getLanguage();
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
        return false;
    }

    @Override
    public void setWarping(boolean warping) {

    }
    @Override
    public void loadStatistics(String statistics) {
//        String jsonString = Arrays.toString(Base64.getDecoder().decode(statistics));
//        try {
//            JSONObject jsonObject1 = (JSONObject) new JSONParser().parse(jsonString);
//            JSONObject jsonObject = (JSONObject) jsonObject1.get("statistics");
//
//            for (Object o : jsonObject.keySet()) {
//                if (o instanceof JsonArray) {
//                    List<Object> objects = new ArrayList<>();
//                    JsonArray jsonElements = (JsonArray) o;
//                    for (int i = 0; i < jsonElements.size(); i++) {
//                        JsonElement jsonElement = jsonElements.get(i);
//                        objects.add(jsonElement);
//                    }
//                    statisticMap.put((String) o, o1);
//                }
//                Object o1 = jsonObject.get(o);
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
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

    public OfflinePrisonPlayer(UUID uuid) {
        this.uuid = uuid;
        time = System.currentTimeMillis();
    }

    public OfflinePrisonPlayer setName(String name) {
        this.name = name;
        return this;
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
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public double getBalance() {
        return balance;
    }

    public long getTime() {
        return time;
    }

    public OfflinePrisonPlayer setTime(long time) {
        this.time = time;
        return this;
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
