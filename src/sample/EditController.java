package sample;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class EditController {

    private Appointment model;

    @FXML
    TextArea formalField;

    @FXML
    Spinner toHourField, fromHourField, toMinuteField, fromMinuteField, repetitionField;

    @FXML
    DatePicker dateField, stopField;

    @FXML
    Label stopFieldText, help;

    @FXML
    TextField roomField;

    private String roomHelp = "Rommet må bestå av bygningsnavnet, et mellomrom og et romnummer";

    private SpinnerValueFactory.IntegerSpinnerValueFactory fromHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
    private SpinnerValueFactory.IntegerSpinnerValueFactory fromMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
    private SpinnerValueFactory.IntegerSpinnerValueFactory toHourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
    private SpinnerValueFactory.IntegerSpinnerValueFactory toMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);

    private SpinnerValueFactory.IntegerSpinnerValueFactory repetitionFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE);

    Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell()
    {
        @Override
        public void updateItem(LocalDate item, boolean empty)
        {
            super.updateItem(item, empty);

            if(item.isBefore(LocalDate.now())) {
                setDisable(true);
            }
        }
    };

    StringConverter<Integer> stringToInt = new StringConverter<Integer>() {

        @Override
        public String toString(Integer object) {
            return null;
        }

        @Override
        public Integer fromString(String string) {
            try {
                int number = Integer.parseInt(string);
                return number;
            } catch (NumberFormatException nfe) {
                System.out.println(nfe);
                return 0;
            }
        }
    };

    //Model listeners
    private ChangeListener<String> formalListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            updateFormal();
        }
    };

    private ChangeListener<LocalTime> fromListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            updateFromMinute();
            updateFromHour();
        }
    };

    private ChangeListener<LocalTime> toListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            updateToMinute();
            updateToHour();
        }
    };

    private ChangeListener<Number> repetitionListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            updateRepetition();
        }
    };

    private ChangeListener<LocalDate> dateListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            updateDate();
        }
    };

    private ChangeListener<LocalDate> repStopListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            updateRepStop();
        }
    };

    private ChangeListener<String> roomListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            updateRoom();
        }
    };

    //View listerners
    private ChangeListener<Integer> fromHourViewListener = (property, oldValue, newValue) -> {
       if (newValue != null) {
           this.model.setFra(LocalTime.of(newValue, this.model.getFra().getMinute()));
       }
    };

    private ChangeListener<Integer> fromMinuteViewListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            this.model.setFra(LocalTime.of(this.model.getFra().getHour(), newValue));
        }
    };

    private ChangeListener<Integer> toHourViewListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            this.model.setTil(LocalTime.of(newValue, this.model.getTil().getMinute()));
        }
    };

    private ChangeListener<Integer> toMinuteViewListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            this.model.setTil(LocalTime.of(this.model.getTil().getHour(), newValue));
        }
    };

    private ChangeListener<Integer> repetitionViewListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            this.model.setRepetisjon(newValue);
        }
    };

    private ChangeListener<String> roomViewListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            if (validateRoomString(newValue)) {
                this.model.setRom(newValue);
                help.setText(null);
            } else {
                help.setText(roomHelp);
            }
        }
    };

    private ChangeListener<String> formalViewListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            this.model.setFormal(newValue);
        }
    };

    private ChangeListener<LocalDate> repStopViewListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            this.model.setSlutt(newValue);
        }
    };

    private ChangeListener<LocalDate> dateViewListener = (property, oldValue, newValue) -> {
        if (newValue != null) {
            this.model.setDato(newValue);
        }
    };

    public void initialize(){
        Appointment m = new Appointment();
        m.setFormal("Hei");
        m.setFra(LocalTime.now());
        m.setTil(LocalTime.now().plusHours(1));
        m.setRepetisjon(3);
        setModel(m);

        fromHourField.setValueFactory(fromHourFactory);
        fromMinuteField.setValueFactory(fromMinuteFactory);
        toHourField.setValueFactory(toHourFactory);
        toMinuteField.setValueFactory(toMinuteFactory);

        repetitionField.setValueFactory(repetitionFactory);

        fromHourField.setEditable(true);
        toHourField.setEditable(true);
        fromMinuteField.setEditable(true);
        toMinuteField.setEditable(true);

        repetitionField.setEditable(true);

        fromHourFactory.valueProperty().addListener(fromHourViewListener);
        fromMinuteFactory.valueProperty().addListener(fromMinuteViewListener);
        toHourFactory.valueProperty().addListener(toHourViewListener);
        toMinuteFactory.valueProperty().addListener(toMinuteViewListener);

        fromHourFactory.setConverter(stringToInt);
        toHourFactory.setConverter(stringToInt);
        fromMinuteFactory.setConverter(stringToInt);
        toMinuteFactory.setConverter(stringToInt);

        repetitionFactory.setConverter(stringToInt);

        roomField.textProperty().addListener(roomViewListener);
        formalField.textProperty().addListener(formalViewListener);
        stopField.valueProperty().addListener(repStopViewListener);
        dateField.valueProperty().addListener(dateViewListener);

        repetitionFactory.valueProperty().addListener(repetitionViewListener);

        dateField.setDayCellFactory(dayCellFactory);
        stopField.setDayCellFactory(dayCellFactory);

        updateView();
    }

    public void setModel(Appointment newModel) {
        if (this.model != null) {
            this.model.formalProperty().removeListener(formalListener);
            this.model.fraProperty().removeListener(fromListener);
            this.model.tilProperty().removeListener(toListener);
            this.model.repetisjonProperty().removeListener(repetitionListener);
            this.model.romProperty().removeListener(roomListener);
            this.model.datoProperty().removeListener(dateListener);
            this.model.sluttProperty().removeListener(repStopListener);

        }
        this.model = newModel;
        if (this.model != null) {
            this.model.formalProperty().addListener(formalListener);
            this.model.fraProperty().addListener(fromListener);
            this.model.tilProperty().addListener(toListener);
            this.model.repetisjonProperty().addListener(repetitionListener);
            this.model.romProperty().addListener(roomListener);
            this.model.datoProperty().addListener(dateListener);
            this.model.sluttProperty().addListener(repStopListener);
        }
    }

    private void checkMinuteLimit() {
        if (this.model.fraProperty().getValue().getHour()==this.model.tilProperty().getValue().getHour()) {
            toMinuteFactory.setMin(this.model.getFra().getMinute());
        } else {
            toMinuteFactory.setMin(0);
        }
    }

    private boolean validateRoomString(String text) {
        String pattern = "[A-ZÆØÅa-zæøå\\- ]+[ ][0-9]+";
        return text.matches(pattern);
    }

    public void updateView(){
        updateFormal();
        updateFromHour();
        updateFromMinute();
        updateToHour();
        updateToMinute();
        updateRepetition();

    }

    public void updateFormal() {
        formalField.setText(model.getFormal());
    }

    public void updateRoom() {
        roomField.setText(model.getRom());
    }

    public void updateFromHour() {
        int hour = this.model.fraProperty().getValue().getHour();
        fromHourField.getValueFactory().valueProperty().setValue(hour);
        toHourFactory.setMin(hour);
        checkMinuteLimit();
    }

    public void updateFromMinute() {
        int minute = this.model.fraProperty().getValue().getMinute();
        fromMinuteField.getValueFactory().valueProperty().setValue(minute);
        checkMinuteLimit();
    }

    public void updateToHour() {
        int hour = this.model.tilProperty().getValue().getHour();
        toHourField.getValueFactory().valueProperty().setValue(hour);
        checkMinuteLimit();
    }

    public void updateToMinute() {
        int minute = this.model.tilProperty().getValue().getMinute();
        toMinuteField.getValueFactory().valueProperty().setValue(minute);
        checkMinuteLimit();
    }

    public void updateRepStop() {
        LocalDate date = this.model.sluttProperty().getValue();
        stopField.setValue(date);
    }

    public void updateDate() {
        LocalDate date = this.model.datoProperty().getValue();
        dateField.setValue(date);
    }

    public void updateRepetition() {
        int reps = this.model.repetisjonProperty().getValue();
        repetitionField.getValueFactory().valueProperty().setValue(reps);
        if (reps == 0) {
            stopField.disableProperty().setValue(true);
            stopField.setValue(null);
            stopFieldText.disableProperty().setValue(true);
        } else {
            stopField.disableProperty().setValue(false);
            stopFieldText.disableProperty().setValue(false);
        }
    }
}
