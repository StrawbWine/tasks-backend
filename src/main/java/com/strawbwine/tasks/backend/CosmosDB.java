package com.strawbwine.tasks.backend;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedFlux;
import com.ctc.wstx.exc.WstxOutputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CosmosDB implements IDatabase {

  private final String databaseName = "TasksDB";
  private final String taskContainerName = "Tasks";
  private final String userContainerName = "Users";

  private CosmosAsyncDatabase database;
  private CosmosAsyncContainer container;

  private String MASTER_KEY = System.getenv("COSMOS_ACCOUNT_KEY");
  private String HOST = System.getenv("COSMOS_ACCOUNT_HOST");

  protected static Logger logger;

  public CosmosDB() {
    logger = LoggerFactory.getLogger(CosmosDB.class.getSimpleName());
    try {
      createDatabaseIfNotExists();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      createContainersIfNotExists();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private CosmosAsyncClient client;

  private CosmosAsyncClient createClient() {
    return new CosmosClientBuilder()
      .endpoint(HOST)
      .key(MASTER_KEY)
      .preferredRegions(Collections.singletonList("West US"))
      .consistencyLevel(ConsistencyLevel.EVENTUAL)
      .contentResponseOnWriteEnabled(true)
      .buildAsyncClient();
  }

  private void createDatabaseIfNotExists() throws Exception {
    logger.info("Create database {} if not exists.", databaseName);
    try {
      client = createClient();
      Mono<CosmosDatabaseResponse> databaseResponseMono = client.createDatabaseIfNotExists(databaseName);
      databaseResponseMono.flatMap(databaseResponse -> {
        database = client.getDatabase(databaseResponse.getProperties().getId());
        logger.info("Checking database {} completed!\n", database.getId());
        return Mono.empty();
      }).block();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      client.close();
    }
  }

  public void createContainersIfNotExists() throws Exception {
    try {
      database = client.getDatabase(databaseName);

      CosmosContainerProperties taskContainerProperties = new CosmosContainerProperties(taskContainerName, "/name");
      CosmosContainerProperties userContainerProperties = new CosmosContainerProperties(userContainerName, "/name");
      Mono<CosmosContainerResponse> taskContainerResponseMono = database.createContainerIfNotExists(taskContainerProperties, ThroughputProperties.createManualThroughput(400));
      Mono<CosmosContainerResponse> userContainerResponseMono = database.createContainerIfNotExists(userContainerProperties, ThroughputProperties.createManualThroughput(400));

      //  Create container with 400 RU/s
      taskContainerResponseMono.flatMap(containerResponse -> {
        container = database.getContainer(containerResponse.getProperties().getId());
        return Mono.empty();
      }).block();
      userContainerResponseMono.flatMap(containerResponse -> {
        container = database.getContainer(containerResponse.getProperties().getId());
        return Mono.empty();
      }).block();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      client.close();
    }
  }

  public void createUser(UserDAO user) {
    try {
      client = createClient();
      database = client.getDatabase(databaseName);
      container = database.getContainer(userContainerName);

      container.createItem(user).block();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      client.close();
    }
  }

  public void getUser() {
    try {
      client = createClient();
      CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
      database = client.getDatabase("AzureSampleFamilyDB");
      container = database.getContainer("Tasks");

      CosmosPagedFlux<UserDAO> pagedFluxResponse = container.queryItems(
        "SELECT * FROM Tasks", queryOptions, UserDAO.class);

      pagedFluxResponse.subscribe(user -> System.out.println(user.getName()));
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      client.close();
    }
  }

  @Override
  public DatabaseResponse write(TodoItem task) {
    return null;
  }

  @Override
  public DatabaseResponse write(User user) {
    return null;
  }

  @Override
  public List<TodoItem> fetchAllTasks() {
    return null;
  }

  @Override
  public List<User> fetchAllUsers() {
    return null;
  }

  @Override
  public List<TodoItem> fetchTasksForUser(User user) {
    return null;
  }

  @Override
  public User fetchUser(String userName) {
    return null;
  }
}

