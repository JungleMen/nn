package com.nn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/nn")
public class NNController {

    private int playerAccount = 4;

    Map<String, List<Integer>> playersPaiMap = new HashMap<>();


    private static Integer[][] PAI = {
            {11, 12, 13, 14},
            {21, 22, 23, 24},
            {31, 32, 33, 34},
            {41, 42, 43, 44},
            {51, 42, 43, 44},
            {61, 62, 63, 64},
            {71, 72, 73, 74},
            {81, 82, 83, 84},
            {91, 92, 93, 94},
            {101, 102, 103, 104},
            {111, 112, 113, 114},
            {121, 122, 123, 124},
            {131, 132, 133, 134}
    };

    @RequestMapping("/fapai")
    @ResponseBody
    public Map<String, List<Integer>> fapai() {
        init();
        for (int account = 1; account <= 4; account++) {
            String playerName = "player" + account;
            List<Integer> paiList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                int result = getPai();
                paiList.add(result);
            }
            playersPaiMap.put(playerName, paiList);
        }
        return playersPaiMap;
    }

    private int getPai() {
        Random random = new Random();
        int dianShu = random.nextInt(13);
        int huaSe = random.nextInt(4);
        int result = PAI[dianShu][huaSe];
        if (result == 0) {
            return getPai();
        }
        PAI[dianShu][huaSe] = 0;
        return result;
    }

    private void init() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 4; j++) {
                PAI[i][j] = Integer.valueOf(String.valueOf(i + 1) + String.valueOf(j + 1));
            }
        }

    }

    @RequestMapping("/jiesuan")
    @ResponseBody
    public Map<String, Integer> jieSuan() {
        Map<String, Integer> playerPriority = new HashMap<>();
        for (int account = 1; account <= 4; account++) {
            String playerName = "player" + account;
            List<Integer> paiList = playersPaiMap.get(playerName);
            if (paiList == null || CollectionUtils.isEmpty(paiList)) {
                continue;
            }
            int[][] paiDetails = paiDetails(paiList);
            int value = tongHuaShun(paiDetails);
            if (value == 0) {
                value = wuXiaoNiu(paiDetails);
            }
            if (value == 0) {
                value = zhaDan(paiDetails);
            }
            if (value == 0) {
                value = wuHuaNiu(paiDetails);
            }
            if (value == 0) {
                value = zhiZunNiu(paiDetails);
            }
            if (value == 0) {
                value = huLu(paiDetails);
            }
            if (value == 0) {
                value = tongHua(paiDetails);
            }
            if (value == 0) {
                value = shunZi(paiDetails);
            }
            if (value == 0) {
                value = niuNiu(paiDetails);
            }
            if (value == 0) {
                value = youNiu(paiDetails);
            }
            if (value == 0) {
                value = wuNiu(paiDetails);
            }
            playerPriority.put(playerName, value);
        }
        if (playerPriority.isEmpty()) {
            throw new RuntimeException("No Player");
        }
        Integer max = Collections.max(playerPriority.values());
        Map<String, Integer> winner = new HashMap<>();
        for (String key : playerPriority.keySet()) {
            if (max == playerPriority.get(key)) {
                winner.put(key, max);
            }
        }
        return winner;
    }

    private int[][] paiDetails(List<Integer> paiList) {
        int[][] detail = new int[5][2];
        for (int i = 0; i < 5; i++) {
            String value = String.valueOf(paiList.get(i));
            String dianShu = value.substring(0, value.length() - 1);
            detail[i][0] = Integer.valueOf(dianShu);
            String huaSe = value.substring(value.length() - 1, value.length());
            detail[i][1] = Integer.valueOf(huaSe);
        }
        return detail;
    }

    private int tongHuaShun(int[][] paiDetails) {
        boolean tongHuaShun = tongHua(paiDetails) > 0 && shunZi(paiDetails) > 0;
        if (!tongHuaShun) {
            return 0;
        }
        int result = 10000;
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        Arrays.sort(dianShu);
        result = result + dianShu[0];
        int huaSe = paiDetails[0][1];
        result = result + huaSe;
        return result;
    }

    private int wuXiaoNiu(int[][] paiDetails) {
        int total = (paiDetails[0][0] > 10 ? 10 : paiDetails[0][0])
                + (paiDetails[1][0] > 10 ? 10 : paiDetails[1][0])
                + (paiDetails[2][0] > 10 ? 10 : paiDetails[2][0])
                + (paiDetails[3][0] > 10 ? 10 : paiDetails[3][0])
                + (paiDetails[4][0] > 10 ? 10 : paiDetails[4][0]);
        boolean lessThan10 = total < 10;
        boolean lessThan5 = paiDetails[0][0] < 5 && paiDetails[1][0] < 5 && paiDetails[2][0] < 5 && paiDetails[3][0] < 5 && paiDetails[4][0] < 5;
        boolean wuXiaoNiu = lessThan10 && lessThan5;
        if (!wuXiaoNiu) {
            return 0;
        }
        int result = 9000;
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        Arrays.sort(dianShu);
        result = result + dianShu[4];
        for (int i = 0; i < paiDetails.length; i++) {
            if (dianShu[4] == paiDetails[i][0]) {
                result = result + paiDetails[i][1];
            }
        }
        return result;
    }

    private int zhaDan(int[][] paiDetails) {
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < dianShu.length; i++) {
            int count = map.containsKey(dianShu[i]) ? map.get(dianShu[i]) : 0;
            map.put(dianShu[i], count + 1);
        }
        int result = 8000;
        for (Integer key : map.keySet()) {
            if (map.get(key) == 4) {
                return result + map.get(key);
            }
        }
        return 0;
    }

    private int wuHuaNiu(int[][] paiDetails) {
        boolean wuHuaNiu = paiDetails[0][0] > 10 && paiDetails[1][0] > 10 && paiDetails[2][0] > 10 && paiDetails[3][0] > 10 && paiDetails[4][0] > 10;
        if (!wuHuaNiu) {
            return 0;
        }
        int result = 7000;
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        Arrays.sort(dianShu);
        result = result + dianShu[4];
        for (int i = 0; i < paiDetails.length; i++) {
            if (dianShu[4] == paiDetails[i][0]) {
                result = result + paiDetails[i][1];
            }
        }
        return result;
    }

    private int zhiZunNiu(int[][] paiDetails) {
        boolean wuHuaNiu = paiDetails[0][0] > 9 && paiDetails[1][0] > 9 && paiDetails[2][0] > 9 && paiDetails[3][0] > 9 && paiDetails[4][0] > 9;
        if (!wuHuaNiu) {
            return 0;
        }
        int result = 6000;
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        Arrays.sort(dianShu);
        result = result + dianShu[4];
        for (int i = 0; i < paiDetails.length; i++) {
            if (dianShu[4] == paiDetails[i][0]) {
                result = result + paiDetails[i][1];
            }
        }
        return result;
    }

    private int huLu(int[][] paiDetails) {
        int[] clonePaiDetailsDianShu = new int[paiDetails.length];
        for (int i = 0; i < paiDetails.length; i++) {
            int val = paiDetails[i][0];
            if (val > 10) {
                val = 10;
            }
            clonePaiDetailsDianShu[i] = val;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < clonePaiDetailsDianShu.length; i++) {
            int count = map.containsKey(clonePaiDetailsDianShu[i]) ? map.get(clonePaiDetailsDianShu[i]) : 0;
            map.put(clonePaiDetailsDianShu[i], count + 1);
        }
        boolean duizi = false;
        boolean sanzhang = false;
        int sanzhangVal = 0;
        for (Integer key : map.keySet()) {
            if (map.get(key) == 3) {
                sanzhangVal = key;
                sanzhang = true;
            }
            if (map.get(key) == 2) {
                duizi = true;
            }
        }
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        if (duizi && sanzhang) {
            int result = 5000;
            Arrays.sort(dianShu);
            if (sanzhangVal == 10) {
                result = result + dianShu[4];
                int maxHuaSe = 0;
                for (int i = 0; i < paiDetails.length; i++) {
                    if (dianShu[4] == paiDetails[i][0]) {
                        maxHuaSe = paiDetails[i][1] > maxHuaSe ? paiDetails[i][1] : maxHuaSe;
                    }
                }
                result = result + maxHuaSe;
            } else {
                result = result + sanzhangVal;
            }

            return result;
        }
        return 0;
    }

    private int tongHua(int[][] paiDetails) {
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        int[] huaSe = {paiDetails[0][1], paiDetails[1][1], paiDetails[2][1], paiDetails[3][1], paiDetails[4][1]};
        boolean tongHua = huaSe[0] == huaSe[1] && huaSe[0] == huaSe[2] && huaSe[0] == huaSe[3] && huaSe[0] == huaSe[4];
        if (!tongHua) {
            return 0;
        }
        int result = 4000;
        Arrays.sort(dianShu);
        result = result + dianShu[4];
        result = result + paiDetails[0][1];
        return result;
    }

    private int shunZi(int[][] paiDetails) {
        int[][] clonePaiDetails = new int[5][2];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                clonePaiDetails[i][j] = paiDetails[i][j];
            }
        }
        int[] dianShu = {clonePaiDetails[0][0], clonePaiDetails[1][0], clonePaiDetails[2][0], clonePaiDetails[3][0], clonePaiDetails[4][0]};
        Arrays.sort(dianShu);
        if ((dianShu[4] - dianShu[3]) == 1 && (dianShu[3] - dianShu[2]) == 1 && (dianShu[2] - dianShu[1]) == 1) {
            int result = 3000;
            if (dianShu[0] == 1 && dianShu[4] == 13) {
                Arrays.sort(dianShu);
                result = result + 14;
                int maxHuaSe = 0;
                for (int i = 0; i < clonePaiDetails.length; i++) {
                    if (dianShu[4] == paiDetails[i][0]) {
                        maxHuaSe = clonePaiDetails[i][1] > maxHuaSe ? clonePaiDetails[i][1] : maxHuaSe;
                    }
                }
                result = result + maxHuaSe;

                return result;
            } else if ((dianShu[2] - dianShu[1]) == 1) {
                Arrays.sort(dianShu);
                result = result + dianShu[4];
                int maxHuaSe = 0;
                for (int i = 0; i < clonePaiDetails.length; i++) {
                    if (dianShu[4] == paiDetails[i][0]) {
                        maxHuaSe = clonePaiDetails[i][1] > maxHuaSe ? clonePaiDetails[i][1] : maxHuaSe;
                    }
                }
                result = result + maxHuaSe;
                return result;
            }
        }
        return 0;
    }

    private int niuNiu(int[][] paiDetails) {
        int sum = 0;
        for (int i = 0; i < paiDetails.length; i++) {
            if (paiDetails[i][0] > 10) {
                sum = sum + 10;
            } else {
                sum = sum + paiDetails[i][0];
            }
        }
        if (sum % 10 == 0) {
            int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
            Arrays.sort(dianShu);
            int result = 2000 + dianShu[4];

            int maxHuaSe = 0;
            for (int i = 0; i < paiDetails.length; i++) {
                if (dianShu[4] == paiDetails[i][0]) {
                    maxHuaSe = paiDetails[i][1] > maxHuaSe ? paiDetails[i][1] : maxHuaSe;
                }
            }
            result = result + maxHuaSe;
            return result;
        }
        return 0;
    }

    private int youNiu(int[][] paiDetails) {
        Integer[] newDianShu = new Integer[paiDetails.length];
        int sum = 0;
        for (int i = 0; i < paiDetails.length; i++) {
            if (paiDetails[i][0] > 10) {
                newDianShu[i] = 10;
            } else {
                newDianShu[i] = paiDetails[i][0];
            }
            sum = sum + newDianShu[i];
        }
        int bull = 0;
        for (int i = 0; i < newDianShu.length - 2; i++) {
            int cardI = newDianShu[i];
            for (int j = i + 1; j < newDianShu.length; j++) {
                int cardJ = newDianShu[j];
                for (int k = j + 1; k < newDianShu.length; k++) {
                    int cardK = newDianShu[k];
                    int total = cardI + cardJ + cardK;
                    if (total % 10 == 0) {
                        int n = (sum - total) % 10;
                        bull = bull < n ? n : bull;
                    }
                }
            }
        }
        if (bull == 0) {
            return 0;
        }
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        Arrays.sort(dianShu);
        int result = 1000 + bull * 13 + dianShu[4];
        int maxHuaSe = 0;
        for (int i = 0; i < paiDetails.length; i++) {
            if (dianShu[4] == paiDetails[i][0]) {
                maxHuaSe = paiDetails[i][1] > maxHuaSe ? paiDetails[i][1] : maxHuaSe;
            }
        }
        result = result + maxHuaSe;
        return result;
    }

    private int wuNiu(int[][] paiDetails) {
        int[] dianShu = {paiDetails[0][0], paiDetails[1][0], paiDetails[2][0], paiDetails[3][0], paiDetails[4][0]};
        Arrays.sort(dianShu);
        int result = dianShu[4];
        int maxHuaSe = 0;
        for (int i = 0; i < paiDetails.length; i++) {
            if (dianShu[4] == paiDetails[i][0]) {
                maxHuaSe = paiDetails[i][1] > maxHuaSe ? paiDetails[i][1] : maxHuaSe;
            }
        }
        result = result + maxHuaSe;
        return result;
    }

}
