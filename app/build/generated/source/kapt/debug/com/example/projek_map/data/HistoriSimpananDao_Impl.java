package com.example.projek_map.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
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
public final class HistoriSimpananDao_Impl implements HistoriSimpananDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HistoriSimpanan> __insertionAdapterOfHistoriSimpanan;

  public HistoriSimpananDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHistoriSimpanan = new EntityInsertionAdapter<HistoriSimpanan>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `histori_simpanan` (`id`,`kodePegawai`,`tanggal`,`jenis`,`jumlah`,`keterangan`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HistoriSimpanan entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getKodePegawai() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getKodePegawai());
        }
        if (entity.getTanggal() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getTanggal());
        }
        if (entity.getJenis() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getJenis());
        }
        statement.bindDouble(5, entity.getJumlah());
        if (entity.getKeterangan() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getKeterangan());
        }
      }
    };
  }

  @Override
  public Object insertHistori(final HistoriSimpanan item,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoriSimpanan.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAllHistori(final List<HistoriSimpanan> list,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHistoriSimpanan.insert(list);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllHistori(final Continuation<? super List<HistoriSimpanan>> $completion) {
    final String _sql = "SELECT * FROM histori_simpanan";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HistoriSimpanan>>() {
      @Override
      @NonNull
      public List<HistoriSimpanan> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final int _cursorIndexOfJenis = CursorUtil.getColumnIndexOrThrow(_cursor, "jenis");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfKeterangan = CursorUtil.getColumnIndexOrThrow(_cursor, "keterangan");
          final List<HistoriSimpanan> _result = new ArrayList<HistoriSimpanan>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoriSimpanan _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
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
            final double _tmpJumlah;
            _tmpJumlah = _cursor.getDouble(_cursorIndexOfJumlah);
            final String _tmpKeterangan;
            if (_cursor.isNull(_cursorIndexOfKeterangan)) {
              _tmpKeterangan = null;
            } else {
              _tmpKeterangan = _cursor.getString(_cursorIndexOfKeterangan);
            }
            _item = new HistoriSimpanan(_tmpId,_tmpKodePegawai,_tmpTanggal,_tmpJenis,_tmpJumlah,_tmpKeterangan);
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
  public Object getHistoriForUser(final String kode,
      final Continuation<? super List<HistoriSimpanan>> $completion) {
    final String _sql = "SELECT * FROM histori_simpanan WHERE kodePegawai = ? ORDER BY tanggal DESC, id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (kode == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, kode);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HistoriSimpanan>>() {
      @Override
      @NonNull
      public List<HistoriSimpanan> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKodePegawai = CursorUtil.getColumnIndexOrThrow(_cursor, "kodePegawai");
          final int _cursorIndexOfTanggal = CursorUtil.getColumnIndexOrThrow(_cursor, "tanggal");
          final int _cursorIndexOfJenis = CursorUtil.getColumnIndexOrThrow(_cursor, "jenis");
          final int _cursorIndexOfJumlah = CursorUtil.getColumnIndexOrThrow(_cursor, "jumlah");
          final int _cursorIndexOfKeterangan = CursorUtil.getColumnIndexOrThrow(_cursor, "keterangan");
          final List<HistoriSimpanan> _result = new ArrayList<HistoriSimpanan>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HistoriSimpanan _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpKodePegawai;
            if (_cursor.isNull(_cursorIndexOfKodePegawai)) {
              _tmpKodePegawai = null;
            } else {
              _tmpKodePegawai = _cursor.getString(_cursorIndexOfKodePegawai);
            }
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
            final double _tmpJumlah;
            _tmpJumlah = _cursor.getDouble(_cursorIndexOfJumlah);
            final String _tmpKeterangan;
            if (_cursor.isNull(_cursorIndexOfKeterangan)) {
              _tmpKeterangan = null;
            } else {
              _tmpKeterangan = _cursor.getString(_cursorIndexOfKeterangan);
            }
            _item = new HistoriSimpanan(_tmpId,_tmpKodePegawai,_tmpTanggal,_tmpJenis,_tmpJumlah,_tmpKeterangan);
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
    final String _sql = "SELECT MAX(id) FROM histori_simpanan";
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
