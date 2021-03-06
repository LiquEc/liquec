/*
 * Open Source Software published under the GNU Licence, Version 2.0.
 */

package io.github.liquec.gui.controller;

import io.github.liquec.gui.chart.LiquEcData;
import io.github.liquec.gui.common.BoundsEnum;
import io.github.liquec.gui.common.LiquefactionEnum;
import io.github.liquec.gui.dialogues.AlertTool;
import io.github.liquec.gui.model.LayerModel;
import io.github.liquec.gui.model.LayerRow;
import io.github.liquec.gui.model.SessionModel;
import io.github.liquec.gui.services.ControllerHelper;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static javafx.beans.binding.Bindings.not;

public class LayerController {
    private static final Logger LOG = LoggerFactory.getLogger(LayerController.class);

    public Label valueStartDepth;

    public TextField textFieldLayerThickness;

    public TextField textFieldSoilType;

    public TextField textFieldSoilUnitWeightAboveGwt;

    public TextField textFieldSoilUnitWeightBelowGwt;

    public TextField textFieldFinesContent;

    public RadioButton liquefactionYesRadioButton;

    public RadioButton liquefactionNoRadioButton;

    public Button buttonOk;

    public Button buttonCancel;

    private Stage stage;

    private SessionModel sessionModel;

    private LayerModel layerModel;

    private String startDepth;

    @Inject
    private ControllerHelper controllerHelper;

    public void initialise(final Stage stage, final SessionModel sessionModel) {
        this.stage = stage;
        this.sessionModel = sessionModel;
        this.initializeStartDepth();
        this.layerModel = new LayerModel();

        buildToggleGroup(liquefactionYesRadioButton, liquefactionNoRadioButton);

        // Start Depth
        this.valueStartDepth.setText(this.startDepth);

        // Buttons
        buttonOk.disableProperty().bind(not(this.layerModel.ableToAddProperty()));

        // Layer Thickness
        Bindings.bindBidirectional(this.textFieldLayerThickness.textProperty(), this.layerModel.layerThicknessProperty());
        this.textFieldLayerThickness.textProperty().addListener((a, b, c) ->
            this.manageLayerModelState("Layer Thickness", b, c));
        this.textFieldLayerThickness.textProperty().addListener((a, b, c) ->
            this.controllerHelper.validateNumberValue(this.textFieldLayerThickness,"((1|2)?\\d{0,1}([\\.]\\d{0,2})?)|30|30\\.|30\\.0|30\\.00", b, c));
        this.textFieldLayerThickness.focusedProperty().addListener((a, b, c) ->
            this.controllerHelper.manageZerosValues(this.textFieldLayerThickness, b, c, "00", true));

        // Soil Type
        Bindings.bindBidirectional(this.textFieldSoilType.textProperty(), this.layerModel.soilTypeProperty());
        this.textFieldSoilType.textProperty().addListener((a, b, c) ->
            this.manageLayerModelState("Soil Type", b, c));
        this.textFieldSoilType.textProperty().addListener((a, b, c) ->
            this.controllerHelper.manageStringsValues(this.textFieldSoilType, b, c, 60));

        // Soil Unit Weight Above Gwt
        Bindings.bindBidirectional(this.textFieldSoilUnitWeightAboveGwt.textProperty(), this.layerModel.soilUnitWeightAboveGwtProperty());
        this.textFieldSoilUnitWeightAboveGwt.textProperty().addListener((a, b, c) ->
            this.manageLayerModelState("Soil Unit Weight Above Gwt", b, c));
        this.textFieldSoilUnitWeightAboveGwt.textProperty().addListener((a, b, c) ->
            this.controllerHelper.validateNumberValue(this.textFieldSoilUnitWeightAboveGwt,"((1|2|3|4)?\\d{0,1}([\\.]\\d{0,1})?)|50|50\\.|50\\.0", b, c));
        this.textFieldSoilUnitWeightAboveGwt.focusedProperty().addListener((a, b, c) ->
            this.controllerHelper.manageLowRangeValue(this.textFieldSoilUnitWeightAboveGwt, b, c, "0", 11.0F, true));

        // Soil Unit Weight Below Gwt
        Bindings.bindBidirectional(this.textFieldSoilUnitWeightBelowGwt.textProperty(), this.layerModel.soilUnitWeightBelowGwtProperty());
        this.textFieldSoilUnitWeightBelowGwt.textProperty().addListener((a, b, c) ->
            this.manageLayerModelState("Soil Unit Weight Below Gwt", b, c));
        this.textFieldSoilUnitWeightBelowGwt.textProperty().addListener((a, b, c) ->
            this.controllerHelper.validateNumberValue(this.textFieldSoilUnitWeightBelowGwt,"((1|2|3|4)?\\d{0,1}([\\.]\\d{0,1})?)|50|50\\.|50\\.0", b, c));
        this.textFieldSoilUnitWeightBelowGwt.focusedProperty().addListener((a, b, c) ->
            this.controllerHelper.manageLowRangeValue(this.textFieldSoilUnitWeightBelowGwt, b, c, "0", 11.0F, true));

        // Fines Content
        Bindings.bindBidirectional(this.textFieldFinesContent.textProperty(), this.layerModel.finesContentProperty());
        this.textFieldFinesContent.textProperty().addListener((a, b, c) ->
            this.manageLayerModelState("Fines Content", b, c));
        this.textFieldFinesContent.textProperty().addListener((a, b, c) ->
            this.controllerHelper.validateNumberValue(this.textFieldFinesContent,"(\\d{0,2}([\\.]\\d{0,1})?)|100|100\\.|100\\.0", b, c));
        this.textFieldFinesContent.focusedProperty().addListener((a, b, c) ->
            this.controllerHelper.manageZerosValues(this.textFieldFinesContent, b, c, "0", false));

        // Liquefaction
        liquefactionYesRadioButton.selectedProperty().bindBidirectional(layerModel.layerLiquefactionProperty());
        this.layerModel.layerLiquefactionProperty().addListener((a, b, c) ->
            this.controllerHelper.trackValues("Layer Liquefaction", b.toString(), c.toString()));

        // Buttons
        buttonOk.setOnAction(e -> saveLayer());
        buttonCancel.setOnAction(e -> exit());

    }

