/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Project\\workspace\\MyApplication\\src\\my\\app\\getdata\\IGetDataService.aidl
 */
package my.app.getdata;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
import my.app.getdata.callback.ICallbackListener;
public interface IGetDataService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements my.app.getdata.IGetDataService
{
private static final java.lang.String DESCRIPTOR = "my.app.getdata.IGetDataService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IGetDataService interface,
 * generating a proxy if needed.
 */
public static my.app.getdata.IGetDataService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof my.app.getdata.IGetDataService))) {
return ((my.app.getdata.IGetDataService)iin);
}
return new my.app.getdata.IGetDataService.Stub.Proxy(obj);
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
case TRANSACTION_echo:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _result = this.echo(_arg0);
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
this.stop();
reply.writeNoException();
return true;
}
case TRANSACTION_start:
{
data.enforceInterface(DESCRIPTOR);
this.start();
reply.writeNoException();
return true;
}
case TRANSACTION_notifyStatus:
{
data.enforceInterface(DESCRIPTOR);
this.notifyStatus();
reply.writeNoException();
return true;
}
case TRANSACTION_addListener:
{
data.enforceInterface(DESCRIPTOR);
my.app.getdata.callback.ICallbackListener _arg0;
_arg0 = my.app.getdata.callback.ICallbackListener.Stub.asInterface(data.readStrongBinder());
this.addListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeListener:
{
data.enforceInterface(DESCRIPTOR);
my.app.getdata.callback.ICallbackListener _arg0;
_arg0 = my.app.getdata.callback.ICallbackListener.Stub.asInterface(data.readStrongBinder());
this.removeListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements my.app.getdata.IGetDataService
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
public java.lang.String echo(java.lang.String message) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(message);
mRemote.transact(Stub.TRANSACTION_echo, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void stop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void start() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_start, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void notifyStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_notifyStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void addListener(my.app.getdata.callback.ICallbackListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void removeListener(my.app.getdata.callback.ICallbackListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_echo = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stop = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_start = (IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_notifyStatus = (IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_addListener = (IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_removeListener = (IBinder.FIRST_CALL_TRANSACTION + 5);
}
public java.lang.String echo(java.lang.String message) throws android.os.RemoteException;
public void stop() throws android.os.RemoteException;
public void start() throws android.os.RemoteException;
public void notifyStatus() throws android.os.RemoteException;
public void addListener(my.app.getdata.callback.ICallbackListener listener) throws android.os.RemoteException;
public void removeListener(my.app.getdata.callback.ICallbackListener listener) throws android.os.RemoteException;
}
