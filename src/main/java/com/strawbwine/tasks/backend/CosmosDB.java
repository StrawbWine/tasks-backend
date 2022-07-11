package com.strawbwine.tasks.backend;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedFlux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CosmosDB implements IDatabase {

  private final String databaseName = "TasksDB";
  private final String containerName = "Tasks";

  private CosmosAsyncDatabase database;
  private CosmosAsyncContainer container;

  private final String MASTER_KEY = System.getenv("COSMOS_ACCOUNT_KEY");
  private final String HOST = System.getenv("COSMOS_ACCOUNT_HOST");

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

  private void createDatabaseIfNotExists() {
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

  public void createContainersIfNotExists() {
    try {
      database = client.getDatabase(databaseName);

      CosmosContainerProperties containerProperties = new CosmosContainerProperties(containerName, "/name");

      Mono<CosmosContainerResponse> containerResponseMono = database.createContainerIfNotExists(containerProperties, ThroughputProperties.createManualThroughput(400));

      //  Create container with 400 RU/s
      containerResponseMono.flatMap(containerResponse -> {
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
      container = database.getContainer(containerName);

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
      database = client.getDatabase(databaseName);
      container = database.getContainer(containerName);
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
    TodoItemDAO todoitemDAO = new TodoItemDAO(task);
    try {
      client = createClient();
      database = client.getDatabase(databaseName);
      container = database.getContainer(containerName);
      container.createItem(todoitemDAO).block();
      return DatabaseResponse.OK;
    } catch (Exception ex) {
      ex.printStackTrace();
      return DatabaseResponse.FAILED;
    } finally {
      client.close();
    }
  }

  @Override
  public DatabaseResponse write(User user) {
    UserDAO userDAO = new UserDAO(user);
    try {
      client = createClient();
      database = client.getDatabase(databaseName);
      container = database.getContainer(containerName);
      container.createItem(userDAO).block();
      return DatabaseResponse.OK;
    } catch (Exception ex) {
      ex.printStackTrace();
      return DatabaseResponse.FAILED;
    } finally {
      client.close();
    }
  }

  @Override
  public List<TodoItem> fetchAllTasks() {
    List<TodoItem> tasks = new ArrayList<>();
    try {
      CosmosPagedFlux<TodoItemDAO> pagedFluxResponse = performQuery(
        databaseName,
        containerName,
        "SELECT * FROM c WHERE c.discriminator = 'TodoItem'",
        TodoItemDAO.class
      );
      tasks = pagedFluxResponse.map(TodoItem::new)
        .collectList().block();
      return tasks;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        client.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
      return tasks;
  }

  @Override
  public List<User> fetchAllUsers() {
    List<User> users = new ArrayList<>();
    try {
      CosmosPagedFlux<UserDAO> pagedFluxResponse = performQuery(
        databaseName,
        containerName,
        "SELECT * FROM c WHERE c.discriminator = 'User'",
        UserDAO.class
      );
      return pagedFluxResponse.map(User::new
        ).collectList().block();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        client.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return users;
  }

  @Override
  public List<TodoItem> fetchTasksForUser(User user) {
    List<TodoItem> tasks = new ArrayList<>();
    try {
      CosmosPagedFlux<TodoItemDAO> pagedFluxResponse = performQuery(
        databaseName,
        containerName,
        String.format("SELECT * FROM c WHERE c.owner.id = '%s' AND c.discriminator = 'TodoItem'", user.getId()),
        TodoItemDAO.class
      );
      return pagedFluxResponse.map(TodoItem::new
        ).collectList().block();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        client.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return tasks;
  }

  @Override
  public User fetchUser(String userName) {
    List<User> users = new ArrayList<>();
    try {
      CosmosPagedFlux<UserDAO> pagedFluxResponse = performQuery(
        databaseName,
        containerName,
        String.format("SELECT * FROM c WHERE c.name = '%s' AND c.discriminator = 'User'", userName),
        UserDAO.class
      );
      users = pagedFluxResponse.map(User::new
        ).collectList().block();
      try {
        return users.get(0);
      } catch (IndexOutOfBoundsException ex) {
        return null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        client.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    try {
      return users.get(0);
    } catch (IndexOutOfBoundsException ex) {
      return null;
    }
  }

  @Override
  public TodoItem fetchTask(String taskName) {
    List<TodoItem> tasks = new ArrayList<>();
    try {
      CosmosPagedFlux<TodoItemDAO> pagedFluxResponse = performQuery(
        databaseName,
        containerName,
        String.format("SELECT * FROM c WHERE c.name = '%s' AND c.discriminator = 'TodoItem'", taskName),
        TodoItemDAO.class
      );
      tasks = pagedFluxResponse.map(TodoItem::new
        ).collectList().block();
      try {
        return tasks.get(0);
      } catch (IndexOutOfBoundsException ex) {
        return null;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        client.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    try {
      return tasks.get(0);
    } catch (IndexOutOfBoundsException ex) {
      return null;
    }
  }

  private <T> CosmosPagedFlux<T> performQuery(String databaseName, String containerName, String query, Class<T> returnType) {
    client = createClient();
    CosmosQueryRequestOptions queryOptions = new CosmosQueryRequestOptions();
    database = client.getDatabase(databaseName);
    container = database.getContainer(containerName);
    return container.queryItems(
      query, queryOptions, returnType);
  }

}

