package br.com.tk.mcs.Remote.Response;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by revolution on 04/02/16.
 */

public class RemotePaymentPermittedResponse
{

   private TransactionResponse transaction = null;
   private int responseCode;
   private int laneState;

   public RemotePaymentPermittedResponse()
   {
      setResponseCode(0);
      setLaneState(0);
      setTransaction(null);
   }

   public static RemotePaymentPermittedResponse toRemotePaymentPermittedResponse(Map<String, Object> remoteValue)
   {
      RemotePaymentPermittedResponse remote = new RemotePaymentPermittedResponse();
      TransactionResponse transaction = new TransactionResponse();
      PropertiesResponse properties = new PropertiesResponse();

      if (remoteValue.containsKey("responseCode"))
      {
         remote.setResponseCode((Integer) remoteValue.get("responseCode"));
      }

      if (remoteValue.containsKey("laneState"))
      {
         remote.setLaneState((Integer) remoteValue.get("laneState"));
      }

      try
      {
         //if ((transaction != null) && remoteValue.containsKey("transaction"))
         if(remoteValue.containsKey("transaction"))
         {
            Map<String, Object> transactionValue = (HashMap<String, Object>) remoteValue.get("transaction");

            if (transactionValue.containsKey("paymentType"))
            {
               transaction.setPaymentType(transactionValue.get("paymentType").toString());
            }

            if (transactionValue.containsKey("paidAmount"))
            {
               transaction.setPaidAmount((Integer) transactionValue.get("paidAmount"));
            }

            if (transactionValue.containsKey("vatPercentage"))
            {
               transaction.setVatPercentage((Integer) transactionValue.get("vatPercentage"));
            }

            if (transactionValue.containsKey("paymentDetails"))
            {
               transaction.setPaymentDetails(transactionValue.get("paymentDetails").toString());
            }

            if (transactionValue.containsKey("idZone"))
            {
               transaction.setIdZone((Integer) transactionValue.get("idZone"));
            }

            if (transactionValue.containsKey("exitTime"))
            {
               transaction.setExitTime(transactionValue.get("exitTime").toString());
            }

            if (transactionValue.containsKey("paymentMeans"))
            {
               transaction.setPaymentMeans(transactionValue.get("paymentMeans").toString());
            }

            if (transactionValue.containsKey("vatAmount"))
            {
               transaction.setVatAmount((Integer) transactionValue.get("vatAmount"));
            }

            if (transactionValue.containsKey("moment"))
            {
               transaction.setMoment(transactionValue.get("moment").toString());
            }

            if (transactionValue.containsKey("panNumber"))
            {
               transaction.setPanNumber(transactionValue.get("panNumber").toString());
            }

            if (transactionValue.containsKey("vehicleClass"))
            {
               transaction.setVehicleClass(transactionValue.get("vehicleClass").toString());
            }

            if (transactionValue.containsKey("fareAmount"))
            {
               transaction.setFareAmount((Integer) transactionValue.get("fareAmount"));
            }

            if (transactionValue.containsKey("transactionId"))
            {
               transaction.setTransactionid(transactionValue.get("transactionId").toString());
            }

            if (transactionValue.containsKey("properties"))
            {

               Map<String, Object> propertiesValue = (HashMap<String, Object>) transactionValue.get("properties");

               if (propertiesValue.containsKey("vehicleSubclass"))
               {
                  properties.setVehicleSubclass(propertiesValue.get("vehicleSubclass").toString());
               }
            }
         }

         remote.setTransaction(transaction);
         remote.getTransaction().setProperties(properties);
      }
      catch(Exception e)
      {
         //
         Log.e(RemotePaymentPermittedResponse.class.getName(), e.getMessage());
      }
      return remote;
   }

   public TransactionResponse getTransaction()
   {
      return transaction;
   }

   public void setTransaction(TransactionResponse transaction)
   {
      this.transaction = transaction;
   }

   public int getResponseCode()
   {
      return responseCode;
   }

   public void setResponseCode(int responseCode)
   {
      this.responseCode = responseCode;
   }

   public int getLaneState()
   {
      return laneState;
   }

   public void setLaneState(int laneState)
   {
      this.laneState = laneState;
   }
}