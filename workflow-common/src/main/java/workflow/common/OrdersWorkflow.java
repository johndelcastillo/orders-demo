
package workflow.common;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import workflow.common.domain.Order;

@WorkflowInterface
public interface OrdersWorkflow {
    @WorkflowMethod
    void processOrder(Order order);
}
