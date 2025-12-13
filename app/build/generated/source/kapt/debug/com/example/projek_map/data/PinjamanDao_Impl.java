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
import androidx.room.SharedSQLiteStatement;
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
public final class PinjamanDao_Impl implements PinjamanDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Pinjaman> __insertionAdapterOfPinjaman;

  private final EntityDeletionOrUpdateAdapter<Pinjaman> __deletionAdapterOfPinjaman;

  private final EntityDeletionOrUpdateAdapter<Pinjaman> __updateAdapterOfPinjaman;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBungaUntukPinjamanAktif;

  public PinjamanDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPinjaman = new EntityInsertionAdapter<Pinjaman>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pinjaman` (`id`,`kodePegawai`,`jumlah`,`tenor`,`status`,`bunga`,`angsuranTerbayar`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pinjaman entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getKodePegawai() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getKodePegawai());
        }
        statement.bindLong(3, entity.getJumlah());
        statement.bindLong(4, entity.getTenor());
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        statement.bindDouble(6, entity.getBunga());
        statement.bindLong(7, entity.getAngsuranTerbayar());
      }
    };
    this.__deletionAdapterOfPinjaman = new EntityDeletionOrUpdateAdapter<Pinjaman>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `pinjaman` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pinjaman entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPinjaman = new EntityDeletionOrUpdateAdapter<Pinjaman>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `pinjaman` SET `id` = ?,`kodePegawai` = ?,`jumlah` = ?,`tenor` = ?,`status` = ?,`bunga` = ?,`angsuranTerbayar` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pinjaman entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getKodePegawai() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getKodePegawai());
        }
        statement.bindLong(3, entity.getJumlah());
        statement.bindLong(4, entity.getTenor());
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        statement.bindDouble(6, entity.getBunga());
        statement.bindLong(7, entity.getAngsuranTerbayar());
        statement.bindLong(8, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE pinjaman SET status = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateBungaUntukPinjamanAktif = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE pinjaman SET bunga = ? WHERE status = 'Proses' OR status = 'Disetujui'";
        return _query;
      }
    };
  }

  @Override
  public Object insertPinjaman(final Pinjaman pinjaman,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPinjaman.insert(pinjaman);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllPinjaman(final List<Pinjaman> list,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPinjaman.insert(list);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePinjaman(final Pinjaman pinjaman,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPinjaman.handle(pinjaman);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePinjaman(final Pinjaman pinjaman,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPinjaman.handle(pinjaman);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final int id, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        if (status == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, status);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBungaUntukPinjamanAktif(final double bunga,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBungaUntukPinjamanAktif.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, bunga);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateBungaUntukPinjamanAktif.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllPinjaman(final Continuation<? super List<Pinjaman>> $completion) {
    final String _sql = "SELECT * FROM pinjaman ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Pinjaman>>() {
      @Override
      @NonNull
      public List<Pinjaman> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfTenor = CursorUtil.getColumnIndexOrThrow(_cursor, "tenor");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBunga = CursorUtil.getColumnIndexOrThrow(_cursor, "bunga");
          final int _cursorIndexOfAngsuranTerbayar = CursorUtil.getColumnIndexOrThrow(_cursor, "angsuranTerbayar");
          final List<Pinjaman> _result = new ArrayList<Pinjaman>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pinjaman _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final int _tmpTenor;
            _tmpTenor = _cursor.getInt(_cursorIndexOfTenor);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final double _tmpBunga;
            _tmpBunga = _cursor.getDouble(_cursorIndexOfBunga);
            final int _tmpAngsuranTerbayar;
            _tmpAngsuranTerbayar = _cursor.getInt(_cursorIndexOfAngsuranTerbayar);
            _item = new Pinjaman(_tmpId,_tmpKodePegawai,_tmpJumlah,_tmpTenor,_tmpStatus,_tmpBunga,_tmpAngsuranTerbayar);
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
  public Object getPinjamanForUser(final String kode,
      final Continuation<? super List<Pinjaman>> $completion) {
    final String _sql = "SELECT * FROM pinjaman WHERE kodePegawai = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Pinjaman>>() {
      @Override
      @NonNull
      public List<Pinjaman> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfTenor = CursorUtil.getColumnIndexOrThrow(_cursor, "tenor");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBunga = CursorUtil.getColumnIndexOrThrow(_cursor, "bunga");
          final int _cursorIndexOfAngsuranTerbayar = CursorUtil.getColumnIndexOrThrow(_cursor, "angsuranTerbayar");
          final List<Pinjaman> _result = new ArrayList<Pinjaman>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pinjaman _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final int _tmpTenor;
            _tmpTenor = _cursor.getInt(_cursorIndexOfTenor);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final double _tmpBunga;
            _tmpBunga = _cursor.getDouble(_cursorIndexOfBunga);
            final int _tmpAngsuranTerbayar;
            _tmpAngsuranTerbayar = _cursor.getInt(_cursorIndexOfAngsuranTerbayar);
            _item = new Pinjaman(_tmpId,_tmpKodePegawai,_tmpJumlah,_tmpTenor,_tmpStatus,_tmpBunga,_tmpAngsuranTerbayar);
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
  public Object getPinjamanById(final int id, final Continuation<? super Pinjaman> $completion) {
    final String _sql = "SELECT * FROM pinjaman WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Pinjaman>() {
      @Override
      @Nullable
      public Pinjaman call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfTenor = CursorUtil.getColumnIndexOrThrow(_cursor, "tenor");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBunga = CursorUtil.getColumnIndexOrThrow(_cursor, "bunga");
          final int _cursorIndexOfAngsuranTerbayar = CursorUtil.getColumnIndexOrThrow(_cursor, "angsuranTerbayar");
          final Pinjaman _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final int _tmpTenor;
            _tmpTenor = _cursor.getInt(_cursorIndexOfTenor);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final double _tmpBunga;
            _tmpBunga = _cursor.getDouble(_cursorIndexOfBunga);
            final int _tmpAngsuranTerbayar;
            _tmpAngsuranTerbayar = _cursor.getInt(_cursorIndexOfAngsuranTerbayar);
            _result = new Pinjaman(_tmpId,_tmpKodePegawai,_tmpJumlah,_tmpTenor,_tmpStatus,_tmpBunga,_tmpAngsuranTerbayar);
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
  public Object getPinjamanAktif(final Continuation<? super List<Pinjaman>> $completion) {
    final String _sql = "SELECT * FROM pinjaman WHERE status = 'Disetujui' OR status = 'Aktif'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Pinjaman>>() {
      @Override
      @NonNull
      public List<Pinjaman> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfTenor = CursorUtil.getColumnIndexOrThrow(_cursor, "tenor");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBunga = CursorUtil.getColumnIndexOrThrow(_cursor, "bunga");
          final int _cursorIndexOfAngsuranTerbayar = CursorUtil.getColumnIndexOrThrow(_cursor, "angsuranTerbayar");
          final List<Pinjaman> _result = new ArrayList<Pinjaman>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pinjaman _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final int _tmpTenor;
            _tmpTenor = _cursor.getInt(_cursorIndexOfTenor);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final double _tmpBunga;
            _tmpBunga = _cursor.getDouble(_cursorIndexOfBunga);
            final int _tmpAngsuranTerbayar;
            _tmpAngsuranTerbayar = _cursor.getInt(_cursorIndexOfAngsuranTerbayar);
            _item = new Pinjaman(_tmpId,_tmpKodePegawai,_tmpJumlah,_tmpTenor,_tmpStatus,_tmpBunga,_tmpAngsuranTerbayar);
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
  public Object getPinjamanLunas(final Continuation<? super List<Pinjaman>> $completion) {
    final String _sql = "SELECT * FROM pinjaman WHERE status = 'Lunas'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Pinjaman>>() {
      @Override
      @NonNull
      public List<Pinjaman> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfTenor = CursorUtil.getColumnIndexOrThrow(_cursor, "tenor");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBunga = CursorUtil.getColumnIndexOrThrow(_cursor, "bunga");
          final int _cursorIndexOfAngsuranTerbayar = CursorUtil.getColumnIndexOrThrow(_cursor, "angsuranTerbayar");
          final List<Pinjaman> _result = new ArrayList<Pinjaman>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pinjaman _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final int _tmpTenor;
            _tmpTenor = _cursor.getInt(_cursorIndexOfTenor);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final double _tmpBunga;
            _tmpBunga = _cursor.getDouble(_cursorIndexOfBunga);
            final int _tmpAngsuranTerbayar;
            _tmpAngsuranTerbayar = _cursor.getInt(_cursorIndexOfAngsuranTerbayar);
            _item = new Pinjaman(_tmpId,_tmpKodePegawai,_tmpJumlah,_tmpTenor,_tmpStatus,_tmpBunga,_tmpAngsuranTerbayar);
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
  public Object getMaxId(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT MAX(id) FROM pinjaman";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @Nullable
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
