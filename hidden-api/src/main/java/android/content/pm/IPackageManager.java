package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.PersistableBundle;
import android.os.RemoteException;

public interface IPackageManager extends IInterface {
    void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage);

//    String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended,
//                                        PersistableBundle appExtras, PersistableBundle launcherExtras,
//                                        SuspendDialogInfo dialogInfo, String suspendingPackage,int flags);

    String[] setPackagesSuspendedAsUser(String[] strArr, boolean z, PersistableBundle persistableBundle, PersistableBundle persistableBundle2, SuspendDialogInfo suspendDialogInfo, String str, int i) throws RemoteException;


    abstract class Stub extends Binder implements IPackageManager {
        public static IPackageManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
