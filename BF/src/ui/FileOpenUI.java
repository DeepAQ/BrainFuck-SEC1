package ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import utils.SessionMgr;

import java.util.ArrayList;

/**
 * Created by adn55 on 16/5/25.
 */
public class FileOpenUI extends Stage {
    private Scene scene;
    private TabPane tabPane;

    public FileOpenUI(TabPane tabPane) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("assets/FileOpenUI.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        this.scene = new Scene(root);
        this.setScene(this.scene);
        this.setTitle("Open");
        this.initStyle(StageStyle.UTILITY);
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);
        this.tabPane = tabPane;

        tableFiles.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("filename"));
        tableFiles.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("lastModified"));
        tableVersions.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("version"));
        tableFiles.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tableVersions.getItems().clear();
                for (VersionProperty version : tableFiles.getItems().get((Integer) newValue).versions) {
                    tableVersions.getItems().add(version);
                }
                tableVersions.getSortOrder().clear();
                tableVersions.getColumns().get(0).setSortType(TableColumn.SortType.DESCENDING);
                tableVersions.getSortOrder().add(tableVersions.getColumns().get(0));
                tableVersions.getSelectionModel().select(0);
            }
        });
        tableVersions.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            btnOpen.setDisable(true);
            if (newValue != null) {
                btnOpen.setDisable(false);
            }
        });

        this.getFileList();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void getFileList() {
        tableFiles.getItems().clear();
        try {
            String fileList = SessionMgr.getFileList();
            JSONArray jsonFiles = (JSONArray) new JSONTokener(fileList).nextValue();
            for (int i = 0; i < jsonFiles.length(); i++) {
                JSONObject jsonFile = jsonFiles.getJSONObject(i);
                String filename = jsonFile.getString("filename");
                ArrayList<VersionProperty> versionProperties = new ArrayList<>();
                JSONArray jsonVersions = jsonFile.getJSONArray("versions");
                for (int j = 0; j < jsonVersions.length(); j++) {
                    versionProperties.add(new VersionProperty(jsonVersions.getString(j)));
                }
                FileProperty fileProperty = new FileProperty(filename,
                        jsonVersions.getString(jsonVersions.length() - 1), versionProperties);
                tableFiles.getItems().add(fileProperty);
            }
        } catch (Exception e) {
            showError(e.getLocalizedMessage());
            this.close();
        }
        if (tableFiles.getItems().isEmpty()) {
            showError("No files saved, try to create a new one~");
            this.close();
        } else {
            tableFiles.getSortOrder().clear();
            tableFiles.getColumns().get(1).setSortType(TableColumn.SortType.DESCENDING);
            tableFiles.getSortOrder().add(tableFiles.getColumns().get(1));
            tableFiles.getSelectionModel().select(0);
        }
    }

    @FXML
    private TableView<FileProperty> tableFiles;
    @FXML
    private TableView<VersionProperty> tableVersions;
    @FXML
    private Button btnOpen;

    @FXML
    protected void onCancelAction(ActionEvent t) {
        this.close();
    }

    @FXML
    protected void onOpenAction(ActionEvent t) {
        try {
            String filename = tableFiles.getSelectionModel().getSelectedItem().getRealFilename();
            String version = tableVersions.getSelectionModel().getSelectedItem().getVersion();
            String code = SessionMgr.getFileContent(filename, version);
            BFTab newTab = new BFTab(filename, version);
            newTab.setCode(code);
            BFTab lastTab = (BFTab) tabPane.getTabs().get(tabPane.getTabs().size() - 1);
            if (lastTab.fileName.isEmpty() && !lastTab.modified) {
                tabPane.getTabs().remove(lastTab);
            }
            tabPane.getTabs().add(newTab);
            tabPane.getSelectionModel().select(newTab);
            this.close();
        } catch (Exception e) {
            showError(e.getLocalizedMessage());
        }
    }

    @FXML
    protected void onFileClicked(MouseEvent t) {
        if (t.getClickCount() == 2) {
            tableVersions.getSelectionModel().select(0);
            onOpenAction(null);
        }
    }

    @FXML
    protected void onVersionClicked(MouseEvent t) {
        if (t.getClickCount() == 2) {
            onOpenAction(null);
        }
    }

    public static class FileProperty {
        private final SimpleStringProperty filename;
        private final SimpleStringProperty lastModified;
        private final ArrayList<VersionProperty> versions;
        private FileProperty(String s1, String s2, ArrayList<VersionProperty> versionProperties) {
            this.filename = new SimpleStringProperty(s1);
            this.lastModified = new SimpleStringProperty(s2);
            this.versions = versionProperties;
        }
        public String getFilename() {
            return filename.get() + ".bf";
        }
        public String getRealFilename() {
            return filename.get();
        }
        public String getLastModified() {
            return lastModified.get();
        }
    }

    public static class VersionProperty {
        private final SimpleStringProperty version;
        private VersionProperty(String s) {
            version = new SimpleStringProperty(s);
        }
        public String getVersion() {
            return version.get();
        }
    }

}

