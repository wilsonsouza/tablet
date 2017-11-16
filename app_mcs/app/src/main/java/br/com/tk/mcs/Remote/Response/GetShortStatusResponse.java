package br.com.tk.mcs.Remote.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by revolution on 03/02/16.
 */

public class GetShortStatusResponse
{

   private String laneName;
   private String moment;
   private int laneMode;
   private int laneState;
   private int levelAlarm;
   private String operatorCode;
   private String operatorNameBin;
   private DeviceResponse device = null;

   public GetShortStatusResponse()
   {
      setLaneName("");
      setMoment("");
      setLaneMode(0);
      setLaneState(0);
      setLevelAlarm(0);
      setOperatorCode("");
      setOperatorNameBin("");
      setDevice(null);
   }

   public static GetShortStatusResponse toGetShortStatusResponse(Map<String, Object> shortStatus)
   {
      String res;
      GetShortStatusResponse status = new GetShortStatusResponse();
      DeviceResponse device = new DeviceResponse();

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

      try
      {
         //09-20-2016
         //if ((null != device) && shortStatus.containsKey("devices"))
         if (shortStatus.containsKey("devices"))
         {
            Map<String, Object> devices = (HashMap<String, Object>) shortStatus.get("devices");

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
         status.device = device;
      }
      catch (Exception e)
      {
         //none for while
      }
      return status;
   }

   public String getMoment()
   {
      return moment;
   }

   public void setMoment(String moment)
   {
      this.moment = moment;
   }

   public int getLaneState()
   {
      return laneState;
   }

   public void setLaneState(int laneState)
   {
      this.laneState = laneState;
   }

   public String getLaneName()
   {
      return laneName;
   }

   public void setLaneName(String laneName)
   {
      this.laneName = laneName;
   }

   public int getLevelAlarm()
   {
      return levelAlarm;
   }

   public void setLevelAlarm(int levelAlarm)
   {
      this.levelAlarm = levelAlarm;
   }

   public int getLaneMode()
   {
      return laneMode;
   }

   public void setLaneMode(int laneMode)
   {
      this.laneMode = laneMode;
   }

   public DeviceResponse getDevice()
   {
      return device;
   }

   public void setDevice(DeviceResponse device)
   {
      this.device = device;
   }

   public String getOperatorCode()
   {
      return operatorCode;
   }

   public void setOperatorCode(String operatorCode)
   {
      this.operatorCode = operatorCode;
   }

   public String getOperatorNameBin()
   {
      return operatorNameBin;
   }

   public void setOperatorNameBin(String operatorNameBin)
   {
      this.operatorNameBin = operatorNameBin;
   }
}
