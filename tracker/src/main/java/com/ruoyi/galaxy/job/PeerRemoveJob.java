package com.ruoyi.galaxy.job;

import com.ruoyi.galaxy.service.IBitPeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("GalaxyTask")
public class PeerRemoveJob {
    @Autowired
    private IBitPeerService peerService;

    public void RemoveTimeoutPeers(Long time) {
        peerService.deleteGlxPeerByTime(time);
    }
}
