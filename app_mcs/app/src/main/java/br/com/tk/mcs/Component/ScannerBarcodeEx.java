package br.com.tk.mcs.Component;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by wilsonsouza on 6/7/17.
 */

public class ScannerBarcodeEx extends LinearLayoutImpl implements ZXingScannerView.ResultHandler
{
   public final ZXingScannerView Data;
   protected OnScannerBarcodeListener m_pDispatch = null;
   //-----------------------------------------------------------------------------------------------------------------//
   public interface OnScannerBarcodeListener
   {
      public void OnHasData(final Result pResult);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public ScannerBarcodeEx(Context pContext, final Point area, final boolean bBorder)
   {
      super(pContext);
      this.Params.SetDimension(area);
      this.Data = new ZXingScannerView(pContext);
      this.Data.setResultHandler(this);
      this.addView(this.Data, this.Params);
      this.SetBorder(bBorder);
      this.Invalidate(true);
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void start()
   {
      this.Data.startCamera();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void stop()
   {
      this.Data.stopCamera();
   }
   //-----------------------------------------------------------------------------------------------------------------//
   public void SetOnScannerBarcodeListener(final OnScannerBarcodeListener pDispatch)
   {
      this.m_pDispatch = pDispatch;
   }
   //-----------------------------------------------------------------------------------------------------------------//
   @Override
   public void handleResult(final Result result)
   {
      /* put your code here */
      if(this.m_pDispatch != null)
      {
         m_pDispatch.OnHasData(result);
      }
   }
}
