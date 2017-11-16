/*

   Sistema de Gestão de Pistas

   (C) 2016 Tecsidel

   Updated by wilson.souza (WR DevInfo)

   Description:

   Changes: replace all Map and HashMap class by ConcurrentMap and ConcurrentHashMap (thread safe)
   Change Object[] to ArrayList<Object> and convert toArray() -> Object[]
 */
package br.com.tk.mcs.Lane;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.tk.mcs.Generic.Company;
import br.com.tk.mcs.Remote.Response.GetLongStatusResponse;
import br.com.tk.mcs.Remote.Response.GetShortStatusResponse;
import br.com.tk.mcs.Remote.Response.RemotePaymentPermittedResponse;
import br.com.tk.mcs.Remote.Response.RemotePaymentResponse;
import br.com.tk.mcs.Remote.Response.TagPlateResponse;
import br.com.tk.mcs.Remote.Response.UserRequestResponse;
import br.com.tk.mcs.Remote.XMLRPCInfo;
import br.com.tk.mcs.Remote.XMLRPCManager;

/**
 * Created by revolution on 12/02/16.
 */

public class Operations implements Serializable
{
   private int m_timeout = 0x100;
   private String m_url;
   private String m_operator;
   private final static String m_sPAYMENTTYPE_PAYMENT = "P";
   private final static String m_sPAYMENTMEANS_CASH = "CA";
   private final static String m_sPAYMENTMEANS_TAG = "TG";

   public enum Tasks
   {
      opChangeResponsable(0),
      opChangeState(1),
      opBarrierExit(2),
      eOperCambiarBarreraEntrada(3),
      opTrafficLightPurple(4),
      opTrafficLightGreen(5),
      eOperSimula(6),
      eOperResetSas(7),
      eOperPagoRemoto(8),
      eOperReclasificacion(9),
      eOperConfAlarmasLevel1(10),
      eOperResetPC(11),
      eOperCambiarBarrerasOversized(12),
      eOperCambiarSemaforoSalida(13),
      eOpercommutarCofre(14),
      eOperCambiarBarreraEscape(15),
      eOperCargaHoppers(16),
      eOperDevuelveCambio(17),
      eOperCambiaSentidoVia(18),
      eOperImprimirRecibo(21);

      private final int m_id;

      Tasks(int id)
      {
         this.m_id = id;
      }

      public int getValue()
      {
         return m_id;
      }
   }

