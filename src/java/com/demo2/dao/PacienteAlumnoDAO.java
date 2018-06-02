/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.demo2.dao;

import com.demo2.models.PacienteAlumno;
import com.demo2.utils.NewHibernateUtil;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author raul
 */
public class PacienteAlumnoDAO {
    public void addPacienteAlumno(PacienteAlumno pacienteAlumno){
        Transaction tx = null;
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        try{
            tx = session.beginTransaction();
            session.save(pacienteAlumno);
            session.getTransaction().commit();
        }catch(Exception e){
            e.printStackTrace();
            tx.rollback();
        }finally{
            session.flush();
            session.close();
        }
    }
    
    public PacienteAlumno findPacienteAlumnoById(int dniAlumno){
        PacienteAlumno pacienteAlumno = null;
        Transaction trns = null;
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        try{
            trns = session.beginTransaction();
            String queryString = "from Libro where dniAlumno = :idToFind";
            Query query = session.createQuery(queryString);
            query.setInteger("idToFind", dniAlumno);
            pacienteAlumno = (PacienteAlumno) query.uniqueResult();
        }catch(RuntimeException e){
            e.printStackTrace();
        }finally{
            session.flush();
            session.close();
        }
        return pacienteAlumno;
    }
    
    public List<PacienteAlumno> listaPacienteAlumnoTodos(){
        Session session = NewHibernateUtil.getSessionFactory().openSession();
        String hql = " from PacienteAlumno";        
        List<PacienteAlumno> pacienteAlumnos= new ArrayList<>();
        try{
            Query query = session.createQuery(hql);
            pacienteAlumnos= (List<PacienteAlumno>) query.list();
            
        }catch(RuntimeException e){
            e.printStackTrace();
        }finally{
            session.flush();
            session.close();
        }
        return pacienteAlumnos;
    }
    
    
    
}
