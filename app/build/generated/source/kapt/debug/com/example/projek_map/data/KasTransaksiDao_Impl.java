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
public final class KasTransaksiDao_Impl implements KasTransaksiDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<KasTransaksi> __insertionAdapterOfKasTransaksi;

  private final EntityDeletionOrUpdateAdapter<KasTransaksi> __deletionAdapterOfKasTransaksi;

  private final EntityDeletionOrUpdateAdapter<KasTransaksi> __updateAdapterOfKasTransaksi;

  public KasTransaksiDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfKasTransaksi = new EntityInsertionAdapter<KasTransaksi>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `kas_transaksi` (`id`,`tanggal`,`jenis`,`kategori`,`deskripsi`,`jumlah`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final KasTransaksi entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTanggal() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTanggal());
        }
        if (entity.getJenis() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getJenis());
        }
        if (entity.getKategori() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getKategori());
        }
        if (entity.getDeskripsi() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDeskripsi());
        }
        statement.bindDouble(6, entity.getJumlah());
      }
    };
    this.__deletionAdapterOfKasTransaksi = new EntityDeletionOrUpdateAdapter<KasTransaksi>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `kas_transaksi` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final KasTransaksi entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfKasTransaksi = new EntityDeletionOrUpdateAdapter<KasTransaksi>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `kas_transaksi` SET `id` = ?,`tanggal` = ?,`jenis` = ?,`kategori` = ?,`deskripsi` = ?,`jumlah` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final KasTransaksi entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTanggal() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTanggal());
        }
        if (entity.getJenis() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getJenis());
        }
        if (entity.getKategori() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getKategori());
        }
        if (entity.getDeskripsi() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getDeskripsi());
        }
        statement.bindDouble(6, entity.getJumlah());
        statement.bindLong(7, entity.getId());
      }
    };
  }

  @Override
  public Object insertTransaksi(final KasTransaksi transaksi,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfKasTransaksi.insert(transaksi);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllTransaksi(final List<KasTransaksi> list,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfKasTransaksi.insert(list);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransaksi(final KasTransaksi transaksi,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfKasTransaksi.handle(transaksi);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTransaksi(final KasTransaksi transaksi,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfKasTransaksi.handle(transaksi);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllTransaksi(final Continuation<? super List<KasTransaksi>> $completion) {
    final String _sql = "SELECT * FROM kas_transaksi ORDER BY tanggal DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<KasTransaksi>>() {
      @Override
      @NonNull
      public List<KasTransaksi> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final int _cursorIndexOfJenis = CursorUtil.getColumnIndexOrThrow(_cursor, "jenis");
          final int _cursorIndexOfKategori = CursorUtil.getColumnIndexOrThrow(_cursor, "kategori");
          final int _cursorIndexOfDeskripsi = CursorUtil.getColumnIndexOrThrow(_cursor, "deskripsi");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final List<KasTransaksi> _result = new ArrayList<KasTransaksi>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final KasTransaksi _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            final String _tmpJenis;
            if (_cursor.isNull(_cursorIndexOfJenis)) {
              _tmpJenis = null;
            } else {
              _tmpJenis = _cursor.getString(_cursorIndexOfJenis);
            }
            final String _tmpKategori;
            if (_cursor.isNull(_cursorIndexOfKategori)) {
              _tmpKategori = null;
            } else {
              _tmpKategori = _cursor.getString(_cursorIndexOfKategori);
            }
            final String _tmpDeskripsi;
            if (_cursor.isNull(_cursorIndexOfDeskripsi)) {
              _tmpDeskripsi = null;
            } else {
              _tmpDeskripsi = _cursor.getString(_cursorIndexOfDeskripsi);
            }
            final double _tmpJumlah;
            _tmpJumlah = _cursor.getDouble(_cursorIndexOfJumlah);
            _item = new KasTransaksi(_tmpId,_tmpTanggal,_tmpJenis,_tmpKategori,_tmpDeskripsi,_tmpJumlah);
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
  public Object getTransaksiById(final int id,
      final Continuation<? super KasTransaksi> $completion) {
    final String _sql = "SELECT * FROM kas_transaksi WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<KasTransaksi>() {
      @Override
      @Nullable
      public KasTransaksi call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final int _cursorIndexOfJenis = CursorUtil.getColumnIndexOrThrow(_cursor, "jenis");
          final int _cursorIndexOfKategori = CursorUtil.getColumnIndexOrThrow(_cursor, "kategori");
          final int _cursorIndexOfDeskripsi = CursorUtil.getColumnIndexOrThrow(_cursor, "deskripsi");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final KasTransaksi _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTanggal;
            if (_cursor.isNull(_cursorIndexOfTanggal)) {
              _tmpTanggal = null;
            } else {
              _tmpTanggal = _cursor.getString(_cursorIndexOfTanggal);
            }
            final String _tmpJenis;
            if (_cursor.isNull(_cursorIndexOfJenis)) {
              _tmpJenis = null;
            } else {
              _tmpJenis = _cursor.getString(_cursorIndexOfJenis);
            }
            final String _tmpKategori;
            if (_cursor.isNull(_cursorIndexOfKategori)) {
              _tmpKategori = null;
            } else {
              _tmpKategori = _cursor.getString(_cursorIndexOfKategori);
            }
            final String _tmpDeskripsi;
            if (_cursor.isNull(_cursorIndexOfDeskripsi)) {
              _tmpDeskripsi = null;
            } else {
              _tmpDeskripsi = _cursor.getString(_cursorIndexOfDeskripsi);
            }
            final double _tmpJumlah;
            _tmpJumlah = _cursor.getDouble(_cursorIndexOfJumlah);
            _result = new KasTransaksi(_tmpId,_tmpTanggal,_tmpJenis,_tmpKategori,_tmpDeskripsi,_tmpJumlah);
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
    final String _sql = "SELECT MAX(id) FROM kas_transaksi";
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

  @Override
  public Object getSaldoKas(final Continuation<? super Double> $completion) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(\n"
            + "            CASE \n"
            + "                WHEN jenis = 'Masuk' THEN jumlah \n"
            + "                ELSE -jumlah \n"
            + "            END\n"
            + "        ), 0.0)\n"
            + "        FROM kas_transaksi\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
