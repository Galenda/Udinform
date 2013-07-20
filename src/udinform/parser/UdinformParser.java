/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package udinform.parser;

import org.jsoup.Jsoup;
//import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 * Udinform  USD and EUR Parser
 * 
 * Программа получает данные о максимальном межбанковском курсе USD и  EUR
 * на сегодняшний день, и выводит курсы пользователю. Если сегодня не банковский
 * день - это сообщается пользователю и показывается информация ближайшего 
 * банковского дня.
 * 
 * @author GalAnt
 */
public class UdinformParser {
    static String msg=""; //выводимое сообщение
    // функция получает данные с сайта, оставляет только нужные, и осуществляет вывод для пользователя.
    // день, месяц, год - входящие данные
    public static int Parse (int d, int  m, int y){
        // формируем запрос на сайт
        String url = "http://www.udinform.com/index.php?option=com_ab_calendar&month="+m+"&year="+y+"&day="+d+"&Itemid=37";
        try {
            Document doc = Jsoup.connect(url).get(); 
            // отбираем необходимые значения
            Elements bold = doc.getElementsByAttributeValue("style", "color: red");        
            int count = 1;
            // Если выборка пустая - значит этот день был не банковский
            if (bold.isEmpty()){
                System.out.println(d+"."+m+"."+y+": Данные недоступны");
                msg +=d+"."+m+"."+y+": Данные недоступны \n";
                return 1;
            }else {
            // Если выборка имеет значения - формируем вывод
            // вывод будет в окно, дублируем в консоль    
            System.out.println("");    
            msg +="\n";
            for (Element src : bold){           
                switch (count){
                    case 1 :System.out.println(src.text()+" на "+ d+"."+m+"."+y);
                            msg +=src.text()+" на "+ d+"."+m+"."+y+"\n";
                            count++;
                            break;
                    case 2 :System.out.println("Доллар: "+src.text());
                            msg +="Доллар: "+src.text()+"\n";
                            count++;
                            break;
                    case 3 :System.out.println("Евро  : "+src.text());
                            msg +="Евро  : "+src.text()+"\n";
                            count++;
                            break;
                }
            }
            }
        }
        catch (IOException e) {}
        // выводим данные пользователю
        JOptionPane.showMessageDialog(null, msg, "http://udinform.com by GalAnt", 1);
        return 0;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        // Получаем сегодняшнюю дату и время
        Calendar date = Calendar.getInstance();
        //Если еще нет 16:00, надо брать вчерашний день
        int hour = date.get(Calendar.HOUR_OF_DAY);
        if (hour<16) {
            date.add(Calendar.DATE,-1);
        }
        // устанавливаем "сегодня" как начльное значение
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH)+1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        // ищем первый банковский день и выводим пользователю
        while (Parse(day,month,year)==1) {           
           date.add(Calendar.DATE,-1);
           year = date.get(Calendar.YEAR);
           month = date.get(Calendar.MONTH)+1;
           day = date.get(Calendar.DAY_OF_MONTH);
        }
    }    
}