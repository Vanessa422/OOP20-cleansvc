package controllers;

import java.time.LocalDate;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.style.markers.SeriesMarkers;
import com.github.lgooddatepicker.components.DatePicker;
import model.DataCharts;
import model.DateException;
import model.DatiDaVisualizzareEnum;

public class AdministratorChartsControllerImpl implements AdministratorChartsController{
    
    public AdministratorChartsControllerImpl() {
        
    }
    
    @Override
    public void onButtonPressed(DatePicker dateStart, DatePicker dateEnd, int choice, JPanel panel, XYChart chart) throws DateException {
      
        // TODO Auto-generated method stub 
        LocalDate dataPartenza, dataArrivo;
        Integer scelta = choice;
        DateException dateExc = new DateException(panel);
        try {
            if((dateStart.getText().isBlank() || dateStart.getText().isEmpty())
                    || (dateEnd.getText().isBlank() || dateEnd.getText().isBlank())) {
                dateExc.warning(panel);
                throw dateExc;
            }    
                dataPartenza = dateStart.getDate();
                dataArrivo = dateEnd.getDate();
                    
                    if(dataArrivo.isBefore(dataPartenza)) {
                        dateExc.warning(panel);
                        throw dateExc;
                    }
        
                DataCharts dataChart = new DataCharts();
                
                chart.addSeries( this.newLegendString(dataArrivo.toString(), dataPartenza.toString(), scelta),
                                        dataChart.getDaysDate(dataPartenza, dataArrivo), 
                                            dataChart.buildChartsFromData(dataPartenza, dataArrivo, scelta)).setMarker(SeriesMarkers.NONE);
                chart.getStyler().setXAxisTicksVisible(true);

                panel.revalidate();
                panel.repaint();
        }       
            catch(IllegalArgumentException e){
                JOptionPane.showMessageDialog(panel, "Formato non valido, riprova.");
                throw e;
            }
        }
    
    public void resetChart(XYChart chart, JPanel panel) {
       for(String s : chart.getSeriesMap().keySet()) {
           chart.removeSeries(s);
       }
       panel.revalidate();
       panel.repaint();
    }

    private String newLegendString(String dataArr, String dataPar, Integer scelta) {
        String choose = scelta.equals(DatiDaVisualizzareEnum.ENTRATE.getIndex()) ?  DatiDaVisualizzareEnum.ENTRATE.getItemName()
                                                                                     : DatiDaVisualizzareEnum.TEMPOLAVORO.getItemName();
        return new String("Da: " + dataPar + " a: " + dataArr + ", "+ choose);
    }
    
}
