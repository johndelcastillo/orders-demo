package worker;

import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowClientOptions;
import com.uber.cadence.serviceclient.ClientOptions;
import com.uber.cadence.serviceclient.IWorkflowService;
import com.uber.cadence.serviceclient.WorkflowServiceTChannel;
import com.uber.cadence.worker.Worker;
import com.uber.cadence.worker.WorkerFactory;

import worker.service.NotificationService;
import worker.service.StockService;
import workflow.common.ConnectionInfo;

public class App {
    public static void main(String[] args) {
        WorkflowClientOptions clientOptions = WorkflowClientOptions.newBuilder().setDomain(ConnectionInfo.NAMESPACE)
                .build();

        IWorkflowService service = new WorkflowServiceTChannel(ClientOptions.newBuilder()
                .setHost(ConnectionInfo.HOST)
                .setPort(ConnectionInfo.PORT)
                .build());

        WorkflowClient workflowClient = WorkflowClient.newInstance(service, clientOptions);

        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker(ConnectionInfo.TASK_QUEUE);

        worker.registerWorkflowImplementationTypes(worker.OrdersWorkflowImpl.class);
        worker.registerActivitiesImplementations(
                new worker.OrdersWorker(new StockService(), new NotificationService()));

        factory.start();
    }
}
