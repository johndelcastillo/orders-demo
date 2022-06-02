package worker;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import worker.domain.StockInfo;

@ActivityInterface
public interface OrdersActivities {
    @ActivityMethod
    StockInfo checkStock(String orderId);

    @ActivityMethod
    void notifyDelay(String emailAddress, Long eta);

    @ActivityMethod
    String packageAndSendOrder(String orderId, String address);

    @ActivityMethod
    void notifySent(String emailAddress, String trackingId);
}
