package com.codingvalhalla.meredith.evidence.gui.dialogs;

import com.codingvalhalla.meredith.evidence.gui.GraphicUserInterface;
import com.codingvalhalla.meredith.evidence.model.Movie;
import com.codingvalhalla.meredith.evidence.model.RatingMPAA;
import com.codingvalhalla.meredith.evidence.utils.StaticAlerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 *
 * @author Meredith
 */
public class DialogMovie extends Dialog<Movie> {

    private final double buttonWidth = 75;
    @SuppressWarnings("FieldMayBeFinal")
    private DialogPane dialogPane;
    private BorderPane rootLayout;
    private GridPane rootGrid;
    private Window window;
    private Stage dialogStage;
    private Scene dialogScene;

    private Movie movie;

    private TextField name;
    private ComboBox<RatingMPAA> rating;
    private TextArea comment;
    private Slider stars;
    private ImageView starsImage;
    private Pane starsImageGroup;
    private Label starsLabel;
    private Pane starsThumb;

    private Button buttonOK;
    private Button buttonCancel;

    @SuppressWarnings("FieldMayBeFinal")
    private ObservableList<RatingMPAA> ratingList = FXCollections.observableArrayList(RatingMPAA.values());

    public DialogMovie() {
        this(new Movie("", RatingMPAA.G, "", 0));
    }

    public DialogMovie(Movie selectedItem) {
        this.movie = selectedItem;
        window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest((WindowEvent event) -> {
            if (StaticAlerts.confirmMessage("close this window without saving")) {
                window.hide();
            } else {
                event.consume();
            }
        });
        dialogPane = getDialogPane();
        init();
        initGrid();
        initProperties();
        addToLayout();
        initListers();
        addValues();
    }

    private void addValues() {
        name.setText(movie.getName());
        rating.getSelectionModel().select(movie.getRatingMPAA());
        stars.setValue(movie.getStars());
        comment.setText(movie.getComments());
    }

    public void afterShow() {
        this.stars.layout();
        this.starsThumb = (Pane) stars.lookup(".thumb");
        starsThumb.setBackground(Background.EMPTY);
        starsThumb.getChildren().clear();
        starsThumb.getChildren().addAll(starsLabel);
    }

    private void init() {
        this.rootLayout = new BorderPane();
        this.rootGrid = new GridPane();
        this.dialogScene = new Scene(rootLayout, 200, 400);
        this.buttonOK = new Button("Save");
        this.buttonCancel = new Button("Cancel");
        this.name = new TextField();
        this.rating = new ComboBox<>(ratingList);
        this.comment = new TextArea();
        this.stars = new Slider(1, 11, 1);
        this.starsImage = new ImageView(new Image(GraphicUserInterface.getResources() + "images/stars0.png", 110, 20, false, true));
        this.starsLabel = new Label();
        this.starsImageGroup = new Pane(starsImage, stars);

    }

    private void addToLayout() {
        rootLayout.setTop(initTop());
        rootLayout.setBottom(initBottom());
        rootLayout.setCenter(rootGrid);
        dialogPane.setContent(rootLayout);

    }

    private VBox initTop() {
        VBox vbox = new VBox();

        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        vbox.getChildren().addAll(new Label("Fill following informations:"));
        return vbox;
    }

    private HBox initBottom() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(buttonOK, buttonCancel);
        return hbox;
    }

    private void initProperties() {
        buttonOK.setPrefWidth(buttonWidth);
        buttonCancel.setPrefWidth(buttonWidth);
        name.setPromptText("Enter name of movie.");
        name.setPrefWidth(200);
        comment.setPromptText("Enter your comment.");
        comment.setPrefSize(200, 150);
        comment.setWrapText(true);

        rating.getSelectionModel().selectFirst();
        rating.prefWidthProperty().bind(name.prefWidthProperty());

        starsImage.setFitWidth(110);
        stars.setPrefSize(125, 20);
        starsImage.setLayoutX(7);
        starsImage.setLayoutY(5);

        rootGrid.setHgap(10.0);
        rootGrid.setVgap(5.0);
        rootGrid.setPadding(new Insets(5, 15, 5, 15));

        for (int i = 0; i < rootGrid.getChildren().size(); i++) {
            if (i % 2 == 0) {
                GridPane.setHalignment(rootGrid.getChildren().get(i), HPos.RIGHT);
            } else {
                GridPane.setHalignment(rootGrid.getChildren().get(i), HPos.LEFT);
                GridPane.setFillWidth(rootGrid.getChildren().get(i), true);
                GridPane.setHgrow(rootGrid.getChildren().get(i), Priority.ALWAYS);
            }

        }

    }

    private void initGrid() {
        rootGrid.addRow(0, new Label("Name:"), name);
        rootGrid.addRow(1, new Label("Rating MPAA:"), rating);
        rootGrid.addRow(2, new Label("Rating:"), starsImageGroup);
        rootGrid.addRow(3, new Label("Comment:"), comment);

    }

    private boolean isValid() {
        String errorMessage = "";

        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "Not valid name.\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            StaticAlerts.errorWithMessege(errorMessage, "Correct the entered data", dialogStage);
            return false;
        }

    }

    @SuppressWarnings("FieldMayBeFinal")
    private EventHandler<ActionEvent> dialogOKEvent = (ActionEvent event) -> {
        if (isValid()) {
            movie.setName(name.getText());
            movie.setRatingMPAA(rating.getSelectionModel().getSelectedItem());
            movie.setStars((int) stars.getValue() - 1);
            movie.setComments(comment.getText());
            setResult(movie);
        }

    };
    @SuppressWarnings("FieldMayBeFinal")
    private EventHandler<ActionEvent> dialogCancelEvent = (ActionEvent event) -> {
        window.fireEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSE_REQUEST));
    };

    private void initListers() {
        stars.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue() - 1;
            starsImage.setImage(new Image(GraphicUserInterface.getResources() + "images/stars" + value + ".png", 110, 20, false, true));
            stars.setValue(Math.round(newValue.doubleValue()));
        });
        starsLabel.textProperty().set("");
        buttonOK.setOnAction(dialogOKEvent);
        buttonCancel.setOnAction(dialogCancelEvent);
    }
}
