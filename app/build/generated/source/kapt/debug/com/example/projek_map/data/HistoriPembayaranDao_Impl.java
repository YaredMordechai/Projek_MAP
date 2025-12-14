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
import java.lang.Double;
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
public final class HistoriPembayaranDao_Impl implements HistoriPembayaranDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HistoriPembayaran> __insertionAdapterOfHistoriPembayaran;

  private final EntityDeletionOrUpdateAdapter<HistoriPembayaran> __deletionAdapterOfHistoriPembayaran;

  private final EntityDeletionOrUpdateAdapter<HistoriPembayaran> __updateAdapterOfHistoriPembayaran;

  public HistoriPembayaranDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHistoriPembayaran = new EntityInsertionAdapter<HistoriPembayaran>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `histori_pembayaran` (`id`,`kodePegawai`,`pinjamanId`,`tanggal`,`jumlah`,`status`,`buktiPembayaranUri`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoriPembayaran entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getKodePegawai() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getKodePegawai());
        }
        statement.bindLong(3, entity.getPinjamanId());
        if (entity.getTanggal() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTanggal());
        }
        statement.bindLong(5, entity.getJumlah());
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
        if (entity.getBuktiPembayaranUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBuktiPembayaranUri());
        }
      }
    };
    this.__deletionAdapterOfHistoriPembayaran = new EntityDeletionOrUpdateAdapter<HistoriPembayaran>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `histori_pembayaran` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoriPembayaran entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHistoriPembayaran = new EntityDeletionOrUpdateAdapter<HistoriPembayaran>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `histori_pembayaran` SET `id` = ?,`kodePegawai` = ?,`pinjamanId` = ?,`tanggal` = ?,`jumlah` = ?,`status` = ?,`buktiPembayaranUri` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoriPembayaran entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getKodePegawai() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getKodePegawai());
        }
        statement.bindLong(3, entity.getPinjamanId());
        if (entity.getTanggal() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTanggal());
        }
        statement.bindLong(5, entity.getJumlah());
        if (entity.getStatus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStatus());
        }
        if (entity.getBuktiPembayaranUri() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getBuktiPembayaranUri());
        }
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertHistori(final HistoriPembayaran histori,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoriPembayaran.insert(histori);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllHistori(final List<HistoriPembayaran> list,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoriPembayaran.insert(list);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHistori(final HistoriPembayaran histori,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHistoriPembayaran.handle(histori);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateHistori(final HistoriPembayaran histori,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHistoriPembayaran.handle(histori);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllHistori(final Continuation<? super List<HistoriPembayaran>> $completion) {
    final String _sql = "SELECT * FROM histori_pembayaran ORDER BY tanggal DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HistoriPembayaran>>() {
      @Override
      @NonNull
      public List<HistoriPembayaran> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfPinjamanId = CursorUtil.getColumnIndexOrThrow(_cursor, "pinjamanId");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBuktiPembayaranUri = CursorUtil.getColumnIndexOrThrow(_cursor, "buktiPembayaranUri");
          final List<HistoriPembayaran> _result = new ArrayList<HistoriPembayaran>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoriPembayaran _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpPinjamanId;
            _tmpPinjamanId = _cursor.getInt(_cursorIndexOfPinjamanId);
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpBuktiPembayaranUri;
            if (_cursor.isNull(_cursorIndexOfBuktiPembayaranUri)) {
              _tmpBuktiPembayaranUri = null;
            } else {
              _tmpBuktiPembayaranUri = _cursor.getString(_cursorIndexOfBuktiPembayaranUri);
            }
            _item = new HistoriPembayaran(_tmpId,_tmpKodePegawai,_tmpPinjamanId,_tmpTanggal,_tmpJumlah,_tmpStatus,_tmpBuktiPembayaranUri);
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
  public Object getHistoriByPinjaman(final int pinjamanId,
      final Continuation<? super List<HistoriPembayaran>> $completion) {
    final String _sql = "SELECT * FROM histori_pembayaran WHERE pinjamanId = ? ORDER BY tanggal DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, pinjamanId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HistoriPembayaran>>() {
      @Override
      @NonNull
      public List<HistoriPembayaran> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfPinjamanId = CursorUtil.getColumnIndexOrThrow(_cursor, "pinjamanId");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBuktiPembayaranUri = CursorUtil.getColumnIndexOrThrow(_cursor, "buktiPembayaranUri");
          final List<HistoriPembayaran> _result = new ArrayList<HistoriPembayaran>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoriPembayaran _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpPinjamanId;
            _tmpPinjamanId = _cursor.getInt(_cursorIndexOfPinjamanId);
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpBuktiPembayaranUri;
            if (_cursor.isNull(_cursorIndexOfBuktiPembayaranUri)) {
              _tmpBuktiPembayaranUri = null;
            } else {
              _tmpBuktiPembayaranUri = _cursor.getString(_cursorIndexOfBuktiPembayaranUri);
            }
            _item = new HistoriPembayaran(_tmpId,_tmpKodePegawai,_tmpPinjamanId,_tmpTanggal,_tmpJumlah,_tmpStatus,_tmpBuktiPembayaranUri);
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
  public Object getHistoriByUser(final String kode,
      final Continuation<? super List<HistoriPembayaran>> $completion) {
    final String _sql = "SELECT * FROM histori_pembayaran WHERE kodePegawai = ? ORDER BY tanggal DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HistoriPembayaran>>() {
      @Override
      @NonNull
      public List<HistoriPembayaran> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfPinjamanId = CursorUtil.getColumnIndexOrThrow(_cursor, "pinjamanId");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBuktiPembayaranUri = CursorUtil.getColumnIndexOrThrow(_cursor, "buktiPembayaranUri");
          final List<HistoriPembayaran> _result = new ArrayList<HistoriPembayaran>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoriPembayaran _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpPinjamanId;
            _tmpPinjamanId = _cursor.getInt(_cursorIndexOfPinjamanId);
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpBuktiPembayaranUri;
            if (_cursor.isNull(_cursorIndexOfBuktiPembayaranUri)) {
              _tmpBuktiPembayaranUri = null;
            } else {
              _tmpBuktiPembayaranUri = _cursor.getString(_cursorIndexOfBuktiPembayaranUri);
            }
            _item = new HistoriPembayaran(_tmpId,_tmpKodePegawai,_tmpPinjamanId,_tmpTanggal,_tmpJumlah,_tmpStatus,_tmpBuktiPembayaranUri);
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
  public Object getHistoriWithBuktiByUser(final String kode,
      final Continuation<? super List<HistoriPembayaran>> $completion) {
    final String _sql = "SELECT * FROM histori_pembayaran WHERE kodePegawai = ? AND buktiPembayaranUri IS NOT NULL ORDER BY tanggal DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HistoriPembayaran>>() {
      @Override
      @NonNull
      public List<HistoriPembayaran> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfPinjamanId = CursorUtil.getColumnIndexOrThrow(_cursor, "pinjamanId");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfBuktiPembayaranUri = CursorUtil.getColumnIndexOrThrow(_cursor, "buktiPembayaranUri");
          final List<HistoriPembayaran> _result = new ArrayList<HistoriPembayaran>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoriPembayaran _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
            final int _tmpPinjamanId;
            _tmpPinjamanId = _cursor.getInt(_cursorIndexOfPinjamanId);
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            final int _tmpJumlah;
            _tmpJumlah = _cursor.getInt(_cursorIndexOfJumlah);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpBuktiPembayaranUri;
            if (_cursor.isNull(_cursorIndexOfBuktiPembayaranUri)) {
              _tmpBuktiPembayaranUri = null;
            } else {
              _tmpBuktiPembayaranUri = _cursor.getString(_cursorIndexOfBuktiPembayaranUri);
            }
            _item = new HistoriPembayaran(_tmpId,_tmpKodePegawai,_tmpPinjamanId,_tmpTanggal,_tmpJumlah,_tmpStatus,_tmpBuktiPembayaranUri);
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
  public Object getTotalTerbayarForPinjaman(final int pinjamanId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COALESCE(SUM(jumlah), 0) FROM histori_pembayaran WHERE pinjamanId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, pinjamanId);
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

  @Override
  public Object getTotalAngsuranBulanan(final String kode, final String bulanPattern,
      final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(CAST(SUM(jumlah) AS REAL), 0.0) \n"
            + "        FROM histori_pembayaran \n"
            + "        WHERE kodePegawai = ? AND tanggal LIKE ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    _argIndex = 2;
    if (bulanPattern == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, bulanPattern);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
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

  @Override
  public Object getMaxId(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT MAX(id) FROM histori_pembayaran";
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
