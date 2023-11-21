package org.example;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Main {
    public static void main(String[] args) {
        var sf = HibernateUtil.getSessionFactory();
        Transaction transaction = null;
        try {
            var session = sf.openSession();

             transaction = session.beginTransaction();
            var employee = new Employee();
            employee.setName("John");
            employee.setCity("NewYork");

            session.save(employee);

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            transaction.commit();
            session.close();
            System.out.printf("Succesfully persist");
        } catch (HibernateException e) {
            e.printStackTrace();
            transaction.rollback();

        }
    }
}