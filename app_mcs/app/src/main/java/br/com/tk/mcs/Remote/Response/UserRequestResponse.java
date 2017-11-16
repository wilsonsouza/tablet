package br.com.tk.mcs.Remote.Response;

/**
 * Created by revolution on 04/02/16.
 */

public enum UserRequestResponse
{
   Authorized(1), NoAuthorized(2), Error(3);

   private int value;

   UserRequestResponse(int value)
   {
      this.value = value;
   }

   public int value()
   {
      return value;
   }

   public static UserRequestResponse fromValue(int value)
   {
      if (value > 0)
      {
         return Authorized;
      }
      else if (value == 0)
      {
         return NoAuthorized;
      }
      return Error;
   }
}
