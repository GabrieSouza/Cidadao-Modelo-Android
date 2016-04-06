package com.cidadaoandroid.principal;

import com.cidadaoandroid.entidades.Convenios;

import org.joda.time.DateTimeComparator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;


/**
 * Created by gabri on 30/03/2016.
 */
public class Comparar implements Comparator<Convenios> {
    DateTimeComparator timeComparator = DateTimeComparator.getTimeOnlyInstance();
    @Override
    public int compare(Convenios lhs, Convenios rhs) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateComparer, dateActual;

        try {
            dateActual = sdf.parse(lhs.getData_fim_vigencia());
            dateComparer = sdf.parse(rhs.getData_fim_vigencia());

            if (dateActual.after(dateComparer)) {
                return -1;
            }
            if (dateActual.before(dateComparer)) {
                return 1;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
        /*DateTime format = null;
        DateTime format1 = null;
        format = new DateTime(DateTime.parse(lhs.getData_fim_vigencia()));
        Log.e("COMPA1", String.valueOf(format));
        format1 = new DateTime(DateTime.parse(rhs.getData_fim_vigencia()));
        Log.e("COMPA2", String.valueOf(format1));
        Log.e("Comparar", String.valueOf(timeComparator.compare(format,format1)));
        return timeComparator.compare(format,format1);*/
    }

