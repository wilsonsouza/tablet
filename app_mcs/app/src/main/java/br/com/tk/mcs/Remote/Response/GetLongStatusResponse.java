package br.com.tk.mcs.Remote.Response;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by revolution on 03/02/16.
 */

public class GetLongStatusResponse
{
   private String laneName;
   private String moment;
   private Integer laneMode;
   private Integer laneState;
   private Integer levelAlarm;
   private String operatorCode;
   private String operatorNameBin;
   private DeviceResponse device = null;
   private List<String> traffics = null;
   private Vector<DetailArray> m_traffics;
   //-----------------------------------------------------------------------------------------------------------------//
   public static class DetailArray
   {
      public String TransactionId;
      public String Moment;
      public String PaymentType;
      public String PaymentMeans;
      public String PanNumber;
      public String VehicleClass;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public GetLongStatusResponse(int laneState, DeviceResponse device)
   {
      setLaneState(laneState);
      setLaneName("");
      setMoment("");
      setLaneMode(0);
      setLevelAlarm(0);
      setOperatorCode("");
      setOperatorNameBin("");
      setDevice(device);
      setTraffics(null);
      this.SetDetailArray(null);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public GetLongStatusResponse()
   {
      setLaneName("");
      setMoment("");
      setLaneMode(0);
      setLaneState(0);
      setLevelAlarm(0);
      setOperatorCode("");
      setOperatorNameBin("");
      setDevice(null);
      setTraffics(null);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @SuppressWarnings("unchecked")
   public static GetLongStatusResponse toGetLongStatusResponse(Map<String, Object> longStatus) throws Exception
   {
      String res;
      GetLongStatusResponse status = new GetLongStatusResponse();
      DeviceResponse device = new DeviceResponse();
      List<String> transaction = new ArrayList<>();

      try
      {
         if (longStatus.containsKey("ShortStatus"))
         {
            Map<String, Object> shortStatus = (HashMap) longStatus.get("ShortStatus");

            if (shortStatus.containsKey("moment"))
            {
               status.setMoment(shortStatus.get("moment").toString());
            }

            if (shortStatus.containsKey("laneState"))
            {
               status.setLaneState((Integer) shortStatus.get("laneState"));
            }

            if (shortStatus.containsKey("laneName"))
            {
               status.setLaneName(shortStatus.get("laneName").toString());
            }

            if (shortStatus.containsKey("levelAlarm"))
            {
               status.setLevelAlarm((Integer) shortStatus.get("levelAlarm"));
            }

            if (shortStatus.containsKey("laneMode"))
            {
               status.setLaneMode((Integer) shortStatus.get("laneMode"));
            }

            if (shortStatus.containsKey("operatorCode"))
            {
               status.setOperatorCode(shortStatus.get("operatorCode").toString());
            }

            if (shortStatus.containsKey("operatorNameBin"))
            {
               status.setOperatorNameBin(shortStatus.get("operatorNameBin").toString());
            }
            //if (device != null && shortStatus.containsKey("devices"))
            if (shortStatus.containsKey("devices"))
            {
               Map<String, Object> devices = (HashMap) shortStatus.get("devices");

               if (devices.containsKey("lightStateCanopy"))
               {
                  device.setLightStateCanopy(devices.get("lightStateCanopy").toString());
               }

               if (devices.containsKey("lightStateEntry"))
               {
                  res = devices.get("lightStateEntry").toString();
                  device.setLightStateEntry(!res.isEmpty() ? Integer.parseInt(res) : 0);
               }

               if (devices.containsKey("lightStateExit"))
               {
                  res = devices.get("lightStateExit").toString();
                  device.setLightStateExit(!res.isEmpty() ? Integer.parseInt(res) : 0);
               }

               if (devices.containsKey("antennaOBU"))
               {
                  device.setAntennaOBU(devices.get("antennaOBU").toString());
               }

               if (devices.containsKey("antennaOBU2"))
               {
                  device.setAntennaOBU2(devices.get("antennaOBU2").toString());
               }

               if (devices.containsKey("barrierExit"))
               {
                  res = devices.get("barrierExit").toString();
                  device.setBarrierExit(!res.isEmpty() ? Integer.parseInt(res) : 0);
               }

               if (devices.containsKey("aspectCameraEntry"))
               {
                  device.setAspectCameraEntry(devices.get("aspectCameraEntry").toString());
               }

               if (devices.containsKey("aspectCameraEntry2"))
               {
                  device.setAspectCameraEntry2(devices.get("aspectCameraEntry2").toString());
               }

               if (devices.containsKey("aspectCameraExit"))
               {
                  device.setAspectCameraExit(devices.get("aspectCameraExit").toString());
               }

               if (devices.containsKey("aspectCameraExit2"))
               {
                  device.setAspectCameraExit2(devices.get("aspectCameraExit2").toString());
               }

               if (devices.containsKey("zoneEntry"))
               {
                  device.setZoneEntry(devices.get("zoneEntry").toString());
               }

               if (devices.containsKey("zoneExit"))
               {
                  device.setZoneExit(devices.get("zoneExit").toString());
               }

               if (devices.containsKey("lastPaymentType"))
               {
                  device.setLastPaymentType(devices.get("lastPaymentType").toString());
               }

               if (devices.containsKey("lastPaymentMeans"))
               {
                  device.setLastPaymentMeans(devices.get("lastPaymentMeans").toString());
               }

               if (devices.containsKey("currentOcr"))
               {
                  device.setCurrentOcr(devices.get("currentOcr").toString());
               }

               if (devices.containsKey("totalVehicles"))
               {
                  res = devices.get("totalVehicles").toString();
                  device.setTotalVehicles(!res.isEmpty() ? Integer.parseInt(res) : 0);
               }

               if (devices.containsKey("vehicleStopped"))
               {
                  res = devices.get("vehicleStopped").toString();
                  device.setVehicleStopped(!res.isEmpty() ? Integer.parseInt(res) : 0);
               }
            }
         }

         if (longStatus.containsKey("DetailArray"))
         {

            Object[] detail = (Object[]) longStatus.get("DetailArray");
            Vector<DetailArray> queue = new Vector<>();

            for (final Object obj : detail)
            {
               Map<String, String> current = (HashMap) obj;
               transaction.add(current.get("transactionId") + " " + current.get("moment") + " " + current.get("paymentType") + " " + current.get("paymentMeans") + " " + current.get("panNumber") + " " + current.get("vehicleClass"));

               DetailArray p = new DetailArray();
               {
                  p.TransactionId = current.get("transactionId");
                  p.Moment = current.get("moment");
                  p.PaymentType = current.get("paymentType");
                  p.PaymentMeans = current.get("paymentMeans");
                  p.PanNumber = current.get("panNumber");
                  p.VehicleClass = current.get("vehicleClass");
                  queue.add(p);
               }
            }

            status.SetDetailArray(queue);

            if (transaction.size() > 0)
            {
               Collections.reverse(transaction);
            }
         }

         status.setDevice(device);
         status.setTraffics(transaction);
      }
      catch (Exception e)
      {
         Log.e(GetLongStatusResponse.class.getName(), e.getMessage());
      }
      return status;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public String getMoment()
   {
      return moment;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public  void setMoment(String moment)
   {
      this.moment = moment;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Integer getLaneState()
   {
      return laneState;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setLaneState(Integer laneState)
   {
      this.laneState = laneState;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public String getLaneName()
   {
      return laneName;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setLaneName(String laneName)
   {
      this.laneName = laneName;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Integer getLevelAlarm()
   {
      return levelAlarm;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setLevelAlarm(Integer levelAlarm)
   {
      this.levelAlarm = levelAlarm;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Integer getLaneMode()
   {
      return laneMode;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public  void setLaneMode(Integer laneMode)
   {
      this.laneMode = laneMode;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public DeviceResponse getDevice()
   {
      return device;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setDevice(DeviceResponse device)
   {
      this.device = device;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public String getOperatorCode()
   {
      return operatorCode;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setOperatorCode(String operatorCode)
   {
      this.operatorCode = operatorCode;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public String getOperatorNameBin()
   {
      return operatorNameBin;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setOperatorNameBin(String operatorNameBin)
   {
      this.operatorNameBin = operatorNameBin;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public List<String> getTraffics()
   {
      return traffics;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void setTraffics(List<String> traffics)
   {
      this.traffics = traffics;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public Vector<DetailArray> GetDetailArray()
   {
      return this.m_traffics;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetDetailArray(Vector<DetailArray> pItems)
   {
      if(pItems.size() > 0)
      {
         this.m_traffics = pItems;
      }
      else
      {
         this.m_traffics = null;
      }
   }
}
