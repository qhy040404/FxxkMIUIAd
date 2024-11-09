package android.content.pm;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.PersistableBundle;
import android.os.RemoteException;

import androidx.annotation.ChecksSdkIntAtLeast;

public interface IPackageManager extends IInterface {
    void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage);

    String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, SuspendDialogInfo dialogInfo, String callingPackage, int userId) throws RemoteException;

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    String[] setPackagesSuspendedAsUser(String[] packageNames, boolean suspended, PersistableBundle appExtras, PersistableBundle launcherExtras, SuspendDialogInfo dialogInfo, int flags, String callingPackage, int suspendingUserId, int targetUserId) throws RemoteException;

    abstract class Stub extends Binder implements IPackageManager {
        public static IPackageManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
