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
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __deletionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __updateAdapterOfUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `users` (`kodePegawai`,`email`,`password`,`nama`,`statusKeanggotaan`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
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
        if (entity.getStatusKeanggotaan() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatusKeanggotaan());
        }
      }
    };
    this.__deletionAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `users` WHERE `kodePegawai` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
        if (entity.getKodePegawai() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getKodePegawai());
        }
      }
    };
    this.__updateAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `users` SET `kodePegawai` = ?,`email` = ?,`password` = ?,`nama` = ?,`statusKeanggotaan` = ? WHERE `kodePegawai` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final User entity) {
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
        if (entity.getStatusKeanggotaan() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatusKeanggotaan());
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
  public Object insertUser(final User user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUser.insert(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertUsers(final List<User> users, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUser.insert(users);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteUser(final User user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfUser.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateUser(final User user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfUser.handle(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllUsers(final Continuation<? super List<User>> $completion) {
    final String _sql = "SELECT * FROM users";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<User>>() {
      @Override
      @NonNull
      public List<User> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfNama = CursorUtil.getColumnIndexOrThrow(_cursor, "nama");
          final int _cursorIndexOfStatusKeanggotaan = CursorUtil.getColumnIndexOrThrow(_cursor, "statusKeanggotaan");
          final List<User> _result = new ArrayList<User>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final User _item;
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
            final String _tmpStatusKeanggotaan;
            if (_cursor.isNull(_cursorIndexOfStatusKeanggotaan)) {
              _tmpStatusKeanggotaan = null;
            } else {
              _tmpStatusKeanggotaan = _cursor.getString(_cursorIndexOfStatusKeanggotaan);
            }
            _item = new User(_tmpKodePegawai,_tmpEmail,_tmpPassword,_tmpNama,_tmpStatusKeanggotaan);
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
  public Object getUserByKode(final String kode, final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE kodePegawai = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<User>() {
      @Override
      @Nullable
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfNama = CursorUtil.getColumnIndexOrThrow(_cursor, "nama");
          final int _cursorIndexOfStatusKeanggotaan = CursorUtil.getColumnIndexOrThrow(_cursor, "statusKeanggotaan");
          final User _result;
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
            final String _tmpStatusKeanggotaan;
            if (_cursor.isNull(_cursorIndexOfStatusKeanggotaan)) {
              _tmpStatusKeanggotaan = null;
            } else {
              _tmpStatusKeanggotaan = _cursor.getString(_cursorIndexOfStatusKeanggotaan);
            }
            _result = new User(_tmpKodePegawai,_tmpEmail,_tmpPassword,_tmpNama,_tmpStatusKeanggotaan);
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
  public Object getUserByEmail(final String email, final Continuation<? super User> $completion) {
    final String _sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<User>() {
      @Override
      @Nullable
      public User call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfNama = CursorUtil.getColumnIndexOrThrow(_cursor, "nama");
          final int _cursorIndexOfStatusKeanggotaan = CursorUtil.getColumnIndexOrThrow(_cursor, "statusKeanggotaan");
          final User _result;
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
            final String _tmpStatusKeanggotaan;
            if (_cursor.isNull(_cursorIndexOfStatusKeanggotaan)) {
              _tmpStatusKeanggotaan = null;
            } else {
              _tmpStatusKeanggotaan = _cursor.getString(_cursorIndexOfStatusKeanggotaan);
            }
            _result = new User(_tmpKodePegawai,_tmpEmail,_tmpPassword,_tmpNama,_tmpStatusKeanggotaan);
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
  public Object countUsers(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM users";
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
