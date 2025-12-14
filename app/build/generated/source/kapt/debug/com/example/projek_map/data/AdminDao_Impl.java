package com.example.projek_map.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AdminDao_Impl implements AdminDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Admin> __insertionAdapterOfAdmin;

  private final EntityDeletionOrUpdateAdapter<Admin> __deletionAdapterOfAdmin;

  private final EntityDeletionOrUpdateAdapter<Admin> __updateAdapterOfAdmin;

  public AdminDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAdmin = new EntityInsertionAdapter<Admin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `admins` (`kodePegawai`,`email`,`password`,`nama`,`role`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Admin entity) {
        if (entity.getKodePegawai() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKodePegawai());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getEmail());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPassword());
        }
        if (entity.getNama() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNama());
        }
        if (entity.getRole() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getRole());
        }
      }
    };
    this.__deletionAdapterOfAdmin = new EntityDeletionOrUpdateAdapter<Admin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `admins` WHERE `kodePegawai` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Admin entity) {
        if (entity.getKodePegawai() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKodePegawai());
        }
      }
    };
    this.__updateAdapterOfAdmin = new EntityDeletionOrUpdateAdapter<Admin>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `admins` SET `kodePegawai` = ?,`email` = ?,`password` = ?,`nama` = ?,`role` = ? WHERE `kodePegawai` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Admin entity) {
        if (entity.getKodePegawai() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKodePegawai());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getEmail());
        }
        if (entity.getPassword() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPassword());
        }
        if (entity.getNama() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getNama());
        }
        if (entity.getRole() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getRole());
        }
        if (entity.getKodePegawai() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getKodePegawai());
        }
      }
    };
  }

  @Override
  public Object insertAdmin(final Admin admin, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAdmin.insert(admin);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAdmins(final List<Admin> admins,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAdmin.insert(admins);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAdmin(final Admin admin, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfAdmin.handle(admin);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAdmin(final Admin admin, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAdmin.handle(admin);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllAdmins(final Continuation<? super List<Admin>> $completion) {
    final String _sql = "SELECT * FROM admins";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Admin>>() {
      @Override
      @NonNull
      public List<Admin> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfNama = CursorUtil.getColumnIndexOrThrow(_cursor, "nama");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final List<Admin> _result = new ArrayList<Admin>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Admin _item;
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpNama;
            if (_cursor.isNull(_cursorIndexOfNama)) {
              _tmpNama = null;
            } else {
              _tmpNama = _cursor.getString(_cursorIndexOfNama);
            }
            final String _tmpRole;
            if (_cursor.isNull(_cursorIndexOfRole)) {
              _tmpRole = null;
            } else {
              _tmpRole = _cursor.getString(_cursorIndexOfRole);
            }
            _item = new Admin(_tmpKodePegawai,_tmpEmail,_tmpPassword,_tmpNama,_tmpRole);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAdminByKode(final String kode, final Continuation<? super Admin> $completion) {
    final String _sql = "SELECT * FROM admins WHERE kodePegawai = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Admin>() {
      @Override
      @Nullable
      public Admin call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfNama = CursorUtil.getColumnIndexOrThrow(_cursor, "nama");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final Admin _result;
          if (_cursor.moveToFirst()) {
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpNama;
            if (_cursor.isNull(_cursorIndexOfNama)) {
              _tmpNama = null;
            } else {
              _tmpNama = _cursor.getString(_cursorIndexOfNama);
            }
            final String _tmpRole;
            if (_cursor.isNull(_cursorIndexOfRole)) {
              _tmpRole = null;
            } else {
              _tmpRole = _cursor.getString(_cursorIndexOfRole);
            }
            _result = new Admin(_tmpKodePegawai,_tmpEmail,_tmpPassword,_tmpNama,_tmpRole);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAdminByEmail(final String email, final Continuation<? super Admin> $completion) {
    final String _sql = "SELECT * FROM admins WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Admin>() {
      @Override
      @Nullable
      public Admin call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfNama = CursorUtil.getColumnIndexOrThrow(_cursor, "nama");
          final int _cursorIndexOfRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final Admin _result;
          if (_cursor.moveToFirst()) {
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpPassword;
            if (_cursor.isNull(_cursorIndexOfPassword)) {
              _tmpPassword = null;
            } else {
              _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
            }
            final String _tmpNama;
            if (_cursor.isNull(_cursorIndexOfNama)) {
              _tmpNama = null;
            } else {
              _tmpNama = _cursor.getString(_cursorIndexOfNama);
            }
            final String _tmpRole;
            if (_cursor.isNull(_cursorIndexOfRole)) {
              _tmpRole = null;
            } else {
              _tmpRole = _cursor.getString(_cursorIndexOfRole);
            }
            _result = new Admin(_tmpKodePegawai,_tmpEmail,_tmpPassword,_tmpNama,_tmpRole);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countAdmins(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM admins";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
