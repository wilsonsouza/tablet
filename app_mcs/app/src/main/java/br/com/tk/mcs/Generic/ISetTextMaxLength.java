
/*

   Sistema de Gestão de Pistas

   (C) 2016 TecSidel

   Created by wilson.souza (WR DevInfo)

   Description:
 */
package br.com.tk.mcs.Generic;

/**
 * Created by wilsonsouza on 07/12/16.
 */

public class ISetTextMaxLength
{
   //-----------------------------------------------------------------------------------------------------------------//
   static public void SetMaxLength(android.widget.EditText pOwner, int nWidth, final int nType)
   {
      pOwner.setFilters(new android.text.InputFilter[]{new android.text.InputFilter.LengthFilter(nWidth)});
      pOwner.setInputType(nType);
   }
}
