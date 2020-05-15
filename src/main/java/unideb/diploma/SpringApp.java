package unideb.diploma;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import unideb.diploma.cache.Cache;
import unideb.diploma.config.AppConfig;
import unideb.diploma.view.graphic.TableView;


/**
 * Entry class of the application.
 * */
public class SpringApp extends Application {
	
	/**
	 * The primary stage of the application.
	 * */
	private static Stage primaryStage;
	
	
	/**
	 * The main method.
	 * @param args the command line arguments passed to the application.
	 * */
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/**
	 * The entry point of the JavaFX application.
	 * @param primaryStage the primary stage for this application.
	 * */
	@Override
	public void start(Stage primaryStage) throws Exception {
		try (ConfigurableApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class)) {
            App app = appContext.getBean(App.class);
            SpringApp.primaryStage = primaryStage;
            initStage();
    		primaryStage.setAlwaysOnTop(true);
    		primaryStage.show();
			ExecutorService executor = Executors.newSingleThreadExecutor();
			executor.execute(() -> {
				app.playGame();
			});
			executor.shutdown();
        }

	}
	
	/**
	 * Initializing the primary stage.
	 * */
	public static void initStage() {
		Platform.runLater(() -> {
			TableView root = new TableView(30, Cache.getState().getTable());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
		});

	}

}
