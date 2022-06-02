package worker;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import worker.service.NotificationService;
import worker.service.StockService;
import workflow.common.ConnectionInfo;

public class App {

    public static void main(String[] args) {
        String targetString = String.format("%s:%d", ConnectionInfo.HOST, ConnectionInfo.PORT);
        WorkflowServiceStubsOptions serviceOptions = WorkflowServiceStubsOptions.newBuilder()
                .setTarget(targetString).build();

        WorkflowServiceStubs service = WorkflowServiceStubs.newServiceStubs(serviceOptions);

        WorkflowClientOptions clientOptions = WorkflowClientOptions.newBuilder()
                .setNamespace(ConnectionInfo.NAMESPACE).build();
        WorkflowClient workflowClient = WorkflowClient.newInstance(service, clientOptions);

        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker(ConnectionInfo.TASK_QUEUE);

        worker.registerWorkflowImplementationTypes(worker.OrdersWorkflowImpl.class);
        worker.registerActivitiesImplementations(
                new worker.OrdersWorker(new StockService(), new NotificationService()));

        factory.start();
    }
}