   private String m_szCupom = "";
   //-----------------------------------------------------------------------------------------------------------------//
   public Operations(String url, final String szOperator)
   {
      this.m_url = url;
      this.m_operator = szOperator;
      this.m_szCupom = "";
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void SetOperatorID(String szOperatorID) //set_operator_id setOperatorId
   {
      this.m_operator = szOperatorID;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public void setTimeOut(int seconds)
   {
      this.m_timeout = seconds;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @SuppressWarnings("unchecked")
   public GetLongStatusResponse getLongStatus()
   {
      try
      {
         Map<String, Object> map = null;
         XMLRPCManager net = new XMLRPCManager(m_url, m_timeout);
         {
            net.execute(new XMLRPCInfo(XMLRPCInfo.XMLRPCMethods.getLongStatus));
            map = (Map) net.get();
         }
         return GetLongStatusResponse.toGetLongStatusResponse(map);
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
         //e.printStackTrace();
      }
      return null;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @SuppressWarnings("unchecked")
   public GetShortStatusResponse getShortStatus()
   {
      try
      {
         Map<String, Object> map = null;
         XMLRPCManager net = new XMLRPCManager(m_url, m_timeout);
         {
            net.execute(new XMLRPCInfo(XMLRPCInfo.XMLRPCMethods.getShortStatus));
            map = (Map) net.get();
         }
         return GetShortStatusResponse.toGetShortStatusResponse(map);
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
         //e.printStackTrace();
      }
      return null;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public UserRequestResponse userRequest(final String user, final String pass)
   {
      try
      {
         Object[] obj = null;
         XMLRPCManager net = new XMLRPCManager(m_url, m_timeout);
         {
            this.m_operator = user;
            net.execute(new XMLRPCInfo(XMLRPCInfo.XMLRPCMethods.userRequest, new String[]{user, pass}));
            obj = (Object[]) net.get();
         }
         return UserRequestResponse.fromValue(Integer.parseInt(obj[0].toString()));
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
         //e.printStackTrace();
      }
      return UserRequestResponse.Error;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public TagPlateResponse tagPlateRequest(final String tagPlate)
   {
      try
      {
         Object[] obj = null;
         XMLRPCManager net = new XMLRPCManager(m_url, m_timeout);
         {
            net.execute(new XMLRPCInfo(XMLRPCInfo.XMLRPCMethods.tagPlateRequest, new String[]{tagPlate}));
            obj = (Object[]) net.get();
         }
         return TagPlateResponse.fromValue(Integer.parseInt(obj[0].toString()));
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
         //e.printStackTrace();
      }
      return null;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public Boolean setLaneON()
   {
      Object res = ((HashMap) setTask(Tasks.opChangeState, m_operator, 2)).get("responseCode");
      return ((Integer) res == 0);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public Boolean setLaneOFF()
   {
      Object res = ((HashMap) setTask(Tasks.opChangeState, m_operator, 0)).get("responseCode");
      return ((Integer) res == 0);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public Boolean setBarrierON()
   {
      Object res = ((HashMap) setTask(Tasks.opBarrierExit, m_operator, 1)).get("responseCode");
      return ((Integer) res == 0);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public Boolean setBarrierOFF()
   {
      Object res = ((HashMap) setTask(Tasks.opBarrierExit, m_operator, 0)).get("responseCode");
      return ((Integer) res == 0);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public Boolean setTrafficLightON()
   {
      Object res = ((HashMap) setTask(Tasks.opTrafficLightGreen, m_operator, 1)).get("responseCode");
      return ((Integer) res == 0);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public Boolean setTrafficLightOFF()
   {
      Object res = ((HashMap) setTask(Tasks.opTrafficLightPurple, m_operator, 0)).get("responseCode");
      return ((Integer) res == 0);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public Boolean setLaneOperator()
   {
      Object res = ((HashMap) setTask(Tasks.opChangeResponsable, m_operator)).get("responseCode");
      return ((Integer) res == 0);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Boolean setClearQueue()
   {
      Object pResult = ((HashMap) setTask(Tasks.eOperResetSas, m_operator)).get("responseCode");
      return ((Integer) pResult == 0);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public boolean SimulePassager()
   {
      Object pSuccess = ((HashMap) setTask(Tasks.eOperSimula, m_operator)).get("responseCode");
      return Integer.parseInt(pSuccess.toString()) == 0;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   private Object setTask(Tasks task, String code, int param, String payment)
   {
      return setOperations(task, code, param, payment);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   private Object setTask(Tasks task, String code, int param)
   {
      return setOperations(task, code, param, "");
   }

   //-----------------------------------------------------------------------------------------------------------------//
   private Object setTask(Tasks task, String code)
   {
      return setOperations(task, code, 0, "");
   }

   //-----------------------------------------------------------------------------------------------------------------//
   private Object setOperations(Tasks task, String code, int param, String payment)
   {
      try
      {
         Object Result = null;
         Map<String, Object> map = new HashMap<>();
         {
            map.put("operationCode", task.getValue());
            map.put("operatorCode", code);
            map.put("parameter_VAL", param);
            map.put("payment_VAL", payment);
         }
         XMLRPCManager net = new XMLRPCManager(m_url, m_timeout);
         {
            net.execute(new XMLRPCInfo(XMLRPCInfo.XMLRPCMethods.operationRequest, map));
            Result = net.get();
         }
         return Result;
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
         //e.printStackTrace();
      }
      return null;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   @SuppressWarnings("unchecked")
   public RemotePaymentPermittedResponse isRemotePaymentPermitted()
   {
      try
      {
         Object obj = null;
         XMLRPCManager net = new XMLRPCManager(m_url, m_timeout);
         {
            net.execute(new XMLRPCInfo(XMLRPCInfo.XMLRPCMethods.isRemotePaymentPermitted));
            obj = net.get();
         }
         return RemotePaymentPermittedResponse.toRemotePaymentPermittedResponse(((Map) obj));
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
         //e.printStackTrace();
      }
      return null;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   // release vehicle stopped on the lane
   @SuppressWarnings("unchecked")
   public RemotePaymentResponse remotePayment(RemotePaymentPermittedResponse remote, String tagPlate, String szIsentoCode)
   {
      try
      {
         boolean bFree = (szIsentoCode != null);
         Map<String, Object> res = null;
         Map<String, Object> map = new HashMap();
         {
            map.put("pan", (tagPlate.length() > 7) ? tagPlate : "");
            map.put("plate", (tagPlate.length() == 7) ? tagPlate : "");

            if (bFree)
            {
               map.put("exemptGroup", szIsentoCode);
            }
         }
         ArrayList<Object> params = new ArrayList<>();
         {
            params.add(this.m_operator);
            params.add("");
            params.add(remote.getTransaction().getVehicleClass());
            params.add(remote.getTransaction().getTransactionid());
            //
            if(bFree)
            {
               params.add("E");
               params.add("NO");
            }
            else
            {
               params.add("P");
               params.add("TG");
            }
            //
            params.add(map);
            params.add(remote.getTransaction().getProperties().getVehicleSubclass());
         }
         Object[] transaction = params.toArray();
         XMLRPCManager net = new XMLRPCManager(m_url, m_timeout);
         {
            net.execute(new XMLRPCInfo(XMLRPCInfo.XMLRPCMethods.remotePayment, transaction));
            res = (Map) net.get();
         }
         return RemotePaymentResponse.fromValue(Integer.parseInt(res.get("responseCode").toString()));
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
         //e.printStackTrace();
      }
      return RemotePaymentResponse.ResponseERROR;
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public RemotePaymentResponse paymentRDCash(String id, String vehicleClass)
   {
      return paymentRD(id, vehicleClass, "", m_sPAYMENTTYPE_PAYMENT, m_sPAYMENTMEANS_CASH);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   public RemotePaymentResponse paymentRDTag(String id, String tagPlate)
   {
      return paymentRD(id, "", tagPlate, m_sPAYMENTTYPE_PAYMENT, m_sPAYMENTMEANS_TAG);
   }

   //-----------------------------------------------------------------------------------------------------------------//
   // process payment of RD within money or TAG
   private RemotePaymentResponse paymentRD(String id, String vehicleClass, String tagPlate, String paymentType,
      String paymentMeans)
   {
      try
      {
         Map<String, Object> map = new HashMap<>();
         {
            map.put("pan", (tagPlate.length() > 7) ? tagPlate : "");
            map.put("plate", (tagPlate.length() == 7) ? tagPlate : "");
         }
         ArrayList<Object> params = new ArrayList<>();
         {
            /* if sp99 use old payment method only supported by sp99 in current version
            if(Company.IsTamoios)
            {
               params.add(this.m_operator);
               params.add("");
               params.add(vehicleClass);
               params.add(id);
               params.add(paymentType);
               params.add(paymentMeans);
               params.add(map);
            }
            else*/
            {
               params.add(id);
               params.add(vehicleClass);
               params.add(tagPlate);
               params.add(this.m_operator);
               params.add(paymentType);
               params.add(paymentMeans);
               params.add(map);
            }
         }
         this.m_szCupom = "";
         Object[] transaction = params.toArray();
         String fmt = "";

         if(Company.IsDebug || Company.IsTecsidel)
         {
            if (vehicleClass.length() == 0)
            {
               fmt = String.format("ID: %s Plate: %s with TAG", id, tagPlate);
            }
            else
            {
               fmt = String.format("ID: %s ClassId: %s with Cash", id, vehicleClass);
            }

            Log.e("paymentRD", fmt);
         }

         Object[] result = null;
         XMLRPCManager net = new XMLRPCManager(m_url, m_timeout);
         {
            net.execute(new XMLRPCInfo(XMLRPCInfo.XMLRPCMethods.paymentRD, transaction));
            result = (Object[]) net.get();
         }
         RemotePaymentResponse hSuccess = RemotePaymentResponse.fromValue(Integer.parseInt(result[0].toString()));

         if(hSuccess == RemotePaymentResponse.ResponseOK && paymentMeans.equals(m_sPAYMENTMEANS_CASH) && result.length > 1)
         {
            this.m_szCupom = result[1].toString();
         }
         return hSuccess;
      }
      catch (Exception e)
      {
         Log.e(this.getClass().getName(), e.getMessage());
      }
      return RemotePaymentResponse.ResponseERROR;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public final String GetCupom()
   {
      return this.m_szCupom;
   }
}
