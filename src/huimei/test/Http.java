package huimei.test;

import com.hm.apollo.controller.PushService;
import com.hm.mayson.module.track.model.TrackEvent;

public class Http {
    public static void main(String[] args) {
        PushService service = new PushService();
        TrackEvent e = new TrackEvent();
        e.setCustomerId(1172L);
        e.setDepartment("儿科");
        e.setDoctorGuid("1");

        String s = service.push("http://127.0.0.1:8093/mayson", "/track/default_recommend", e);
        System.out.println(s);

    }
}