    private void initializeStartDepth() {
        if (this.sessionModel.getLayerData().size() == 0) {
            this.startDepth = "0.00";
            return;
        }
        this.startDepth = this.sessionModel.getLayerData().get(this.sessionModel.getLayerData().size() - 1).getFinalDepth();
    }

    private void buildToggleGroup(final Toggle liquefactionYes, final Toggle liquefactionNo) {
        ToggleGroup normativeGroup = new ToggleGroup();
        liquefactionYes.setToggleGroup(normativeGroup);
        liquefactionNo.setToggleGroup(normativeGroup);
    }

    private void manageLayerModelState(final String name, final String oldValue, final String newValue) {
        this.controllerHelper.trackValues(name, oldValue, newValue);
        this.layerModel.checkAbleToAdd();
    }

    private void saveLayer() {
        if (this.isOutOfBounds()) {
            AlertTool.filterErrorAlert(
                "Out Of Bounds",
                "You can't add this layer thickness.",
                "Depth Bound (m): " + BoundsEnum.MAX_DEPTH.getPositiveValue() + ", Thickness (m): " + (BoundsEnum.MAX_DEPTH.getPositiveValue() - Float.valueOf(this.startDepth)));
            return;
        }
        this.sessionModel.getLayerData().add(this.buildLayerRow());
        this.sessionModel.getLayerChartData().add(this.buildLayerChartData());
        this.sessionModel.setChangesSaved(false);
        this.sessionModel.checkAbleToCalculate();
        this.sessionModel.checkAbleToAddLayer();
        this.sessionModel.checkAbleToRemoveLastLayer();
        this.sessionModel.checkAbleToAddSpt();
        this.exit();
    }

    private boolean isOutOfBounds() {
        boolean outOfBounds = false;
        if (this.sessionModel.getLayerData().size() > 0) {
            outOfBounds = this.getFloatValueFinalDepth() > BoundsEnum.MAX_DEPTH.getPositiveValue();
        }
        return outOfBounds;
    }

    private LayerRow buildLayerRow() {
        return new LayerRow(
            this.startDepth,
            this.getFormattedNumber(String.valueOf(this.getFloatValueFinalDepth())),
            this.layerModel.getSoilType().toLowerCase(),
            this.layerModel.getSoilUnitWeightAboveGwt(),
            this.layerModel.getSoilUnitWeightBelowGwt(),
            this.layerModel.getFinesContent(),
            LiquefactionEnum.getDescription(this.layerModel.isLayerLiquefaction()));
    }

    private XYChart.Series<Number,Number> buildLayerChartData() {
        final XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
        series.setName(this.layerModel.getSoilType().toLowerCase());
        final ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
        data.add(LiquEcData.getChartInverseDataLowerX(Float.valueOf(this.layerModel.getLayerThickness())));
        data.add(LiquEcData.getChartInverseDataUpperX(Float.valueOf(this.layerModel.getLayerThickness())));
        series.setData(data);
        return series;
    }

    private Float getFloatValueFinalDepth() {
        final Float startDepth = Float.valueOf(this.startDepth);
        final Float layerThickness = Float.valueOf(this.layerModel.getLayerThickness());
        return startDepth + layerThickness;
    }

    private String getFormattedNumber(final String number) {
        if (number.matches("(\\d)+[\\.]\\d")) {
            return number + "0";
        }
        return number;
    }

    private void exit() {
        stage.close();
    }

}
