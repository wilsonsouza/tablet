package br.com.tk.mcs.Remote.Response;

/**
 * Created by revolution on 05/02/16.
 */

public class TransactionResponse
{
   private String paymentType;
   private int paidAmount;
   private int vatPercentage;
   private PropertiesResponse properties = null;
   private String paymentDetails;
   private Integer idZone = 0;
   private String exitTime;
   private String paymentMeans;
   private Integer vatAmount = 0;
   private String moment;
   private String panNumber;
   private String tagNumber;
   private String vehicleClass;
   private Integer fareAmount = 0;
   private String transactionid;

   public TransactionResponse()
   {
      setPaymentType("");
      setPaidAmount(0);
      setVatPercentage(0);
      setProperties(null);
      setPaymentDetails("");
      setIdZone(0);
      setExitTime("");
      setPaymentMeans("");
      setVatAmount(0);
      setMoment("");
      setPanNumber("");
      setVehicleClass("");
      setFareAmount(0);
      setTransactionid("");
      setTagNumber("");
   }

   public String getPaymentType()
   {
      return paymentType;
   }

   public void setPaymentType(String paymentType)
   {
      this.paymentType = paymentType;
   }

   public int getPaidAmount()
   {
      return paidAmount;
   }

   public void setPaidAmount(int paidAmount)
   {
      this.paidAmount = paidAmount;
   }

   public int getVatPercentage()
   {
      return vatPercentage;
   }

   public void setVatPercentage(int vatPercentage)
   {
      this.vatPercentage = vatPercentage;
   }

   public PropertiesResponse getProperties()
   {
      return properties;
   }

   public void setProperties(PropertiesResponse properties)
   {
      this.properties = properties;
   }

   public String getPaymentDetails()
   {
      return paymentDetails;
   }

   public void setPaymentDetails(String paymentDetails)
   {
      this.paymentDetails = paymentDetails;
   }

   public Integer getIdZone()
   {
      return idZone;
   }

   public void setIdZone(Integer idZone)
   {
      this.idZone = idZone;
   }

   public String getExitTime()
   {
      return exitTime;
   }

   public void setExitTime(String exitTime)
   {
      this.exitTime = exitTime;
   }

   public String getPaymentMeans()
   {
      return paymentMeans;
   }

   public void setPaymentMeans(String paymentMeans)
   {
      this.paymentMeans = paymentMeans;
   }

   public Integer getVatAmount()
   {
      return vatAmount;
   }

   public void setVatAmount(Integer vatAmount)
   {
      this.vatAmount = vatAmount;
   }

   public String getMoment()
   {
      return moment;
   }

   public void setMoment(String moment)
   {
      this.moment = moment;
   }

   public String getPanNumber()
   {
      return panNumber;
   }

   public void setPanNumber(String panNumber)
   {
      this.panNumber = panNumber;
   }

   public String getTagNumber()
   {
      return tagNumber;
   }

   public void setTagNumber(String panNumber)
   {
      this.tagNumber = panNumber;
   }

   public String getVehicleClass()
   {
      return vehicleClass;
   }

   public void setVehicleClass(String vehicleClass)
   {
      this.vehicleClass = vehicleClass;
   }

   public Integer getFareAmount()
   {
      return fareAmount;
   }

   public void setFareAmount(Integer fareAmount)
   {
      this.fareAmount = fareAmount;
   }

   public String getTransactionid()
   {
      return transactionid;
   }

   public void setTransactionid(String transactionid)
   {
      this.transactionid = transactionid;
   }
}
