/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Project\\workspace\\MyApplication\\src\\my\\app\\getdata\\callback\\ICallbackListener.aidl
 */
package my.app.getdata.callback;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface ICallbackListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements my.app.getdata.callback.ICallbackListener
{
private static final java.lang.String DESCRIPTOR = "my.app.getdata.callback.ICallbackListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an ICallbackListener interface,
 * generating a proxy if needed.
 */
public static my.app.getdata.callback.ICallbackListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof my.app.getdata.callback.ICallbackListener))) {
return ((my.app.getdata.callback.ICallbackListener)iin);
}
return new my.app.getdata.callback.ICallbackListener.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_callbackMessage:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.callbackMessage(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements my.app.getdata.callback.ICallbackListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void callbackMessage(int msg) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(msg);
mRemote.transact(Stub.TRANSACTION_callbackMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_callbackMessage = (IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void callbackMessage(int msg) throws android.os.RemoteException;
}
