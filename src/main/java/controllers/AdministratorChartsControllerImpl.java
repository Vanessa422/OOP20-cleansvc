package controllers;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
                chart.getStyler().setYAxisTicksVisible(true);

                panel.revalidate();
                panel.repaint();
        }       
            catch(IllegalArgumentException e){
                JOptionPane.showMessageDialog(panel, "Formato non valido, riprova.");
                throw e;
            }
        }
    
    public void resetChart(XYChart chart, JPanel panel) {           
        Thread thread = new Thread(new Runnable() {
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        chart.getSeriesMap().clear();
                        panel.revalidate();
                        panel.repaint();
                    }
                });
            }
        });
        thread.start();
    }


    public void deleteLast(XYChart chart, JPanel panel) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                       
                    }
                });
            }
            });
        thread.start();        
    }
       

    private String newLegendString(String dataArr, String dataPar, Integer scelta) {
        String choose = scelta.equals(DatiDaVisualizzareEnum.ENTRATE.getIndex()) ?  DatiDaVisualizzareEnum.ENTRATE.getItemName()
                                                                                     : DatiDaVisualizzareEnum.TEMPOLAVORO.getItemName();
        return new String("Da: " + dataPar + " a: " + dataArr + ", "+ choose);
    }
    
}
