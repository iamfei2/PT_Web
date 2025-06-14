package com.ruoyi.galaxy.util;

import com.ruoyi.galaxy.domain.GlxTorrent;
import com.ruoyi.galaxy.service.IGlxPeerService;
import com.ruoyi.galaxy.service.IGlxTorrentService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Date;

@Component
public class PointsUtil {
    private static int T0 = 4;
    private static int N0 = 7;
    private static int B0 = 50;
    private static int L = 300;

    @Autowired
    private IGlxPeerService peerService;

    @Autowired
    private IGlxTorrentService torrentService;

    /**
     * 魔力值计算
     * @param T 存在周数
     * @param S 种子大小(GB)
     * @param N 做种人数
     * @return
     */
    private double calcA(double T, double S, int N) {
        double c1 = 1 - Math.pow(10, -(T / T0));
        double c2 = 1 + Math.pow(2, 0.5) * Math.pow(10, -(N-1) / (N0 - 1));
        return c1 * S * c2;
    }

    /**
     * 计算B值
     * @param A
     * @return
     */
    private double calcB(double A) {
        return B0 * 2 / 3.141592653 * Math.atan(A / L);
    }

    /**
     * 通过种子计算魔力值
     * @param createTime
     * @param size
     * @param peerCount
     * @return
     */
    public double countUserPoint(Date createTime, Double size, int peerCount) {
        double tsp = (double)(new Date().getTime() - createTime.getTime()) / 1000 / (double)(86400 * 7); //周
        double fileSize = size / ConfigUtil.TRANSMISSION_UNIT;
        double a = calcA(tsp, fileSize, peerCount);
        double b = calcB(a);
        DecimalFormat df = new DecimalFormat(".000");
        return Double.parseDouble(df.format(b));
    }
}
